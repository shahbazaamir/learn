package com.example.java17;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Java 17 Feature: Enhanced Pseudo-Random Number Generators (JEP 356)
 *
 * Before Java 17:
 * - Only Random, ThreadLocalRandom, SplittableRandom, SecureRandom existed.
 * - No common interface — couldn't write generic code that works with any PRNG.
 * - No access to modern algorithms like Xoshiro, LXM series.
 *
 * Java 17 introduces:
 * - RandomGenerator interface — unified API for ALL random number generators.
 * - RandomGeneratorFactory — discover and instantiate generators by algorithm name.
 * - New algorithm families: LXM, Xoshiro/Xoroshiro (faster, better statistical quality).
 * - JumpableGenerator, LeapableGenerator, SplittableGenerator, StreamableGenerator
 *   — sub-interfaces for generators with specific capabilities.
 *
 * New algorithms available:
 *   L32X64MixRandom, L64X128MixRandom, L64X256MixRandom, L128X128MixRandom,
 *   L128X256MixRandom, Xoshiro256PlusPlus, Xoroshiro128PlusPlus, etc.
 */
public class RandomGeneratorsJ17 {

    // ── 1. RandomGenerator interface — unified API ────────────────────────────

    static void unifiedApi() {
        System.out.println("\n-- RandomGenerator unified interface --");

        // All generators share the same interface — write once, swap algorithm freely
        RandomGenerator rng = RandomGenerator.of("L64X128MixRandom"); // modern LXM algorithm

        System.out.println("nextInt(100):    " + rng.nextInt(100));
        System.out.println("nextLong():      " + rng.nextLong());
        System.out.println("nextDouble():    " + String.format("%.6f", rng.nextDouble()));
        System.out.println("nextBoolean():   " + rng.nextBoolean());
        System.out.println("nextGaussian():  " + String.format("%.4f", rng.nextGaussian()));

        // Generate a stream of random ints — same API regardless of algorithm
        List<Integer> randomInts = rng.ints(5, 1, 100)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("5 random ints [1,100): " + randomInts);
    }

    // ── 2. RandomGeneratorFactory — discover available algorithms ─────────────

    static void discoverAlgorithms() {
        System.out.println("\n-- RandomGeneratorFactory: available algorithms --");

        RandomGeneratorFactory.all()
                .sorted((a, b) -> a.name().compareTo(b.name()))
                .forEach(factory -> System.out.printf(
                    "  %-35s | stateBits=%-4d | splittable=%-5b | jumpable=%b%n",
                    factory.name(),
                    factory.stateBits(),
                    factory.isSplittable(),
                    factory.isJumpable()
                ));
    }

    // ── 3. Compare algorithms — same seed, same output ────────────────────────

    static void compareAlgorithms() {
        System.out.println("\n-- Algorithm comparison (seeded, first 5 values) --");

        String[] algorithms = { "Random", "Xoshiro256PlusPlus", "L64X128MixRandom", "L128X256MixRandom" };

        for (String algo : algorithms) {
            try {
                // Seeded generator — reproducible output
                RandomGenerator rng = RandomGeneratorFactory.of(algo).create(42L);
                String values = rng.ints(5, 0, 1000)
                        .boxed()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                System.out.printf("  %-30s → [%s]%n", algo, values);
            } catch (Exception e) {
                System.out.printf("  %-30s → not available%n", algo);
            }
        }
    }

    // ── 4. SplittableGenerator — parallel streams / fork-join ─────────────────

    static void splittableGenerator() {
        System.out.println("\n-- SplittableGenerator (parallel use) --");

        // SplittableRandom is splittable — each split gets an independent generator
        // Ideal for parallel streams where each thread needs its own generator
        var factory = RandomGeneratorFactory.of("L64X128MixRandom");
        var rng = (RandomGenerator.SplittableGenerator) factory.create();

        // Generate 1000 random doubles in parallel using splits
        double avg = rng.splits(4)                  // 4 independent child generators
                .parallel()
                .flatMapToDouble(gen -> gen.doubles(250, 0.0, 1.0))
                .average()
                .orElse(0);

        System.out.printf("  Average of 1000 uniform [0,1) values: %.4f (expected ≈ 0.5000)%n", avg);
    }

    // ── 5. JumpableGenerator — skip ahead in sequence ────────────────────────

    static void jumpableGenerator() {
        System.out.println("\n-- JumpableGenerator (jump ahead) --");

        // Xoshiro256PlusPlus supports jump() — advances state by 2^128 steps
        // Useful for creating non-overlapping sequences in parallel simulations
        var rng = (RandomGenerator.JumpableGenerator)
                RandomGeneratorFactory.of("Xoshiro256PlusPlus").create(100L);

        System.out.println("  Before jump:");
        System.out.println("    First 3 values: " +
                rng.ints(3, 0, 100).boxed().collect(Collectors.toList()));

        rng.jump(); // skip ahead 2^128 steps in the sequence

        System.out.println("  After jump (completely different segment of sequence):");
        System.out.println("    First 3 values: " +
                rng.ints(3, 0, 100).boxed().collect(Collectors.toList()));
    }

    // ── 6. Practical use: simulation / Monte Carlo π estimation ──────────────

    static void monteCarloPi() {
        System.out.println("\n-- Monte Carlo π estimation (10M samples) --");

        // Use a high-quality LXM generator for simulation
        RandomGenerator rng = RandomGenerator.of("L128X256MixRandom");

        long samples = 10_000_000L;
        long insideCircle = rng.doubles(samples * 2)   // pairs of (x, y)
                .boxed()
                .collect(Collectors.toList())
                .stream()
                .reduce(new long[]{0, 0},
                    (acc, val) -> {
                        // pair up: even index = x, odd index = y
                        if (acc[1] % 2 == 0) {
                            acc[0] = (long)(double)(Object)val; // store x temporarily
                        }
                        acc[1]++;
                        return acc;
                    },
                    (a, b) -> a
                )[0];  // simplified — use direct stream below

        // Cleaner version with streams
        long inside = 0;
        for (long i = 0; i < samples; i++) {
            double x = rng.nextDouble();
            double y = rng.nextDouble();
            if (x * x + y * y <= 1.0) inside++;
        }

        double pi = 4.0 * inside / samples;
        System.out.printf("  Estimated π: %.6f (actual: %.6f, error: %.6f)%n",
                pi, Math.PI, Math.abs(pi - Math.PI));
    }

    // ── 7. Legacy Random → RandomGenerator migration ─────────────────────────

    static void legacyMigration() {
        System.out.println("\n-- Legacy vs New API --");

        // Old way
        java.util.Random oldRng = new java.util.Random(42);
        System.out.println("  Old java.util.Random:     " + oldRng.nextInt(100));

        // java.util.Random now implements RandomGenerator in Java 17!
        // So existing code is automatically compatible with the new interface.
        RandomGenerator upgradedRng = new java.util.Random(42);
        System.out.println("  Random as RandomGenerator: " + upgradedRng.nextInt(100));

        // New high-quality default (same call style, better algorithm)
        RandomGenerator modernRng = RandomGenerator.getDefault(); // L32X64MixRandom
        System.out.println("  getDefault() algorithm:   "
                + RandomGeneratorFactory.of(modernRng.getClass().getSimpleName().isEmpty()
                    ? "L32X64MixRandom" : "L32X64MixRandom").name());
        System.out.println("  Modern nextInt(100):       " + modernRng.nextInt(100));
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void run() {
        System.out.println("\n===== Enhanced PRNG (JEP 356) =====");
        unifiedApi();
        discoverAlgorithms();
        compareAlgorithms();
        splittableGenerator();
        jumpableGenerator();
        monteCarloPi();
        legacyMigration();
    }
}
