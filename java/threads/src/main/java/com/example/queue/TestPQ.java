package com.example.queue;

import java.util.List;
import java.util.PriorityQueue;

public class TestPQ {
    public static void test(String[] args) {
        List<Integer> l = List.of(1,5,2,4,3,6);
        PriorityQueue<Integer> pq = new PriorityQueue<>(l);
        
        testBasicOperations(pq);
        testAdditionalMethods();
    }
    
    private static void testBasicOperations(PriorityQueue<Integer> pq) {
        System.out.println("Initial queue: " + pq);
        while (!pq.isEmpty()) {
            System.out.println("Polled: " + pq.poll());
        }
    }
    
    private static void testAdditionalMethods() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        
        // offer() - adds element
        pq.offer(10);
        pq.offer(5);
        pq.offer(15);
        System.out.println("\nAfter offers: " + pq);
        
        // peek() - views head without removing
        System.out.println("Peek: " + pq.peek());
        System.out.println("Queue after peek: " + pq);
        
        // size() and isEmpty()
        System.out.println("Size: " + pq.size());
        System.out.println("Is empty: " + pq.isEmpty());
        
        // contains()
        System.out.println("Contains 5: " + pq.contains(5));
        System.out.println("Contains 20: " + pq.contains(20));
        
        // remove() - removes specific element
        pq.remove(5);
        System.out.println("After removing 5: " + pq);
        
        // clear()
        pq.clear();
        System.out.println("After clear - size: " + pq.size());
    }
}
