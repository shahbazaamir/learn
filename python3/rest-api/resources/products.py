
import json
from falcon import HTTP_200, HTTP_201, HTTP_404


class ProductResource:
    def __init__(self):
        self.items = {}

    def on_get(self, req, resp, item_id=None):
        """Handles GET requests"""
        if item_id:
            item = self.items.get(item_id)
            if item:
                resp.media = item
                resp.status = HTTP_200
            else:
                resp.status = HTTP_404
        else:
            resp.media = list(self.items.values())
            resp.status = HTTP_200

    def on_post(self, req, resp):
        """Handles POST requests"""
        new_item = req.media
        item_id = str(len(self.items) + 1)
        new_item['id'] = item_id
        self.items[item_id] = new_item
        resp.media = new_item
        resp.status = HTTP_201

    def on_delete(self, req, resp, item_id):
        """Handles DELETE requests"""
        if item_id in self.items:
            del self.items[item_id]
            resp.status = HTTP_200
        else:
            resp.status = HTTP_404
