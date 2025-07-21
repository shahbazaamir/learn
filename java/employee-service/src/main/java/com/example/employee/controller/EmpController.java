
package com.example.employee.controller;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
 
import com.example.employee.repository.entity.Employee;
import com.example.employee.service.EmployeeNewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v2/employees")
public class EmpController {
    
   
    @Autowired
    
    EmployeeNewService employeeService;

     //query - GET
    //command - PUT POST
    @PostMapping
    public ResponseEntity<String> createEmployee( @Valid @RequestBody Employee employee ) {
        System.out.println(" employee is " + employee);
        employeeService.saveEmployee(employee);
        return ResponseEntity.status(201).body("Employee created successfully " );
    }

    @PutMapping
    public ResponseEntity<String> updateEmployee( @Valid @RequestBody Employee employee ) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.status(200).body( employeeService.getAllEmployees());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity< Employee > getEmployee( @PathVariable String id ,
                                                       @RequestParam( required=false) String name) {
        System.out.println(" id :"+id +" name : "+name);
        Optional<Employee> emp = employeeService.getEmployeeByIdAndName( Integer.parseInt(id) , name);
        if (emp.isPresent() ) {
            return ResponseEntity.status(200).body( emp.get());
        }
        return ResponseEntity.status(204).body( null);
    }


    

     
}
