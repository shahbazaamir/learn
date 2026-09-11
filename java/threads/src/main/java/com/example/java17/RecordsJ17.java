package com.example.java17;

import java.util.List;
import java.util.Objects;

/**
 * Java 17 Feature: Records (JEP 395)
 * Final in Java 16. A core daily-use feature from Java 17 onwards.
 *
 * A record is a transparent, immutable data carrier.
 * The compiler auto-generates:
 *   - Canonical constructor (all-args)
 *   - Accessor methods (component name, no get prefix)
 *   - equals() and hashCode() based on all components
 *   - toString()
 *
 * What records CANNOT do:
 *   - Extend other classes (implicitly extends java.lang.Record)
 *   - Have non-static mutable fields
 *   - Be abstract
 *
 * What records CAN do:
 *   - Implement interfaces
 *   - Have static fields and methods
 *   - Have instance methods
 *   - Have multiple constructors (with compact canonical)
 *   - Use generics
 *   - Be nested (inside classes, methods)
 */
public class RecordsJ17 {

    // ── 1. Basic record ───────────────────────────────────────────────────────

    record Point(double x, double y) {
        // Instance method — allowed in records
        double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        // Static factory method — preferred over new keyword in production
        static Point origin() { return new Point(0, 0); }
        static Point of(double x, double y) { return new Point(x, y); }
    }

    // ── 2. Compact canonical constructor — validation and normalization ────────

    record Employee(String name, int age, String department, double salary) {

        // Compact constructor — no parameter list, no this.x = x needed
        // The compiler adds the assignments after your code runs
        Employee {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(department, "department cannot be null");
            if (age < 18 || age > 70)
                throw new IllegalArgumentException("Age must be 18–70, got: " + age);
            if (salary < 0)
                throw new IllegalArgumentException("Salary cannot be negative");

            // Normalize
            name       = name.trim();
            department = department.trim().toLowerCase();
        }

        // Derived accessor — not a component but computed from components
        boolean isSenior()     { return age >= 40; }
        String  displayName()  { return name + " (" + department + ")"; }
        double  monthlySalary(){ return salary / 12; }
    }

    // ── 3. Records implementing interfaces ────────────────────────────────────

    interface Printable {
        void print();
    }

    interface Persistable {
        String toJson();
    }

    record Product(int id, String name, double price, String category)
            implements Printable, Persistable {

        @Override
        public void print() {
            System.out.printf("  [%d] %-25s $%7.2f  (%s)%n", id, name, price, category);
        }

        @Override
        public String toJson() {
            return """
                    {"id":%d,"name":"%s","price":%.2f,"category":"%s"}"""
                    .formatted(id, name, price, category);
        }

        // Static field allowed
        static final double TAX_RATE = 0.18;

        double priceWithTax() { return price * (1 + TAX_RATE); }
    }

    // ── 4. Generic records ────────────────────────────────────────────────────

    record Pair<A, B>(A first, B second) {
        // Swap the pair
        Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    record ApiResponse<T>(T data, String status, int code, String message) {
        boolean isSuccess() { return code >= 200 && code < 300; }

        static <T> ApiResponse<T> ok(T data) {
            return new ApiResponse<>(data, "success", 200, "OK");
        }
        static <T> ApiResponse<T> notFound(String msg) {
            return new ApiResponse<>(null, "error", 404, msg);
        }
        static <T> ApiResponse<T> serverError(String msg) {
            return new ApiResponse<>(null, "error", 500, msg);
        }
    }

    // ── 5. Nested records ─────────────────────────────────────────────────────

    record Address(String street, String city, String country, String zipCode) {}

    record Customer(String id, String name, String email, Address address) {
        // Override canonical constructor with extra validation
        Customer {
            Objects.requireNonNull(id, "id required");
            if (!email.contains("@"))
                throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    // ── 6. Records in collections and streams ─────────────────────────────────

    record OrderItem(String productName, int quantity, double unitPrice) {
        double total() { return quantity * unitPrice; }
    }

    record Order(String orderId, String customerId, List<OrderItem> items) {
        // Defensive copy in compact constructor — records are immutable by convention
        Order {
            items = List.copyOf(items); // unmodifiable
        }

        double grandTotal() {
            return items.stream().mapToDouble(OrderItem::total).sum();
        }

        int itemCount() {
            return items.stream().mapToInt(OrderItem::quantity).sum();
        }
    }

    // ── 7. Local records (declared inside a method) ───────────────────────────

    static void localRecords() {
        System.out.println("\n-- Local Records (inside a method) --");

        // Records can be declared locally — great for intermediate data in streams
        record NameScore(String name, int score) {}

        List<NameScore> scores = List.of(
                new NameScore("Alice", 92),
                new NameScore("Bob",   78),
                new NameScore("Carol", 88),
                new NameScore("Dave",  95)
        );

        scores.stream()
                .filter(ns -> ns.score() >= 85)
                .sorted((a, b) -> b.score() - a.score())
                .forEach(ns -> System.out.printf("  %-10s → %d%n", ns.name(), ns.score()));
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Records (JEP 395) =====");

        // Basic record
        System.out.println("\n-- Basic record: Point --");
        Point p1 = Point.of(3, 4);
        Point p2 = new Point(6, 8);
        System.out.println("  p1 = " + p1);
        System.out.printf("  Distance p1→p2: %.2f%n", p1.distanceTo(p2));
        System.out.println("  Origin: " + Point.origin());
        System.out.println("  p1.equals(Point.of(3,4)): " + p1.equals(Point.of(3, 4))); // true

        // Compact constructor + validation
        System.out.println("\n-- Compact constructor: Employee --");
        Employee emp = new Employee("  Alice Smith  ", 35, "  Engineering  ", 90000);
        System.out.println("  " + emp);
        System.out.println("  displayName: " + emp.displayName());
        System.out.println("  Senior? " + emp.isSenior());
        System.out.printf("  Monthly: $%.2f%n", emp.monthlySalary());

        try {
            new Employee("Bob", 15, "HR", 50000); // age < 18
        } catch (IllegalArgumentException e) {
            System.out.println("  Validation caught: " + e.getMessage());
        }

        // Records implementing interfaces
        System.out.println("\n-- Records implementing interfaces: Product --");
        List<Product> products = List.of(
                new Product(1, "Laptop",     999.99, "electronics"),
                new Product(2, "Headphones", 149.99, "electronics"),
                new Product(3, "Coffee Mug",  12.99, "kitchenware")
        );
        products.forEach(Product::print);
        System.out.println("  JSON: " + products.get(0).toJson());
        System.out.printf("  With tax: $%.2f%n", products.get(0).priceWithTax());

        // Generic records
        System.out.println("\n-- Generic records --");
        Pair<String, Integer> pair = new Pair<>("Java", 17);
        System.out.println("  pair = " + pair);
        System.out.println("  swapped = " + pair.swap());

        ApiResponse<Employee> response = ApiResponse.ok(emp);
        System.out.println("  ApiResponse: " + response.status() + " [" + response.code() + "]");
        System.out.println("  Success? " + response.isSuccess());

        // Nested records
        System.out.println("\n-- Nested records: Customer --");
        Customer customer = new Customer(
                "C001", "Bob Jones", "bob@example.com",
                new Address("123 Main St", "Mumbai", "India", "400001")
        );
        System.out.println("  " + customer.name() + " → " + customer.address().city()
                + ", " + customer.address().country());

        // Records in collections/streams
        System.out.println("\n-- Records in orders/streams --");
        Order order = new Order("ORD-001", "C001", List.of(
                new OrderItem("Laptop",   1, 999.99),
                new OrderItem("Mouse",    2,  29.99),
                new OrderItem("USB Hub",  1,  49.99)
        ));
        order.items().forEach(item ->
            System.out.printf("  %-12s x%d  = $%.2f%n",
                    item.productName(), item.quantity(), item.total()));
        System.out.printf("  Items: %d, Grand Total: $%.2f%n",
                order.itemCount(), order.grandTotal());

        localRecords();
    }
}
