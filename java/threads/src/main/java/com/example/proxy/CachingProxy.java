package com.example.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * CachingProxy is a structural proxy that adds an in-memory caching layer
 * on top of RealDatabaseService to avoid redundant expensive database calls.
 *
 * <p>Cache is invalidated on every write operation to maintain consistency.</p>
 */
public class CachingProxy implements DatabaseService {
    private final RealDatabaseService realService;
    private final Map<String, String> cache = new HashMap<>();

    /**
     * Initializes the proxy with a new RealDatabaseService instance.
     */
    public CachingProxy() {
        this.realService = new RealDatabaseService();
    }
    
    /**
     * Returns cached result if available, otherwise fetches from database and caches it.
     * @param query the SQL query string
     * @return cached or freshly fetched result
     */
    @Override
    public String getData(String query) {
        if (cache.containsKey(query)) {
            System.out.println("Cache hit for: " + query);
            return cache.get(query);
        }
        
        System.out.println("Cache miss, fetching from database: " + query);
        String result = realService.getData(query);
        cache.put(query, result);
        return result;
    }
    
    /**
     * Delegates write to real service and clears the cache to prevent stale reads.
     * @param data the data to persist
     */
    @Override
    public void saveData(String data) {
        realService.saveData(data);
        // Invalidate cache on write
        cache.clear();
        System.out.println("Cache cleared after save");
    }
}