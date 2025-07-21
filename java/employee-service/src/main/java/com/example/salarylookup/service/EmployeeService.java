
package com.example.salarylookup.service;

import com.example.salarylookup.exception.EmployeeNotFoundException;
import com.example.salarylookup.exception.InvalidInputException;
import com.example.salarylookup.exception.EmployeeDataPersists;
import com.example.salarylookup.exception.EmployeeUpdateFailedException;
import org.springframework.stereotype.Service;
import com.example.salarylookup.model.EmployeeResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.HashMap;
import java.util.Map;

@Qualifier("employeeService")
@Service
public class EmployeeService {
    private final Map<String, Double> employeeData;

    public EmployeeService() {
        this.employeeData = new HashMap<>();
    }

    public void updateEmployee( EmployeeResponse employee ) {
        String employeeName = employee.getEmployeeName();
        Double salary = employee.getSalary();
        if( employeeData.get( employeeName ) != null ) {
                employeeData.put(employeeName, salary);
        } else {
                throw new EmployeeUpdateFailedException("Employee not found: " + employeeName);
        }
    }

    public void createEmployee( EmployeeResponse employee ) {
        String employeeName = employee.getEmployeeName();
        Double salary = employee.getSalary();
        if( employeeData.get( employeeName ) == null ) {
                employeeData.put(employeeName, salary);
        } else {
            throw new EmployeeDataPersists(" Unable to create employee , as empolyee found with same name");
        }
    }

    public Map<String,Double> getAllEmployees() {
        if( this.employeeData.isEmpty()) {
            throw new EmployeeNotFoundException("No Employees found ");
        }
        return  this.employeeData;
    }
    public Double getSalary(String employeeName) {
        if (!employeeData.containsKey(employeeName)) {
            throw new EmployeeNotFoundException("Employee not found: " + employeeName);
        }

        return employeeData.get(employeeName);
    }
    
     
}
