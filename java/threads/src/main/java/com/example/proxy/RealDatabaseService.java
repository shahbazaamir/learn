package com.example.proxy;

<<<<<<< HEAD
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
=======
public class RealDatabaseService implements DatabaseService {
    
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
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
    
<<<<<<< HEAD
    /**
     * Simulates a slow database write operation (500ms delay).
     * @param data the data to persist
     */
=======
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
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