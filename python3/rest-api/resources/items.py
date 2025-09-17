
import json
from falcon import HTTP_200, HTTP_201, HTTP_404
from pymongo import MongoClient

class ItemResource:
    def __init__(self):
        # Connect to the MongoDB server
        client = MongoClient('mongodb://localhost:27017/')
        # Access the 'my_falcon_db' database (it will be created if it doesn't exist)
        db = client.my_falcon_db
        # Access the 'items' collection (it will be created if it doesn't exist)
        self.collection = db.items

    def on_get(self, req, resp, item_id=None):
        """Handles GET requests"""
        if item_id:
            # Retrieve a single item by its id
            item = self.collection.find_one({"_id": item_id})
            if item:
                # Convert MongoDB's ObjectId to string
                item['_id'] = str(item['_id'])
                resp.media = item
                resp.status = HTTP_200
            else:
                resp.status = HTTP_404
        else:
            # Retrieve all items
            items = list(self.collection.find())
            # Convert ObjectId to string for all items
            for item in items:
                item['_id'] = str(item['_id'])
            resp.media = items
            resp.status = HTTP_200

    def on_post(self, req, resp):
        """Handles POST requests"""
        new_item = req.media
        # Insert the new item into the MongoDB collection
        result = self.collection.insert_one(new_item)
        # Add the generated MongoDB _id to the item
        new_item['_id'] = str(result.inserted_id)
        resp.media = new_item
        resp.status = HTTP_201

    def on_delete(self, req, resp, item_id):
        """Handles DELETE requests"""
        # Delete an item by its id
        result = self.collection.delete_one({"_id": item_id})
        if result.deleted_count > 0:
            resp.status = HTTP_200
        else:
            resp.status = HTTP_404
