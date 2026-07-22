"""
MongoDB database connection management using Motor (async driver).
"""
from typing import Optional

import structlog
from motor.motor_asyncio import AsyncIOMotorClient, AsyncIOMotorDatabase
from pymongo.errors import ConnectionFailure, ServerSelectionTimeoutError

from app.core.config import settings
from app.exceptions.custom_exceptions import DatabaseConnectionException

logger = structlog.get_logger(__name__)


class DatabaseManager:
    """Manages MongoDB connection lifecycle."""
    
    def __init__(self) -> None:
        self._client: Optional[AsyncIOMotorClient] = None
        self._db: Optional[AsyncIOMotorDatabase] = None
    
    async def connect(self) -> None:
        """Establish connection to MongoDB."""
        try:
            logger.info(
                "Connecting to MongoDB",
                url=settings.MONGODB_URL,
                db=settings.MONGODB_DB_NAME
            )
            self._client = AsyncIOMotorClient(
                settings.MONGODB_URL,
                **settings.mongodb_connection_params,
                serverSelectionTimeoutMS=5000,
            )
            # Verify connection
            await self._client.admin.command("ping")
            self._db = self._client[settings.MONGODB_DB_NAME]
            logger.info("MongoDB connection established successfully")
        except (ConnectionFailure, ServerSelectionTimeoutError) as e:
            logger.error("Failed to connect to MongoDB", error=str(e))
            raise DatabaseConnectionException(details={"error": str(e)}) from e
    
    async def disconnect(self) -> None:
        """Close MongoDB connection."""
        if self._client is not None:
            self._client.close()
            self._client = None
            self._db = None
            logger.info("MongoDB connection closed")
    
    @property
    def client(self) -> AsyncIOMotorClient:
        """Get the MongoDB client."""
        if self._client is None:
            raise DatabaseConnectionException(
                details={"error": "Database client not initialized"}
            )
        return self._client
    
    @property
    def db(self) -> AsyncIOMotorDatabase:
        """Get the database instance."""
        if self._db is None:
            raise DatabaseConnectionException(
                details={"error": "Database not initialized"}
            )
        return self._db
    
    async def health_check(self) -> bool:
        """Check if the database connection is healthy."""
        try:
            await self._client.admin.command("ping")
            return True
        except Exception as e:
            logger.error("Database health check failed", error=str(e))
            return False


# Global database manager instance
db_manager = DatabaseManager()


async def get_database() -> AsyncIOMotorDatabase:
    """
    Dependency for getting the database instance.
    
    Yields:
        AsyncIOMotorDatabase: The database instance.
    """
    return db_manager.db
