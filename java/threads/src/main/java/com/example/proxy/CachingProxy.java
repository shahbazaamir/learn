package com.example.proxy;

import java.util.HashMap;
import java.util.Map;

public class CachingProxy implements DatabaseService {
    private final RealDatabaseService realService;
    private final Map<String, String> cache = new HashMap<>();
    
    public CachingProxy() {
        this.realService = new RealDatabaseService();
    }
    
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
    
    @Override
    public void saveData(String data) {
        realService.saveData(data);
        // Invalidate cache on write
        cache.clear();
        System.out.println("Cache cleared after save");
    }
}