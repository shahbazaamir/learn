package com.example.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

    // Try with fair and unfair locks
    private static final ReentrantLock lock = new ReentrantLock(true); // true = fair

    public static void main(String[] args) {
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            for (int i = 0; i < 3; i++) {
                try {
                    lock.lock();
                    System.out.println(threadName + " acquired the lock.");
                    Thread.sleep(20000); // simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println(threadName + " releasing the lock.");
                    lock.unlock();
                }
            }
        };

        // Start multiple threads
        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
