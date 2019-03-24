package org.project.banking;

import java.util.List;

import javax.ejb.Remote;

import com.google.gson.JsonObject;

import model.Account;
import model.Customer;

@Remote
public interface AccountRemote {
	public List<Object> select();

	void saveAccount(Account account, Customer customer);

	Account findAccount(Account account);


	List<Account> retrieveAllAccounts();

	String findAccount(Account account, String dataType);

	List<Account> searchAccount(String searchKeyword);

	String searchAccount(String accountNo, String dataType);
}
