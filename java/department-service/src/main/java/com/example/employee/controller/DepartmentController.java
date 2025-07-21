
package com.example.employee.controller;

import java.util.Map;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
 
import com.example.employee.repository.entity.Department;
import com.example.employee.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v2/department")
public class DepartmentController {
    
   
    @Autowired
    DepartmentService departmentService;

     

    @PostMapping
    public ResponseEntity<String> createDepartment( @Valid @RequestBody Department employee ) {
        departmentService.createDepartment(employee);
        return ResponseEntity.status(201).body("Department created successfully " );
    }

    @PutMapping
    public ResponseEntity<String> updateDepartment( @Valid @RequestBody Department employee ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(  @PathVariable ("id") String id) {
        departmentService.deleteDepartment(Integer.parseInt(id));
        return ResponseEntity.status(200).body("Department deleted successfully " );
    }
    
    @GetMapping
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.status(200).body( departmentService.getAllDepartments());
    }

     
     
}
