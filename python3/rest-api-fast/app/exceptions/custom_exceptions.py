"""
Custom exceptions for the application.
"""
from typing import Any, Dict, Optional


class BaseAPIException(Exception):
    """Base exception for all API exceptions."""
    
    def __init__(
        self,
        message: str,
        status_code: int = 500,
        details: Optional[Dict[str, Any]] = None
    ):
        self.message = message
        self.status_code = status_code
        self.details = details or {}
        super().__init__(self.message)


class ProductNotFoundException(BaseAPIException):
    """Exception raised when a product is not found."""
    
    def __init__(self, product_id: str):
        super().__init__(
            message=f"Product with ID '{product_id}' not found",
            status_code=404,
            details={"product_id": product_id}
        )


class ProductAlreadyExistsException(BaseAPIException):
    """Exception raised when attempting to create a product that already exists."""
    
    def __init__(self, identifier: str):
        super().__init__(
            message=f"Product with identifier '{identifier}' already exists",
            status_code=409,
            details={"identifier": identifier}
        )


class DatabaseConnectionException(BaseAPIException):
    """Exception raised when database connection fails."""
    
    def __init__(self, details: Optional[Dict[str, Any]] = None):
        super().__init__(
            message="Failed to connect to database",
            status_code=503,
            details=details or {}
        )


class ValidationException(BaseAPIException):
    """Exception raised for validation errors."""
    
    def __init__(self, message: str, details: Optional[Dict[str, Any]] = None):
        super().__init__(
            message=message,
            status_code=422,
            details=details or {}
        )
