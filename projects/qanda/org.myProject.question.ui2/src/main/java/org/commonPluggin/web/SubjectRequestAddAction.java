package org.commonPluggin.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SubjectRequestAddAction {
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try{
			Class c= Class.forName("org.myProject.common.qAndA.");
			Object o=c.newInstance();
			Method m =c.getDeclaredMethod("login", String.class,String.class);
			Object res=m.invoke(o, request.getParameter("userId"),request.getParameter("password"));
			request.setAttribute("ajaxResponse", res);
			request.getRequestDispatcher("/qanda/AjaxResponse.jsp").forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
