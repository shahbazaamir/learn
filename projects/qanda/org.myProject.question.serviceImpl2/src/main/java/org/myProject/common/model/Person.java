package org.myProject.common.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

@Table(name="person")
public class Person {
	@Id
	@Column(name="person_id")
	private String personId;
	
	@Column(name="person_name")
	private String personName;
	
	@CollectionOfElements (targetElement=java.lang.String.class) 
	 @JoinTable(
			 name="bank_account",
			 joinColumns=@JoinColumn(name="person_id")
			 ) 
	@Column(name="bank_account")
	private Set<String> bankAccounts ;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Set<String> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(Set<String> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

}
