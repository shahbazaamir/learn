# Products REST API

A production-level REST API built with **FastAPI** and **MongoDB** for managing a products catalogue.

## Features

- Full CRUD on a `products` collection
- Soft-delete (deactivate) alongside hard-delete
- Pagination, filtering (category, active status), and full-text search
- Layered architecture: Router → Service → Repository → MongoDB
- Pydantic v2 request/response validation
- Structured JSON logging via `structlog`
- Global exception handlers with consistent error envelopes
- MongoDB index management (unique SKU, category, text search)
- Docker & docker-compose setup (multi-stage image, Mongo Express UI)
- Async test suite (`pytest-asyncio` + `httpx`)

---

## Project Structure

```
rest-api-fast/
├── app/
│   ├── api/
│   │   └── v1/
│   │       ├── endpoints/
│   │       │   └── products.py      # Route handlers
│   │       └── router.py            # v1 router aggregator
│   ├── core/
│   │   ├── config.py                # Pydantic settings (env-driven)
│   │   └── logging.py               # structlog configuration
│   ├── db/
│   │   └── database.py              # Motor client + health check
│   ├── exceptions/
│   │   ├── custom_exceptions.py     # Domain exceptions
│   │   └── handlers.py              # FastAPI exception handlers
│   ├── models/
│   │   └── product.py               # MongoDB document model
│   ├── repositories/
│   │   └── product_repository.py    # Raw DB operations
│   ├── schemas/
│   │   └── product.py               # Request / response schemas
│   └── services/
│       └── product_service.py       # Business logic
├── tests/
│   ├── conftest.py                  # Fixtures (test DB, async client)
│   └── test_products.py             # Endpoint test suite
├── logs/                            # Rotating log files (git-ignored)
├── .env                             # Local environment variables
├── .env.example                     # Template for .env
├── Dockerfile                       # Multi-stage production image
├── docker-compose.yml               # App + MongoDB + Mongo Express
├── main.py                          # Application entry point
├── pytest.ini
└── requirements.txt
```

---

## Quick Start

### Local (venv)

```bash
# 1. Create and activate a virtual environment
python3 -m venv .venv
source .venv/bin/activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Copy and edit the env file
cp .env.example .env
# Edit MONGODB_URL if needed

# 4. Start MongoDB locally (or use Docker)
docker run -d -p 27017:27017 --name mongo mongo:7.0

# 5. Run the API
python main.py
# or
uvicorn main:app --reload
```

API is available at **http://localhost:8000**  
Swagger UI (dev only): **http://localhost:8000/docs**

---

### Docker Compose

```bash
# Start API + MongoDB
docker compose up -d

# Start API + MongoDB + Mongo Express UI
docker compose --profile dev up -d

# View logs
docker compose logs -f api

# Stop
docker compose down
```

| Service | URL |
|---------|-----|
| API | http://localhost:8000 |
| Swagger UI | http://localhost:8000/docs |
| Mongo Express | http://localhost:8081 (admin/admin123) |

---

## API Reference

All endpoints live under `/api/v1/products`.

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/v1/products/` | Create a product |
| `GET` | `/api/v1/products/` | List products (paginated) |
| `GET` | `/api/v1/products/{id}` | Get a product by ID |
| `PATCH` | `/api/v1/products/{id}` | Partially update a product |
| `DELETE` | `/api/v1/products/{id}` | Hard-delete a product |
| `PATCH` | `/api/v1/products/{id}/deactivate` | Soft-delete (deactivate) |
| `GET` | `/health` | Health check |

### Create Product — `POST /api/v1/products/`

**Request body:**
```json
{
  "name": "Wireless Headphones",
  "description": "Noise-cancelling over-ear headphones",
  "price": 129.99,
  "stock": 50,
  "category": "Electronics",
  "sku": "WH-1001",
  "tags": ["wireless", "audio"],
  "is_active": true
}
```

**Response `201`:**
```json
{
  "id": "60d5ec49f3a4a82e14b9c123",
  "name": "Wireless Headphones",
  "price": 129.99,
  "stock": 50,
  "category": "Electronics",
  "sku": "WH-1001",
  "tags": ["wireless", "audio"],
  "is_active": true,
  "created_at": "2024-01-01T00:00:00Z",
  "updated_at": "2024-01-01T00:00:00Z"
}
```

### List Products — `GET /api/v1/products/`

Query parameters:

| Param | Type | Default | Description |
|-------|------|---------|-------------|
| `page` | int | 1 | Page number |
| `page_size` | int | 20 | Items per page (max 100) |
| `category` | string | — | Filter by category |
| `is_active` | bool | — | Filter by active status |
| `search` | string | — | Full-text search (name/description) |
| `sort_by` | string | `created_at` | Field to sort by |
| `sort_order` | string | `desc` | `asc` or `desc` |

### Error Response

All errors follow a consistent envelope:
```json
{
  "success": false,
  "message": "Product with ID '...' not found",
  "details": { "product_id": "..." }
}
```

---

## Running Tests

Tests require a running MongoDB instance (uses an isolated `products_test_db` database).

```bash
# With MongoDB running locally
pytest -v

# With coverage
pip install pytest-cov
pytest --cov=app --cov-report=term-missing
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `APP_NAME` | Products API | Application name |
| `APP_ENV` | development | Environment name |
| `DEBUG` | true | Enable debug mode / Swagger UI |
| `MONGODB_URL` | mongodb://localhost:27017 | MongoDB connection string |
| `MONGODB_DB_NAME` | products_db | Database name |
| `LOG_LEVEL` | INFO | Logging level |
| `SECRET_KEY` | — | **Required in production** |

---

## Production Checklist

- [ ] Set `DEBUG=false` and `APP_ENV=production`
- [ ] Set a strong random `SECRET_KEY`
- [ ] Restrict `CORS` origins in `main.py`
- [ ] Use MongoDB Atlas or a secured self-hosted instance
- [ ] Enable TLS/HTTPS (via reverse proxy, e.g. nginx)
- [ ] Set up log aggregation (e.g. Datadog, ELK)
- [ ] Configure resource limits in docker-compose
