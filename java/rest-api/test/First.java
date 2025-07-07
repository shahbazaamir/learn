//
//  First.java
//  spring-test
//
//  Created by
//
package test;

import java.util.Map;
import java.util.HashMap;





public class First {
    
    public static void main(String args []) throws Exception  {
        //System.out.println("Hello "+ args[0] );
        int [] numbers = { 1,2,3};
        for ( int n : numbers ){
            System.out.println(n);
        }
	
       // System.out.println("Hello "+ args[1] );
        System.out.println("element 0 "+ numbers[0] );
        numbers [0] = 5;
        System.out.println("element 0 "+ numbers[0] );
        Map m = new HashMap();
    }
}
