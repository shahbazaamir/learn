package com.example.basics;

public class Main {
    public static void main(String[] args) throws  Exception{
        int i =0;
        Thread t = new MyThread(i);
        Thread t1 = new MyThread(i);
        Thread t2 = new MyThread(i);
        t.start();
        t1.start();
        t2.start();
        t.join();
        t1.join();
        t2.join();
    }
}
