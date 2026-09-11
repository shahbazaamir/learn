package com.example.records;

public class RecordDemo {

    record Employee(String id ,String name){};

    public static void main(String[] args) {
        Employee e1 = new Employee("1", "Ravi");
        Employee e2 = new Employee("1", "Ravi");
        System.out.println(e1.equals(e2));
    }
}
