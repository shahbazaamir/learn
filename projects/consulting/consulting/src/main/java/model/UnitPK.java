package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the UNIT database table.
 * 
 */
@Embeddable
public class UnitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="BANK_CODE", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String bankCode;

	@Column(name="UNIT_ID", unique=true, nullable=false, length=100)
	private String unitId;

	public UnitPK() {
	}
	public String getBankCode() {
		return this.bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getUnitId() {
		return this.unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UnitPK)) {
			return false;
		}
		UnitPK castOther = (UnitPK)other;
		return 
			this.bankCode.equals(castOther.bankCode)
			&& this.unitId.equals(castOther.unitId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.bankCode.hashCode();
		hash = hash * prime + this.unitId.hashCode();
		
		return hash;
	}
}