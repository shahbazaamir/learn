
package com.example.employee.service;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import com.example.employee.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;
 
@Service
public class EmployeeService2 {
    private List<Employee> employeeData;

    

    public void updateEmployee( Employee employee ) {
        
    }

    public void createEmployee( Employee employee ) {
        if(employeeData==null) employeeData=new ArrayList();
                employeeData.add(employee );
         
    }
    
    public List<Employee> getEmployee( String id ) {
        if(employeeData==null) return new ArrayList();
              return  employeeData.stream().filter(e-> id.equals(e.getId()) ).collect(Collectors.toList());
         
    }

    public List<Employee> getAllEmployees() {
         
        return  this.employeeData;
    }
     
    public Employee maxSalary(){
        Optional<Employee> maxSalaryEmployee = getAllEmployees().stream().max( Comparator.comparingDouble( Employee::getSalary) );
        return maxSalaryEmployee.get();
    }
    
    public Map<String, List<Employee>> grouped(){
        Map<String, List<Employee>> grouped = getAllEmployees().stream().collect(Collectors.groupingBy(Employee::getDepartmentId));
        return grouped;
    }
    
    public Map<String, Optional<Employee>> maxSalaryByDpt(){
        Map<String, Optional<Employee>> max = getAllEmployees().stream().collect(
            Collectors.groupingBy(Employee::getDepartmentId,
                                  Collectors.maxBy( Comparator.comparingDouble(Employee :: getSalary)  )
                                  ));
        return max;
    }
    
    public Employee minSalary(){
        Optional<Employee> maxSalaryEmployee = getAllEmployees().stream().min( Comparator.comparingDouble( Employee::getSalary) );
        return maxSalaryEmployee.get();
    }
    
    public double avgSalary(){
        double avg = getAllEmployees().stream().mapToDouble( Employee :: getSalary ).average().orElse(0.0);
        return avg;
    }
     
}

//list.stream.collect( Collectors.groupingBy( Employee::getDepartmentId ,
//Collectors.maxBy( Employee::getSalary )))
