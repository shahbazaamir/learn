package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the BRANCH database table.
 * 
 */
@Entity
@Table(name="BRANCH")
@NamedQuery(name="Branch.findAll", query="SELECT b FROM Branch b")
public class Branch implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BranchPK id;

	@Column(name="BRANCH_ADDRESS", length=100)
	private String branchAddress;

	@Column(name="BRANCH_NAME", length=100)
	private String branchName;

	//bi-directional many-to-one association to Bank
	@ManyToOne
	@JoinColumn(name="BANK_CODE", nullable=false, insertable=false, updatable=false)
	private Bank bank;

	//bi-directional many-to-one association to Customer
	@OneToMany(mappedBy="branch1")
	private List<Customer> customers1;

	//bi-directional many-to-one association to Unit
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Unit unit;

	//bi-directional many-to-one association to Customer
	@OneToMany(mappedBy="branch2")
	private List<Customer> customers2;

	public Branch() {
	}

	public BranchPK getId() {
		return this.id;
	}

	public void setId(BranchPK id) {
		this.id = id;
	}

	public String getBranchAddress() {
		return this.branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public List<Customer> getCustomers1() {
		return this.customers1;
	}

	public void setCustomers1(List<Customer> customers1) {
		this.customers1 = customers1;
	}

	public Customer addCustomers1(Customer customers1) {
		getCustomers1().add(customers1);
		customers1.setBranch1(this);

		return customers1;
	}

	public Customer removeCustomers1(Customer customers1) {
		getCustomers1().remove(customers1);
		customers1.setBranch1(null);

		return customers1;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public List<Customer> getCustomers2() {
		return this.customers2;
	}

	public void setCustomers2(List<Customer> customers2) {
		this.customers2 = customers2;
	}

	public Customer addCustomers2(Customer customers2) {
		getCustomers2().add(customers2);
		customers2.setBranch2(this);

		return customers2;
	}

	public Customer removeCustomers2(Customer customers2) {
		getCustomers2().remove(customers2);
		customers2.setBranch2(null);

		return customers2;
	}

}