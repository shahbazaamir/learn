package com.example.deadlock;

/**
 * Classic circular deadlock using two locks (Pool and Bucket).
 *
 * Thread-A: locks Pool first, then tries to lock Bucket
 * Thread-B: locks Bucket first, then tries to lock Pool
 *
 * Neither can proceed → deadlock.
 */
//import com.example.deadlock.DeadLockExample;
//import com.example.deadlock.Pool;

/*
public class CircularDeadlock {

    public static void main(String[] args) throws InterruptedException {
        Pool pool = new Pool();
        Bucket bucket = new Bucket();

        // Thread-A: Pool → Bucket
        Thread threadA = new Thread(() -> {
            synchronized (pool) {
                System.out.println("Thread-A: locked Pool");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("Thread-A: waiting for Bucket...");
                synchronized (bucket) {                        // blocked — Thread-B holds Bucket
                    pool.add("from-A");
                    bucket.store("from-A");
                    System.out.println("Thread-A: done (you will never see this)");
                }
            }
        }, "Thread-A");

        // Thread-B: Bucket → Pool  (reverse order)
        Thread threadB = new Thread(() -> {
            synchronized (bucket) {
                System.out.println("Thread-B: locked Bucket");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("Thread-B: waiting for Pool...");
                synchronized (pool) {                          // blocked — Thread-A holds Pool
                    bucket.store("from-B");
                    pool.add("from-B");
                    System.out.println("Thread-B: done (you will never see this)");
                }
            }
        }, "Thread-B");

        threadA.start();
        threadB.start();

        // Give threads time to reach deadlock, then report state
        Thread.sleep(2000);
        System.out.println("\n--- Deadlock detected (threads stuck) ---");
        System.out.println("Thread-A state: " + threadA.getState());
        System.out.println("Thread-B state: " + threadB.getState());
        System.out.println("\nCircular dependency:");
        System.out.println("  Thread-A holds Pool      → waiting for Bucket");
        System.out.println("  Thread-B holds Bucket    → waiting for Pool");
    }
}

/**
 * Bucket acts as the second lock resource (alongside Pool).

class Bucket {
    private java.util.List<String> items = new java.util.ArrayList<>();

    public void store(String data) {
        items.add(data);
    }

    public String fetch(int index) {
        return items.get(index);
    }
}

*/

