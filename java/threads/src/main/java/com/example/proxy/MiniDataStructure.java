package com.example.proxy;

public class MiniDataStructure implements DataStructure{

    private int [] x = new int [10];
    @Override
    public int get(int i) {
        return x[i];
    }

    @Override
    public void set(int i, int v) {
        x[i] = v;
    }
}
