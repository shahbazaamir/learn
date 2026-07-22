"""
Tests for /api/v1/products endpoints.
"""
import pytest
from httpx import AsyncClient

pytestmark = pytest.mark.asyncio

BASE_URL = "/api/v1/products"


# ──────────────────────────────────────────────
# POST /products  (Create)
# ──────────────────────────────────────────────

class TestCreateProduct:
    async def test_create_product_success(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        response = await client.post(BASE_URL + "/", json=sample_product_payload)
        assert response.status_code == 201
        data = response.json()
        assert data["name"] == sample_product_payload["name"]
        assert data["sku"] == sample_product_payload["sku"]
        assert data["price"] == sample_product_payload["price"]
        assert "id" in data
        assert "created_at" in data
        assert "updated_at" in data

    async def test_create_product_duplicate_sku(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        await client.post(BASE_URL + "/", json=sample_product_payload)
        response = await client.post(BASE_URL + "/", json=sample_product_payload)
        assert response.status_code == 409
        assert response.json()["success"] is False

    async def test_create_product_missing_required_fields(self, client: AsyncClient):
        response = await client.post(BASE_URL + "/", json={"name": "Incomplete"})
        assert response.status_code == 422

    async def test_create_product_invalid_price(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        payload = {**sample_product_payload, "price": -10}
        response = await client.post(BASE_URL + "/", json=payload)
        assert response.status_code == 422

    async def test_create_product_negative_stock(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        payload = {**sample_product_payload, "stock": -1}
        response = await client.post(BASE_URL + "/", json=payload)
        assert response.status_code == 422

    async def test_create_product_tags_normalised(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        payload = {**sample_product_payload, "tags": ["  AUDIO  ", "WIRELESS"]}
        response = await client.post(BASE_URL + "/", json=payload)
        assert response.status_code == 201
        assert set(response.json()["tags"]) == {"audio", "wireless"}


# ──────────────────────────────────────────────
# GET /products  (List)
# ──────────────────────────────────────────────

class TestListProducts:
    async def test_list_products_empty(self, client: AsyncClient):
        response = await client.get(BASE_URL + "/")
        assert response.status_code == 200
        data = response.json()
        assert data["items"] == []
        assert data["total"] == 0

    async def test_list_products_returns_created(
        self,
        client: AsyncClient,
        sample_product_payload: dict,
        sample_product_payload_2: dict,
    ):
        await client.post(BASE_URL + "/", json=sample_product_payload)
        await client.post(BASE_URL + "/", json=sample_product_payload_2)
        response = await client.get(BASE_URL + "/")
        assert response.status_code == 200
        data = response.json()
        assert data["total"] == 2
        assert len(data["items"]) == 2

    async def test_list_products_pagination(
        self,
        client: AsyncClient,
        sample_product_payload: dict,
        sample_product_payload_2: dict,
    ):
        await client.post(BASE_URL + "/", json=sample_product_payload)
        await client.post(BASE_URL + "/", json=sample_product_payload_2)
        response = await client.get(BASE_URL + "/?page=1&page_size=1")
        assert response.status_code == 200
        data = response.json()
        assert len(data["items"]) == 1
        assert data["total"] == 2
        assert data["pages"] == 2

    async def test_list_products_filter_by_category(
        self,
        client: AsyncClient,
        sample_product_payload: dict,
    ):
        await client.post(BASE_URL + "/", json=sample_product_payload)
        response = await client.get(BASE_URL + "/?category=Electronics")
        assert response.status_code == 200
        assert response.json()["total"] >= 1

    async def test_list_products_filter_nonexistent_category(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        await client.post(BASE_URL + "/", json=sample_product_payload)
        response = await client.get(BASE_URL + "/?category=Nonexistent")
        assert response.status_code == 200
        assert response.json()["total"] == 0

    async def test_list_products_invalid_page_size(self, client: AsyncClient):
        response = await client.get(BASE_URL + "/?page_size=0")
        assert response.status_code == 422


# ──────────────────────────────────────────────
# GET /products/{id}  (Read)
# ──────────────────────────────────────────────

class TestGetProduct:
    async def test_get_product_success(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.get(f"{BASE_URL}/{product_id}")
        assert response.status_code == 200
        assert response.json()["id"] == product_id

    async def test_get_product_not_found(self, client: AsyncClient):
        fake_id = "64c9f1a2e4b0a1b2c3d4e5f6"
        response = await client.get(f"{BASE_URL}/{fake_id}")
        assert response.status_code == 404
        assert response.json()["success"] is False

    async def test_get_product_invalid_id(self, client: AsyncClient):
        response = await client.get(f"{BASE_URL}/not-an-objectid")
        assert response.status_code == 404


# ──────────────────────────────────────────────
# PATCH /products/{id}  (Update)
# ──────────────────────────────────────────────

class TestUpdateProduct:
    async def test_update_product_price(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.patch(
            f"{BASE_URL}/{product_id}", json={"price": 79.99}
        )
        assert response.status_code == 200
        assert response.json()["price"] == 79.99

    async def test_update_product_stock(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.patch(
            f"{BASE_URL}/{product_id}", json={"stock": 200}
        )
        assert response.status_code == 200
        assert response.json()["stock"] == 200

    async def test_update_product_not_found(self, client: AsyncClient):
        fake_id = "64c9f1a2e4b0a1b2c3d4e5f6"
        response = await client.patch(f"{BASE_URL}/{fake_id}", json={"price": 10.0})
        assert response.status_code == 404

    async def test_update_product_invalid_price(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.patch(
            f"{BASE_URL}/{product_id}", json={"price": -5}
        )
        assert response.status_code == 422


# ──────────────────────────────────────────────
# DELETE /products/{id}  (Delete)
# ──────────────────────────────────────────────

class TestDeleteProduct:
    async def test_delete_product_success(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.delete(f"{BASE_URL}/{product_id}")
        assert response.status_code == 204
        # Verify it's gone
        get_resp = await client.get(f"{BASE_URL}/{product_id}")
        assert get_resp.status_code == 404

    async def test_delete_product_not_found(self, client: AsyncClient):
        fake_id = "64c9f1a2e4b0a1b2c3d4e5f6"
        response = await client.delete(f"{BASE_URL}/{fake_id}")
        assert response.status_code == 404


# ──────────────────────────────────────────────
# PATCH /products/{id}/deactivate  (Soft delete)
# ──────────────────────────────────────────────

class TestDeactivateProduct:
    async def test_deactivate_product_success(
        self, client: AsyncClient, sample_product_payload: dict
    ):
        create_resp = await client.post(BASE_URL + "/", json=sample_product_payload)
        product_id = create_resp.json()["id"]
        response = await client.patch(f"{BASE_URL}/{product_id}/deactivate")
        assert response.status_code == 200
        assert response.json()["is_active"] is False

    async def test_deactivate_product_not_found(self, client: AsyncClient):
        fake_id = "64c9f1a2e4b0a1b2c3d4e5f6"
        response = await client.patch(f"{BASE_URL}/{fake_id}/deactivate")
        assert response.status_code == 404


# ──────────────────────────────────────────────
# Health check
# ──────────────────────────────────────────────

class TestHealthCheck:
    async def test_health_endpoint_reachable(self, client: AsyncClient):
        response = await client.get("/health")
        # May be 200 or 503 depending on DB connection in test env
        assert response.status_code in (200, 503)
        assert "status" in response.json()
