
package com.example.employee.service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.employee.model.Employee;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
 

class EmployeeService2Test {
    private final EmployeeService2 employeeService = new EmployeeService2();

    @Test
    void create() {
        Employee requestBody = new Employee("John","1","1", 50000.0);
        employeeService.createEmployee( requestBody );
        assertEquals("John", employeeService.getEmployee("1").get(0).getEmployeeName());
         
    }

    
    @Test
    void maxminSalary() {
        Employee one = new Employee("Rahul","1","1", 50000.0);
        Employee two = new Employee("Saurav","2","1", 60000.0);
        Employee three = new Employee("Sachin","3","1", 70000.0);
        employeeService.createEmployee( one );
        employeeService.createEmployee( two );
        employeeService.createEmployee( three );
        
        Employee one1 = new Employee("Messi","11","2", 10000.0);
        Employee two1 = new Employee("Ronaldo","12","2", 20000.0);
        Employee three1 = new Employee("Mabappe","13","2", 30000.0);
        employeeService.createEmployee( one1 );
        employeeService.createEmployee( two1 );
        employeeService.createEmployee( three1 );
        
        assertEquals(70000.0, employeeService.maxSalary().getSalary());
        assertEquals(10000.0, employeeService.minSalary().getSalary());
        assertEquals(3, employeeService.grouped().get("1").size() );
       
        assertEquals(70000.0, employeeService.maxSalaryByDpt().get("1").get().getSalary() );
        assertEquals(30000.0, employeeService.maxSalaryByDpt().get("2").get().getSalary() );
    }
     
    @Test
    void avg() {
        Employee one = new Employee("Rahul","1","1", 50000.0);
        Employee two = new Employee("Saurav","2","1", 60000.0);
        Employee three = new Employee("Sachin","3","1", 70000.0);
        employeeService.createEmployee( one );
        employeeService.createEmployee( two );
        employeeService.createEmployee( three );
        assertEquals(60000.0, employeeService.avgSalary() );
        
    }
    
    @Test
    void streamTest() {
        List<Double> d = List.of(1.0,2.0,3.0,4.0,5.0);
        d.stream().mapToDouble(e->e).average();
    }
    
    @Test
    void hashMapTest() {
        Map<String,String> emp1 = new HashMap<String,String>();
        emp1.put("name","Sachin");
        emp1.put("id","1");
        emp1.put("city","Mumbai");
    }
}
