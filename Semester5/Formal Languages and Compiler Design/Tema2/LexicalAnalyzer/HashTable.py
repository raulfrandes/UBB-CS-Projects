class HashTable:
    def __init__(self, size=100):
        self.size = size
        self.table = [None] * size
    
    def _hash(self, key):
        return (sum(ord(c) for c in key) + len(key)) % self.size

    def put(self, key):
        index = self._hash(key)

        original_index = index
        while self.table[index] is not None:
            if self.table[index] == key:
                return index
            index = (index + 1) % self.size
            if index == original_index:
                return

        self.table[index] = key
        return index

    def get(self, key):
        index = self._hash(key)
        original_index = index
        while self.table[index] is not None:
            if self.table[index] == key:
                return index
            index = (index + 1) % self.size
            if index == original_index:
                break
        return None
    
    def display(self):
        print("Symbol Table (TS):")
        for i, entry in enumerate(self.table):
            if entry is not None:
                print(f"Index {i}: {entry}")


