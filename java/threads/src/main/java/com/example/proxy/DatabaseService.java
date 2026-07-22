package com.example.proxy;

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
     void saveData(String data);
}