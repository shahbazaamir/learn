package com.example.java21;

/**
 * Java 21 Features: Text Blocks (JEP 378, final since Java 15) + String Enhancements
 *
 * Text Blocks:
 * - Multi-line string literals with no escaping needed.
 * - Indentation is handled automatically — leading whitespace aligned to content.
 * - Support \s (trailing space) and \ (line continuation) escape sequences.
 *
 * String methods added in Java 11–21:
 * - isBlank(), strip(), stripLeading(), stripTrailing()  (Java 11)
 * - lines(), repeat()                                    (Java 11)
 * - indent(), stripIndent(), translateEscapes()          (Java 12–15)
 * - formatted()                                          (Java 15)
 */
public class TextBlocks {

    // ── 1. Basic Text Block ───────────────────────────────────────────────────

    static void basicTextBlock() {
        System.out.println("\n-- Basic Text Block --");

        // Old way — escape hell
        String jsonOld = "{\n" +
                "    \"name\": \"Alice\",\n" +
                "    \"age\": 30,\n" +
                "    \"city\": \"Mumbai\"\n" +
                "}";

        // Text block — clean, readable, no escapes
        String json = """
                {
                    "name": "Alice",
                    "age": 30,
                    "city": "Mumbai"
                }
                """;

        System.out.println("JSON:\n" + json);
    }

    // ── 2. HTML Text Block ────────────────────────────────────────────────────

    static void htmlTextBlock() {
        System.out.println("-- HTML Text Block --");

        String html = """
                <html>
                    <body>
                        <h1>Hello, Java 21!</h1>
                        <p>Text blocks make HTML readable.</p>
                    </body>
                </html>
                """;

        System.out.println(html);
    }

    // ── 3. SQL Text Block ─────────────────────────────────────────────────────

    static void sqlTextBlock() {
        System.out.println("-- SQL Text Block --");

        String sql = """
                SELECT e.id, e.name, e.department, d.location
                FROM   employees e
                JOIN   departments d ON e.dept_id = d.id
                WHERE  e.age > 30
                ORDER  BY e.name ASC;
                """;

        System.out.println(sql);
    }

    // ── 4. Text Block with .formatted() ──────────────────────────────────────

    static void textBlockFormatted() {
        System.out.println("-- Text Block + formatted() --");

        String template = """
                Employee Report
                ---------------
                Name:       %s
                Department: %s
                Salary:     $%.2f
                """;

        String report = template.formatted("Bob Smith", "Engineering", 95_000.0);
        System.out.println(report);
    }

    // ── 5. Escape sequences in text blocks ────────────────────────────────────

    static void escapeSequences() {
        System.out.println("-- Text Block Escape Sequences --");

        // \s — explicit trailing space (prevents stripping)
        String withSpaces = """
                col1   \s
                col2   \s
                """;
        System.out.println("Trailing spaces preserved: [" + withSpaces.lines()
                .findFirst().orElse("") + "]");

        // \ at end of line — line continuation (no newline in output)
        String singleLine = """
                This is a very long string that \
                continues on the next line in source but \
                is one line in output.
                """;
        System.out.println("Single line result: " + singleLine);
    }

    // ── 6. String methods — Java 11–21 ────────────────────────────────────────

    static void stringMethods() {
        System.out.println("\n-- New String Methods --");

        // isBlank() — true for empty or whitespace-only
        System.out.println("\"  \".isBlank(): " + "  ".isBlank());           // true
        System.out.println("\"hi\".isBlank(): " + "hi".isBlank());           // false

        // strip() — Unicode-aware trim (better than trim())
        String padded = "  hello world  ";
        System.out.println("strip():         [" + padded.strip() + "]");
        System.out.println("stripLeading():  [" + padded.stripLeading() + "]");
        System.out.println("stripTrailing(): [" + padded.stripTrailing() + "]");

        // lines() — splits by line terminators, returns Stream<String>
        String multiline = "line1\nline2\nline3";
        System.out.println("lines(): " + multiline.lines().toList());

        // repeat() — repeat a string n times
        System.out.println("repeat: " + "ab".repeat(4));  // abababab

        // indent() — adds/removes indentation (Java 12)
        String indented = "hello".indent(4);
        System.out.print("indent(4): [" + indented + "]");

        // stripIndent() — removes common leading whitespace (Java 15)
        String raw = "    line1\n    line2\n    line3";
        System.out.println("stripIndent:\n" + raw.stripIndent());

        // formatted() — instance method equivalent of String.format() (Java 15)
        String msg = "Hello, %s! You are %d years old.".formatted("Alice", 30);
        System.out.println(msg);

        // charAt, chars, codePoints — unchanged but worth noting
        "Java".chars().forEach(c -> System.out.print((char)c + " "));
        System.out.println();
    }

    // ── 7. String Templates — reference (preview in Java 21, final in Java 25) ─

    static void stringTemplatesNote() {
        System.out.println("\n-- String Templates (Preview in Java 21) --");
        System.out.println("""
                String Templates (JEP 430) are preview in Java 21.
                They allow: STR."Hello, \\{name}! You are \\{age} years old."
                This is safer than String.format() — processed at compile time.
                Not shown here as it requires --enable-preview.
                Use .formatted() as the production alternative.
                """);
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Text Blocks & String Enhancements =====");
        basicTextBlock();
        htmlTextBlock();
        sqlTextBlock();
        textBlockFormatted();
        escapeSequences();
        stringMethods();
        stringTemplatesNote();
    }
}
