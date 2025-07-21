package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.employee.repository.DepartmentHierarchyRepository;
import com.example.employee.model.Employee;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import java.util.List;
import java.util.Optional;
import java.util.Map;


@Service
public class DepartmentHierarchyServiceImpl implements DepartmentHierarchyService  {
    
    @Autowired
    DepartmentHierarchyRepository departmentHierachyRepository;
    
   
    
    @Transactional( rollbackFor=RuntimeException.class , propagation=Propagation.MANDATORY    )
    public void deleteDepartmentHierarchy(Long id ){
         
        departmentHierachyRepository.deleteByParentId(id);
        departmentHierachyRepository.deleteByChildId(id);
    }
}
