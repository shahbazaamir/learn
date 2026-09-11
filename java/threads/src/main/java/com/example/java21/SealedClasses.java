package com.example.java21;

/**
 * Java 21 Feature: Sealed Classes and Interfaces (JEP 409 — final since Java 17)
 *
 * Sealed classes restrict which classes can extend/implement them.
 * Combined with pattern matching switch, they enable exhaustive type checking
 * — the compiler knows all possible subtypes, so no default case is needed.
 */
public class SealedClasses {

    // ── 1. Sealed Interface ───────────────────────────────────────────────────

    // Only the listed types can implement this interface
    sealed interface Result<T> permits Result.Success, Result.Failure, Result.Loading {

        record Success<T>(T value) implements Result<T> {}
        record Failure<T>(String error, int code) implements Result<T> {}
        record Loading<T>() implements Result<T> {}
    }

    // ── 2. Sealed Class Hierarchy ─────────────────────────────────────────────

    // A payment model — exactly three payment types, no more allowed
    sealed abstract class Payment permits CreditCard, BankTransfer, Crypto {}

    final class CreditCard extends Payment {
        private final String cardNumber;
        private final double limit;
        CreditCard(String cardNumber, double limit) {
            this.cardNumber = cardNumber;
            this.limit = limit;
        }
        String masked() { return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4); }
        double limit() { return limit; }
    }

    final class BankTransfer extends Payment {
        private final String accountNumber;
        private final String ifsc;
        BankTransfer(String accountNumber, String ifsc) {
            this.accountNumber = accountNumber;
            this.ifsc = ifsc;
        }
        String accountNumber() { return accountNumber; }
        String ifsc() { return ifsc; }
    }

    // non-sealed — allows further extension outside this file
    non-sealed class Crypto extends Payment {
        private final String walletAddress;
        Crypto(String walletAddress) { this.walletAddress = walletAddress; }
        String walletAddress() { return walletAddress; }
    }

    // ── 3. Exhaustive switch — no default needed ───────────────────────────────

    static String processPayment(Payment payment) {
        // Compiler verifies all subtypes are covered — won't compile if one is missing
        return switch (payment) {
            case CreditCard cc ->
                "Credit card: %s (limit: %.0f)".formatted(cc.masked(), cc.limit());
            case BankTransfer bt ->
                "Bank transfer to %s [%s]".formatted(bt.accountNumber(), bt.ifsc());
            case Crypto c ->
                "Crypto wallet: " + c.walletAddress();
        };
    }

    // ── 4. Result type pattern — like Rust/Kotlin Result ──────────────────────

    static <T> String handleResult(Result<T> result) {
        return switch (result) {
            case Result.Success<T> s   -> "✅ Success: " + s.value();
            case Result.Failure<T> f   -> "❌ Error %d: %s".formatted(f.code(), f.error());
            case Result.Loading<T> ignored -> "⏳ Loading...";
        };
    }

    // ── 5. Sealed interface with guarded patterns ─────────────────────────────

    sealed interface Notification permits EmailNotification, SmsNotification, PushNotification {}
    record EmailNotification(String to, String subject, String body) implements Notification {}
    record SmsNotification(String phone, String message) implements Notification {}
    record PushNotification(String deviceId, String title, boolean urgent) implements Notification {}

    static String describeNotification(Notification n) {
        return switch (n) {
            case EmailNotification(String to, String subject, String ignoredBody) ->
                "Email to %s: %s".formatted(to, subject);
            case SmsNotification(String phone, String msg) ->
                "SMS to %s: %s".formatted(phone, msg);
            case PushNotification(String ignoredId, String title, boolean urgent) when urgent ->
                "🔔 URGENT push: " + title;
            case PushNotification(String ignoredId2, String title, boolean ignoredUrgent) ->
                "Push: " + title;
        };
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Sealed Classes =====");

        SealedClasses sc = new SealedClasses();

        // Payment processing
        Payment cc = sc.new CreditCard("1234567890123456", 50000);
        Payment bt = sc.new BankTransfer("0012345678", "HDFC0001234");
        Payment cr = sc.new Crypto("0xABCDEF1234567890");

        System.out.println(processPayment(cc));
        System.out.println(processPayment(bt));
        System.out.println(processPayment(cr));

        // Result type
        System.out.println(handleResult(new Result.Success<>("Employee loaded")));
        System.out.println(handleResult(new Result.Failure<>("Not found", 404)));
        System.out.println(handleResult(new Result.Loading<>()));

        // Notifications
        System.out.println(describeNotification(new EmailNotification("a@b.com", "Welcome", "Hi!")));
        System.out.println(describeNotification(new SmsNotification("+919999999999", "OTP: 1234")));
        System.out.println(describeNotification(new PushNotification("device-1", "Server Down", true)));
        System.out.println(describeNotification(new PushNotification("device-2", "Daily Report", false)));
    }
}
