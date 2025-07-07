package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.model.Employee;


import java.util.List;
import java.util.Optional;
import java.util.Map;



public interface EmployeeService {
    public void createEmployee( Employee employee ) ;
    
    public List<Employee> getEmployee( String id ) ;

    public List<Employee> getAllEmployees() ;
     
    public Employee maxSalary();
    
    public Map<String, List<Employee>> grouped();
    
    public Map<String, Optional<Employee>> maxSalaryByDpt() ;
    
    public Employee minSalary() ;
    
    public double avgSalary() ;
    
    public Employee deleteDepartment(Long id) ;
    
    

}
