package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the UNIT database table.
 * 
 */
@Entity
@Table(name="UNIT")
@NamedQuery(name="Unit.findAll", query="SELECT u FROM Unit u")
public class Unit implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UnitPK id;

	@Column(name="COUNTRY_CODE", length=2)
	private String countryCode;

	@Column(name="UNIT_DESCRIPTION", length=100)
	private String unitDescription;

	//bi-directional many-to-one association to Branch
	@OneToMany(mappedBy="unit")
	private List<Branch> branches;

	//bi-directional many-to-one association to Bank
	@ManyToOne
	@JoinColumn(name="BANK_CODE", nullable=false, insertable=false, updatable=false)
	private Bank bank;

	public Unit() {
	}

	public UnitPK getId() {
		return this.id;
	}

	public void setId(UnitPK id) {
		this.id = id;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getUnitDescription() {
		return this.unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

	public List<Branch> getBranches() {
		return this.branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public Branch addBranch(Branch branch) {
		getBranches().add(branch);
		branch.setUnit(this);

		return branch;
	}

	public Branch removeBranch(Branch branch) {
		getBranches().remove(branch);
		branch.setUnit(null);

		return branch;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

}