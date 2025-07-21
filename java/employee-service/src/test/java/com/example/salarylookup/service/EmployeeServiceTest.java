
package com.example.salarylookup.service;

import com.example.salarylookup.exception.EmployeeNotFoundException;
import com.example.salarylookup.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.salarylookup.model.EmployeeResponse;
import com.example.salarylookup.exception.EmployeeUpdateFailedException;

class EmployeeServiceTest {
    private final EmployeeService employeeService = new EmployeeService();

    @Test
    void testGetSalary_ValidEmployee() {
        EmployeeResponse requestBody = new EmployeeResponse("John", 50000.0);
	employeeService.createEmployee( requestBody );
        assertEquals(50000.0, employeeService.getSalary("John"));
    }

    @Test
    void testGetSalary_InvalidEmployee() {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getSalary("Unknown"));
    }

    @Test
    void testUpdate_ValidEmployee() {
        EmployeeResponse requestBody = new EmployeeResponse("John", 50000.0);
	employeeService.createEmployee( requestBody );
        assertEquals(50000.0, employeeService.getSalary("John"));

        EmployeeResponse requestBody2 = new EmployeeResponse("John", 50001.0);
	employeeService.updateEmployee( requestBody2 );
        assertEquals(50001.0, employeeService.getSalary("John"));
    }

    @Test
    void testUpdate_InvalidEmployee() {
        EmployeeResponse requestBody = new EmployeeResponse("John", 50000.0);
        assertThrows(EmployeeUpdateFailedException.class, () -> employeeService.updateEmployee( requestBody ) );

    }
}
