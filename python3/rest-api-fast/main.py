"""
Main FastAPI application factory and entry point.
"""
import time
from contextlib import asynccontextmanager

import structlog
import uvicorn
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.middleware.trustedhost import TrustedHostMiddleware
from fastapi.responses import JSONResponse

from app.api.v1.router import api_v1_router
from app.core.config import settings
from app.core.logging import setup_logging
from app.db.database import db_manager
from app.exceptions.handlers import register_exception_handlers
from app.repositories.product_repository import ProductRepository

# Initialise structured logging first
setup_logging()
logger = structlog.get_logger(__name__)


# ──────────────────────────────────────────────
# Lifespan (startup / shutdown)
# ──────────────────────────────────────────────

@asynccontextmanager
async def lifespan(app: FastAPI):
    """Manage application startup and shutdown."""
    logger.info(
        "Starting application",
        name=settings.APP_NAME,
        version=settings.APP_VERSION,
        env=settings.APP_ENV,
    )
    
    # Connect to MongoDB
    await db_manager.connect()
    
    # Ensure indexes exist
    product_repo = ProductRepository(db_manager.db)
    await product_repo.create_indexes()
    
    logger.info("Application startup complete")
    yield
    
    # Shutdown
    logger.info("Shutting down application")
    await db_manager.disconnect()


# ──────────────────────────────────────────────
# App factory
# ──────────────────────────────────────────────

def create_app() -> FastAPI:
    """Create and configure the FastAPI application."""
    
    app = FastAPI(
        title=settings.APP_NAME,
        version=settings.APP_VERSION,
        description="Production-level REST API for managing a products catalogue. Backed by MongoDB.",
        docs_url="/docs" if settings.DEBUG else None,
        redoc_url="/redoc" if settings.DEBUG else None,
        openapi_url="/openapi.json" if settings.DEBUG else None,
        lifespan=lifespan,
    )
    
    # ── Middleware ──────────────────────────────
    
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"] if settings.DEBUG else [],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )
    
    if not settings.DEBUG:
        app.add_middleware(TrustedHostMiddleware, allowed_hosts=["*"])
    
    # ── Request logging middleware ──────────────
    
    @app.middleware("http")
    async def log_requests(request: Request, call_next):
        start = time.perf_counter()
        response = await call_next(request)
        duration_ms = round((time.perf_counter() - start) * 1000, 2)
        logger.info(
            "Request handled",
            method=request.method,
            path=request.url.path,
            status_code=response.status_code,
            duration_ms=duration_ms,
        )
        response.headers["X-Process-Time-Ms"] = str(duration_ms)
        return response
    
    # ── Exception handlers ──────────────────────
    register_exception_handlers(app)
    
    # ── Routers ────────────────────────────────
    app.include_router(api_v1_router, prefix=settings.API_V1_PREFIX)
    
    # ── Health & root endpoints ─────────────────
    
    @app.get("/", include_in_schema=False)
    async def root():
        return {
            "app": settings.APP_NAME,
            "version": settings.APP_VERSION,
            "status": "running",
        }
    
    @app.get("/health", tags=["Health"])
    async def health_check():
        """Check application and database health."""
        db_healthy = await db_manager.health_check()
        overall = "healthy" if db_healthy else "degraded"
        status_code = 200 if db_healthy else 503
        return JSONResponse(
            status_code=status_code,
            content={
                "status": overall,
                "database": "up" if db_healthy else "down",
                "version": settings.APP_VERSION,
            },
        )
    
    return app


app = create_app()


# ──────────────────────────────────────────────
# Run directly with `python main.py`
# ──────────────────────────────────────────────

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG,
        log_level=settings.LOG_LEVEL.lower(),
    )
