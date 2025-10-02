package com.example.messaging;

    import java.util.concurrent.BlockingQueue;

    public class Producer implements Runnable {
        private final BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        // Method to send a message externally
        public void send(String message) throws InterruptedException {
            queue.put(message);
            System.out.println(Thread.currentThread().getName() + " produced: " + message);
        }

        @Override
        public void run() {
            // This thread could listen for messages to send,
            // but here we leave it empty (or handle auto-produce if you want).
        }
    }



