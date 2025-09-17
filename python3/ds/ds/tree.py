# tree_module.py

class TreeNode:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None


class BinaryTree:
    def __init__(self):
        self.root = None

    def put(self, value):
        if not self.root:
            self.root = TreeNode(value)
        else:
            self._put(self.root, value)

    def _put(self, node, value):
        raise NotImplementedError("Must be implemented in subclass.")

    def find(self, value):
        return self._find(self.root, value)

    def _find(self, node, value):
        if not node:
            return False
        if node.value == value:
            return True
        return self._find(node.left, value) or self._find(node.right, value)


class BinarySearchTree(BinaryTree):
    def _put(self, node, value):
        if value < node.value:
            if node.left is None:
                node.left = TreeNode(value)
            else:
                self._put(node.left, value)
        else:
            if node.right is None:
                node.right = TreeNode(value)
            else:
                self._put(node.right, value)

    def _find(self, node, value):
        if not node:
            return False
        if value == node.value:
            return True
        elif value < node.value:
            return self._find(node.left, value)
        else:
            return self._find(node.right, value)


class AVLTree(BinarySearchTree):
    def __init__(self):
        super().__init__()
        self.height_map = {}

    def put(self, value):
        self.root = self._put(self.root, value)

    def _height(self, node):
        return self.height_map.get(node, 0)

    def _update_height(self, node):
        self.height_map[node] = 1 + max(self._height(node.left), self._height(node.right))

    def _balance_factor(self, node):
        return self._height(node.left) - self._height(node.right)

    def _rotate_right(self, y):
        x = y.left
        T = x.right

        x.right = y
        y.left = T

        self._update_height(y)
        self._update_height(x)

        return x

    def _rotate_left(self, x):
        y = x.right
        T = y.left

        y.left = x
        x.right = T

        self._update_height(x)
        self._update_height(y)

        return y

    def _put(self, node, value):
        if not node:
            new_node = TreeNode(value)
            self.height_map[new_node] = 1
            return new_node

        if value < node.value:
            node.left = self._put(node.left, value)
        else:
            node.right = self._put(node.right, value)

        self._update_height(node)
        balance = self._balance_factor(node)

        # Balancing
        if balance > 1:
            if value < node.left.value:
                return self._rotate_right(node)
            else:
                node.left = self._rotate_left(node.left)
                return self._rotate_right(node)
        if balance < -1:
            if value > node.right.value:
                return self._rotate_left(node)
            else:
                node.right = self._rotate_right(node.right)
                return self._rotate_left(node)

        return node
