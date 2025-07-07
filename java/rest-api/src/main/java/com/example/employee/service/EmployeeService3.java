package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.EmployeeRepository;
//import com.example.employee.repository.EmployeeRepositoryImpl;
import com.example.employee.repository.TestRepository;
import com.example.employee.repository.entity.Employee;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
import java.util.List;
import java.util.Optional;

//import javax.annotation.PostConstruct;


@Service
public class EmployeeService3 implements EmployeeNewService  {

    @Autowired
    TestRepository testRepository;
    
    //@PostConstruct
    public void afterCreate(){
        System.out.println("\n\n\n Employee Service created");
    }
    
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService3(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    
    public Employee createEmployee(Employee employee) {
        Optional<Employee> emp = employeeRepository.findById(employee.getId());
        if ( emp.isPresent() ){
            throw new RuntimeException("Employee Exists");
        }
        return employeeRepository.save(employee);
    }
    
    @Transactional(rollbackFor=RuntimeException.class  , propagation=Propagation.REQUIRES_NEW   )
    public void deleteDepartment(Long id) {
        employeeRepository.deleteByDepartment( id );
    }
    
    
    public Optional<Employee> getEmployeeByIdAndName(Integer id , String name) {
        return testRepository.findByIdOrName(id,name);
    }

    
    
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    // Create or Update
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Read All
    @Transactional( isolation=Isolation.SERIALIZABLE)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Read by ID
    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    // Delete
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    // Optional: Update (only if you're doing partial updates or validations)
    public Employee updateEmployee(Integer id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
            .map(existing -> {
                existing.setName(updatedEmployee.getName());
                existing.setDepartment(updatedEmployee.getDepartment());
                existing.setSalary(updatedEmployee.getSalary());
                return employeeRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
