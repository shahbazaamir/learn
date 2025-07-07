package com.example.employee.repository.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "department_hierarchy")
public class DepartmentHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long parentId;

    @Column
    private  Long childId;

    
    public DepartmentHierarchy() {}

    public DepartmentHierarchy(Long id , Long parentId ,Long childId) {
        this.parentId = parentId;
        this.childId = childId;
        this.id = id;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }
}
