package org.myProject.common.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the QUESTIONS database table.
 * 
 */
@Entity
@Table(name="EXPENSE_SHARE_DETAILS")
@NamedQuery(name="ExpenseShareDetails.findAll", query="SELECT q FROM ExpenseShareDetails q")
public class ExpenseShareDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="EXPENSE_SHARE_ID")
	private String expenseShareId;
	
	@Column(name="EXPENSE_ID")
	private String expenseId;

	@Column(name="SHARE_PERC")
	private String sharePerc;

	
	public ExpenseShareDetails() {
	}


	public String getExpenseShareId() {
		return expenseShareId;
	}


	public void setExpenseShareId(String expenseShareId) {
		this.expenseShareId = expenseShareId;
	}


	public String getExpenseId() {
		return expenseId;
	}


	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}


	public String getSharePerc() {
		return sharePerc;
	}


	public void setSharePerc(String sharePerc) {
		this.sharePerc = sharePerc;
	}

		
	
}