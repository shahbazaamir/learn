package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapProxy implements InvocationHandler {

    Map<String,String> target ;
    public MapProxy(Map<String,String> m) {
        this.target=m;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Proxy method called");
        return method.invoke(  args);
    }

}
