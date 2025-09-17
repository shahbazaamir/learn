
from app import app

# This file serves as the entry point for WSGI servers like Gunicorn
if __name__ == "__main__":
    from wsgiref import simple_server
    httpd = simple_server.make_server('127.0.0.1', 8000, app)
    httpd.serve_forever()
