package com.example.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

// Main application
public class MessageQueueApp {

    private MessageQueueApp(){

    }
    private  static MessageQueueApp instance   ;

    public static MessageQueueApp getInstance(){
        if (instance==null){
            instance = new MessageQueueApp();
            instance.start();
        }
        return  instance;
    }

    private Producer producer = null;
    Consumer consumer = null;

    public void produce(String msg) throws Exception{
        producer.send(msg);
    }

    public  void start() {
        // Shared queue
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

        // Create producer and consumer
         producer = new Producer(queue);
         consumer = new Consumer(queue);

        // Start threads
        new Thread(producer, "Producer-Thread").start();
        new Thread(consumer, "Consumer-Thread").start();
    }


}
