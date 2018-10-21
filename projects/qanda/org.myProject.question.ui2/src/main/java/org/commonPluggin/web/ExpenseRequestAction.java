package org.commonPluggin.web;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExpenseRequestAction {
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try{
			Class c= Class.forName("org.myProject.expenseManagement.ExpenseManagement");
			Object o=c.newInstance();
			Method m =c.getDeclaredMethod("saveExpenseTxn",Map.class);
			Object res=m.invoke(o,  request.getParameterMap());
			if("".equals(((Map)res).get("status"))){
				request.setAttribute("result", res);
				request.getRequestDispatcher("/qanda/AddQuestion.jsp").forward(request, response);
			}else{
				request.setAttribute("result", res);
				request.getRequestDispatcher("/qanda/AddQuestion.jsp").forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
