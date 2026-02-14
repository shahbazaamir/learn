package com.example.proxy;

public interface DatabaseService {
    String getData(String query);
    void saveData(String data);
}