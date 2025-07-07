package com.example.employee.controller;

import java.util.Map;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
 
import com.example.employee.repository.entity.Employee;
import com.example.employee.service.EmployeeNewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v2/healthcheck")
public class HealthCheckController {
    
    
    @GetMapping
    public String test() {
        return "Server is live";
    }
    
    @GetMapping("/secure")
    //@PreAuthorize("hasAuthority('SCOPE_profile')") // Or just @PreAuthorize("isAuthenticated()")
    public String secureApi() {
        return "Authenticated access";
    }
}
