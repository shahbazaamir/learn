package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

<<<<<<< HEAD
/**
 * DynamicProxyExample demonstrates Java's built-in dynamic proxy mechanism
 * using {@link java.lang.reflect.Proxy} and {@link InvocationHandler}.
 *
 * <p>Creates a runtime proxy that wraps any DatabaseService with method-level
 * logging and latency tracking without modifying the original class.</p>
 */
public class DynamicProxyExample {

    /**
     * Creates a dynamic logging proxy for the given DatabaseService.
     * @param target the real service to wrap
     * @return a proxy instance that logs all method calls and their duration
     */
=======
public class DynamicProxyExample {
    
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
    public static DatabaseService createLoggingProxy(DatabaseService target) {
        return (DatabaseService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{DatabaseService.class},
            new LoggingInvocationHandler(target)
        );
    }
    
<<<<<<< HEAD
    /**
     * InvocationHandler that intercepts all method calls to log method name
     * and measure execution time.
     */
    static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;

        /**
         * @param target the real object to delegate method calls to
         */
=======
    static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;
        
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
        public LoggingInvocationHandler(Object target) {
            this.target = target;
        }
        
<<<<<<< HEAD
        /**
         * Intercepts method invocation, logs method name and execution duration.
         * @param proxy  the proxy instance
         * @param method the method being invoked
         * @param args   method arguments
         * @return result of the delegated method call
         * @throws Throwable if the underlying method throws
         */
=======
>>>>>>> 0d2ddc6ae2b3bd24abc1135e391cbef7cca59fae
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTime = System.currentTimeMillis();
            System.out.println("Calling method: " + method.getName());
            
            try {
                Object result = method.invoke(target, args);
                long duration = System.currentTimeMillis() - startTime;
                System.out.println("Method " + method.getName() + " completed in " + duration + "ms");
                return result;
            } catch (Exception e) {
                System.out.println("Method " + method.getName() + " failed: " + e.getMessage());
                throw e;
            }
        }
    }
}