package com.example.java21;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Java 21 Feature: Virtual Threads — Project Loom (JEP 444, final in Java 21)
 *
 * Virtual threads are lightweight threads managed by the JVM, not the OS.
 * You can run MILLIONS of virtual threads without running out of memory.
 * They are ideal for I/O-bound tasks (blocking calls, DB queries, HTTP requests).
 *
 * Key difference from platform threads:
 * - Platform thread → 1:1 with OS thread (~1MB stack each, limited to ~thousands)
 * - Virtual thread  → M:N mapped to a small pool of carrier threads (~KB each, millions possible)
 */
public class VirtualThreads {

    // ── 1. Create a single virtual thread ────────────────────────────────────

    static void singleVirtualThread() throws InterruptedException {
        System.out.println("\n-- Single Virtual Thread --");

        Thread vThread = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> {
                    System.out.println("Running in: " + Thread.currentThread());
                    System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
                });

        vThread.join(); // wait for it to finish
    }

    // ── 2. Virtual thread via factory ─────────────────────────────────────────

    static void virtualThreadFactory() throws InterruptedException {
        System.out.println("\n-- Virtual Thread Factory --");

        ThreadFactory factory = Thread.ofVirtual().name("worker-", 0).factory();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            Thread t = factory.newThread(() ->
                System.out.println("Task " + taskId + " on " + Thread.currentThread().getName())
            );
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) t.join();
    }

    // ── 3. ExecutorService with virtual threads ───────────────────────────────

    static void virtualThreadExecutor() throws InterruptedException {
        System.out.println("\n-- ExecutorService (Virtual Threads) --");

        // newVirtualThreadPerTaskExecutor() — creates a new virtual thread per submitted task
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                int taskId = i;
                Future<String> f = executor.submit(() -> {
                    // Simulate I/O blocking — virtual thread yields here, freeing the carrier thread
                    Thread.sleep(Duration.ofMillis(100));
                    return "Task " + taskId + " done by " + Thread.currentThread().getName();
                });
                futures.add(f);
            }

            for (Future<String> f : futures) {
                try {
                    System.out.println(f.get());
                } catch (ExecutionException e) {
                    System.err.println("Error: " + e.getCause());
                }
            }
        }
    }

    // ── 4. Scale demo — 10,000 virtual threads vs platform thread limit ───────

    static void scaleDemo() throws InterruptedException {
        System.out.println("\n-- Scale Demo: 10,000 Virtual Threads --");

        int count = 10_000;
        CountDownLatch latch = new CountDownLatch(count);
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(50)); // simulate I/O
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        long elapsed = Duration.between(start, Instant.now()).toMillis();
        System.out.printf("10,000 virtual threads with 50ms I/O each completed in %d ms%n", elapsed);
        System.out.println("(With platform threads, this would need a huge thread pool)");
    }

    // ── 5. Thread.startVirtualThread — simplest API ───────────────────────────

    static void simplestApi() throws InterruptedException {
        System.out.println("\n-- Thread.startVirtualThread (simplest) --");

        Thread t = Thread.startVirtualThread(() ->
            System.out.println("Hello from virtual thread: " + Thread.currentThread().isVirtual())
        );
        t.join();
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() throws Exception {
        System.out.println("\n===== Virtual Threads (Project Loom) =====");
        singleVirtualThread();
        virtualThreadFactory();
        virtualThreadExecutor();
        scaleDemo();
        simplestApi();
    }
}
