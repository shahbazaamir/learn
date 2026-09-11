package com.example.java17;

import java.util.List;
import java.util.Map;

/**
 * Java 17 Feature: Pattern Matching for instanceof (JEP 394)
 * Final in Java 16. Used extensively from Java 17 onwards.
 *
 * Before Java 16:
 *   if (obj instanceof String) {
 *       String s = (String) obj;  // explicit cast — redundant
 *       ...
 *   }
 *
 * Java 16+:
 *   if (obj instanceof String s) {  // bind variable 's' in one step
 *       ...use s directly...
 *   }
 *
 * The binding variable is:
 * - Only in scope where the condition is TRUE (flow-scoped).
 * - Final — cannot be reassigned.
 * - Works with &&, negation (!), and complex conditions.
 *
 * Also covers: switch expressions (JEP 361, final Java 14) —
 * a prerequisite for pattern matching switch in Java 21.
 */
public class PatternMatchingJ17 {

    // ── 1. Basic pattern matching instanceof ──────────────────────────────────

    static void basicPatternMatching() {
        System.out.println("\n-- Basic Pattern Matching instanceof --");

        Object[] values = { "Hello Java 17", 42, 3.14, List.of(1, 2, 3), null, true };

        for (Object obj : values) {

            // Bind + cast in one expression — 's' is a String in scope here
            if (obj instanceof String s) {
                System.out.println("String(len=" + s.length() + "): " + s.toUpperCase());

            } else if (obj instanceof Integer i) {
                System.out.println("Integer: " + i * 2 + " (doubled)");

            } else if (obj instanceof Double d) {
                System.out.printf("Double: %.4f%n", d);

            } else if (obj instanceof List<?> list) {
                System.out.println("List of " + list.size() + " elements");

            } else if (obj instanceof Boolean b) {
                System.out.println("Boolean: " + b);

            } else {
                System.out.println("null or unknown: " + obj);
            }
        }
    }

    // ── 2. Compound conditions with && ────────────────────────────────────────

    static void compoundConditions() {
        System.out.println("\n-- Compound Conditions (flow scoping) --");

        Object[] values = { "hi", "Hello World!", 10, 150, -5 };

        for (Object obj : values) {

            // Pattern binding + guard in one line
            if (obj instanceof String s && s.length() > 5) {
                System.out.println("Long string: " + s);

            } else if (obj instanceof String s) {
                System.out.println("Short string: " + s);

            } else if (obj instanceof Integer i && i > 0 && i <= 100) {
                System.out.println("Positive int in range: " + i);

            } else if (obj instanceof Integer i && i < 0) {
                System.out.println("Negative int: " + i);

            } else if (obj instanceof Integer i) {
                System.out.println("Large int: " + i);
            }
        }
    }

    // ── 3. Negation — useful for early-return / guard clauses ─────────────────

    static String processInput(Object input) {
        // Guard clause — exit early if not the expected type
        if (!(input instanceof String s)) {
            return "Expected a String but got: " + (input == null ? "null" : input.getClass().getSimpleName());
        }
        // 's' is in scope here — we know it's a String
        return "Processed: " + s.trim().toLowerCase().replace(" ", "_");
    }

    // ── 4. Pattern matching with inheritance ──────────────────────────────────

    static abstract class Animal {
        abstract String name();
    }
    static class Dog extends Animal {
        String name() { return "Dog"; }
        String breed() { return "Labrador"; }
        void fetch() { System.out.println("Fetching!"); }
    }
    static class Cat extends Animal {
        String name() { return "Cat"; }
        boolean isIndoor() { return true; }
        void purr() { System.out.println("Purring..."); }
    }
    static class Bird extends Animal {
        String name() { return "Bird"; }
        double wingspan() { return 0.5; }
    }

    static void describeAnimal(Animal animal) {
        // No more manual casting — the bound variable has the specific type
        if (animal instanceof Dog dog) {
            System.out.println("Dog: " + dog.breed());
            dog.fetch();
        } else if (animal instanceof Cat cat && cat.isIndoor()) {
            System.out.println("Indoor cat");
            cat.purr();
        } else if (animal instanceof Bird bird) {
            System.out.printf("Bird with %.1fm wingspan%n", bird.wingspan());
        }
    }

    // ── 5. Switch Expressions (JEP 361, final Java 14) ────────────────────────
    // Foundation for pattern matching switch in Java 21.
    // Shown here as it's a core Java 17 feature used daily.

    static void switchExpressions() {
        System.out.println("\n-- Switch Expressions (JEP 361) --");

        // Arrow form — no fall-through, returns a value
        for (int month = 1; month <= 12; month++) {
            int days = switch (month) {
                case 1, 3, 5, 7, 8, 10, 12 -> 31;
                case 4, 6, 9, 11            -> 30;
                case 2                      -> 28; // simplified
                default -> throw new IllegalArgumentException("Invalid month: " + month);
            };
            System.out.printf("  Month %2d → %d days%n", month, days);
        }

        // yield — return from a block arm
        System.out.println("\n-- yield in switch --");
        int score = 73;
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8     -> "B";
            case 7     -> "C";
            case 6     -> "D";
            default    -> {
                if (score < 0) yield "Invalid score";
                yield "F";
            }
        };
        System.out.println("Score " + score + " → Grade: " + grade);

        // switch on String (available since Java 7, commonly used with expressions)
        String[] httpMethods = { "GET", "POST", "PUT", "DELETE", "PATCH" };
        for (String method : httpMethods) {
            String description = switch (method) {
                case "GET"    -> "Read resource";
                case "POST"   -> "Create resource";
                case "PUT"    -> "Replace resource";
                case "DELETE" -> "Delete resource";
                case "PATCH"  -> "Partial update";
                default       -> "Unknown method";
            };
            System.out.printf("  %-7s → %s%n", method, description);
        }
    }

    // ── 6. Real-world: processing a heterogeneous event stream ────────────────

    sealed interface AppEvent permits PatternMatchingJ17.UserEvent,
                                      PatternMatchingJ17.OrderEvent,
                                      PatternMatchingJ17.SystemEvent {}
    record UserEvent(String userId, String action)      implements AppEvent {}
    record OrderEvent(String orderId, double amount)    implements AppEvent {}
    record SystemEvent(String level, String message)    implements AppEvent {}

    static void processEvent(AppEvent event) {
        // Pattern matching makes this safe, readable, no casting
        if (event instanceof UserEvent ue) {
            System.out.printf("  [USER]   %s performed '%s'%n", ue.userId(), ue.action());
        } else if (event instanceof OrderEvent oe && oe.amount() > 1000) {
            System.out.printf("  [ORDER]  🚨 High-value order %s: $%.2f%n", oe.orderId(), oe.amount());
        } else if (event instanceof OrderEvent oe) {
            System.out.printf("  [ORDER]  Order %s: $%.2f%n", oe.orderId(), oe.amount());
        } else if (event instanceof SystemEvent se) {
            System.out.printf("  [SYSTEM] [%s] %s%n", se.level(), se.message());
        }
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Pattern Matching instanceof (JEP 394) =====");

        basicPatternMatching();
        compoundConditions();

        System.out.println("\n-- Negation / guard clause --");
        System.out.println(processInput("Hello World"));
        System.out.println(processInput(42));
        System.out.println(processInput(null));

        System.out.println("\n-- Pattern matching with inheritance --");
        List<Animal> animals = List.of(new Dog(), new Cat(), new Bird());
        animals.forEach(PatternMatchingJ17::describeAnimal);

        switchExpressions();

        System.out.println("\n-- Real-world: event stream processing --");
        List<AppEvent> events = List.of(
            new UserEvent("U001", "LOGIN"),
            new OrderEvent("ORD-101", 250.00),
            new OrderEvent("ORD-102", 5999.99),
            new SystemEvent("ERROR", "DB connection timeout"),
            new UserEvent("U002", "LOGOUT")
        );
        events.forEach(PatternMatchingJ17::processEvent);
    }
}
