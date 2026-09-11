package com.example.java21;

import java.util.*;

/**
 * Java 21 Feature: Sequenced Collections (JEP 431)
 *
 * Before Java 21, there was no uniform way to:
 * - Get the first/last element of a collection.
 * - Iterate in reverse.
 *
 * Java 21 adds three new interfaces to the Collections hierarchy:
 *
 *   SequencedCollection<E>   — first(), last(), addFirst(), addLast(), reversed()
 *   SequencedSet<E>          — same, but for Sets (no duplicates)
 *   SequencedMap<K,V>        — firstEntry(), lastEntry(), reversed(), etc.
 *
 * Implemented by: ArrayList, LinkedList, TreeSet, LinkedHashSet, TreeMap, LinkedHashMap
 */
public class SequencedCollections {

    // ── 1. SequencedCollection — List ────────────────────────────────────────

    static void sequencedList() {
        System.out.println("\n-- SequencedCollection (List) --");

        SequencedCollection<String> list = new ArrayList<>(List.of("B", "C", "D"));

        list.addFirst("A");   // insert at front
        list.addLast("E");    // insert at end
        System.out.println("List:  " + list);             // [A, B, C, D, E]

        System.out.println("First: " + list.getFirst());  // A
        System.out.println("Last:  " + list.getLast());   // E

        list.removeFirst();   // remove A
        list.removeLast();    // remove E
        System.out.println("After remove: " + list);     // [B, C, D]

        // Reversed view — NOT a copy, backed by original
        SequencedCollection<String> reversed = list.reversed();
        System.out.println("Reversed: " + reversed);     // [D, C, B]
    }

    // ── 2. SequencedCollection — Deque / LinkedList ───────────────────────────

    static void sequencedDeque() {
        System.out.println("\n-- SequencedCollection (Deque) --");

        SequencedCollection<Integer> deque = new LinkedList<>(List.of(10, 20, 30));
        deque.addFirst(5);
        deque.addLast(40);
        System.out.println("Deque: " + deque);           // [5, 10, 20, 30, 40]
        System.out.println("First: " + deque.getFirst()); // 5
        System.out.println("Last:  " + deque.getLast());  // 40
    }

    // ── 3. SequencedSet — LinkedHashSet (insertion-ordered, no duplicates) ────

    static void sequencedSet() {
        System.out.println("\n-- SequencedSet (LinkedHashSet) --");

        SequencedSet<String> set = new LinkedHashSet<>();
        set.add("Java");
        set.add("Python");
        set.add("Go");
        set.addFirst("C");      // insert at front
        set.addLast("Kotlin");  // insert at end

        System.out.println("Set:   " + set);             // [C, Java, Python, Go, Kotlin]
        System.out.println("First: " + set.getFirst());  // C
        System.out.println("Last:  " + set.getLast());   // Kotlin
        System.out.println("Rev:   " + set.reversed());  // [Kotlin, Go, Python, Java, C]
    }

    // ── 4. SequencedSet — TreeSet (sorted order) ─────────────────────────────

    static void sequencedTreeSet() {
        System.out.println("\n-- SequencedSet (TreeSet) --");

        SequencedSet<Integer> sorted = new TreeSet<>(List.of(5, 2, 8, 1, 9, 3));
        System.out.println("Sorted: " + sorted);           // [1, 2, 3, 5, 8, 9]
        System.out.println("First:  " + sorted.getFirst()); // 1
        System.out.println("Last:   " + sorted.getLast());  // 9
        System.out.println("Rev:    " + sorted.reversed()); // [9, 8, 5, 3, 2, 1]
    }

    // ── 5. SequencedMap — LinkedHashMap ───────────────────────────────────────

    static void sequencedMap() {
        System.out.println("\n-- SequencedMap (LinkedHashMap) --");

        SequencedMap<String, Integer> scores = new LinkedHashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Carol", 92);

        scores.putFirst("Zara", 99);   // insert at front
        scores.putLast("Dave", 78);    // insert at end

        System.out.println("Map:         " + scores);
        System.out.println("firstEntry:  " + scores.firstEntry());  // Zara=99
        System.out.println("lastEntry:   " + scores.lastEntry());   // Dave=78
        System.out.println("firstKey:    " + scores.sequencedKeySet().getFirst());
        System.out.println("lastValue:   " + scores.sequencedValues().getLast());

        // Reversed map view
        SequencedMap<String, Integer> rev = scores.reversed();
        System.out.println("Reversed map: " + rev);
    }

    // ── 6. SequencedMap — TreeMap (sorted by key) ─────────────────────────────

    static void sequencedTreeMap() {
        System.out.println("\n-- SequencedMap (TreeMap) --");

        SequencedMap<String, String> capitals = new TreeMap<>();
        capitals.put("India",  "New Delhi");
        capitals.put("France", "Paris");
        capitals.put("Brazil", "Brasília");
        capitals.put("USA",    "Washington DC");

        System.out.println("Map:        " + capitals);
        System.out.println("First entry: " + capitals.firstEntry()); // Brazil (alphabetical)
        System.out.println("Last entry:  " + capitals.lastEntry());  // USA
    }

    // ── 7. Before vs After comparison ────────────────────────────────────────

    static void beforeVsAfter() {
        System.out.println("\n-- Before vs After Java 21 --");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

        // BEFORE Java 21 — verbose and error-prone
        String firstBefore = list.get(0);
        String lastBefore  = list.get(list.size() - 1);
        System.out.println("Before — first: " + firstBefore + ", last: " + lastBefore);

        // AFTER Java 21 — clean and uniform
        String firstAfter = list.getFirst();
        String lastAfter  = list.getLast();
        System.out.println("After  — first: " + firstAfter + ", last: " + lastAfter);
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Sequenced Collections =====");
        sequencedList();
        sequencedDeque();
        sequencedSet();
        sequencedTreeSet();
        sequencedMap();
        sequencedTreeMap();
        beforeVsAfter();
    }
}
