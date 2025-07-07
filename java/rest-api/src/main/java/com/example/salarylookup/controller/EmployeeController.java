
package com.example.salarylookup.controller;

import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.example.salarylookup.exception.EmployeeNotFoundException;
import com.example.salarylookup.exception.InvalidInputException;
import com.example.salarylookup.exception.EmployeeDataPersists;
import com.example.salarylookup.exception.EmployeeUpdateFailedException;
import com.example.salarylookup.model.EmployeeResponse;
import com.example.salarylookup.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
   
    @Autowired
    EmployeeService employeeService;

     

    @PostMapping
    public ResponseEntity<String> createEmployee( @Valid @RequestBody EmployeeResponse employee ) {
	    employeeService.createEmployee(employee);
	    return ResponseEntity.status(201).body("Employee created successfully " );
    }

    @PutMapping
    public ResponseEntity<String> updateEmployee( @Valid @RequestBody EmployeeResponse employee ) {
	    employeeService.updateEmployee(employee);
	    return ResponseEntity.status(200).body("Employee updated successfully " );
    }

    @GetMapping
    public ResponseEntity<Map<String,Double>> getEmployees() {
        return ResponseEntity.ok( employeeService.getAllEmployees() );
    }

    @GetMapping("/{employeeName}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable  @NotBlank(message = "Employee name cannot be blank") String employeeName) {
        Double salary = employeeService.getSalary(employeeName);
        return ResponseEntity.ok(new EmployeeResponse(employeeName, salary));
    }

    @ExceptionHandler(EmployeeDataPersists.class)
    public ResponseEntity<String> handleEmployeeFound(EmployeeDataPersists e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
        return ResponseEntity.status(204).body(e.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInput(InvalidInputException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidInput(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler( EmployeeUpdateFailedException.class)
    public ResponseEntity<String> handleInvalidInput(EmployeeUpdateFailedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
