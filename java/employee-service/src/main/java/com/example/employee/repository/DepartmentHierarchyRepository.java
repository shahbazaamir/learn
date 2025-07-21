package com.example.employee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.employee.repository.entity.DepartmentHierarchy;

public interface DepartmentHierarchyRepository extends JpaRepository<DepartmentHierarchy, Integer> {
    void deleteByParentId(Long parentId);
    void deleteByChildId(Long childId);
}
