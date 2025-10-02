package com.example.server;


import com.example.messaging.MessageQueueApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

    class RequestHandler implements Runnable {

        private final Socket clientSocket;

        public RequestHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    // Get the I/O streams for communication
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                // Read the user's request (e.g., "GET / HTTP/1.1")
                try {
                    String line = input.readLine();
                    String path = "";
                    String method = "GET";
                    if (!line.isEmpty()) {
                        System.out.println("Request Line: " + line);
                        String[] parts = line.split(" ");

                        if (parts.length == 3) {
                            method = parts[0];       // GET / POST
                            path = parts[1];         // /hello, /api, etc.
                            String httpVersion = parts[2];  // HTTP/1.1

                            System.out.println("Method: " + method);
                            System.out.println("Path: " + path);
                            System.out.println("HTTP Version: " + httpVersion);
                            // In a real server, you'd parse this request here
                        }
                    }
                    System.out.println("Parsing Header");
                    // --- Parse headers ---
                    Map<String, String> headers = new HashMap<>();
                    String headerLine;
                    int contentLength = 0;
                    while ((headerLine = input.readLine()) != null && !headerLine.isEmpty()) {
                        System.out.println("Header: " + headerLine);
                        String[] parts = headerLine.split(":", 2);
                        if (parts.length == 2) {
                            headers.put(parts[0].trim(), parts[1].trim());
                        }
                        if (headerLine.toLowerCase().startsWith("content-length")) {
                            contentLength = Integer.parseInt(parts[1].trim());
                        }
                    }

                    // --- Read body if present ---
                    char[] bodyChars = new char[contentLength];
                    int read = 0;
                    if (contentLength > 0) {
                        read = input.read(bodyChars, 0, contentLength);
                    }
                    String requestBody = new String(bodyChars, 0, read);
                    System.out.println("Body: " + requestBody);

                    // Send a simple HTTP response back to the user
                    if ("/".equals(path) && "GET".equals(method)) {
                        String htmlContent = "<h1>Hello from the Java Server!</h1>";

                        output.println("HTTP/1.1 200 OK"); // Status line
                        output.println("Content-Type: text/html"); // Header
                        output.println("Content-Length: " + htmlContent.length()); // Header
                        output.println(); // Blank line separates headers from body
                        output.println(htmlContent); // The body/content
                    } else if ("/msg".equals(path) && "POST".equals(method)){
                        MessageQueueApp.getInstance().produce(requestBody);
                    }   else {
                        String htmlContent = "";
                        output.println("HTTP/1.1 404 NOT FOUND"); // Status line
                        output.println("Content-Type: text/html");
                        output.println(htmlContent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    output.println("HTTP/1.1 500 ISE"); // Statu
                    output.println("Content-Type: text/html");
                }

            } catch (Exception e) {
                System.out.println("An I/O error occurred: " + e.getMessage());
                e.printStackTrace();

            } finally {
                try {
                    // Close the dedicated client socket when done
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
