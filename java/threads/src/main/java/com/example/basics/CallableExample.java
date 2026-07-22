package com.example.basics;

import java.util.concurrent.*;

public class CallableExample {
    public static void runnable(String[] args) {
        ThreadPoolExecutor tp = new ThreadPoolExecutor(5,5,100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), // Task queue
                new ThreadPoolExecutor.CallerRunsPolicy());
        tp.execute(new MyThread(1));
        tp.shutdown();
    }

    public static void callable(String[] args) throws Exception {
        ThreadPoolExecutor tp = new ThreadPoolExecutor(5,5,100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), // Task queue
                new ThreadPoolExecutor.CallerRunsPolicy());
        Future<String> f =tp.submit(   () -> {
                    System.out.println( Thread.currentThread());
                    return null;
                }
        );
        Future<String> f2 =tp.submit(   () -> {

                    return "Hello";
                }
        );
        System.out.println(f2.isDone());
        System.out.println(f2.isCancelled());
        f.get();
        System.out.println(f2.get());
        System.out.println(f2.isDone());

        tp.shutdown();
    }



    public static void executor(String[] args) throws Exception{
        ExecutorService e = Executors.newFixedThreadPool(
                4
        );
        Future<String> f1 = e.submit(
                () -> {
                    return "Hi";
                }
        );
        Future<String> f2 = e.submit(
                () -> {
                    return "Hello";
                }
        );
        e.submit(
                () -> {
                    System.out.println("Hey");
                }
        );
        System.out.println(f1.isDone());
        System.out.println(f2.isDone());
        System.out.println(f2.get());
        System.out.println(f1.get());
        System.out.println(f1.isDone());
        System.out.println(f2.isDone());
        e.shutdown();
    }

    public static void main(String[] args) throws Exception{
        executor(args);
    }
}
