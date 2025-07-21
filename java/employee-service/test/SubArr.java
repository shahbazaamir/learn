//
//  SubArr.java
//  spring-test
//
//  Created by Zainab Firdaus on 26/04/25.
//
package test;

import java.util.List;
import java.util.Map;

public class SubArr{
    public static void main(String [] args) throws Exception {
        String input = "lockdown";
        int length = 4;
        String [] words = {"lock","down","lo","ck"};
        int [] costs = { 50,50,5,5};
        System.out.println(" minimum cost:"+findMinCost( input,length,words,costs ));
    }
    
    public static int findMinCost(String input,int length,String [] words , int [] costs){
        int cost = 0;
        for( int i=0; i< length; i++){
            if(input.contains( words [i])){
                //System.out.println( words[i]);
                cost+=costs[i];
            }
        }
        return cost;
    }
}

/*
import java.util.List;

public class MinCostToFormWord {
    
    public static int minCostToFormWord(String target, List<WordCost> subwordsWithCost) {
        int n = target.length();
        int[] dp = new int[n + 1];
        
        // Initialize dp array with a large value
        for (int i = 0; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[0] = 0; // Base case: empty string cost is 0
        
        for (int i = 1; i <= n; i++) {
            for (WordCost wc : subwordsWithCost) {
                String word = wc.word;
                int cost = wc.cost;
                if (i >= word.length() && target.substring(i - word.length(), i).equals(word)) {
                    if (dp[i - word.length()] != Integer.MAX_VALUE) {
                        dp[i] = Math.min(dp[i], dp[i - word.length()] + cost);
                    }
                }
            }
        }
        
        return dp[n] == Integer.MAX_VALUE ? -1 : dp[n];
    }
    
    // Helper class to store subword and its cost
    public static class WordCost {
        String word;
        int cost;
        
        WordCost(String word, int cost) {
            this.word = word;
            this.cost = cost;
        }
    }

    // Example usage
    public static void main(String[] args) {
        List<WordCost> subwords = List.of(
            new WordCost("a", 1),
            new WordCost("p", 2),
            new WordCost("pl", 3),
            new WordCost("e", 1)
        );
        
        String target = "apple";
        int result = minCostToFormWord(target, subwords);
        System.out.println("Minimum cost to form '" + target + "': " + result);
    }
}


*/
