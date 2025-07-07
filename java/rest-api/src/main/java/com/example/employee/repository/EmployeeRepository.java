package com.example.employee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.employee.repository.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // You can add custom queries here if needed
    List<Employee> getByName(String name);
    
    
    List<Employee> getByNameAndId(String name,Integer id);
    
    
    @Query("SELECT e FROM Employee e WHERE e.name = :name AND e.id = :id")
    List<Employee> myQuery(String name,Integer id);
    
    void deleteByDepartment(Long department);
}


