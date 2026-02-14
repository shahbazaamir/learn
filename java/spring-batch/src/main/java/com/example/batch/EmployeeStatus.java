package com.example.batch;

import java.math.BigDecimal;

public class EmployeeStatus {
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private BigDecimal salary;
    private String apiStatus;
    private String dbStatus;
    private String errorMessage;
    
    public EmployeeStatus() {}
    
    public EmployeeStatus(Employee employee) {
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.department = employee.getDepartment();
        this.salary = employee.getSalary();
    }
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    
    public String getApiStatus() { return apiStatus; }
    public void setApiStatus(String apiStatus) { this.apiStatus = apiStatus; }
    
    public String getDbStatus() { return dbStatus; }
    public void setDbStatus(String dbStatus) { this.dbStatus = dbStatus; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}