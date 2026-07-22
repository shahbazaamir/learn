package com.example.deadlock;

import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

public class DeadLockExample {

    public static void main(String[] args) throws Exception {
        System.out.println("start");
        Pool p = new Pool();
        /*p.add("Hello");
        p.add("World");
        p.add(".");
        p.add("How");
        p.add("are");
        p.add("you");
        p.add(".");
        p.add("EOF");*/
        String s = """
                FlashAttention is a hardware-aware attention algorithm designed to speed up Transformer models and drastically reduce memory usage. Developed by Tri Dao and colleagues, it avoids memory bottlenecks by using tiling and kernel fusion, allowing large language models (LLMs) to process massive context windows without exceeding GPU memory
                """;
        Thread r = new Reader(p);
        Thread t = new Scanner(p,s);
        r.start();
        t.start();
        t.join();




    }

}



class Scanner extends Thread{

    private Pool p;
    private String s;
    public Scanner(Pool p,String s){
        this.p = p;
        this.s= s;
    }

    public  void  scan() throws Exception{
        synchronized(p) {
            System.out.println("Scanning");
            //Thread.sleep(200);
            String [] lines = s.split("\\.");
            //p.add();
            for ( String line : lines) {
                String [] words = line.split("\\s+");
                for(String word : words){
                    p.add(word);
                }
                p.add(".");
                p.notifyAll();
                p.wait(1000000);
            }
            System.out.println("Scanning Done");
            p.notifyAll();
        }
    }
    public void run()   {
        try{
            scan();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class Reader extends Thread {

    private int counter = 1;
    private Pool p;
    public Reader(Pool p){
        this.p = p;
    }

    public  void read() throws Exception {

        synchronized(p) {
            System.out.println("Reading");
            String s = p.pull(++counter);
            if(s== null){
                p.notifyAll();
                p.wait(100000);
                s = p.pull(++counter);
            }
            StringBuilder sb = new StringBuilder((s));
            while (s!= null) {
                if (".".equals(s)) {
                    sb.append(s);
                    System.out.println(sb.toString());
                    sb = new StringBuilder();
                    p.notifyAll();
                    p.wait(1000000);
                } else {
                    sb.append(s).append(" ");
                }
                s = p.pull(++counter);
            }
            System.out.println("Read done");
            p.notifyAll();
        }

    }
    public void run()   {
        try{
            read();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class Pool {
    private Map<Integer, String> m;
    int counter = 0;

    public Pool() {
        m = new HashMap<Integer, String>();
    }

    public void add(String data) {
        m.put(++counter, data);
    }

    public String pull(int counter){
        return m.get(counter);
    }


}
