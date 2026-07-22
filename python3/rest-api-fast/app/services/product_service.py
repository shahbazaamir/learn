"""
Product service — orchestrates business logic and delegates DB work to the repository.
"""
import math
from typing import Optional

import structlog

from app.repositories.product_repository import ProductRepository
from app.schemas.product import (
    ProductCreateRequest,
    ProductListResponse,
    ProductResponse,
    ProductUpdateRequest,
)

logger = structlog.get_logger(__name__)


class ProductService:
    """Business logic layer for products."""
    
    def __init__(self, repository: ProductRepository) -> None:
        self._repo = repository
    
    # ──────────────────────────────────────────
    # Internal helpers
    # ──────────────────────────────────────────
    
    @staticmethod
    def _to_product_response(data: dict) -> ProductResponse:
        return ProductResponse(**data)
    
    # ──────────────────────────────────────────
    # CRUD methods
    # ──────────────────────────────────────────
    
    async def create_product(self, payload: ProductCreateRequest) -> ProductResponse:
        """
        Create a new product.
        
        Args:
            payload: Validated create request.
        
        Returns:
            ProductResponse: The created product.
        """
        logger.info("Creating product", sku=payload.sku, name=payload.name)
        data = payload.model_dump()
        product_doc = await self._repo.create(data)
        return self._to_product_response(product_doc)
    
    async def get_product(self, product_id: str) -> ProductResponse:
        """
        Retrieve a product by ID.
        
        Args:
            product_id: The product's unique identifier.
        
        Returns:
            ProductResponse: The found product.
        """
        product_doc = await self._repo.get_by_id(product_id)
        return self._to_product_response(product_doc)
    
    async def list_products(
        self,
        *,
        page: int = 1,
        page_size: int = 20,
        category: Optional[str] = None,
        is_active: Optional[bool] = None,
        search: Optional[str] = None,
        sort_by: str = "created_at",
        sort_order: str = "desc",
    ) -> ProductListResponse:
        """
        List products with pagination and optional filters.
        
        Returns:
            ProductListResponse: Paginated product list.
        """
        items_raw, total = await self._repo.get_all(
            page=page,
            page_size=page_size,
            category=category,
            is_active=is_active,
            search=search,
            sort_by=sort_by,
            sort_order=sort_order,
        )
        items = [self._to_product_response(doc) for doc in items_raw]
        pages = math.ceil(total / page_size) if page_size else 1
        return ProductListResponse(
            items=items,
            total=total,
            page=page,
            page_size=page_size,
            pages=pages,
        )
    
    async def update_product(
        self, product_id: str, payload: ProductUpdateRequest
    ) -> ProductResponse:
        """
        Partially update a product.
        
        Args:
            product_id: The product's unique identifier.
            payload: Fields to update (only non-None values applied).
        
        Returns:
            ProductResponse: The updated product.
        """
        logger.info("Updating product", product_id=product_id)
        update_data = payload.model_dump(exclude_none=True)
        product_doc = await self._repo.update(product_id, update_data)
        return self._to_product_response(product_doc)
    
    async def delete_product(self, product_id: str) -> bool:
        """
        Permanently delete a product.
        
        Args:
            product_id: The product's unique identifier.
        
        Returns:
            True on success.
        """
        logger.info("Deleting product", product_id=product_id)
        return await self._repo.delete(product_id)
    
    async def deactivate_product(self, product_id: str) -> ProductResponse:
        """
        Soft-delete (deactivate) a product.
        
        Args:
            product_id: The product's unique identifier.
        
        Returns:
            ProductResponse: The deactivated product.
        """
        logger.info("Deactivating product", product_id=product_id)
        product_doc = await self._repo.soft_delete(product_id)
        return self._to_product_response(product_doc)
