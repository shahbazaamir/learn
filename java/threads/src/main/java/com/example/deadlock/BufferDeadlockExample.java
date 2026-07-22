package com.example.deadlock;

public class BufferDeadlockExample {

    static class DataBuffer {
        private final String bufferName;

        public DataBuffer(String name) {
            this.bufferName = name;
        }

        public String getName() {
            return bufferName;
        }

        // Simulates copying data from this buffer to a destination buffer
        public void transferData(DataBuffer destinationBuffer) {
            // Lock the source buffer to read from it safely
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " LOCKED " + this.getName() + " for reading.");

                try {
                    Thread.sleep(50); // Simulates time taken to read large chunk of data
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(Thread.currentThread().getName() + " WAITING to lock " + destinationBuffer.getName() + " for writing.");

                // Lock the destination buffer to write to it safely
                synchronized (destinationBuffer) {
                    System.out.println(Thread.currentThread().getName() + " successfully copied data!");
                }
            }
        }
    }

    public static void main(String[] args) {
        DataBuffer bufferA = new DataBuffer("Buffer-A");
        DataBuffer bufferB = new DataBuffer("Buffer-B");

        // Thread 1: Reads from A and writes to B
        Thread thread1 = new Thread(() -> bufferA.transferData(bufferB), "Reader-Thread-1");

        // Thread 2: Reads from B and writes to A
        Thread thread2 = new Thread(() -> bufferB.transferData(bufferA), "Reader-Thread-2");

        thread1.start();
        thread2.start();
    }
}
