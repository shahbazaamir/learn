package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ACCOUNT database table.
 * 
 */
@Embeddable
public class AccountPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="UNIT_ID", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String unitId;

	@Column(name="BANK_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String bankCode;

	@Column(name="BRANCH_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=10)
	private String branchCode;

	@Column(insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String cif;

	@Column(name="ACCOUNT_NO", unique=true, nullable=false, length=50)
	private String accountNo;

	public AccountPK() {
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
	public String getAccountNo() {
		return this.accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AccountPK)) {
			return false;
		}
		AccountPK castOther = (AccountPK)other;
		return 
			this.unitId.equals(castOther.unitId)
			&& this.bankCode.equals(castOther.bankCode)
			&& this.branchCode.equals(castOther.branchCode)
			&& this.cif.equals(castOther.cif)
			&& this.accountNo.equals(castOther.accountNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.unitId.hashCode();
		hash = hash * prime + this.bankCode.hashCode();
		hash = hash * prime + this.branchCode.hashCode();
		hash = hash * prime + this.cif.hashCode();
		hash = hash * prime + this.accountNo.hashCode();
		
		return hash;
	}
}