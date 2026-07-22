"""
Product repository — all raw MongoDB operations live here.
"""
from datetime import datetime, timezone
from typing import Optional

import structlog
from bson import ObjectId
from motor.motor_asyncio import AsyncIOMotorDatabase
from pymongo import ASCENDING, DESCENDING, ReturnDocument
from pymongo.errors import DuplicateKeyError

from app.exceptions.custom_exceptions import (
    ProductAlreadyExistsException,
    ProductNotFoundException,
)
from app.models.product import ProductDocument

logger = structlog.get_logger(__name__)

PRODUCTS_COLLECTION = "products"


class ProductRepository:
    """Data access layer for the products collection."""
    
    def __init__(self, db: AsyncIOMotorDatabase) -> None:
        self._db = db
        self._collection = db[PRODUCTS_COLLECTION]
    
    # ──────────────────────────────────────────
    # Index management
    # ──────────────────────────────────────────
    
    async def create_indexes(self) -> None:
        """Create necessary indexes on the products collection."""
        await self._collection.create_index(
            [("sku", ASCENDING)], unique=True, name="sku_unique"
        )
        await self._collection.create_index(
            [("category", ASCENDING)], name="category_idx"
        )
        await self._collection.create_index(
            [("is_active", ASCENDING)], name="is_active_idx"
        )
        await self._collection.create_index(
            [("name", "text"), ("description", "text")], name="text_search_idx"
        )
        logger.info("Product collection indexes created")
    
    # ──────────────────────────────────────────
    # Internal helpers
    # ──────────────────────────────────────────
    
    @staticmethod
    def _doc_to_response(doc: dict) -> dict:
        """Normalise raw MongoDB document to response-friendly dict."""
        if doc is None:
            return None
        doc["id"] = str(doc.pop("_id"))
        return doc
    
    @staticmethod
    def _valid_object_id(product_id: str) -> ObjectId:
        """Validate and convert string to ObjectId, raising 404 if invalid."""
        if not ObjectId.is_valid(product_id):
            raise ProductNotFoundException(product_id)
        return ObjectId(product_id)
    
    # ──────────────────────────────────────────
    # CRUD operations
    # ──────────────────────────────────────────
    
    async def create(self, data: dict) -> dict:
        """
        Insert a new product document.
        
        Args:
            data: Validated product data dict.
        
        Returns:
            The created product document.
        
        Raises:
            ProductAlreadyExistsException: If SKU is not unique.
        """
        now = datetime.now(timezone.utc)
        document = {
            **data,
            "created_at": now,
            "updated_at": now,
        }
        try:
            result = await self._collection.insert_one(document)
            logger.info("Product created", product_id=str(result.inserted_id))
            created_doc = await self._collection.find_one({"_id": result.inserted_id})
            return self._doc_to_response(created_doc)
        except DuplicateKeyError:
            raise ProductAlreadyExistsException(data.get("sku", "unknown"))
    
    async def get_by_id(self, product_id: str) -> dict:
        """
        Retrieve a single product by its ID.
        
        Args:
            product_id: String representation of ObjectId.
        
        Returns:
            Product document dict.
        
        Raises:
            ProductNotFoundException: If product does not exist.
        """
        oid = self._valid_object_id(product_id)
        doc = await self._collection.find_one({"_id": oid})
        if not doc:
            raise ProductNotFoundException(product_id)
        return self._doc_to_response(doc)
    
    async def get_by_sku(self, sku: str) -> Optional[dict]:
        """Retrieve a product by SKU, returning None if not found."""
        doc = await self._collection.find_one({"sku": sku})
        return self._doc_to_response(doc) if doc else None
    
    async def get_all(
        self,
        *,
        page: int = 1,
        page_size: int = 20,
        category: Optional[str] = None,
        is_active: Optional[bool] = None,
        search: Optional[str] = None,
        sort_by: str = "created_at",
        sort_order: str = "desc",
    ) -> tuple[list[dict], int]:
        """
        Retrieve paginated list of products with optional filters.
        
        Returns:
            Tuple of (list of product dicts, total count).
        """
        query: dict = {}
        
        if category:
            query["category"] = category
        if is_active is not None:
            query["is_active"] = is_active
        if search:
            query["$text"] = {"$search": search}
        
        sort_direction = DESCENDING if sort_order.lower() == "desc" else ASCENDING
        skip = (page - 1) * page_size
        
        total = await self._collection.count_documents(query)
        cursor = (
            self._collection
            .find(query)
            .sort(sort_by, sort_direction)
            .skip(skip)
            .limit(page_size)
        )
        docs = await cursor.to_list(length=page_size)
        items = [self._doc_to_response(doc) for doc in docs]
        return items, total
    
    async def update(self, product_id: str, data: dict) -> dict:
        """
        Update a product by ID (partial update).
        
        Args:
            product_id: String representation of ObjectId.
            data: Fields to update (None values excluded).
        
        Returns:
            Updated product document dict.
        
        Raises:
            ProductNotFoundException: If product does not exist.
        """
        oid = self._valid_object_id(product_id)
        update_data = {k: v for k, v in data.items() if v is not None}
        update_data["updated_at"] = datetime.now(timezone.utc)
        
        updated_doc = await self._collection.find_one_and_update(
            {"_id": oid},
            {"$set": update_data},
            return_document=ReturnDocument.AFTER,
        )
        if not updated_doc:
            raise ProductNotFoundException(product_id)
        logger.info("Product updated", product_id=product_id)
        return self._doc_to_response(updated_doc)
    
    async def delete(self, product_id: str) -> bool:
        """
        Hard-delete a product by ID.
        
        Args:
            product_id: String representation of ObjectId.
        
        Returns:
            True if deleted.
        
        Raises:
            ProductNotFoundException: If product does not exist.
        """
        oid = self._valid_object_id(product_id)
        result = await self._collection.delete_one({"_id": oid})
        if result.deleted_count == 0:
            raise ProductNotFoundException(product_id)
        logger.info("Product deleted", product_id=product_id)
        return True
    
    async def soft_delete(self, product_id: str) -> dict:
        """
        Soft-delete a product by marking it inactive.
        
        Returns:
            Updated product document dict.
        """
        return await self.update(product_id, {"is_active": False})
    
    async def count(self, query: Optional[dict] = None) -> int:
        """Return count of documents matching the given query."""
        return await self._collection.count_documents(query or {})
