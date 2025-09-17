
package com.example.employee.controller;

import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
 
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@RestController
@RequestMapping("/api/v2/employees")
public class EmpController {
    
   
    @Autowired
    EmployeeService2 employeeService;

     

    @PostMapping
    public ResponseEntity<String> createEmployee( @Valid @RequestBody Employee employee ) {
        employeeService.createEmployee(employee);
        return ResponseEntity.status(201).body("Employee created successfully " );
    }

    @PutMapping
    public ResponseEntity<String> updateEmployee( @Valid @RequestBody Employee employee ) {
        return null;
    }

    @GetMapping
    public ResponseEntity<Map<String,Double>> getEmployees() {
        return null;
    }

    @GetMapping("/{max-salary}")
    public ResponseEntity<Employee> getEmployee( ) {
        employeeService.maxSalary();
        return null;
    }

     
}
