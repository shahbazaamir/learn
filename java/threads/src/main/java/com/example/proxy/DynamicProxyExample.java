package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyExample {
    
    public static DatabaseService createLoggingProxy(DatabaseService target) {
        return (DatabaseService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{DatabaseService.class},
            new LoggingInvocationHandler(target)
        );
    }
    
    static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;
        
        public LoggingInvocationHandler(Object target) {
            this.target = target;
        }
        
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