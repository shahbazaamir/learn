package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CUSTOMER database table.
 * 
 */
@Embeddable
public class CustomerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="UNIT_ID", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String unitId;

	@Column(name="BANK_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String bankCode;

	@Column(name="BRANCH_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=10)
	private String branchCode;

	@Column(unique=true, nullable=false, length=50)
	private String cif;

	public CustomerPK() {
	}
	public String getUnitId() {
		return this.unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getBankCode() {
		return this.bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchCode() {
		return this.branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getCif() {
		return this.cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CustomerPK)) {
			return false;
		}
		CustomerPK castOther = (CustomerPK)other;
		return 
			this.unitId.equals(castOther.unitId)
			&& this.bankCode.equals(castOther.bankCode)
			&& this.branchCode.equals(castOther.branchCode)
			&& this.cif.equals(castOther.cif);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.unitId.hashCode();
		hash = hash * prime + this.bankCode.hashCode();
		hash = hash * prime + this.branchCode.hashCode();
		hash = hash * prime + this.cif.hashCode();
		
		return hash;
	}
}