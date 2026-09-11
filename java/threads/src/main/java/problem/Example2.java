package problem;

import java.util.HashMap;
import java.util.Map;

public class Example2 {
    public static void main(String[] args) {
        Map<String,Integer> dept = new HashMap<>();
        dept.put("ECE",30);
        dept.put("IT",40);
        dept.put("CS",20);
        dept.put("MECH",10);
        Map<Integer,String> r = new HashMap<>();

        dept.entrySet().stream().forEach((e)->{
            r.put(e.getValue(),e.getKey());
        });
        System.out.println(r);
    }
}
