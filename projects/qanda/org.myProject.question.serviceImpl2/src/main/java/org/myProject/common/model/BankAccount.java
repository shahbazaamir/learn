package org.myProject.common.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="bank_account")
public class BankAccount {

	@Id
	@Column(name="account_id")
	private String accountId;
	
	@Column(name="account_no")
	private String accountNo;
	
	
	@Column(name="person_id")
	private String personId;


	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public String getPersonId() {
		return personId;
	}


	public void setPersonId(String personId) {
		this.personId = personId;
	}
}
