package org.myProject.common.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.commonPluggin.web.ExpenseRequestAction;
import org.commonPluggin.web.LoginAction;
import org.commonPluggin.web.QAndAAction;
import org.commonPluggin.web.QAndALoadQAction;
import org.commonPluggin.web.QAndAModifyQAction;
import org.commonPluggin.web.SubjectRequestAction;
import org.commonPluggin.web.SubjectRequestAddAction;
import org.myProject.common.constants.WebProjectConstant;

public class CommonRouter {

	public void route(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String ajaxResp = null ;
		Map m=request.getParameterMap();
		String actionId=((String[])m.get(WebProjectConstant.REQUEST_KEY_ACTION_ID))[0];
		int action=0;
		if(actionId==null){
			throw new WebRouterException("actionId cannot be null");
		}else{
			try{
				action=Integer.parseInt(actionId);
			}catch(NumberFormatException e){
				throw new WebRouterException("actionId can be numbers only,actionId:"+actionId);
			}
			switch(action){
			case 1:{
				LoginAction la=new LoginAction();
				la.execute(request,response);
			}
			break;
			case 2:{
				QAndAAction la=new QAndAAction();
				la.execute(request,response);
			}
			break;
			
			case 3:{
				QAndALoadQAction la=new QAndALoadQAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			case 4:{
				QAndAModifyQAction la=new QAndAModifyQAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			case 5:{
				ExpenseRequestAction la=new ExpenseRequestAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			case 6:{
				ExpenseRequestAction la=new ExpenseRequestAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			case 7:{
				SubjectRequestAction la=new SubjectRequestAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			case 8:{
				SubjectRequestAddAction la=new SubjectRequestAddAction();
				//ajaxResp  = la.execute(request,response);
				la.execute(request,response);
			}
			break;
			default:{
				throw new WebRouterException("unsupported actionId :"+actionId);
			}
			}
		}
		//return ajaxResp;
		
	}
	
}
