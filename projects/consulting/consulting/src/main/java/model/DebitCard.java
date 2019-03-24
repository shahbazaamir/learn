package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the DEBIT_CARD database table.
 * 
 */
@Entity
@Table(name="DEBIT_CARD")
@NamedQuery(name="DebitCard.findAll", query="SELECT d FROM DebitCard d")
public class DebitCard implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DebitCardPK id;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="BANK_CODE", referencedColumnName="BANK_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="BRANCH_CODE", referencedColumnName="BRANCH_CODE", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="CIF", referencedColumnName="CIF", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="UNIT_ID", referencedColumnName="UNIT_ID", nullable=false, insertable=false, updatable=false)
		})
	private Customer customer;

	public DebitCard() {
	}

	public DebitCardPK getId() {
		return this.id;
	}

	public void setId(DebitCardPK id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}