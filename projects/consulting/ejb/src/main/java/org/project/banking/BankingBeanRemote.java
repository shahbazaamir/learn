package org.project.banking;

import java.util.List;

import javax.ejb.Remote;

//import org.project.common.vo.CommonVO;

import model.Account;
import model.Bank;
import model.BankingDetail;
import model.Branch;
import model.Country;
import model.Customer;

@Remote
public interface BankingBeanRemote {

	void saveCountry(Country country);

	void saveBank(Country country, Bank bank);

	String searchCountry(String countryCode);

	String searchAllCountry(String countryCode);
	String searchAllBank(String bankCode);

	void modifyCountry(Country country);

	void modifyBank(Country country, Bank bank);

	void addBranch(Branch branch, Bank bank);

	void modifyBranch(Branch branch, Bank bank);

	void addCustomer(Customer customer, Branch branch);

	void modifyCustomer(Customer customer, Branch branch);

	void modifyAccount(Account account, Customer customer);


	String searchAll(String modelName, String Code);

	void saveBankingDetail(BankingDetail baningDetail);

	void modifyBankingDetail(BankingDetail baningDetail);

}
