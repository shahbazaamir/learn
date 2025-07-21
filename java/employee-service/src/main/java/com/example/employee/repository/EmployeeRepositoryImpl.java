package com.example.demo.repository;

import com.example.employee.repository.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl  {

    @PersistenceContext
    private EntityManager entityManager;

     
    public Optional<Employee> findByIdOrName(Integer id, String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate predicate = cb.conjunction(); // equivalent to TRUE

        if (id != null) {
            predicate = cb.and(predicate, cb.equal(root.get("id"), id));
        }

        if (name != null) {
            predicate = cb.and(predicate, cb.equal(root.get("name"), name));
        }

        query.where(predicate);

        return entityManager.createQuery(query)
                            .getResultStream()
                            .findFirst();
    }
}
