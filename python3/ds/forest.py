from __future__ import annotations
from collections import deque
from typing import Any, Dict, List, Optional, Iterator, Iterable


class TreeNode:  # pylint: disable=too-few-public-methods
    """A single node in a rooted tree used inside a Forest.

    Attributes
    ----------
    value: Any
        The data stored in the node.
    parent: Optional[TreeNode]
        Reference to the parent node. *None* if the node is a root.
    children: List[TreeNode]
        List of references to child nodes (ordered).
    """

    __slots__ = ("value", "parent", "children")

    def __init__(self, value: Any):
        self.value: Any = value
        self.parent: Optional[TreeNode] = None
        self.children: List[TreeNode] = []

    # ---------------------------------------------------------------------
    # Basic relationships
    # ---------------------------------------------------------------------
    def add_child(self, child: "TreeNode") -> None:
        """Attach *child* as the last child of *self*."""
        child.parent = self
        self.children.append(child)

    def remove_child(self, child: "TreeNode") -> None:
        """Detach *child* from *self* (if present)."""
        self.children.remove(child)
        child.parent = None

    def is_root(self) -> bool:  # noqa: D401
        """Return *True* if the node has no parent."""
        return self.parent is None

    def is_leaf(self) -> bool:  # noqa: D401
        """Return *True* if the node has no children."""
        return not self.children

    # ------------------------------------------------------------------
    # Traversal helpers (yield nodes)
    # ------------------------------------------------------------------
    def dfs(self) -> Iterator["TreeNode"]:
        """Depth‑first traversal starting from *self* (pre‑order)."""
        stack = [self]
        while stack:
            node = stack.pop()
            yield node
            # Extend in reverse so that the left‑most child is processed first.
            stack.extend(reversed(node.children))

    def bfs(self) -> Iterator["TreeNode"]:
        """Breadth‑first traversal starting from *self*."""
        q: deque[TreeNode] = deque([self])
        while q:
            node = q.popleft()
            yield node
            q.extend(node.children)

    # ------------------------------------------------------------------
    # Utility ‑ for nicer debugging / printing
    # ------------------------------------------------------------------
    def __repr__(self) -> str:  # noqa: D401
        return f"TreeNode({self.value!r})"


class Forest:
    """A collection of *disjoint rooted trees* (a forest).

    Each tree is represented by its **root** node.  Nodes are stored in an
    internal dictionary so any node can be accessed in *O(1)* by its *id()*.

    The Forest offers convenience methods to create trees, attach or detach
    nodes, delete subtrees, and perform traversals over the entire forest or
    individual trees.
    """

    def __init__(self):
        self._roots: List[TreeNode] = []
        self._nodes: Dict[int, TreeNode] = {}

    # ------------------------------------------------------------------
    # Tree / node creation helpers
    # ------------------------------------------------------------------
    def create_tree(self, value: Any) -> TreeNode:
        """Create a **new tree** with a single root containing *value*.

        Returns the *root* node so the caller can add children or store a
        reference. The node is automatically registered inside the Forest.
        """
        root = TreeNode(value)
        self._register_node(root)
        self._roots.append(root)
        return root

    def add_child(self, parent: TreeNode, value: Any) -> TreeNode:
        """Create a new *child* node with *value* and attach it to *parent*."""
        if parent not in self._nodes.values():  # pragma: no cover – sanity check
            raise ValueError("Parent node does not belong to this Forest")
        child = TreeNode(value)
        self._register_node(child)
        parent.add_child(child)
        return child

    def move_subtree(self, node: TreeNode, new_parent: TreeNode) -> None:
        """Re‑attach an existing *node* (subtree) under *new_parent*."""
        if node is new_parent or _is_descendant(new_parent, node):
            raise ValueError("Cannot move a node under itself or its descendant")
        if node.parent is not None:
            node.parent.remove_child(node)
        else:
            # node was a root; remove from roots list
            self._roots.remove(node)
        new_parent.add_child(node)

    def delete_subtree(self, node: TreeNode) -> None:
        """Delete *node* and all its descendants from the Forest."""
        # Remove references from parent or roots list
        if node.parent is not None:
            node.parent.remove_child(node)
        else:
            self._roots.remove(node)
        # Unregister nodes recursively
        for n in node.dfs():
            self._nodes.pop(id(n), None)

    # ------------------------------------------------------------------
    # Traversal helpers (Forest‑wide)
    # ------------------------------------------------------------------
    def dfs(self) -> Iterator[TreeNode]:
        """Depth‑first traversal over **all** trees in the forest."""
        for root in self._roots:
            yield from root.dfs()

    def bfs(self) -> Iterator[TreeNode]:
        """Breadth‑first traversal over **all** trees in the forest."""
        q: deque[TreeNode] = deque(self._roots)
        while q:
            node = q.popleft()
            yield node
            q.extend(node.children)

    # ------------------------------------------------------------------
    # Misc utilities
    # ------------------------------------------------------------------
    def roots(self) -> List[TreeNode]:
        """Return a **copy** of the current list of root nodes."""
        return list(self._roots)

    def find(self, predicate) -> Optional[TreeNode]:  # type: ignore[valid-type]
        """Return **first** node matching *predicate*, else *None*."""
        return next((n for n in self.dfs() if predicate(n)), None)

    def size(self) -> int:  # noqa: D401
        """Total number of nodes in the forest."""
        return len(self._nodes)

    def __len__(self) -> int:
        return self.size()

    def __iter__(self) -> Iterator[TreeNode]:
        return self.dfs()

    # ------------------------------------------------------------------
    # Internal helpers
    # ------------------------------------------------------------------
    def _register_node(self, node: TreeNode) -> None:
        self._nodes[id(node)] = node

    def __repr__(self) -> str:  # noqa: D401
        roots = ", ".join(repr(r) for r in self._roots)
        return f"Forest([{roots}])"


# ----------------------------------------------------------------------
# Helper function (outside class to avoid circular dependency)
# ----------------------------------------------------------------------

def _is_descendant(node: TreeNode, possible_ancestor: TreeNode) -> bool:
    """Return *True* if *node* is a descendant of *possible_ancestor*."""
    current = node.parent
    while current is not None:
        if current is possible_ancestor:
            return True
        current = current.parent
    return False


# ----------------------------------------------------------------------
# Example usage (only executed when run as a script)
# ----------------------------------------------------------------------

if __name__ == "__main__":
    forest = Forest()
    root1 = forest.create_tree("A")
    root2 = forest.create_tree("X")

    b = forest.add_child(root1, "B")
    c = forest.add_child(root1, "C")
    forest.add_child(b, "D")
    forest.add_child(root2, "Y")

    print("Forest DFS order:", list(n.value for n in forest.dfs()))
    print("Forest BFS order:", list(n.value for n in forest.bfs()))

    # Move subtree
    forest.move_subtree(c, b)
    print("After moving C under B:", list(n.value for n in forest.dfs()))

    # Delete subtree rooted at B
    forest.delete_subtree(b)
    print("After deleting subtree B:", list(n.value for n in forest.dfs()))
