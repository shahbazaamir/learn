package com.example.proxy;

public class CustomerFacade implements Facade{
    @Override
    public void destroy() {
        System.out.println("Customer Facade destroy");
    }
    @Override
    public void init() {
        System.out.println("Customer Facade init");
    }
}
