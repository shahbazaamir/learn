
import falcon
from resources.products import ProductResource
from resources.items import ItemResource

# Create the Falcon API instance
app = falcon.App()

# Create a route for items
#tems = ProductResource()
items = ItemResource()
app.add_route('/items', items)
app.add_route('/items/{item_id}', items)
