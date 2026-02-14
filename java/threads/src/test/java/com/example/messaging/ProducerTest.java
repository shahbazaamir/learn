package com.example.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {
    
    private BlockingQueue<String> queue;
    private Producer producer;

    @BeforeEach
    void setUp() {
        queue = new ArrayBlockingQueue<>(3);
        producer = new Producer(queue);
    }

    @Test
    void testSendMessage() throws Exception {
        producer.send("Test Message");
        
        assertEquals("Test Message", queue.take());
    }

    @Test
    void testSendMultipleMessages() throws Exception {
        producer.send("Message 1");
        producer.send("Message 2");
        
        assertEquals("Message 1", queue.take());
        assertEquals("Message 2", queue.take());
    }

    @Test
    void testQueueCapacity() throws Exception {
        // Fill queue to capacity
        producer.send("Msg1");
        producer.send("Msg2");
        producer.send("Msg3");
        
        assertEquals(3, queue.size());
    }

   // @Test
    void testRunMethod() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        
        // Let producer run briefly
        Thread.sleep(100);
        producerThread.interrupt();
        
        assertTrue(queue.size() > 0, "Producer should have added messages");
    }
}