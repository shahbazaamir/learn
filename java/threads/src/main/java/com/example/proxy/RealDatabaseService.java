package com.example.proxy;

public class RealDatabaseService implements DatabaseService {
    
    @Override
    public String getData(String query) {
        // Simulate expensive database operation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data for: " + query;
    }
    
    @Override
    public void saveData(String data) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Saved: " + data);
    }
}