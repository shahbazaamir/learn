
package com.example.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;
    
    private String id;

    private String departmentId;
    
    @NotNull(message = "Salary cannot be null")
    @Positive(message = "Salary must be positive")
    private Double salary;

}
