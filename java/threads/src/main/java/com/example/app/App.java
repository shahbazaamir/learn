package com.example.app;

import com.example.messaging.MessageQueueApp;
import com.example.server.MyHttpServer;

public class App {
    public static void main(String args[]){
        MyHttpServer s = MyHttpServer.getInstance();
        //MessageQueueApp m = MessageQueueApp.getInstance();
    }
}
