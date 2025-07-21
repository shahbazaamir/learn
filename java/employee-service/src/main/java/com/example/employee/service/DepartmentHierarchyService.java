package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.DepartmentRepository;

import com.example.employee.repository.entity.DepartmentHierarchy;

import java.util.List;
import java.util.Optional;
import java.util.Map;



public interface DepartmentHierarchyService {
    public void deleteDepartmentHierarchy(Long id );
    }
