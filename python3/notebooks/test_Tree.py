from ds.tree import BinarySearchTree, AVLTree

print("Binary Search Tree:")
bst = BinarySearchTree()
bst.put(10)
bst.put(5)
bst.put(15)
print(bst.find(5))   # True
print(bst.find(20))  # False

print("\nAVL Tree:")
avl = AVLTree()
for val in [10, 20, 30, 40, 50, 25]:
    avl.put(val)
print(avl.find(25))  # True
print(avl.find(100)) # False
