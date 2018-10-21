package org.commonPluggin.web;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QAndALoadQAction {
	public void execute(HttpServletRequest request, HttpServletResponse response) {
	try{
		Class c= Class.forName("org.myProject.qAndA.QAndAAdapter");
		Object o=c.newInstance();
		Method m =c.getDeclaredMethod("loadQuestion",Map.class);
		Object res=m.invoke(o,  request.getParameterMap());
		
		request.setAttribute("ajaxResponse", res);
		request.getRequestDispatcher("/qanda/AjaxResponse.jsp").forward(request, response);
		
		//out.print(res);
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}
