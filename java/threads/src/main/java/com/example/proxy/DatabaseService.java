package com.example.proxy;

<<<<<<< HEAD
/**
 * DatabaseService defines the contract for database operations.
 * All proxy implementations must implement this interface.
 */
public interface DatabaseService {
    /**
     * Retrieves data from the database.
     * @param query the SQL query string
     * @return query result as a String
     */
    String getData(String query);

    /**
     * Saves data to the database.
     * @param data the data to persist
     */
=======
public interface DatabaseService {
    String getData(String query);
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
    void saveData(String data);
}