package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.DepartmentRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
import com.example.employee.repository.entity.Department;
import java.util.List;
import java.util.Optional;
import java.util.Map;


@Service
public class DepartmentServiceImpl implements DepartmentService  {
    
    @Autowired
    DepartmentRepository departmentRepository;
    
     
    
    @Autowired
    EmployeeNewService employeeService;
    
    @Autowired
    DepartmentHierarchyService departmentHierarchyService;
    
    //@Transactional( propagation=Propagation.REQUIRED   )
    public void deleteDepartment(int id){
        Optional<Department> dep =departmentRepository.findById(id);
        Department d = dep.get();
        employeeService.deleteDepartment(d.getId());
        departmentRepository.delete(d);
        departmentHierarchyService.deleteDepartmentHierarchy(d.getId());
    }
    
    @Transactional( isolation=Isolation.SERIALIZABLE)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    
    public void createDepartment(Department d){
        
        departmentRepository.save(d);
        
    }
}
