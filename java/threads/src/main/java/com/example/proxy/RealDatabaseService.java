package com.example.proxy;

/**
 * RealDatabaseService is the actual implementation of DatabaseService.
 * Simulates expensive database operations with Thread.sleep delays.
 */
public class RealDatabaseService implements DatabaseService {

    /**
     * Simulates a slow database read operation (1000ms delay).
     * @param query the SQL query string
     * @return result string for the given query
     */
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
    
    /**
     * Simulates a slow database write operation (500ms delay).
     * @param data the data to persist
     */
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