package com.example.messaging;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String message;
            while (!(message = queue.take()).equals("END")) {
                System.out.println(Thread.currentThread().getName() + " consumed: " + message);
                Thread.sleep(1000); // simulate processing
            }
            System.out.println(Thread.currentThread().getName() + " finished.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
