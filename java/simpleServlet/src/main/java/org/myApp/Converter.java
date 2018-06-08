package org.myApp;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myApp.TempConvertor;

public class Converter extends HttpServlet
{
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
		try{
		String farenhiet=request.getParameter("farenhiet");
		if(farenhiet != null ){
			String celcius=TempConvertor.farenhietToCelcius(Float.parseFloat(farenhiet));
			request.setAttribute("celcius",celcius);
			RequestDispatcher requestDispatcher=request.getRequestDispatcher("/conv/result.jsp");
			requestDispatcher.forward(request,response);
		}
		}catch(Exception e){
			e.printStackTrace();
			RequestDispatcher requestDispatcher=request.getRequestDispatcher("/conv/error.jsp");
			requestDispatcher.forward(request,response);
		}

	}
	public void doPost(HttpServletRequest request,HttpServletResponse response){
	}
}