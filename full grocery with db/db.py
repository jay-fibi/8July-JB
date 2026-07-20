"""Lightweight SQLite helpers for Hello there grocery auth + orders."""

from __future__ import annotations

import hashlib
import hmac
import secrets
import sqlite3
from pathlib import Path

DB_PATH = Path(__file__).resolve().parent / "users.db"

_ITERATIONS = 120_000
_SALT_BYTES = 16

SEED_PRODUCTS = [
    ("Organic Bananas", "Fruit", 1.29, "🍌", "Sweet ripe bananas, sold per lb"),
    ("Honeycrisp Apples", "Fruit", 2.49, "🍎", "Crisp and juicy, per lb"),
    ("Navel Oranges", "Fruit", 1.99, "🍊", "Seedless oranges, per lb"),
    ("Fresh Avocados", "Fruit", 1.75, "🥑", "Ready to eat, each"),
    ("Red Grapes", "Fruit", 3.49, "🍇", "Seedless red grapes, per lb"),
    ("Broccoli Crowns", "Veggies", 2.29, "🥦", "Fresh green broccoli"),
    ("Baby Carrots", "Veggies", 1.89, "🥕", "1 lb bag"),
    ("Roma Tomatoes", "Veggies", 1.59, "🍅", "Perfect for sauces, per lb"),
    ("Leafy Spinach", "Veggies", 2.99, "🥬", "Pre-washed 10 oz"),
    ("Whole Milk", "Dairy", 3.49, "🥛", "Gallon, vitamin D"),
    ("Cheddar Cheese", "Dairy", 4.99, "🧀", "8 oz block"),
    ("Greek Yogurt", "Dairy", 1.29, "🥛", "Single serve cup"),
    ("Sourdough Loaf", "Bakery", 4.50, "🥖", "Fresh baked daily"),
    ("Croissants", "Bakery", 5.99, "🥐", "Pack of 4"),
    ("Free-range Eggs", "Dairy", 4.29, "🥚", "Dozen large"),
    ("Olive Oil", "Pantry", 8.99, "🫒", "Extra virgin 500ml"),
    ("Basmati Rice", "Pantry", 6.49, "🍚", "2 lb bag"),
    ("Pasta Penne", "Pantry", 1.99, "🍝", "16 oz box"),
    ("Chicken Breast", "Meat", 7.99, "🍗", "Boneless, per lb"),
    ("Atlantic Salmon", "Meat", 12.99, "🐟", "Fresh fillet, per lb"),
]


def get_connection() -> sqlite3.Connection:
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON")
    return conn


def init_db() -> None:
    """Create tables and seed products if empty."""
    with get_connection() as conn:
        conn.executescript(
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE COLLATE NOCASE,
                password_hash TEXT NOT NULL,
                created_at TEXT NOT NULL DEFAULT (datetime('now'))
            );

            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                price REAL NOT NULL,
                emoji TEXT NOT NULL DEFAULT '',
                description TEXT NOT NULL DEFAULT '',
                in_stock INTEGER NOT NULL DEFAULT 1
            );

            CREATE TABLE IF NOT EXISTS orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                total REAL NOT NULL,
                status TEXT NOT NULL DEFAULT 'placed',
                notes TEXT NOT NULL DEFAULT '',
                created_at TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );

            CREATE TABLE IF NOT EXISTS order_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                order_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                product_name TEXT NOT NULL,
                unit_price REAL NOT NULL,
                quantity INTEGER NOT NULL,
                line_total REAL NOT NULL,
                FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                FOREIGN KEY (product_id) REFERENCES products(id)
            );
            """
        )

        count = conn.execute("SELECT COUNT(*) AS n FROM products").fetchone()["n"]
        if count == 0:
            conn.executemany(
                """
                INSERT INTO products (name, category, price, emoji, description)
                VALUES (?, ?, ?, ?, ?)
                """,
                SEED_PRODUCTS,
            )
        conn.commit()


def _hash_password(password: str, salt: bytes | None = None) -> str:
    if salt is None:
        salt = secrets.token_bytes(_SALT_BYTES)
    digest = hashlib.pbkdf2_hmac(
        "sha256",
        password.encode("utf-8"),
        salt,
        _ITERATIONS,
    )
    return f"{salt.hex()}${digest.hex()}"


def _verify_password(password: str, stored: str) -> bool:
    try:
        salt_hex, _hash_hex = stored.split("$", 1)
        salt = bytes.fromhex(salt_hex)
    except ValueError:
        return False
    candidate = _hash_password(password, salt)
    return hmac.compare_digest(candidate, stored)


def create_user(name: str, email: str, password: str) -> tuple[bool, str, dict | None]:
    name = name.strip()
    email = email.strip().lower()

    if not name or not email or not password:
        return False, "Please fill in all fields.", None
    if "@" not in email or "." not in email.split("@")[-1]:
        return False, "Please enter a valid email address.", None
    if len(password) < 6:
        return False, "Password must be at least 6 characters.", None

    password_hash = _hash_password(password)

    try:
        with get_connection() as conn:
            cur = conn.execute(
                "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)",
                (name, email, password_hash),
            )
            conn.commit()
            user_id = cur.lastrowid
    except sqlite3.IntegrityError:
        return False, "An account with this email already exists.", None

    return True, "Account created successfully.", {
        "id": user_id,
        "name": name,
        "email": email,
    }


def authenticate_user(email: str, password: str) -> tuple[bool, str, dict | None]:
    email = email.strip().lower()

    if not email or not password:
        return False, "Please fill in all fields.", None

    with get_connection() as conn:
        row = conn.execute(
            "SELECT id, name, email, password_hash FROM users WHERE email = ?",
            (email,),
        ).fetchone()

    if row is None or not _verify_password(password, row["password_hash"]):
        return False, "Invalid email or password.", None

    return True, "Login successful.", {
        "id": row["id"],
        "name": row["name"],
        "email": row["email"],
    }


def get_user_by_id(user_id: int) -> dict | None:
    with get_connection() as conn:
        row = conn.execute(
            "SELECT id, name, email FROM users WHERE id = ?",
            (user_id,),
        ).fetchone()
    if row is None:
        return None
    return {"id": row["id"], "name": row["name"], "email": row["email"]}


def user_count() -> int:
    with get_connection() as conn:
        row = conn.execute("SELECT COUNT(*) AS n FROM users").fetchone()
        return int(row["n"])


def list_products(category: str | None = None) -> list[dict]:
    with get_connection() as conn:
        if category and category.lower() != "all":
            rows = conn.execute(
                """
                SELECT id, name, category, price, emoji, description, in_stock
                FROM products
                WHERE in_stock = 1 AND category = ?
                ORDER BY name
                """,
                (category,),
            ).fetchall()
        else:
            rows = conn.execute(
                """
                SELECT id, name, category, price, emoji, description, in_stock
                FROM products
                WHERE in_stock = 1
                ORDER BY category, name
                """
            ).fetchall()
    return [
        {
            "id": r["id"],
            "name": r["name"],
            "category": r["category"],
            "price": float(r["price"]),
            "emoji": r["emoji"],
            "description": r["description"],
            "in_stock": bool(r["in_stock"]),
        }
        for r in rows
    ]


def list_categories() -> list[str]:
    with get_connection() as conn:
        rows = conn.execute(
            "SELECT DISTINCT category FROM products WHERE in_stock = 1 ORDER BY category"
        ).fetchall()
    return [r["category"] for r in rows]


def create_order(
    user_id: int,
    items: list[dict],
    notes: str = "",
) -> tuple[bool, str, dict | None]:
    """
    items: [{product_id, quantity}, ...]
    Validates products, computes totals, inserts order + line items.
    """
    if not items:
        return False, "Your cart is empty.", None

    user = get_user_by_id(user_id)
    if user is None:
        return False, "User not found. Please sign in again.", None

    # Normalize quantities
    qty_by_product: dict[int, int] = {}
    for raw in items:
        try:
            pid = int(raw.get("product_id"))
            qty = int(raw.get("quantity"))
        except (TypeError, ValueError):
            return False, "Invalid cart item.", None
        if qty < 1 or qty > 99:
            return False, "Quantity must be between 1 and 99.", None
        qty_by_product[pid] = qty_by_product.get(pid, 0) + qty

    if not qty_by_product:
        return False, "Your cart is empty.", None

    notes = (notes or "").strip()[:500]

    with get_connection() as conn:
        placeholders = ",".join("?" * len(qty_by_product))
        rows = conn.execute(
            f"""
            SELECT id, name, price, in_stock
            FROM products
            WHERE id IN ({placeholders})
            """,
            tuple(qty_by_product.keys()),
        ).fetchall()

        found = {r["id"]: r for r in rows}
        if len(found) != len(qty_by_product):
            return False, "One or more products are unavailable.", None

        line_rows: list[tuple] = []
        total = 0.0
        for pid, qty in qty_by_product.items():
            p = found[pid]
            if not p["in_stock"]:
                return False, f"{p['name']} is out of stock.", None
            unit = float(p["price"])
            line_total = round(unit * qty, 2)
            total += line_total
            line_rows.append(
                (pid, p["name"], unit, qty, line_total)
            )

        total = round(total, 2)

        cur = conn.execute(
            """
            INSERT INTO orders (user_id, total, status, notes)
            VALUES (?, ?, 'placed', ?)
            """,
            (user_id, total, notes),
        )
        order_id = cur.lastrowid

        conn.executemany(
            """
            INSERT INTO order_items
                (order_id, product_id, product_name, unit_price, quantity, line_total)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            [
                (order_id, pid, name, unit, qty, line_total)
                for pid, name, unit, qty, line_total in line_rows
            ],
        )
        conn.commit()

    order = get_order(order_id, user_id)
    return True, "Order placed successfully.", order


def get_order(order_id: int, user_id: int | None = None) -> dict | None:
    with get_connection() as conn:
        if user_id is not None:
            row = conn.execute(
                """
                SELECT id, user_id, total, status, notes, created_at
                FROM orders
                WHERE id = ? AND user_id = ?
                """,
                (order_id, user_id),
            ).fetchone()
        else:
            row = conn.execute(
                """
                SELECT id, user_id, total, status, notes, created_at
                FROM orders
                WHERE id = ?
                """,
                (order_id,),
            ).fetchone()

        if row is None:
            return None

        items = conn.execute(
            """
            SELECT product_id, product_name, unit_price, quantity, line_total
            FROM order_items
            WHERE order_id = ?
            ORDER BY id
            """,
            (order_id,),
        ).fetchall()

    return {
        "id": row["id"],
        "user_id": row["user_id"],
        "total": float(row["total"]),
        "status": row["status"],
        "notes": row["notes"] or "",
        "created_at": row["created_at"],
        "items": [
            {
                "product_id": i["product_id"],
                "product_name": i["product_name"],
                "unit_price": float(i["unit_price"]),
                "quantity": i["quantity"],
                "line_total": float(i["line_total"]),
            }
            for i in items
        ],
    }


def list_orders_for_user(user_id: int) -> list[dict]:
    with get_connection() as conn:
        rows = conn.execute(
            """
            SELECT id, user_id, total, status, notes, created_at
            FROM orders
            WHERE user_id = ?
            ORDER BY datetime(created_at) DESC, id DESC
            """,
            (user_id,),
        ).fetchall()

        result = []
        for row in rows:
            items = conn.execute(
                """
                SELECT product_id, product_name, unit_price, quantity, line_total
                FROM order_items
                WHERE order_id = ?
                ORDER BY id
                """,
                (row["id"],),
            ).fetchall()
            result.append(
                {
                    "id": row["id"],
                    "user_id": row["user_id"],
                    "total": float(row["total"]),
                    "status": row["status"],
                    "notes": row["notes"] or "",
                    "created_at": row["created_at"],
                    "items": [
                        {
                            "product_id": i["product_id"],
                            "product_name": i["product_name"],
                            "unit_price": float(i["unit_price"]),
                            "quantity": i["quantity"],
                            "line_total": float(i["line_total"]),
                        }
                        for i in items
                    ],
                }
            )
    return result


if __name__ == "__main__":
    init_db()
    print(f"Database ready at {DB_PATH}")
    print(f"Users: {user_count()}")
    print(f"Products: {len(list_products())}")
