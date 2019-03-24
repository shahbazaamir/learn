package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the BANKING_DETAILS database table.
 * 
 */
@Entity
@Table(name="BANKING_DETAILS")
@NamedQuery(name="BankingDetail.findAll", query="SELECT b FROM BankingDetail b")
public class BankingDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BANKING_ID", unique=true, nullable=false, length=20)
	private String bankingId;

	@Column(name="ACCOUNT_NO", length=50)
	private String accountNo;

	@Column(length=100)
	private String bank;

	@Column(length=100)
	private String branch;

	@Column(length=20)
	private String cif;

	@Column(length=20)
	private String country;

	@Column(name="CREDIT_CARD_DETAILS", length=2000)
	private String creditCardDetails;

	@Column(name="CUSTOMER_NAME", length=100)
	private String customerName;

	@Column(name="DEBIT_CARD_DETAILS", length=2000)
	private String debitCardDetails;

	@Column(name="INTERNET_BANKING_DETAILS", length=2000)
	private String internetBankingDetails;

	public BankingDetail() {
	}

	public String getBankingId() {
		return this.bankingId;
	}

	public void setBankingId(String bankingId) {
		this.bankingId = bankingId;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCreditCardDetails() {
		return this.creditCardDetails;
	}

	public void setCreditCardDetails(String creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDebitCardDetails() {
		return this.debitCardDetails;
	}

	public void setDebitCardDetails(String debitCardDetails) {
		this.debitCardDetails = debitCardDetails;
	}

	public String getInternetBankingDetails() {
		return this.internetBankingDetails;
	}

	public void setInternetBankingDetails(String internetBankingDetails) {
		this.internetBankingDetails = internetBankingDetails;
	}

}