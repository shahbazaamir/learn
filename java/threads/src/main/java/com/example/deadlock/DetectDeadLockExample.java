package com.example.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Deadlock Detection using Java Management Beans (JMX)
 *
 * ThreadMXBean is part of java.lang.management — it gives you runtime
 * information about all threads in the JVM, including:
 *  - findDeadlockedThreads()  → detects threads deadlocked on object monitors
 *                               AND java.util.concurrent locks (Java 6+)
 *  - findMonitorDeadlockedThreads() → only object monitor (synchronized) deadlocks
 *  - getThreadInfo()          → full stack trace, lock info, blocked state
 *
 * This example:
 *  1. Reproduces the same BankAccount deadlock from BankDeadLockExample
 *  2. Runs a background detector that polls every second
 *  3. When deadlock is found, prints which threads are stuck, what lock
 *     each one holds, and what lock each one is waiting for
 *  4. Interrupts the deadlocked threads to recover
 */
public class DetectDeadLockExample {

    // ── Same BankAccount from the original example ────────────────────────────

    static class BankAccount {
        private final String name;
        private double balance;

        BankAccount(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        String getName() { return name; }

        void transferFunds(BankAccount target, double amount) {
            synchronized (this) {
                System.out.printf("[%s] Locked %-20s | Waiting to lock %s%n",
                        Thread.currentThread().getName(), this.name, target.name);
                try {
                    Thread.sleep(80); // widen the deadlock window
                } catch (InterruptedException e) {
                    System.out.printf("[%s] Interrupted while holding lock on %s%n",
                            Thread.currentThread().getName(), this.name);
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (target) {
                    this.balance -= amount;
                    target.balance += amount;
                    System.out.printf("[%s] Transfer complete: %.0f from %s to %s%n",
                            Thread.currentThread().getName(), amount, this.name, target.name);
                }
            }
        }
    }

    // ── Deadlock Detector ─────────────────────────────────────────────────────

    static class DeadlockDetector implements Runnable {

        private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        private final Thread[] monitoredThreads; // threads to interrupt on detection

        DeadlockDetector(Thread... monitoredThreads) {
            this.monitoredThreads = monitoredThreads;
        }

        @Override
        public void run() {
            // findDeadlockedThreads() covers both synchronized AND Lock-based deadlocks
            long[] deadlockedIds = threadMXBean.findDeadlockedThreads();

            if (deadlockedIds == null) {
                System.out.println("[Detector] No deadlock detected yet...");
                return;
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("  ⚠️  DEADLOCK DETECTED — " + deadlockedIds.length + " thread(s) involved");
            System.out.println("=".repeat(60));

            // getThreadInfo with maxDepth for stack trace
            ThreadInfo[] infos = threadMXBean.getThreadInfo(deadlockedIds, true, true);

            for (ThreadInfo info : infos) {
                System.out.println("\nThread: " + info.getThreadName()
                        + "  (id=" + info.getThreadId() + ")");
                System.out.println("  State          : " + info.getThreadState());
                System.out.println("  Blocked count  : " + info.getBlockedCount());
                System.out.println("  Waited count   : " + info.getWaitedCount());

                if (info.getLockName() != null) {
                    System.out.println("  Waiting for    : " + info.getLockName());
                }
                if (info.getLockOwnerName() != null) {
                    System.out.println("  Held by        : " + info.getLockOwnerName()
                            + " (id=" + info.getLockOwnerId() + ")");
                }

                // Locks this thread currently HOLDS
                if (info.getLockedMonitors().length > 0) {
                    System.out.println("  Holds monitors :");
                    for (var monitor : info.getLockedMonitors()) {
                        System.out.println("    → " + monitor.getClassName()
                                + "@" + Integer.toHexString(monitor.getIdentityHashCode())
                                + " (locked at " + monitor.getLockedStackFrame() + ")");
                    }
                }

                // Stack trace — first 5 frames are enough to pinpoint the deadlock
                System.out.println("  Stack trace (top 5 frames):");
                StackTraceElement[] stack = info.getStackTrace();
                int frames = Math.min(5, stack.length);
                for (int i = 0; i < frames; i++) {
                    System.out.println("    at " + stack[i]);
                }
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("  ✅ Attempting recovery: interrupting deadlocked threads...");
            System.out.println("=".repeat(60) + "\n");

            // Recovery: interrupt the deadlocked threads
            for (Thread t : monitoredThreads) {
                for (long deadId : deadlockedIds) {
                    if (t.getId() == deadId) {
                        System.out.println("[Detector] Interrupting: " + t.getName());
                        t.interrupt();
                    }
                }
            }
        }
    }

    // ── Main ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {

        BankAccount alice = new BankAccount("Alice's Account", 1000);
        BankAccount bob   = new BankAccount("Bob's Account",   2000);

        // Tx-1: Alice → Bob (locks Alice first, then tries Bob)
        Thread tx1 = new Thread(() -> alice.transferFunds(bob, 100), "Tx-Thread-1");

        // Tx-2: Bob → Alice (locks Bob first, then tries Alice)
        // Classic circular-wait: Tx1 holds Alice + waits Bob, Tx2 holds Bob + waits Alice
        Thread tx2 = new Thread(() -> bob.transferFunds(alice, 50),  "Tx-Thread-2");

        // Start the deadlock detector — polls every 1 second
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "DeadlockDetector");
            t.setDaemon(true); // don't block JVM shutdown
            return t;
        });

        scheduler.scheduleAtFixedRate(
                new DeadlockDetector(tx1, tx2),
                1,   // initial delay — give threads time to deadlock
                1,   // period
                TimeUnit.SECONDS
        );

        System.out.println("[Main] Starting transactions — deadlock expected...\n");
        tx1.start();
        tx2.start();

        // Wait for both threads to finish (via interrupt + recovery)
        tx1.join(10_000);
        tx2.join(10_000);

        scheduler.shutdownNow();

        System.out.println("\n[Main] Done. Final states:");
        System.out.printf("  Alice's balance: %.0f%n", alice.balance);
        System.out.printf("  Bob's   balance: %.0f%n", bob.balance);
    }
}
