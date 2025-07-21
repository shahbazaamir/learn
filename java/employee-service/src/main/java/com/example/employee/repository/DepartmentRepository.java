package com.example.employee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.employee.repository.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    
}
