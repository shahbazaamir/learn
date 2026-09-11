package com.example.leetcode;

import java.util.HashMap;
import java.util.Map;

public class LC3 {
    static boolean audit=true;
    public static void main(String[] args) {

        validate("aaa",1);
        validate("abc",3);
        validate("abca",3);
        validate("abcabc",3);
        validate("abcabcbb",3);
        validate("abcdef",6);

        validate("ccbbcc",2);
    }
    public static void validate(String s,int exp) {
        int res = longestSubs(s);
        System.out.println(res);
        if(res==exp){
            System.out.println("success");
        } else {
            System.out.println("fail");
            System.out.println("error :"+(exp-res));
        }
    }
    public static int longestSubs(String s){
        int left=0;
        int right=0;
        int longestSubs =0;
        Map<Character,Integer> m = new HashMap<>();
        try{
            int n = s.length();
            while(right <n){
                char c = s.charAt(right);
                Integer idx=m.get(c);
                if( idx!= null){
                    left = idx+1;
                }
                m.put(c,right);
                int len = (right-left)+1;
                if ( len>longestSubs){
                    longestSubs = len;
                }
                right++;
            }
            if(audit) System.out.println("left:"+left+",right: "+right);
            return longestSubs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

}
