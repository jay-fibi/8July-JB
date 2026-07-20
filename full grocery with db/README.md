# Full grocery with db

Hello there grocery — login/signup, product catalog, cart, and orders backed by SQLite.

## Run

```bash
python3 server.py
```

Open: http://127.0.0.1:8000/login.html

## Features

- Sign up / sign in (passwords hashed with PBKDF2)
- Protected store home page
- Product catalog (seeded in SQLite)
- Cart + place order (stored in DB)
- Order history per user

## API

- `POST /api/signup`
- `POST /api/login`
- `GET /api/products`
- `GET /api/orders?user_id=`
- `POST /api/orders`
- `GET /api/health`

The SQLite file `users.db` is created automatically on first run.
