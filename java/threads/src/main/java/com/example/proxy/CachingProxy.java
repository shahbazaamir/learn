package com.example.proxy;

import java.util.HashMap;
import java.util.Map;


public class CachingProxy  {
    private final RealDatabaseService realService;
    private final Map<String, String> cache = new HashMap<>();
    

    public CachingProxy() {
        this.realService = new RealDatabaseService();
    }
    


     public void saveData(String data) {
        realService.saveData(data);
        // Invalidate cache on write
        cache.clear();
        System.out.println("Cache cleared after save");
    }
}