package com.example.java17;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Java 17 Feature: Strong Encapsulation of JDK Internals (JEP 403)
 *
 * History:
 *   Java 9  — JEP 261: module system, --illegal-access flag introduced
 *             (default: permit, with warnings on reflection to JDK internals)
 *   Java 16 — JEP 396: --illegal-access changed to default DENY (but override still possible)
 *   Java 17 — JEP 403: --illegal-access flag REMOVED entirely.
 *             Strong encapsulation is now unconditional.
 *             Accessing JDK internal APIs via reflection is permanently blocked.
 *
 * What this means:
 *   - Code that used sun.misc.Unsafe, com.sun.*, sun.* internals via reflection BREAKS.
 *   - Legitimate use cases must use --add-opens or official replacement APIs.
 *   - Forces use of public, supported APIs (java.lang.invoke, VarHandle, etc.)
 *
 * This file demonstrates:
 *   1. What breaks — trying to access internal fields via reflection
 *   2. The correct replacement APIs
 *   3. How to use --add-opens as a targeted workaround (with explanation)
 *   4. Official alternatives: VarHandle, MethodHandles, java.lang.reflect (public only)
 */
public class StrongEncapsulationJ17 {

    // ── 1. What breaks — accessing internal JDK fields ────────────────────────

    static void demonstrateBlocking() {
        System.out.println("\n-- Strong Encapsulation: What Gets Blocked --");

        // Attempt to access String's internal 'value' byte array
        // In Java 8, this worked. In Java 17+, it throws InaccessibleObjectException.
        try {
            Field valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true); // ← BLOCKED in Java 17+
            byte[] bytes = (byte[]) valueField.get("Hello");
            System.out.println("  Got internal bytes: " + Arrays.toString(bytes));
        } catch (InaccessibleObjectException e) {
            System.out.println("  ✅ BLOCKED (expected in Java 17+):");
            System.out.println("     " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Error: " + e);
        }

        // Attempt to access internal Integer cache
        try {
            Class<?> integerCacheClass = Class.forName("java.lang.Integer$IntegerCache");
            Field cacheField = integerCacheClass.getDeclaredField("cache");
            cacheField.setAccessible(true); // ← BLOCKED in Java 17+
            System.out.println("  Got Integer cache (should not happen in Java 17+)");
        } catch (InaccessibleObjectException e) {
            System.out.println("  ✅ Integer cache BLOCKED: module java.base is encapsulated");
        } catch (ClassNotFoundException e) {
            System.out.println("  Class not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── 2. Correct approach — use public APIs only ────────────────────────────

    static void publicApiApproach() {
        System.out.println("\n-- Correct Approach: Public APIs --");

        // Instead of accessing String internals — use public methods
        String s = "Hello, Java 17!";
        byte[] bytes = s.getBytes();   // public API
        System.out.println("  String bytes (UTF-8): " + bytes.length + " bytes");
        System.out.println("  String chars: " + s.chars().count());
        System.out.println("  CodePoints:   " + s.codePoints().count());

        // Instead of hacking Integer cache — just use the public valueOf
        Integer a = Integer.valueOf(100);
        Integer b = Integer.valueOf(100);
        System.out.println("  Integer.valueOf(100) cached: " + (a == b)); // true, within cache range
    }

    // ── 3. VarHandle — the official replacement for Unsafe field access ────────

    static void varHandleDemo() throws Throwable {
        System.out.println("\n-- VarHandle: Official Replacement for Unsafe --");

        // VarHandle provides atomic, memory-ordered access to fields
        // This is what you should use instead of sun.misc.Unsafe
        class Counter {
            volatile int count = 0;
        }

        var lookup = java.lang.invoke.MethodHandles.lookup();
        var countHandle = lookup.findVarHandle(Counter.class, "count", int.class);

        Counter counter = new Counter();

        // Atomic compare-and-set — like Unsafe.compareAndSwapInt but safe
        boolean swapped = (boolean) countHandle.compareAndSet(counter, 0, 1);
        System.out.println("  CAS(0 → 1): " + swapped + ", count = " + counter.count);

        // Atomic get-and-add
        int prev = (int) countHandle.getAndAdd(counter, 5);
        System.out.println("  getAndAdd(5): prev=" + prev + ", count=" + counter.count);

        // Volatile read/write via VarHandle
        countHandle.setVolatile(counter, 100);
        System.out.println("  Volatile set to 100, read: " + countHandle.getVolatile(counter));
    }

    // ── 4. MethodHandles — reflection replacement ─────────────────────────────

    static void methodHandlesDemo() throws Throwable {
        System.out.println("\n-- MethodHandles: Type-safe Reflection --");

        var lookup = java.lang.invoke.MethodHandles.lookup();

        // Find and invoke a method — like reflection but type-safe and faster
        var toUpperCase = lookup.findVirtual(
                String.class, "toUpperCase",
                java.lang.invoke.MethodType.methodType(String.class)
        );

        String result = (String) toUpperCase.invoke("hello java 17");
        System.out.println("  toUpperCase via MethodHandle: " + result);

        // Find a static method
        var parseInt = lookup.findStatic(
                Integer.class, "parseInt",
                java.lang.invoke.MethodType.methodType(int.class, String.class)
        );
        int parsed = (int) parseInt.invoke("12345");
        System.out.println("  Integer.parseInt via MethodHandle: " + parsed);

        // Find a constructor
        var pointConstructor = lookup.findConstructor(
                java.awt.Point.class,
                java.lang.invoke.MethodType.methodType(void.class, int.class, int.class)
        );
        java.awt.Point p = (java.awt.Point) pointConstructor.invoke(3, 4);
        System.out.println("  java.awt.Point via MethodHandle: " + p);
    }

    // ── 5. What's still accessible — your own module's classes ───────────────

    static void ownClassReflection() throws Exception {
        System.out.println("\n-- Reflection on your own classes still works --");

        // Reflection on non-JDK classes is unaffected
        class Secret {
            private String value = "confidential";
            private int code = 42;
        }

        Secret s = new Secret();
        Field valueField = Secret.class.getDeclaredField("value");
        valueField.setAccessible(true); // ✅ works — this is YOUR code, not JDK internals
        System.out.println("  Private field via reflection: " + valueField.get(s));

        Field codeField = Secret.class.getDeclaredField("code");
        codeField.setAccessible(true);
        System.out.println("  Private int field: " + codeField.get(s));
    }

    // ── 6. Module system — key flags to know ─────────────────────────────────

    static void moduleSystemNotes() {
        System.out.println("\n-- Module System Notes (JEP 403) --");
        System.out.println("""
                  Key JVM flags for module control:

                  --add-opens <module>/<package>=<reading-module>
                    Opens an internal package for deep reflection to a specific module.
                    e.g. --add-opens java.base/java.lang=ALL-UNNAMED
                    Use only as a temporary workaround while migrating legacy code.

                  --add-exports <module>/<package>=<reading-module>
                    Makes an internal package's public types accessible.
                    Does NOT allow deep reflection (setAccessible).

                  --add-reads <module>=<other-module>
                    Adds a reads edge so a module can read another.

                  --illegal-access
                    REMOVED in Java 17. No longer accepted — pass it and JVM errors.

                  Migration path for code using JDK internals:
                  ┌────────────────────────────────┬──────────────────────────────────┐
                  │ Old (blocked in Java 17+)       │ Replacement                      │
                  ├────────────────────────────────┼──────────────────────────────────┤
                  │ sun.misc.Unsafe                │ VarHandle, MethodHandles          │
                  │ sun.reflect.*                  │ java.lang.invoke.*                │
                  │ com.sun.net.httpserver         │ java.net.http.HttpClient (J11)    │
                  │ Reflection on JDK fields       │ Public API methods                │
                  │ Base64 in sun.misc             │ java.util.Base64 (J8)             │
                  └────────────────────────────────┴──────────────────────────────────┘
                """);
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Strong Encapsulation of JDK Internals (JEP 403) =====");

        demonstrateBlocking();
        publicApiApproach();

        try {
            varHandleDemo();
            methodHandlesDemo();
            ownClassReflection();
        } catch (Throwable t) {
            System.out.println("  Error in demo: " + t.getMessage());
        }

        moduleSystemNotes();
    }
}
