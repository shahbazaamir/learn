"""
Product API endpoints — v1 router.
"""
from typing import Optional

import structlog
from fastapi import APIRouter, Depends, HTTPException, Query, status
from motor.motor_asyncio import AsyncIOMotorDatabase

from app.db.database import get_database
from app.repositories.product_repository import ProductRepository
from app.schemas.product import (
    ErrorResponse,
    ProductCreateRequest,
    ProductListResponse,
    ProductResponse,
    ProductUpdateRequest,
)
from app.services.product_service import ProductService

logger = structlog.get_logger(__name__)

router = APIRouter(prefix="/products", tags=["Products"])


# ──────────────────────────────────────────────
# Dependency
# ──────────────────────────────────────────────

def get_product_service(
    db: AsyncIOMotorDatabase = Depends(get_database),
) -> ProductService:
    """Build and inject ProductService with its repository."""
    repo = ProductRepository(db)
    return ProductService(repo)


# ──────────────────────────────────────────────
# Endpoints
# ──────────────────────────────────────────────

@router.post(
    "/",
    response_model=ProductResponse,
    status_code=status.HTTP_201_CREATED,
    summary="Create a product",
    responses={
        409: {"model": ErrorResponse, "description": "SKU already exists"},
        422: {"model": ErrorResponse, "description": "Validation error"},
    },
)
async def create_product(
    payload: ProductCreateRequest,
    service: ProductService = Depends(get_product_service),
) -> ProductResponse:
    """Create a new product in the catalogue."""
    return await service.create_product(payload)


@router.get(
    "/",
    response_model=ProductListResponse,
    summary="List products",
    responses={
        200: {"description": "Paginated product list"},
    },
)
async def list_products(
    page: int = Query(default=1, ge=1, description="Page number"),
    page_size: int = Query(default=20, ge=1, le=100, description="Items per page"),
    category: Optional[str] = Query(default=None, description="Filter by category"),
    is_active: Optional[bool] = Query(default=None, description="Filter by active status"),
    search: Optional[str] = Query(default=None, description="Full-text search on name/description"),
    sort_by: str = Query(default="created_at", description="Field to sort by"),
    sort_order: str = Query(default="desc", pattern="^(asc|desc)$", description="Sort direction"),
    service: ProductService = Depends(get_product_service),
) -> ProductListResponse:
    """Return a paginated, filterable list of products."""
    return await service.list_products(
        page=page,
        page_size=page_size,
        category=category,
        is_active=is_active,
        search=search,
        sort_by=sort_by,
        sort_order=sort_order,
    )


@router.get(
    "/{product_id}",
    response_model=ProductResponse,
    summary="Get a product",
    responses={
        404: {"model": ErrorResponse, "description": "Product not found"},
    },
)
async def get_product(
    product_id: str,
    service: ProductService = Depends(get_product_service),
) -> ProductResponse:
    """Retrieve a single product by its ID."""
    return await service.get_product(product_id)


@router.patch(
    "/{product_id}",
    response_model=ProductResponse,
    summary="Update a product",
    responses={
        404: {"model": ErrorResponse, "description": "Product not found"},
        422: {"model": ErrorResponse, "description": "Validation error"},
    },
)
async def update_product(
    product_id: str,
    payload: ProductUpdateRequest,
    service: ProductService = Depends(get_product_service),
) -> ProductResponse:
    """Partially update a product. Only provided fields are changed."""
    return await service.update_product(product_id, payload)


@router.delete(
    "/{product_id}",
    status_code=status.HTTP_204_NO_CONTENT,
    summary="Delete a product",
    responses={
        404: {"model": ErrorResponse, "description": "Product not found"},
    },
)
async def delete_product(
    product_id: str,
    service: ProductService = Depends(get_product_service),
) -> None:
    """Permanently delete a product by its ID."""
    await service.delete_product(product_id)


@router.patch(
    "/{product_id}/deactivate",
    response_model=ProductResponse,
    summary="Deactivate a product",
    responses={
        404: {"model": ErrorResponse, "description": "Product not found"},
    },
)
async def deactivate_product(
    product_id: str,
    service: ProductService = Depends(get_product_service),
) -> ProductResponse:
    """Soft-delete a product by setting is_active=false."""
    return await service.deactivate_product(product_id)
