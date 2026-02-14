package com.example.proxy;

/**
 * ProxyTest demonstrates all three proxy patterns:
 * <ul>
 *   <li>{@link CachingProxy} - in-memory caching layer</li>
 *   <li>{@link SecurityProxy} - role-based access control</li>
 *   <li>{@link DynamicProxyExample} - runtime logging proxy via reflection</li>
 * </ul>
 *
 * Run with:
 * <pre>mvn compile exec:java -Dexec.mainClass="com.example.proxy.ProxyTest"</pre>
 */
public class ProxyTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Caching Proxy ===");
        testCachingProxy();
        
        System.out.println("\n=== Testing Security Proxy ===");
        testSecurityProxy();
        
        System.out.println("\n=== Testing Dynamic Proxy ===");
        testDynamicProxy();
    }
    
    private static void testCachingProxy() {
        DatabaseService proxy = new CachingProxy();
        
        // First call - cache miss
        proxy.getData("SELECT * FROM users");
        
        // Second call - cache hit
        proxy.getData("SELECT * FROM users");
        
        // Save operation clears cache
        proxy.saveData("New user data");
        
        // Third call - cache miss again
        proxy.getData("SELECT * FROM users");
    }
    
    private static void testSecurityProxy() {
        DatabaseService realService = new RealDatabaseService();
        
        // Admin user - has full access
        DatabaseService adminProxy = new SecurityProxy(realService, "ADMIN");
        adminProxy.getData("SELECT * FROM sensitive_data");
        adminProxy.saveData("Admin data");
        
        // Regular user - read only
        DatabaseService userProxy = new SecurityProxy(realService, "USER");
        userProxy.getData("SELECT * FROM public_data");
        
        try {
            userProxy.saveData("User data"); // Should fail
        } catch (SecurityException e) {
            System.out.println("Expected security exception: " + e.getMessage());
        }
    }
    
    private static void testDynamicProxy() {
        DatabaseService realService = new RealDatabaseService();
        DatabaseService loggingProxy = DynamicProxyExample.createLoggingProxy(realService);
        
        loggingProxy.getData("SELECT * FROM logs");
        loggingProxy.saveData("Log entry");
    }
}