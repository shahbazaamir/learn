package com.example.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockDetector extends Thread {

    @Override
    public void run(){
        ThreadMXBean obj= ManagementFactory.getThreadMXBean();
        int counter =0;
        while(counter < 10){
            try {
                Thread.sleep(1000);
                long [] ids = obj.findDeadlockedThreads();
                if(ids != null && ids.length ==0 ){
                    System.out.println("No Deadlock");
                }
                ThreadInfo [] tis = obj.getThreadInfo(ids);
                for ( ThreadInfo ti : tis){
                    System.out.println(ti.getThreadName());
                    System.out.println(ti.getLockName());
                    System.out.println(ti.getLockOwnerName());
                    System.out.println();
                }
            } catch (InterruptedException ie){

            }

            counter++;
        }
    }
}
