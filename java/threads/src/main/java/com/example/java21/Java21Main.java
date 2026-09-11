package com.example.java21;

/**
 * Java 21 Features — Main Runner
 *
 * Runs all feature examples in sequence.
 * Each class demonstrates one major Java 21 feature area.
 *
 * Features covered:
 * ┌─────────────────────────────────┬──────────────────────────────────────────────┐
 * │ File                            │ Feature (JEP)                                │
 * ├─────────────────────────────────┼──────────────────────────────────────────────┤
 * │ RecordPatterns.java             │ Records + Record Patterns (JEP 440)          │
 * │ SealedClasses.java              │ Sealed Classes (JEP 409)                     │
 * │ PatternMatching.java            │ Pattern Matching instanceof + switch (441)   │
 * │ SequencedCollections.java       │ Sequenced Collections (JEP 431)              │
 * │ TextBlocks.java                 │ Text Blocks + String methods (JEP 378)       │
 * │ VirtualThreads.java             │ Virtual Threads (JEP 444)                    │
 * │ StructuredConcurrency.java      │ Structured Concurrency (JEP 453)             │
 * └─────────────────────────────────┴──────────────────────────────────────────────┘
 *
 * To run:  mvn exec:java -Dexec.mainClass="com.example.java21.Java21Main"
 */
public class Java21Main {

    public static void main(String[] args) throws Exception {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       Java 21 Features — Examples        ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // 1. Records + Record Patterns
        RecordPatterns.run();

        // 2. Sealed Classes
        SealedClasses.run();

        // 3. Pattern Matching (instanceof + switch)
        PatternMatching.run();

        // 4. Sequenced Collections
        SequencedCollections.run();

        // 5. Text Blocks + String methods
        TextBlocks.run();

        // 6. Virtual Threads (Project Loom)
        VirtualThreads.run();

        // 7. Structured Concurrency
        StructuredConcurrency.run();

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║              All examples done!          ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
