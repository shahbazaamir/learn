class HashTable:
    def __init__(self, size=10):
        self.size = size
        self.buckets = [[] for _ in range(self.size)]  # list of lists

    def _hash(self, key):
        return hash(key) % self.size

    def put(self, key, value):
        index = self._hash(key)
        # Check if key exists and update it
        for i, (k, v) in enumerate(self.buckets[index]):
            if k == key:
                self.buckets[index][i] = (key, value)
                return
        # Otherwise, insert new key-value pair
        self.buckets[index].append((key, value))

    def get(self, key):
        index = self._hash(key)
        for k, v in self.buckets[index]:
            if k == key:
                return v
        return None  # key not found

    def remove(self, key):
        index = self._hash(key)
        for i, (k, _) in enumerate(self.buckets[index]):
            if k == key:
                del self.buckets[index][i]
                return True
        return False  # key not found

    def __str__(self):
        return str(self.buckets)
