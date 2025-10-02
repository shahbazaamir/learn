package com.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyHttpServer {

    private MyHttpServer(){

    }
    private static MyHttpServer instance;
    public static MyHttpServer getInstance(){
        if( instance==null){
            instance=new MyHttpServer();
            instance.start();
        }
        return instance;
    }


    // A fixed-size pool to manage worker threads efficiently
    private   final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    private  final int PORT = 8080;

    //private static RequestReader r=new RequestReader();

    public  void start( ) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            // The main server loop: waits for connections forever
            while (true) {
                // *** 1. The main thread blocks/waits here for a user request ***
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected: " + clientSocket.getInetAddress());
                //r.read(clientSocket);
                // *** 2. Hand off the request handling to a worker thread ***
                THREAD_POOL.execute(new RequestHandler(clientSocket));

                // *** 3. The main thread loops back immediately to serverSocket.accept()
                //    to wait for the NEXT client request.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}