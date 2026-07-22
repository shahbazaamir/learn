"""
Pydantic schemas for Product request/response validation.
"""
from datetime import datetime
from decimal import Decimal
from typing import Optional

from pydantic import BaseModel, Field, field_validator, ConfigDict


class PyObjectId(str):
    """Custom ObjectId type for Pydantic v2 that serializes to string."""
    
    @classmethod
    def __get_validators__(cls):
        yield cls.validate
    
    @classmethod
    def validate(cls, v):
        from bson import ObjectId
        if isinstance(v, ObjectId):
            return str(v)
        if isinstance(v, str) and ObjectId.is_valid(v):
            return v
        raise ValueError("Invalid ObjectId")


# ──────────────────────────────────────────────
# Request Schemas
# ──────────────────────────────────────────────

class ProductCreateRequest(BaseModel):
    """Schema for creating a new product."""
    
    name: str = Field(..., min_length=1, max_length=200, description="Product name")
    description: Optional[str] = Field(None, max_length=2000, description="Product description")
    price: float = Field(..., gt=0, description="Product price (must be greater than 0)")
    stock: int = Field(..., ge=0, description="Available stock quantity")
    category: str = Field(..., min_length=1, max_length=100, description="Product category")
    sku: str = Field(..., min_length=1, max_length=100, description="Stock Keeping Unit identifier")
    tags: list[str] = Field(default=[], description="Product tags")
    is_active: bool = Field(default=True, description="Whether product is active")
    
    model_config = ConfigDict(
        json_schema_extra={
            "example": {
                "name": "Wireless Headphones",
                "description": "High-quality noise-cancelling wireless headphones",
                "price": 129.99,
                "stock": 50,
                "category": "Electronics",
                "sku": "WH-1001",
                "tags": ["wireless", "audio", "headphones"],
                "is_active": True
            }
        }
    )
    
    @field_validator("name", "category", "sku")
    @classmethod
    def strip_whitespace(cls, v: str) -> str:
        """Strip leading/trailing whitespace from string fields."""
        return v.strip()
    
    @field_validator("tags")
    @classmethod
    def lowercase_tags(cls, v: list[str]) -> list[str]:
        """Normalize tags to lowercase."""
        return [tag.strip().lower() for tag in v if tag.strip()]


class ProductUpdateRequest(BaseModel):
    """Schema for updating an existing product (all fields optional)."""
    
    name: Optional[str] = Field(None, min_length=1, max_length=200)
    description: Optional[str] = Field(None, max_length=2000)
    price: Optional[float] = Field(None, gt=0)
    stock: Optional[int] = Field(None, ge=0)
    category: Optional[str] = Field(None, min_length=1, max_length=100)
    tags: Optional[list[str]] = None
    is_active: Optional[bool] = None
    
    model_config = ConfigDict(
        json_schema_extra={
            "example": {
                "price": 99.99,
                "stock": 30,
                "is_active": True
            }
        }
    )
    
    @field_validator("name", "category")
    @classmethod
    def strip_whitespace(cls, v: Optional[str]) -> Optional[str]:
        return v.strip() if v else v
    
    @field_validator("tags")
    @classmethod
    def lowercase_tags(cls, v: Optional[list[str]]) -> Optional[list[str]]:
        if v is None:
            return v
        return [tag.strip().lower() for tag in v if tag.strip()]


# ──────────────────────────────────────────────
# Response Schemas
# ──────────────────────────────────────────────

class ProductResponse(BaseModel):
    """Schema for product responses."""
    
    id: str = Field(..., description="Product unique identifier")
    name: str
    description: Optional[str] = None
    price: float
    stock: int
    category: str
    sku: str
    tags: list[str] = []
    is_active: bool
    created_at: datetime
    updated_at: datetime
    
    model_config = ConfigDict(
        from_attributes=True,
        json_schema_extra={
            "example": {
                "id": "60d5ec49f3a4a82e14b9c123",
                "name": "Wireless Headphones",
                "description": "High-quality noise-cancelling wireless headphones",
                "price": 129.99,
                "stock": 50,
                "category": "Electronics",
                "sku": "WH-1001",
                "tags": ["wireless", "audio", "headphones"],
                "is_active": True,
                "created_at": "2024-01-01T00:00:00Z",
                "updated_at": "2024-01-01T00:00:00Z"
            }
        }
    )


class ProductListResponse(BaseModel):
    """Schema for paginated product list responses."""
    
    items: list[ProductResponse]
    total: int = Field(..., description="Total number of matching products")
    page: int = Field(..., description="Current page number")
    page_size: int = Field(..., description="Number of items per page")
    pages: int = Field(..., description="Total number of pages")


# ──────────────────────────────────────────────
# API Response Envelope
# ──────────────────────────────────────────────

class APIResponse(BaseModel):
    """Standard API response envelope."""
    
    success: bool = True
    message: str = "Operation completed successfully"
    data: Optional[dict | list] = None


class ErrorResponse(BaseModel):
    """Standard error response schema."""
    
    success: bool = False
    message: str
    details: Optional[dict] = None
    error_code: Optional[str] = None
