package com.example.proxy;

import java.lang.reflect.InvocationHandler;

public class ProxyDataStructure implements InvocationHandler {

    private DataStructure target;
    public ProxyDataStructure(DataStructure d){
        this.target = d;
    }

    @Override
    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
        System.out.println("Proxy method called");
        return method.invoke(target,args);
    }
}
