package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FacadeInvocationHandler implements InvocationHandler {

    Facade target ;
    public FacadeInvocationHandler(Facade f) {
        this.target=f;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Facade Proxy method called");
            return method.invoke( target, args);
    }
}
