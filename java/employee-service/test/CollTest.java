//
//  First.java
//  spring-test
//
//  Created by
//
package test;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

import java.util.stream.*;



public class CollTest {
    
    "main"
    
    public static void main (String args []) throws Exception  {
        List<String> l = new ArrayList<>();
        //Set l = new HashSet();
        //Set l = new TreeSet();
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");
        l.add("1");
        l.add("2");
        l.add("5");
        l.add("6");
        l.add("1");
        l.add("2");
        
        
        List<Integer> l2 = new ArrayList<>();
        //Set l = new HashSet();
        //Set l = new TreeSet();
        l2.add(3);
        l2.add(4);
        l2.add(5);
        l2.add(6);
        l2.add(1);
        l2.add(2);
        //filter -
        //map - per
        
        
        
        
        System.out.println( "original array" + l );
        int max = 0;
        for ( String s : l ){ // for each
            int element = Integer.parseInt(s);
            if ( element > max) {
                max = element;
            }
        }
        
         l.stream().forEach(  s -> {
                int max2 = 0;
                int element = Integer.parseInt(s);
                if ( element > max2) {
                    max2 = element;
                }
                
        });
        
        List<Integer> result = l2.stream()
                                  .filter( a -> a> 4).collect( Collectors.toList());
        Stream<String> s =l.stream()
        .distinct();
        List<String> result5 =  s.collect( Collectors.toList());
        System.out.println("unique records :" + result5);
        //s.map(e->e);
        
        List<Integer> result3 = l.stream().map( ( e ) -> {
            
            return Integer.parseInt(e);
        }).collect( Collectors.toList()) ;
        
        //System.out.println( result3 );
        long l5 = l2.stream().count();
       // System.out.println( l5 );
        Consumer<Integer> c = CollTest::print;
        Consumer<Integer> c1 = a -> System.out.println(a);
        //c.accept(max);
        
        
        
        BiConsumer<Integer,Integer> c2 = CollTest::sum;
        //c2.accept(1,2);
        
        BiConsumer<Integer,Integer> c3 =  (a,b) ->System.out.println(a+b) ;
        
        //sum(1,2);
        
        
    }
    
    public static void print( int a) {
        System.out.println(a);
    }
    
    public static void sum( int a , int b) {
        System.out.println(a+b);
    }
    
    
    public static void main2(String args []) throws Exception  {
        //List l = new ArrayList();
        //Set l = new HashSet();
        Map<String,Object> h = new HashMap<String,Object>();
        //h.put( "1", )
        Set<String> l = new TreeSet<String>();
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");
        l.add("1");
        l.add("2");
         
        System.out.println( l );
    }
}
