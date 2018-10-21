package org.myProject.common.dao;

import java.math.BigDecimal;

public interface ExpenseDAO {
	int addExpense(String txnId,String userId,BigDecimal amount);
	int addExpenseDetails(String expenseShareId, String expenseId, String sharePerc) ;
}
