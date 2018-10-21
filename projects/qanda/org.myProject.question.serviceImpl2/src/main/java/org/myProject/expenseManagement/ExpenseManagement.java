package org.myProject.expenseManagement;

import java.math.BigDecimal;
import java.util.Map;

import org.myProject.common.constants.ProjectConstants;
import org.myProject.common.dao.DAOFactory;
import org.myProject.common.dao.ExpenseDAO;

//import com.sun.faces.application.WebPrintWriter;

public class ExpenseManagement {
	public Map  saveExpenseTxn (Map map ){
		ExpenseDAO expenseDAO =(ExpenseDAO) DAOFactory.getDAO(DAOFactory.EXPENSE_DAO);
		expenseDAO.addExpense(((String[])map.get("txnId"))[0], 
				((String[])map.get("userId"))[0],
				new BigDecimal(((String[])map.get("amount"))[0]));
		String expDetails[]=(String[])map.get("expenseShareId");
		for(int i=0;i<expDetails.length;i++){
			expenseDAO.addExpenseDetails(((String[])map.get("expenseShareId"))[i],
					((String[])map.get("expenseId"))[i],
					((String[])map.get("sharePerc"))[i]);
		}
		map.put("status", ProjectConstants.REQUEST_STATUS_SUCCESS);
		return map;
	}
}
