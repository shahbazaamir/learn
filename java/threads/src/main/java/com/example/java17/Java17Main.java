package com.example.java17;

/**
 * Java 17 Features — Main Runner
 *
 * Java 17 is an LTS release. Features that became FINAL in Java 17:
 *
 * ┌──────────────────────────────────┬──────────────────────────────────────────┬───────────┐
 * │ File                             │ Feature                                  │ JEP       │
 * ├──────────────────────────────────┼──────────────────────────────────────────┼───────────┤
 * │ SealedClassesJ17.java            │ Sealed Classes (final in Java 17)        │ JEP 409   │
 * │ PatternMatchingJ17.java          │ Pattern Matching instanceof (final J16)  │ JEP 394   │
 * │                                  │ Switch Expressions (final Java 14)       │ JEP 361   │
 * │ RecordsJ17.java                  │ Records (final in Java 16)               │ JEP 395   │
 * │ RandomGeneratorsJ17.java         │ Enhanced PRNG (new in Java 17)           │ JEP 356   │
 * │ StrongEncapsulationJ17.java      │ Strong Encapsulation JDK (final J17)     │ JEP 403   │
 * └──────────────────────────────────┴──────────────────────────────────────────┴───────────┘
 *
 * Other Java 17 changes (no runnable example needed):
 *   JEP 356 — Context-Specific Deserialization Filters
 *   JEP 382 — New macOS Rendering Pipeline
 *   JEP 391 — macOS/AArch64 Port
 *   JEP 398 — Deprecate Applet API for Removal
 *   JEP 407 — Remove RMI Activation
 *   JEP 410 — Remove Experimental AOT and JIT Compiler
 *   JEP 411 — Deprecate Security Manager for Removal
 *
 * To run:
 *   javac -source 17 -target 17 src/main/java/com/example/java17/*.java -d /tmp/java17out
 *   java -cp /tmp/java17out com.example.java17.Java17Main
 */
public class Java17Main {

    public static void main(String[] args) throws Exception {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       Java 17 Features — Examples        ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // 1. Sealed Classes (JEP 409)
        SealedClassesJ17.run();

        // 2. Pattern Matching instanceof + Switch Expressions (JEP 394, 361)
        PatternMatchingJ17.run();

        // 3. Records (JEP 395)
        RecordsJ17.run();

        // 4. Enhanced PRNG (JEP 356)
        RandomGeneratorsJ17.run();

        // 5. Strong Encapsulation of JDK Internals (JEP 403)
        StrongEncapsulationJ17.run();

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║          All examples complete!          ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
