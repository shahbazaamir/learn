package com.example.java17;

/**
 * Java 17 Feature: Sealed Classes and Interfaces (JEP 409)
 * Final in Java 17 (preview in Java 15, second preview in Java 16).
 *
 * A sealed class/interface restricts which classes can extend or implement it.
 * The permitted subtypes must be listed explicitly using the `permits` clause.
 *
 * Why it matters:
 * - Gives the compiler full knowledge of all subtypes.
 * - Enables EXHAUSTIVE switch — no default case needed (compiler verifies).
 * - Models closed domain hierarchies (HTTP status, payment types, AST nodes).
 * - Works together with records and pattern matching for expressive code.
 *
 * Three subtype modifiers:
 *   final        — no further subclassing allowed
 *   sealed       — can be extended but only by its own permits list
 *   non-sealed   — opened back up — anyone can extend it
 */
public class SealedClassesJ17 {

    // ── 1. Sealed interface with record implementations ───────────────────────

    sealed interface HttpResponse permits HttpResponse.Ok, HttpResponse.NotFound,
                                          HttpResponse.ServerError, HttpResponse.Redirect {

        int statusCode();
        String body();

        record Ok(String body) implements HttpResponse {
            public int statusCode() { return 200; }
        }

        record NotFound(String path) implements HttpResponse {
            public int statusCode() { return 404; }
            public String body()    { return "Not found: " + path; }
        }

        record ServerError(String message, Throwable cause) implements HttpResponse {
            public int statusCode() { return 500; }
            public String body()    { return "Internal error: " + message; }
        }

        record Redirect(String location) implements HttpResponse {
            public int statusCode() { return 302; }
            public String body()    { return "Redirect to: " + location; }
        }
    }

    // Exhaustive check via instanceof — switch type patterns are Java 21+
    static String handleResponse(HttpResponse response) {
        if (response instanceof HttpResponse.Ok ok)
            return "✅ 200 OK: " + ok.body();
        if (response instanceof HttpResponse.NotFound nf)
            return "❌ 404: " + nf.body();
        if (response instanceof HttpResponse.ServerError se)
            return "💥 500: " + se.body();
        if (response instanceof HttpResponse.Redirect rd)
            return "↩️  302: " + rd.body();
        throw new AssertionError("Unhandled HttpResponse type: " + response.getClass());
    }

    // ── 2. Sealed abstract class hierarchy — geometry ─────────────────────────

    sealed abstract static class Shape
            permits SealedClassesJ17.Circle,
                    SealedClassesJ17.Rectangle,
                    SealedClassesJ17.Triangle {
        abstract double area();
        abstract double perimeter();
        abstract String name();
    }

    // final — cannot be further subclassed
    static final class Circle extends Shape {
        private final double radius;
        Circle(double radius) { this.radius = radius; }

        @Override public double area()      { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
        @Override public String name()      { return "Circle(r=" + radius + ")"; }
    }

    static final class Rectangle extends Shape {
        private final double w, h;
        Rectangle(double w, double h) { this.w = w; this.h = h; }

        @Override public double area()      { return w * h; }
        @Override public double perimeter() { return 2 * (w + h); }
        @Override public String name()      { return "Rectangle(" + w + "×" + h + ")"; }
    }

    static final class Triangle extends Shape {
        private final double a, b, c;
        Triangle(double a, double b, double c) { this.a = a; this.b = b; this.c = c; }

        @Override public double area() {
            double s = (a + b + c) / 2;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }
        @Override public double perimeter() { return a + b + c; }
        @Override public String name()      { return "Triangle(" + a + "," + b + "," + c + ")"; }
    }

    static void describeShape(Shape shape) {
        // instanceof pattern matching (Java 17) — switch type patterns are Java 21+
        String desc;
        if (shape instanceof Circle c)
            desc = "Circle    — area=%.2f, circumference=%.2f".formatted(c.area(), c.perimeter());
        else if (shape instanceof Rectangle r)
            desc = "Rectangle — area=%.2f, perimeter=%.2f".formatted(r.area(), r.perimeter());
        else if (shape instanceof Triangle t)
            desc = "Triangle  — area=%.2f, perimeter=%.2f".formatted(t.area(), t.perimeter());
        else
            throw new AssertionError("Unknown shape");
        System.out.println(shape.name() + " → " + desc);
    }

    // ── 3. non-sealed — open extension point ──────────────────────────────────

    sealed interface Validator<T> permits RangeValidator, RegexValidator, CompositeValidator {}

    static final class RangeValidator implements Validator<Integer> {
        private final int min, max;
        RangeValidator(int min, int max) { this.min = min; this.max = max; }
        boolean validate(int value) { return value >= min && value <= max; }
        @Override public String toString() { return "Range[" + min + ".." + max + "]"; }
    }

    static final class RegexValidator implements Validator<String> {
        private final String pattern;
        RegexValidator(String pattern) { this.pattern = pattern; }
        boolean validate(String value) { return value.matches(pattern); }
        @Override public String toString() { return "Regex[" + pattern + "]"; }
    }

    // non-sealed — users of this library can create custom composite validators
    static non-sealed class CompositeValidator implements Validator<String> {
        @Override public String toString() { return "CompositeValidator"; }
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Sealed Classes (JEP 409) =====");

        // HTTP responses
        System.out.println("\n-- Sealed interface: HttpResponse --");
        HttpResponse[] responses = {
            new HttpResponse.Ok("Hello, world"),
            new HttpResponse.NotFound("/api/users/999"),
            new HttpResponse.ServerError("DB timeout", new RuntimeException("Connection refused")),
            new HttpResponse.Redirect("https://example.com/new-url"),
        };
        for (HttpResponse r : responses) {
            System.out.printf("  [%d] %s%n", r.statusCode(), handleResponse(r));
        }

        // Shape hierarchy
        System.out.println("\n-- Sealed abstract class: Shape --");
        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 4, 5),
        };
        for (Shape s : shapes) describeShape(s);

        // Validators
        System.out.println("\n-- Sealed interface with non-sealed: Validator --");
        RangeValidator ageValidator = new RangeValidator(18, 65);
        RegexValidator emailValidator = new RegexValidator("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

        System.out.println("  Age 25 valid?        " + ageValidator.validate(25));
        System.out.println("  Age 10 valid?        " + ageValidator.validate(10));
        System.out.println("  Email valid?         " + emailValidator.validate("user@example.com"));
        System.out.println("  Bad email valid?     " + emailValidator.validate("not-an-email"));
    }
}
