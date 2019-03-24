package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CREDIT_CARD database table.
 * 
 */
@Entity
@Table(name="CREDIT_CARD")
@NamedQuery(name="CreditCard.findAll", query="SELECT c FROM CreditCard c")
public class CreditCard implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CreditCardPK id;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="CIF", referencedColumnName="CIF", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Customer customer;

	public CreditCard() {
	}

	public CreditCardPK getId() {
		return this.id;
	}

	public void setId(CreditCardPK id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}