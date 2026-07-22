package com.example.executor;

import com.example.lock.MyCallableThread;
import com.example.weather.WeatherResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        MyCallableThread t2 = new MyCallableThread();
        Future<List<WeatherResponse>> f = executor.submit(t2);
        try {
            List<WeatherResponse> r = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
