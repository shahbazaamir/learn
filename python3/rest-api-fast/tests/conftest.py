"""
Pytest configuration and shared fixtures.
"""
import asyncio
from typing import AsyncGenerator

import pytest
import pytest_asyncio
from httpx import ASGITransport, AsyncClient
from motor.motor_asyncio import AsyncIOMotorClient, AsyncIOMotorDatabase

from app.core.config import settings
from main import app

# ──────────────────────────────────────────────
# Event loop
# ──────────────────────────────────────────────

@pytest.fixture(scope="session")
def event_loop():
    """Create a single event loop for the entire test session."""
    loop = asyncio.new_event_loop()
    yield loop
    loop.close()


# ──────────────────────────────────────────────
# Database fixtures
# ──────────────────────────────────────────────

TEST_DB_NAME = "products_test_db"


@pytest_asyncio.fixture(scope="session")
async def test_db() -> AsyncGenerator[AsyncIOMotorDatabase, None]:
    """Create a test database and drop it after tests complete."""
    client = AsyncIOMotorClient(settings.MONGODB_URL)
    db = client[TEST_DB_NAME]
    yield db
    # Teardown: drop test database
    await client.drop_database(TEST_DB_NAME)
    client.close()


@pytest_asyncio.fixture(autouse=True)
async def clean_products_collection(test_db: AsyncIOMotorDatabase):
    """Clear the products collection before each test."""
    await test_db["products"].delete_many({})
    yield
    await test_db["products"].delete_many({})


# ──────────────────────────────────────────────
# HTTP client fixture
# ──────────────────────────────────────────────

@pytest_asyncio.fixture(scope="session")
async def client(test_db: AsyncIOMotorDatabase) -> AsyncGenerator[AsyncClient, None]:
    """
    Async HTTP test client with the test database injected.
    Overrides the `get_database` dependency.
    """
    from app.db.database import get_database
    
    async def override_get_database():
        return test_db
    
    app.dependency_overrides[get_database] = override_get_database
    
    async with AsyncClient(
        transport=ASGITransport(app=app), base_url="http://test"
    ) as ac:
        yield ac
    
    app.dependency_overrides.clear()


# ──────────────────────────────────────────────
# Sample data factories
# ──────────────────────────────────────────────

@pytest.fixture
def sample_product_payload() -> dict:
    """Return a valid product creation payload."""
    return {
        "name": "Test Headphones",
        "description": "High-quality test headphones",
        "price": 99.99,
        "stock": 100,
        "category": "Electronics",
        "sku": "TEST-001",
        "tags": ["audio", "wireless"],
        "is_active": True,
    }


@pytest.fixture
def sample_product_payload_2() -> dict:
    """Return a second valid product creation payload."""
    return {
        "name": "Test Keyboard",
        "description": "Mechanical test keyboard",
        "price": 149.99,
        "stock": 50,
        "category": "Electronics",
        "sku": "TEST-002",
        "tags": ["mechanical", "keyboard"],
        "is_active": True,
    }
