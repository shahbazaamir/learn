package com.example.java21;

/**
 * Java 21 Feature: Records + Record Patterns (JEP 440)
 *
 * Records    — compact, immutable data carriers (final since Java 16).
 * Record Patterns — deconstruct record components directly inside instanceof and switch.
 */
public class RecordPatterns {

    // ── 1. Basic Records ──────────────────────────────────────────────────────

    // A record auto-generates: constructor, getters, equals, hashCode, toString
    record Point(int x, int y) {}

    record Employee(String name, int age, String department) {
        // Compact canonical constructor — add validation
        Employee {
            if (age < 18) throw new IllegalArgumentException("Age must be >= 18");
            name = name.trim();
        }

        // Custom method inside a record
        boolean isSenior() {
            return age >= 40;
        }
    }

    // ── 2. Nested Records ────────────────────────────────────────────────────

    record Address(String city, String country) {}
    record Person(String name, Address address) {}

    // ── 3. Generic Records ───────────────────────────────────────────────────

    record Pair<A, B>(A first, B second) {}

    // ── 4. Record Patterns in instanceof (JEP 440) ───────────────────────────
    // Deconstruct directly — no need for manual .x(), .y() calls

    static String describePoint(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            // x and y are already extracted
            return "Point at x=%d, y=%d, distance from origin=%.2f".formatted(
                    x, y, Math.sqrt(x * x + y * y));
        }
        return "Not a point";
    }

    // ── 5. Nested Record Patterns ─────────────────────────────────────────────

    static String describePersonCity(Object obj) {
        // Deconstruct Person AND its nested Address in one expression
        if (obj instanceof Person(String name, Address(String city, String country))) {
            return "%s lives in %s, %s".formatted(name, city, country);
        }
        return "Unknown";
    }

    // ── 6. Record Patterns in switch ─────────────────────────────────────────

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    static double area(Shape shape) {
        return switch (shape) {
            case Circle(double r)              -> Math.PI * r * r;
            case Rectangle(double w, double h) -> w * h;
            case Triangle(double b, double h)  -> 0.5 * b * h;
        };
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Records & Record Patterns =====");

        // Basic record usage
        Point p = new Point(3, 4);
        System.out.println("Point: " + p);                   // auto toString
        System.out.println("x=" + p.x() + ", y=" + p.y());  // auto accessors

        Employee e = new Employee("  Alice  ", 35, "Engineering");
        System.out.println(e);
        System.out.println("Senior? " + e.isSenior());

        // Record equality — by value, not reference
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println("Points equal: " + p1.equals(p2)); // true

        // Generic record
        Pair<String, Integer> pair = new Pair<>("Java", 21);
        System.out.println("Pair: " + pair);

        // Record pattern — instanceof
        System.out.println(describePoint(new Point(3, 4)));
        System.out.println(describePoint("not a point"));

        // Nested record pattern
        Person person = new Person("Bob", new Address("Mumbai", "India"));
        System.out.println(describePersonCity(person));

        // Record pattern in switch — area calculation
        System.out.printf("Circle area: %.2f%n",    area(new Circle(5)));
        System.out.printf("Rectangle area: %.2f%n", area(new Rectangle(4, 6)));
        System.out.printf("Triangle area: %.2f%n",  area(new Triangle(3, 8)));
    }
}
