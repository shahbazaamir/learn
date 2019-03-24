package org.project.banking.customer;

import javax.ejb.Remote;

import model.Customer;

@Remote
public interface CustomerBeanRemote {

	void saveCustomer(Customer customer);

	Customer findCustomer(Customer customer);

}
