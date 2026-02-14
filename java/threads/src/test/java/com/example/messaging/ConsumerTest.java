package com.example.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {
    
    private BlockingQueue<String> queue;
    private Consumer consumer;

    @BeforeEach
    void setUp() {
        queue = new ArrayBlockingQueue<>(5);
        consumer = new Consumer(queue);
    }

   // @Test
    void testConsumerProcessesMessages() throws InterruptedException {
        queue.put("Test Message");
        queue.put("END");
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(1000);
        
        assertTrue(queue.isEmpty(), "Consumer should process all messages");
    }

  //  @Test
    void testConsumerStopsOnEndMessage() throws InterruptedException {
        queue.put("Message 1");
        queue.put("Message 2");
        queue.put("END");
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(1000);
        
        assertFalse(consumerThread.isAlive(), "Consumer thread should terminate");
    }

  //  @Test
    void testConsumerHandlesInterruption() {
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.interrupt();
        
        // Consumer should handle interruption gracefully
        assertDoesNotThrow(() -> consumerThread.join(1000));
    }

   // @Test
    void testEmptyQueue() throws InterruptedException {
        queue.put("END");
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(1000);
        
        assertTrue(queue.isEmpty());
    }
}