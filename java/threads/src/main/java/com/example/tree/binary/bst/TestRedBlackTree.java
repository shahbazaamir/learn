package com.example.tree.binary.bst;

import java.util.Map;
import java.util.TreeMap;


import com.example.weather.WeatherAPI;


public class TestRedBlackTree {
    //private static final Logger logger = LogManager.getLogger(TestRedBlackTree.class);


    public static void main(String[] args) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        treeMap.put("4", "four");
        treeMap.put("5", "five");
        treeMap.put("6", "six");
        treeMap.put("1","one");
        treeMap.put("2", "two");
        treeMap.put("3", "three");

      //  logger.info("treeMap: {}", treeMap.get("2"));
        for(Map.Entry<String,String> entry: treeMap.entrySet()){
      //      logger.info("key: {}, value: {}", entry.getKey(), entry.getValue());
        }
    }
}
