package com.example.proxy;

import java.util.HashMap;
import java.util.Map;


public class MapProxyTest {
    public static void mapTest(String[] args) {
        Map<String,String> mapProxy = ProxyWrapper.proxyMap( new HashMap<>() );
        mapProxy.put("1", "2");
        System.out.println(mapProxy.get("1"));
    }

    public static void dsmain(String[] args) {
        DataStructure d = ProxyWrapper.proxyDataStructure( new MiniDataStructure()  );
        d.set(0,1);
        System.out.println(d.get(0));
        System.out.println(d.get(1));
    }

    public static void main(String[] args) {
        Facade f = ProxyWrapper.proxyFacade(new CustomerFacade());
        f.init();
        f.destroy();
    }
}
