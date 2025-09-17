# queue_module.py
from collections import deque
import heapq

# ------------------------
# Queue Classes
# ------------------------

class Queue:
    def __init__(self):
        self.queue = deque()

    def enqueue(self, item): self.queue.append(item)
    def dequeue(self): return self.queue.popleft() if not self.is_empty() else self._raise_empty()
    def peek(self): return self.queue[0] if not self.is_empty() else None
    def is_empty(self): return len(self.queue) == 0
    def size(self): return len(self.queue)
    def _raise_empty(self): raise IndexError("Queue is empty")


class CircularQueue:
    def __init__(self, capacity):
        self.queue = [None] * capacity
        self.head = self.tail = -1
        self.capacity = capacity

    def enqueue(self, item):
        if (self.tail + 1) % self.capacity == self.head:
            raise OverflowError("Circular Queue is full")
        if self.head == -1: self.head = 0
        self.tail = (self.tail + 1) % self.capacity
        self.queue[self.tail] = item

    def dequeue(self):
        if self.is_empty(): raise IndexError("Circular Queue is empty")
        item = self.queue[self.head]
        if self.head == self.tail: self.head = self.tail = -1
        else: self.head = (self.head + 1) % self.capacity
        return item

    def is_empty(self): return self.head == -1
    def peek(self): return self.queue[self.head] if not self.is_empty() else None


class Deque:
    def __init__(self): self.deque = deque()
    def add_front(self, item): self.deque.appendleft(item)
    def add_rear(self, item): self.deque.append(item)
    def remove_front(self): return self.deque.popleft() if not self.is_empty() else self._raise_empty()
    def remove_rear(self): return self.deque.pop() if not self.is_empty() else self._raise_empty()
    def is_empty(self): return len(self.deque) == 0
    def size(self): return len(self.deque)
    def _raise_empty(self): raise IndexError("Deque is empty")


class PriorityQueue:
    def __init__(self): self.heap = []
    def enqueue(self, priority, item): heapq.heappush(self.heap, (priority, item))
    def dequeue(self): return heapq.heappop(self.heap)[1] if not self.is_empty() else self._raise_empty()
    def peek(self): return self.heap[0][1] if not self.is_empty() else None
    def is_empty(self): return len(self.heap) == 0
    def _raise_empty(self): raise IndexError("Priority Queue is empty")

# ------------------------
# Heap Classes
# ------------------------

class MinHeap:
    def __init__(self): self.heap = []
    def push(self, item): heapq.heappush(self.heap, item)
    def pop(self): return heapq.heappop(self.heap) if not self.is_empty() else self._raise_empty()
    def peek(self): return self.heap[0] if not self.is_empty() else None
    def is_empty(self): return len(self.heap) == 0
    def size(self): return len(self.heap)
    def _raise_empty(self): raise IndexError("Heap is empty")

class MaxHeap:
    def __init__(self): self.heap = []
    def push(self, item): heapq.heappush(self.heap, -item)
    def pop(self): return -heapq.heappop(self.heap) if not self.is_empty() else self._raise_empty()
    def peek(self): return -self.heap[0] if not self.is_empty() else None
    def is_empty(self): return len(self.heap) == 0
    def size(self): return len(self.heap)
    def _raise_empty(self): raise IndexError("Heap is empty")

class CustomHeap:
    def __init__(self, key=lambda x: x):
        self.heap = []
        self.key = key

    def push(self, item):
        heapq.heappush(self.heap, (self.key(item), item))

    def pop(self):
        if self.is_empty():
            raise IndexError("CustomHeap is empty")
        return heapq.heappop(self.heap)[1]

    def peek(self):
        return self.heap[0][1] if not self.is_empty() else None

    def is_empty(self):
        return len(self.heap) == 0

    def size(self):
        return len(self.heap)
