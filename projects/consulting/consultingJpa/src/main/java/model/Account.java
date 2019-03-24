package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ACCOUNT database table.
 * 
 */
@Entity
@Table(name="ACCOUNT")
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AccountPK id;

	@Column(name="ACCOUNT_TYPE", length=50)
	private String accountType;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="CIF", referencedColumnName="CIF", nullable=false, insertable=false, updatable=false)
	private Customer customer1;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="CIF", referencedColumnName="CIF", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Customer customer2;

	public Account() {
	}

	public AccountPK getId() {
		return this.id;
	}

	public void setId(AccountPK id) {
		this.id = id;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Customer getCustomer1() {
		return this.customer1;
	}

	public void setCustomer1(Customer customer1) {
		this.customer1 = customer1;
	}

	public Customer getCustomer2() {
		return this.customer2;
	}

	public void setCustomer2(Customer customer2) {
		this.customer2 = customer2;
	}

}