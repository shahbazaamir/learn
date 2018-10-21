package org.commonPluggin.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginAction {
	

	

	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try{
			Class c= Class.forName("org.myProject.common.dao.LoginDAO");
			Object o=c.newInstance();
			Method m =c.getDeclaredMethod("login", String.class,String.class);
			Object res=m.invoke(o, request.getParameter("userId"),request.getParameter("password"));
			if((Boolean)res){
				HttpSession s=request.getSession();
				s.setAttribute("userId", request.getParameter("userId"));
				request.getRequestDispatcher("/login/LoginSuccess.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("/login/LoginError.jsp").forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	}
