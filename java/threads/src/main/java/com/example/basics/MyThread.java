package com.example.basics;

public class MyThread extends Thread{

    int i ;

    public MyThread(int i){
        this.i=i;
    }

    @Override
    public void run(){
        for(int i =0 ; i <100 ; i++){
            System.out.println(++this.i);

        }
    }
}
