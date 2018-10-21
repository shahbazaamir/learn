package org.myProject.common.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the QUESTIONS database table.
 * 
 */
@Entity
@Table(name="EXPENSE_TXN")
@NamedQuery(name="ExpenseTxn.findAll", query="SELECT q FROM ExpenseTxn q")
public class ExpenseTxn implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name="AMOUNT")
	private BigDecimal amount;
	
	@Id
	@Column(name="TXN_ID")
	private String txnId;

	@Column(name="USER_ID")
	private String userId;

		
	public ExpenseTxn() {
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getTxnId() {
		return txnId;
	}


	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

		
	
}