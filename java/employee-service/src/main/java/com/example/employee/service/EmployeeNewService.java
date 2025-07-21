package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.repository.entity.Employee;


import java.util.List;
import java.util.Optional;

//import javax.annotation.PostConstruct;


 
public interface EmployeeNewService  {

    
    // Create or Update
    public Employee saveEmployee(Employee employee) ;

    // Read All
    public List<Employee> getAllEmployees() ;

    // Read by ID
    public Optional<Employee> getEmployeeById(Integer id) ;

    // Delete
    public void deleteEmployee(Integer id);

    // Optional: Update (only if you're doing partial updates or validations)
    public Employee updateEmployee(Integer id, Employee updatedEmployee) ;
    
    public void deleteDepartment(Long id) ;
    public Optional<Employee> getEmployeeByIdAndName(Integer id , String name);
    
}
