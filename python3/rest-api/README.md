my_falcon_api/
│
├── app.py
├── resources/
│   ├── __init__.py
│   ├── items.py
├── requirements.txt
└── wsgi.py



python3 -m venv venv
source venv/bin/activate


pip install -r requirements.txt --user


curl http://127.0.0.1:8000/items


curl -X POST http://127.0.0.1:8000/items -d '{"name": "Laptop"}' -H "Content-Type: application/json"


curl -X DELETE http://127.0.0.1:8000/items/1


curl http://127.0.0.1:8000/items/1





