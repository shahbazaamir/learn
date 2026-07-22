package com.example.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleExample {
    public static void main(String[] args) {
        ExecutorService e= Executors.newFixedThreadPool(4);
        CompletableFuture<Void> c1 = CompletableFuture.runAsync(
                () -> {
                    System.out.println("Hey");
                } , e
        );
    }
}
