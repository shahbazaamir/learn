# trie_module.py

class TrieNode:
    def __init__(self):
        self.children = {}
        self.end_of_word = False


class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word: str):
        node = self.root
        for ch in word:
            if ch not in node.children:
                node.children[ch] = TrieNode()
            node = node.children[ch]
        node.end_of_word = True

    def search(self, word: str) -> bool:
        node = self._find_node(word)
        return node is not None and node.end_of_word

    def starts_with(self, prefix: str) -> bool:
        return self._find_node(prefix) is not None

    def delete(self, word: str) -> bool:
        return self._delete(self.root, word, 0)

    def _delete(self, node: TrieNode, word: str, index: int) -> bool:
        if index == len(word):
            if not node.end_of_word:
                return False  # Word not found
            node.end_of_word = False
            return len(node.children) == 0  # Can delete node if it's a leaf
        ch = word[index]
        if ch not in node.children:
            return False  # Word not found
def autocomplete(self, prefix: str) -> list[str]:
    node = self._find_node(prefix)
    results = []

    def dfs(curr_node, path):
        if curr_node.end_of_word:
            results.append(prefix + path)
        for ch, next_node in curr_node.children.items():
            dfs(next_node, path + ch)

    if node:
        dfs(node, "")
    return results

def all_words(self) -> list[str]:
    results = []

    def dfs(node, path):
        if node.end_of_word:
            results.append(path)
        for ch, child in node.children.items():
            dfs(child, path + ch)

    dfs(self.root, "")
    return results


def spellcheck(self, word: str, max_distance: int = 1) -> list[str]:
    from difflib import get_close_matches
    return get_close_matches(word, self.all_words(), n=5, cutoff=1 - max_distance * 0.2)
