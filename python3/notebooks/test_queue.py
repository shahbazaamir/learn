from queue_module import Queue, CircularQueue, Deque, PriorityQueue

# FIFO Queue
q = Queue()
q.enqueue("A")
q.enqueue("B")
print(q.dequeue())  # A

# Circular Queue
cq = CircularQueue(3)
cq.enqueue(1)
cq.enqueue(2)
print(cq.dequeue())  # 1

# Deque
dq = Deque()
dq.add_rear("rear")
dq.add_front("front")
print(dq.remove_front())  # front
print(dq.remove_rear())   # rear

# Priority Queue
pq = PriorityQueue()
pq.enqueue(2, "low")
pq.enqueue(1, "high")
print(pq.dequeue())  # high
