"""
Lightweight local server for Hello there grocery.

Serves static HTML and a JSON API backed by SQLite.

Usage:
    python server.py
    # then open http://127.0.0.1:8000/login.html
"""

from __future__ import annotations

import json
import mimetypes
import re
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import parse_qs, urlparse

import db

ROOT = Path(__file__).resolve().parent
HOST = "127.0.0.1"
PORT = 8000

EMAIL_RE = re.compile(r"^[^\s@]+@[^\s@]+\.[^\s@]+$")


class AppHandler(BaseHTTPRequestHandler):
    server_version = "GroceryAuth/1.0"

    def log_message(self, fmt: str, *args) -> None:
        print(f"[{self.log_date_time_string()}] {self.address_string()} {fmt % args}")

    def _cors(self) -> None:
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")

    def _send_json(self, status: int, payload: dict) -> None:
        body = json.dumps(payload).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.send_header("Cache-Control", "no-store")
        self._cors()
        self.end_headers()
        self.wfile.write(body)

    def _send_error_json(self, status: int, error: str) -> None:
        self._send_json(status, {"ok": False, "error": error})

    def _read_json(self) -> dict | None:
        length = int(self.headers.get("Content-Length", "0") or 0)
        if length <= 0:
            return {}
        if length > 64_000:
            return None
        raw = self.rfile.read(length)
        try:
            data = json.loads(raw.decode("utf-8"))
        except (UnicodeDecodeError, json.JSONDecodeError):
            return None
        if not isinstance(data, dict):
            return None
        return data

    def _safe_path(self, url_path: str) -> Path | None:
        if url_path in ("", "/"):
            url_path = "/login.html"
        rel = url_path.lstrip("/")
        if not rel or ".." in rel.split("/"):
            return None
        candidate = (ROOT / rel).resolve()
        try:
            candidate.relative_to(ROOT)
        except ValueError:
            return None
        if not candidate.is_file():
            return None
        allowed = {".html", ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico", ".webp"}
        if candidate.suffix.lower() not in allowed:
            return None
        return candidate

    def _serve_file(self, path: Path) -> None:
        data = path.read_bytes()
        ctype, _ = mimetypes.guess_type(str(path))
        if ctype is None:
            ctype = "application/octet-stream"
        if ctype.startswith("text/") or ctype in ("application/javascript", "application/json"):
            ctype = f"{ctype}; charset=utf-8"
        self.send_response(200)
        self.send_header("Content-Type", ctype)
        self.send_header("Content-Length", str(len(data)))
        self._cors()
        self.end_headers()
        self.wfile.write(data)

    def do_OPTIONS(self) -> None:
        self.send_response(204)
        self._cors()
        self.send_header("Content-Length", "0")
        self.end_headers()

    def do_GET(self) -> None:
        try:
            parsed = urlparse(self.path)
            path = parsed.path
            qs = parse_qs(parsed.query)

            if path == "/api/health":
                self._send_json(
                    200,
                    {
                        "ok": True,
                        "users": db.user_count(),
                        "products": len(db.list_products()),
                        "db": str(db.DB_PATH.name),
                    },
                )
                return

            if path == "/api/products":
                category = (qs.get("category") or ["all"])[0]
                products = db.list_products(category)
                self._send_json(
                    200,
                    {
                        "ok": True,
                        "products": products,
                        "categories": db.list_categories(),
                    },
                )
                return

            if path == "/api/orders":
                try:
                    user_id = int((qs.get("user_id") or ["0"])[0])
                except ValueError:
                    self._send_error_json(400, "Invalid user_id.")
                    return
                if user_id < 1 or db.get_user_by_id(user_id) is None:
                    self._send_error_json(401, "Please sign in to view orders.")
                    return
                orders = db.list_orders_for_user(user_id)
                self._send_json(200, {"ok": True, "orders": orders})
                return

            file_path = self._safe_path(path)
            if file_path is None:
                self._send_error_json(404, "Not found")
                return
            self._serve_file(file_path)
        except Exception as exc:
            self.log_message("GET error: %s", exc)
            self._send_error_json(500, "Server error. Please try again.")

    def do_POST(self) -> None:
        try:
            parsed = urlparse(self.path)
            data = self._read_json()
            if data is None:
                self._send_error_json(400, "Invalid JSON body")
                return

            if parsed.path == "/api/signup":
                self._handle_signup(data)
                return
            if parsed.path == "/api/login":
                self._handle_login(data)
                return
            if parsed.path == "/api/orders":
                self._handle_create_order(data)
                return

            self._send_error_json(404, "Not found")
        except Exception as exc:
            self.log_message("POST error: %s", exc)
            self._send_error_json(500, f"Server error: {exc}")

    def _handle_signup(self, data: dict) -> None:
        name = str(data.get("name", "")).strip()
        email = str(data.get("email", "")).strip()
        password = str(data.get("password", ""))
        confirm = str(data.get("confirmPassword", data.get("confirm_password", "")))
        terms = bool(data.get("terms", False))

        if not name or not email or not password or not confirm:
            self._send_error_json(400, "Please fill in all fields.")
            return
        if not EMAIL_RE.match(email):
            self._send_error_json(400, "Please enter a valid email address.")
            return
        if len(password) < 6:
            self._send_error_json(400, "Password must be at least 6 characters.")
            return
        if password != confirm:
            self._send_error_json(400, "Passwords do not match.")
            return
        if not terms:
            self._send_error_json(400, "Please agree to the Terms and Privacy Policy.")
            return

        ok, message, user = db.create_user(name, email, password)
        if not ok:
            status = 409 if "already exists" in message else 400
            self._send_error_json(status, message)
            return

        self._send_json(200, {"ok": True, "message": message, "user": user})

    def _handle_login(self, data: dict) -> None:
        email = str(data.get("email", "")).strip()
        password = str(data.get("password", ""))

        if not email or not password:
            self._send_error_json(400, "Please fill in all fields.")
            return
        if not EMAIL_RE.match(email):
            self._send_error_json(400, "Please enter a valid email address.")
            return

        ok, message, user = db.authenticate_user(email, password)
        if not ok:
            self._send_error_json(401, message)
            return

        self._send_json(200, {"ok": True, "message": message, "user": user})

    def _handle_create_order(self, data: dict) -> None:
        try:
            user_id = int(data.get("user_id", 0))
        except (TypeError, ValueError):
            self._send_error_json(400, "Invalid user_id.")
            return

        if user_id < 1 or db.get_user_by_id(user_id) is None:
            self._send_error_json(401, "Please sign in to place an order.")
            return

        items = data.get("items")
        if not isinstance(items, list):
            self._send_error_json(400, "Items must be a list.")
            return

        notes = str(data.get("notes", ""))
        ok, message, order = db.create_order(user_id, items, notes)
        if not ok:
            self._send_error_json(400, message)
            return

        self._send_json(200, {"ok": True, "message": message, "order": order})


def main() -> None:
    db.init_db()
    server = ThreadingHTTPServer((HOST, PORT), AppHandler)
    print("Hello there grocery server")
    print(f"  URL:  http://{HOST}:{PORT}/login.html")
    print(f"  Home: http://{HOST}:{PORT}/home.html")
    print(f"  DB:   {db.DB_PATH}")
    print("  API:  /api/signup /api/login /api/products /api/orders /api/health")
    print("Press Ctrl+C to stop.\n")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\nShutting down…")
    finally:
        server.server_close()


if __name__ == "__main__":
    main()
