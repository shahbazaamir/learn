package com.example.java21;

/**
 * Java 21 Features: Pattern Matching
 *
 * 1. Pattern Matching for instanceof (JEP 394, final since Java 16)
 *    — No manual cast after type check.
 *
 * 2. Pattern Matching for switch (JEP 441, final in Java 21)
 *    — Switch on any type (not just int/String/enum).
 *    — Type patterns, guarded patterns (when), null handling.
 *
 * 3. Switch Expressions (JEP 361, final since Java 14)
 *    — switch as an expression that returns a value (arrow syntax).
 */
public class PatternMatching {

    // ── 1. Pattern Matching for instanceof ────────────────────────────────────

    static void instanceofPatterns() {
        System.out.println("\n-- Pattern Matching for instanceof --");

        Object[] values = { "Hello", 42, 3.14, true, null, new int[]{1,2,3} };

        for (Object obj : values) {

            // OLD way — explicit cast required after check
            // if (obj instanceof String) { String s = (String) obj; ... }

            // NEW way — bind and cast in one step
            if (obj instanceof String s) {
                System.out.println("String of length " + s.length() + ": " + s.toUpperCase());

            } else if (obj instanceof Integer i && i > 10) {
                // Guarded pattern — 'i' is bound AND guard 'i > 10' must pass
                System.out.println("Integer > 10: " + i);

            } else if (obj instanceof Double d) {
                System.out.printf("Double: %.4f%n", d);

            } else if (obj instanceof Boolean b) {
                System.out.println("Boolean: " + b);

            } else if (obj instanceof int[] arr) {
                System.out.println("int[] of length " + arr.length);

            } else {
                System.out.println("Other or null: " + obj);
            }
        }
    }

    // ── 2. Pattern Matching switch — type patterns ────────────────────────────

    static String formatValue(Object obj) {
        // switch can now match on any type
        return switch (obj) {
            case Integer i   -> "int:    " + i;
            case Long l      -> "long:   " + l;
            case Double d    -> "double: %.2f".formatted(d);
            case String s    -> "string: \"" + s + "\"";
            case Boolean b   -> "bool:   " + b;
            case null        -> "(null)";
            default          -> "other:  " + obj.getClass().getSimpleName();
        };
    }

    // ── 3. Guarded patterns in switch (when clause) ───────────────────────────

    static String classifyNumber(Object obj) {
        return switch (obj) {
            case Integer i when i < 0    -> i + " is negative";
            case Integer i when i == 0   -> "zero";
            case Integer i when i < 100  -> i + " is a small positive";
            case Integer i               -> i + " is a large positive";
            case Double d when d < 0     -> d + " is negative double";
            case Double d                -> d + " is positive double";
            case null                    -> "null value";
            default                      -> "not a number";
        };
    }

    // ── 4. switch with null handling ──────────────────────────────────────────

    static void nullHandlingInSwitch() {
        System.out.println("\n-- Null handling in switch (Java 21) --");

        // Before Java 21: switch throws NullPointerException on null input
        // Now: null can be an explicit case

        Object[] inputs = { "active", "inactive", null, "unknown" };
        for (Object status : inputs) {
            String result = switch (status) {
                case String s when s.equals("active")   -> "✅ Active";
                case String s when s.equals("inactive") -> "⏸ Inactive";
                case null                               -> "⚠️  No status";
                default                                 -> "❓ Unknown: " + status;
            };
            System.out.println(result);
        }
    }

    // ── 5. switch expression (returns a value) ────────────────────────────────

    static void switchExpressions() {
        System.out.println("\n-- Switch Expressions --");

        // Arrow form — no fall-through, no break needed
        for (int day = 1; day <= 7; day++) {
            String type = switch (day) {
                case 1, 7 -> "Weekend";
                case 2, 3, 4, 5, 6 -> "Weekday";
                default -> "Invalid";
            };
            System.out.println("Day " + day + ": " + type);
        }

        // yield — return a value from a block arm
        int score = 85;
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8     -> "B";
            case 7     -> "C";
            case 6     -> "D";
            default    -> {
                if (score < 0) yield "Invalid";
                yield "F";
            }
        };
        System.out.println("Score " + score + " → Grade " + grade);
    }

    // ── 6. Combining sealed types + pattern switch ─────────────────────────────

    sealed interface Animal permits Dog, Cat, Bird {}
    record Dog(String name, String breed)  implements Animal {}
    record Cat(String name, boolean indoor) implements Animal {}
    record Bird(String name, boolean canFly) implements Animal {}

    static String describeAnimal(Animal animal) {
        return switch (animal) {
            case Dog(String n, String b)            -> "%s is a %s dog".formatted(n, b);
            case Cat(String n, boolean i) when i         -> "%s is an indoor cat".formatted(n);
            case Cat(String n, boolean ignored)          -> "%s is an outdoor cat".formatted(n);
            case Bird(String n, boolean f) when f        -> "%s can fly".formatted(n);
            case Bird(String n, boolean ignoredFly)      -> "%s cannot fly".formatted(n);
        };
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Pattern Matching =====");

        instanceofPatterns();

        System.out.println("\n-- Pattern switch: type patterns --");
        Object[] values = { 42, 3.14, "hello", true, null, 1000L };
        for (Object v : values) System.out.println(formatValue(v));

        System.out.println("\n-- Pattern switch: guarded patterns --");
        Object[] numbers = { -5, 0, 42, 200, 3.14, -1.5, null, "text" };
        for (Object n : numbers) System.out.println(classifyNumber(n));

        nullHandlingInSwitch();
        switchExpressions();

        System.out.println("\n-- Sealed + Pattern switch --");
        Animal[] animals = {
            new Dog("Rex", "German Shepherd"),
            new Cat("Whiskers", true),
            new Cat("Shadow", false),
            new Bird("Eagle", true),
            new Bird("Penguin", false)
        };
        for (Animal a : animals) System.out.println(describeAnimal(a));
    }
}
