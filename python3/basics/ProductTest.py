from product import Product
from product import Laptop,details


product1 = Product("Laptop", 999.99)
product2 = Laptop("Gaming Laptop", 9991.99)

print("Product Details:")
product1.display()
product2.display()

class ProductX:
    def __init__( self):
        self.name = "x"
        self.price = "x"


product3 = ProductX(  )    
product2.details()
details(product3)

