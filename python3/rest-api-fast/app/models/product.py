"""
MongoDB document model for Product.
"""
from datetime import datetime, timezone
from typing import Optional

from bson import ObjectId
from pydantic import BaseModel, Field, ConfigDict


class ProductDocument(BaseModel):
    """Represents a Product document stored in MongoDB."""
    
    id: Optional[ObjectId] = Field(default=None, alias="_id")
    name: str
    description: Optional[str] = None
    price: float
    stock: int
    category: str
    sku: str
    tags: list[str] = []
    is_active: bool = True
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
    updated_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
    
    model_config = ConfigDict(
        arbitrary_types_allowed=True,
        populate_by_name=True,
        json_encoders={
            ObjectId: str,
            datetime: lambda v: v.isoformat(),
        }
    )
    
    def to_dict(self) -> dict:
        """Serialize to dictionary for MongoDB insertion."""
        data = self.model_dump(by_alias=True, exclude_none=True)
        return data
