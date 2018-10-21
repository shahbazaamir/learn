package org.myProject.common.dao;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.ExpenseShareDetails;
import org.myProject.common.model.ExpenseTxn;

public class ExpenseDAOHibImpl implements ExpenseDAO{

	@Override
	public int addExpense(String txnId, String userId, BigDecimal amount) {
		try{
		ExpenseTxn expenseTxn=new ExpenseTxn();
		expenseTxn.setTxnId(txnId);
		expenseTxn.setUserId(userId);
		expenseTxn.setAmount(amount);
		addExpense( expenseTxn) ;
		}catch(Exception e){
			e.printStackTrace();
		}finally{	
		}
		return 1;
	}
	
	@Override
	public int addExpenseDetails(String expenseShareId, String expenseId, String sharePerc) {
		try{
		ExpenseShareDetails expenseShareDetails=new ExpenseShareDetails();
		expenseShareDetails.setExpenseId(expenseId);
		expenseShareDetails.setExpenseShareId(expenseShareId);
		expenseShareDetails.setSharePerc(sharePerc);
		addExpenseDetails( expenseShareDetails) ;
		}catch(Exception e){
			e.printStackTrace();
		}finally{	
		}
		return 1;
	}
	
	public int addExpense(ExpenseTxn expenseTxn) {
		Session s=DatabaseHelper.getSessionFactory().getCurrentSession();  
		try{
		Transaction t=s.beginTransaction();
		s.save(expenseTxn);
		t.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{	
			DatabaseHelper.closeResources(s);
		}
		return 1;
	}
	
	public int addExpenseDetails(ExpenseShareDetails expenseShareDetails) {
		Session s=DatabaseHelper.getSessionFactory().getCurrentSession();  
		try{
		Transaction t=s.beginTransaction();
		s.save(expenseShareDetails);
		t.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{	
			DatabaseHelper.closeResources(s);
		}
		return 1;
	}

}
