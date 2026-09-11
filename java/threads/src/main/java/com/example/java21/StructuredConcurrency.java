package com.example.java21;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * Java 21 Feature: Structured Concurrency (JEP 453 — preview in Java 21)
 *
 * Structured Concurrency treats a group of related tasks as a single unit.
 * Key guarantees:
 * - If the parent scope closes, ALL child tasks are cancelled automatically.
 * - If any child fails, others are cancelled.
 * - No thread leaks — all threads started in a scope finish before the scope exits.
 *
 * Think of it as try-with-resources for concurrent tasks.
 *
 * NOTE: Requires --enable-preview flag in Java 21.
 *       In Java 23+ it is no longer preview.
 *
 * We use ExecutorService + CompletableFuture to demonstrate the same patterns
 * without requiring preview flags, then show the StructuredTaskScope API as comments.
 */
public class StructuredConcurrency {

    // Simulated services
    static String fetchUser(int userId) throws InterruptedException {
        Thread.sleep(Duration.ofMillis(80));
        return "User-" + userId;
    }

    static String fetchOrders(int userId) throws InterruptedException {
        Thread.sleep(Duration.ofMillis(120));
        return "Orders[ORD001, ORD002] for User-" + userId;
    }

    static String fetchInventory(String orderId) throws InterruptedException {
        Thread.sleep(Duration.ofMillis(60));
        return "Stock OK for " + orderId;
    }

    // ── 1. Fan-out / Fan-in with CompletableFuture (structured style) ─────────
    // Run multiple tasks concurrently, collect all results

    static void fanOutFanIn() throws Exception {
        System.out.println("\n-- Fan-out / Fan-in (parallel tasks) --");

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<String> userFuture   = CompletableFuture.supplyAsync(() -> {
                try { return fetchUser(42); }
                catch (InterruptedException e) { throw new RuntimeException(e); }
            }, exec);

            CompletableFuture<String> ordersFuture = CompletableFuture.supplyAsync(() -> {
                try { return fetchOrders(42); }
                catch (InterruptedException e) { throw new RuntimeException(e); }
            }, exec);

            // Wait for both
            String user   = userFuture.get();
            String orders = ordersFuture.get();

            System.out.println("User:   " + user);
            System.out.println("Orders: " + orders);
            System.out.println("Combined response ready.");
        }
    }

    // ── 2. ShutdownOnFailure — cancel siblings if one fails ───────────────────

    static void shutdownOnFailure() throws Exception {
        System.out.println("\n-- ShutdownOnFailure (cancel all if one fails) --");

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<String> goodTask = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(Duration.ofMillis(50));
                    return "Good task result";
                } catch (InterruptedException e) { throw new RuntimeException(e); }
            }, exec);

            CompletableFuture<String> failingTask = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(Duration.ofMillis(30));
                    throw new RuntimeException("Service unavailable (HTTP 503)");
                } catch (InterruptedException e) { throw new RuntimeException(e); }
            }, exec);

            // Cancel the other if one fails
            CompletableFuture<Void> combined = CompletableFuture.allOf(goodTask, failingTask)
                .exceptionally(ex -> {
                    goodTask.cancel(true);
                    failingTask.cancel(true);
                    return null;
                });

            try {
                combined.get(1, TimeUnit.SECONDS);
                System.out.println("Both succeeded");
            } catch (ExecutionException | TimeoutException e) {
                System.out.println("One task failed — others cancelled. Error: "
                        + (failingTask.isCompletedExceptionally() ? "failingTask" : "goodTask"));
            }
        }
    }

    // ── 3. ShutdownOnSuccess — return first successful result, cancel rest ─────

    static void shutdownOnSuccess() throws Exception {
        System.out.println("\n-- ShutdownOnSuccess (first result wins) --");

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {

            // Two data sources — take whichever responds first
            CompletableFuture<String> primaryDB = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(Duration.ofMillis(200)); // slower
                    return "Result from PRIMARY database";
                } catch (InterruptedException e) { throw new CancellationException(); }
            }, exec);

            CompletableFuture<String> replicaDB = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(Duration.ofMillis(80)); // faster
                    return "Result from REPLICA database";
                } catch (InterruptedException e) { throw new CancellationException(); }
            }, exec);

            // First one to complete wins
            String result = CompletableFuture.anyOf(primaryDB, replicaDB)
                    .thenApply(r -> (String) r)
                    .get();

            primaryDB.cancel(true);
            replicaDB.cancel(true);

            System.out.println("First result: " + result);
        }
    }

    // ── 4. Pipeline — sequential dependent tasks on virtual threads ───────────

    static void pipeline() throws Exception {
        System.out.println("\n-- Pipeline (sequential dependent tasks) --");

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {

            String result = CompletableFuture
                .supplyAsync(() -> {
                    try { return fetchUser(7); }
                    catch (InterruptedException e) { throw new RuntimeException(e); }
                }, exec)
                .thenApplyAsync(user -> {
                    try { return user + " → " + fetchOrders(7); }
                    catch (InterruptedException e) { throw new RuntimeException(e); }
                }, exec)
                .thenApplyAsync(userOrders -> {
                    try { return userOrders + " → " + fetchInventory("ORD001"); }
                    catch (InterruptedException e) { throw new RuntimeException(e); }
                }, exec)
                .get();

            System.out.println("Pipeline result: " + result);
        }
    }

    /*
     * ── REFERENCE: StructuredTaskScope API (preview, requires --enable-preview) ──
     *
     * try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
     *     Subtask<String> user   = scope.fork(() -> fetchUser(42));
     *     Subtask<String> orders = scope.fork(() -> fetchOrders(42));
     *
     *     scope.join();           // wait for all forks
     *     scope.throwIfFailed();  // propagate any exception
     *
     *     return user.get() + " | " + orders.get();
     * }
     *
     * try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
     *     scope.fork(() -> fetchFromPrimary());
     *     scope.fork(() -> fetchFromReplica());
     *     scope.join();
     *     return scope.result();  // first successful result
     * }
     */

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() throws Exception {
        System.out.println("\n===== Structured Concurrency =====");
        fanOutFanIn();
        shutdownOnFailure();
        shutdownOnSuccess();
        pipeline();
    }
}
