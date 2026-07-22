"""
API v1 router — aggregates all v1 endpoint routers.
"""
from fastapi import APIRouter

from app.api.v1.endpoints.products import router as products_router

api_v1_router = APIRouter()
api_v1_router.include_router(products_router)
