package org.project.banking.customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Account;
import model.Customer;

/**
 * Session Bean implementation class CustomerBean
 */
@Stateless
public class CustomerBean implements CustomerBeanRemote {

    /**
     * Default constructor. 
     */
    public CustomerBean() {
        // TODO Auto-generated constructor stub
    }
    @PersistenceContext(unitName = "ConsultingJPA")
    private EntityManager entityManager;
     
 
    @Override
    public void saveCustomer(Customer customer) {
        entityManager.persist(customer);
    }
    
    @Override
    public Customer findCustomer(Customer customer) {
        //Customer p = entityManager.find(Customer.class, customer.getCif());
        return null ;
    }
}
