package com.example.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageQueueAppTest {

    @BeforeEach
    void setUp() {
        // Reset singleton instance before each test
        resetSingleton();
    }

    @AfterEach
    void tearDown() {
        resetSingleton();
    }

    @Test
    void testSingletonInstance() {
        MessageQueueApp instance1 = MessageQueueApp.getInstance();
        MessageQueueApp instance2 = MessageQueueApp.getInstance();
        
        assertSame(instance1, instance2, "Should return same instance");
        assertNotNull(instance1, "Instance should not be null");
    }
/*
    //@Test
    void testInstanceInitialization() {
        MessageQueueApp app = MessageQueueApp.getInstance();
        
       // assertNotNull(app.producer, "Producer should be initialized");
        //assertNotNull(app.consumer, "Consumer should be initialized");
    }

    @Test
    void testProduceMessage() throws Exception {
        MessageQueueApp app = MessageQueueApp.getInstance();
        Producer mockProducer = mock(Producer.class);
       // app.producer = mockProducer;
        
        String testMessage = "Test Message";
        app.produce(testMessage);
        
        verify(mockProducer).send(testMessage);
    }

    @Test
    void testProduceThrowsException() throws Exception {
        MessageQueueApp app = MessageQueueApp.getInstance();
        Producer mockProducer = mock(Producer.class);
        app.producer = mockProducer;
        
        doThrow(new RuntimeException("Queue full")).when(mockProducer).send(anyString());
        
        assertThrows(Exception.class, () -> app.produce("test"));
    }

    @Test
    void testMultipleMessages() throws Exception {
        MessageQueueApp app = MessageQueueApp.getInstance();
        Producer mockProducer = mock(Producer.class);
        app.producer = mockProducer;
        
        app.produce("Message 1");
        app.produce("Message 2");
        app.produce("Message 3");
        
        verify(mockProducer, times(3)).send(anyString());
    }
    */
    @Test
    void testThreadSafety() throws InterruptedException {
        final MessageQueueApp[] instances = new MessageQueueApp[10];
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> instances[index] = MessageQueueApp.getInstance());
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        MessageQueueApp firstInstance = instances[0];
        for (MessageQueueApp instance : instances) {
            assertSame(firstInstance, instance, "All instances should be the same");
        }
    }

    private void resetSingleton() {
        try {
            java.lang.reflect.Field instanceField = MessageQueueApp.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors in tests
        }
    }
}