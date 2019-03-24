package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CUSTOMER database table.
 * 
 */
@Entity
@Table(name="CUSTOMER")
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CustomerPK id;

	@Column(name="CUSTOMER_NAME", length=50)
	private String customerName;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="customer1")
	private List<Account> accounts1;

	//bi-directional many-to-one association to Branch
	@ManyToOne
	@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false)
	private Branch branch1;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="customer2")
	private List<Account> accounts2;

	//bi-directional many-to-one association to CreditCard
	@OneToMany(mappedBy="customer")
	private List<CreditCard> creditCards;

	//bi-directional many-to-one association to Branch
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Branch branch2;

	//bi-directional many-to-one association to DebitCard
	@OneToMany(mappedBy="customer")
	private List<DebitCard> debitCards;

	//bi-directional many-to-one association to InternetBanking
	@OneToMany(mappedBy="customer")
	private List<InternetBanking> internetBankings;

	public Customer() {
	}

	public CustomerPK getId() {
		return this.id;
	}

	public void setId(CustomerPK id) {
		this.id = id;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<Account> getAccounts1() {
		return this.accounts1;
	}

	public void setAccounts1(List<Account> accounts1) {
		this.accounts1 = accounts1;
	}

	public Account addAccounts1(Account accounts1) {
		getAccounts1().add(accounts1);
		accounts1.setCustomer1(this);

		return accounts1;
	}

	public Account removeAccounts1(Account accounts1) {
		getAccounts1().remove(accounts1);
		accounts1.setCustomer1(null);

		return accounts1;
	}

	public Branch getBranch1() {
		return this.branch1;
	}

	public void setBranch1(Branch branch1) {
		this.branch1 = branch1;
	}

	public List<Account> getAccounts2() {
		return this.accounts2;
	}

	public void setAccounts2(List<Account> accounts2) {
		this.accounts2 = accounts2;
	}

	public Account addAccounts2(Account accounts2) {
		getAccounts2().add(accounts2);
		accounts2.setCustomer2(this);

		return accounts2;
	}

	public Account removeAccounts2(Account accounts2) {
		getAccounts2().remove(accounts2);
		accounts2.setCustomer2(null);

		return accounts2;
	}

	public List<CreditCard> getCreditCards() {
		return this.creditCards;
	}

	public void setCreditCards(List<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}

	public CreditCard addCreditCard(CreditCard creditCard) {
		getCreditCards().add(creditCard);
		creditCard.setCustomer(this);

		return creditCard;
	}

	public CreditCard removeCreditCard(CreditCard creditCard) {
		getCreditCards().remove(creditCard);
		creditCard.setCustomer(null);

		return creditCard;
	}

	public Branch getBranch2() {
		return this.branch2;
	}

	public void setBranch2(Branch branch2) {
		this.branch2 = branch2;
	}

	public List<DebitCard> getDebitCards() {
		return this.debitCards;
	}

	public void setDebitCards(List<DebitCard> debitCards) {
		this.debitCards = debitCards;
	}

	public DebitCard addDebitCard(DebitCard debitCard) {
		getDebitCards().add(debitCard);
		debitCard.setCustomer(this);

		return debitCard;
	}

	public DebitCard removeDebitCard(DebitCard debitCard) {
		getDebitCards().remove(debitCard);
		debitCard.setCustomer(null);

		return debitCard;
	}

	public List<InternetBanking> getInternetBankings() {
		return this.internetBankings;
	}

	public void setInternetBankings(List<InternetBanking> internetBankings) {
		this.internetBankings = internetBankings;
	}

	public InternetBanking addInternetBanking(InternetBanking internetBanking) {
		getInternetBankings().add(internetBanking);
		internetBanking.setCustomer(this);

		return internetBanking;
	}

	public InternetBanking removeInternetBanking(InternetBanking internetBanking) {
		getInternetBankings().remove(internetBanking);
		internetBanking.setCustomer(null);

		return internetBanking;
	}

}