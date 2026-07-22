package com.example.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderProcessingSystem {

    // Custom thread pool to prevent blocking the global ForkJoinPool
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        OrderProcessingSystem system = new OrderProcessingSystem();

        System.out.println("[Main] Initiating order for ID: ORD-992");

        // Kick off the asynchronous pipeline
        CompletableFuture<Void> orderPipeline = system.processOrderAsync("ORD-992", "USR-441");

        // The pipeline runs in the background. Main thread can do other things here.
        System.out.println("[Main] Doing other work while order processes...");

        // Block main thread temporarily just to wait for demo completion
        orderPipeline.join();
        executor.shutdown();
    }

    public CompletableFuture<Void> processOrderAsync(String orderId, String userId) {

        // Step 1: Fetch User Info (Async)
        CompletableFuture<String> userTask = CompletableFuture.supplyAsync(() -> {
            return fetchUser(userId);
        }, executor);

        // Step 2: Fetch Payment Info (Async & Concurrent with Step 1)
        CompletableFuture<Double> paymentAmountTask = CompletableFuture.supplyAsync(() -> {
            return getOrderAmount(orderId);
        }, executor);

        // Step 3: Combine both results when they are ready to process payment
        CompletableFuture<Boolean> paymentResultTask = userTask.thenCombineAsync(paymentAmountTask, (user, amount) -> {
            return executePayment(user, amount);
        }, executor);

        // Step 4: Finalize by sending a confirmation email based on payment success
        // We use exceptionally() to gracefully handle any failures in the chain
        CompletableFuture<Void> finalPipeline = paymentResultTask
                .thenAcceptAsync(success -> {
                    if (success) {
                        sendEmail("User notified: Order processed successfully.");
                    } else {
                        sendEmail("User notified: Order payment failed.");
                    }
                }, executor)
                .exceptionally(ex -> {
                    System.err.println("Pipeline crashed: " + ex.getMessage());
                    sendEmail("System Alert: Order failed due to an internal error.");
                    return null;
                });

        return finalPipeline;
    }

    // --- Mock Service Methods simulating I/O operations ---

    private String fetchUser(String userId) {
        sleep(150); // Simulate network latency
        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Fetched User Details for " + userId);
        return "John Doe (john@example.com)";
    }

    private double getOrderAmount(String orderId) {
        sleep(100); // Simulate DB queries
        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Fetched Order Amount for " + orderId);
        return 249.99;
    }

    private boolean executePayment(String user, double amount) {
        sleep(200); // Simulate payment gateway handshake
        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Successfully charged $" + amount + " to " + user);
        return true;
    }

    private void sendEmail(String message) {
        sleep(50); // Simulate SMTP latency
        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Email Sent: " + message);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
