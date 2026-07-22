class Product :
    def __init__(self, name, price):
        self.name = name
        self.price = price

    def display(self):
        print(f"Product Name: {self.name}, Price: ${self.price:.2f}")
    
class Laptop(Product):
    """    
    def __init__(self, name, price, ram, storage):
        super().__init__(name, price)
        self.ram = ram
        self.storage = storage
    """
    def details(self):
###        super().display()
###        print(f"RAM: {self.ram}GB, Storage: {self.storage}GB")
        print("this is a laptop")

def details(product) :
    print(f"Product Name: {product.name}, Price: ${product.price:.2f}")