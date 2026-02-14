package com.example.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProxyWrapper {
    public static Map<String,String> proxyMap( Map<String,String> m ) {
        // Create an instance of the Proxy class

        return (Map<String,String> )
                Proxy.newProxyInstance( m.getClass() .getClassLoader(), new Class[]{Map.class}, new MapProxy(m)) ;


    }

    public static DataStructure proxyDataStructure( DataStructure m ) {
        // Create an instance of the Proxy class

        return (DataStructure)
                Proxy.newProxyInstance(m.getClass() .getClassLoader(), new Class[]{DataStructure.class}, new ProxyDataStructure(m)) ;


    }

    public static Facade proxyFacade(Facade f){
        return (Facade) Proxy.newProxyInstance(f.getClass().getClassLoader(),
                new Class[]{Facade.class},
                new FacadeInvocationHandler(f)) ;
    }
}
