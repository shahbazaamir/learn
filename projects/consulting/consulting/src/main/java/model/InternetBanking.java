package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the INTERNET_BANKING database table.
 * 
 */
@Entity
@Table(name="INTERNET_BANKING")
@NamedQuery(name="InternetBanking.findAll", query="SELECT i FROM InternetBanking i")
public class InternetBanking implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InternetBankingPK id;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="CIF", referencedColumnName="CIF", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Customer customer;

	public InternetBanking() {
	}

	public InternetBankingPK getId() {
		return this.id;
	}

	public void setId(InternetBankingPK id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}