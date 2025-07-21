package com.example.employee.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
public class Employee {

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Long department;

    private Double salary;

    // Constructors
    public Employee() {}

    public Employee(Integer id, String name, Long department, Double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Getters and Setters
    // ...
}
