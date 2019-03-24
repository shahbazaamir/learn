package org.project.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.project.common.router.CommonRouter;
import org.project.common.vo.CommonVO;
import org.project.naming.JSPReference;

import com.google.gson.Gson;

/**
 * Servlet implementation class CommonController
 */

public class CommonController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletContext servletContext ;
    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// TODO Auto-generated method stub
		//super.init();
    	servletContext =servletConfig.getServletContext() ;
    	
	}

	/**
     * Default constructor. 
     */
    public CommonController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("INFO : CommonServlet do get");
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession() ;
		
		System.out.println("INFO : CommonServlet do post");
		CommonRouter objCommonRouter= new CommonRouter();
		CommonVO objCommonVO = new CommonVO();
		
		objCommonVO.setSessionId(session.getId());
		
		CommonUtil objCommonUtil =new CommonUtil() ;
		HashMap<String , Object> objMap = objCommonUtil.getRequestMap(request);
		objCommonVO.setParamHm(objMap);
		try{
		objCommonVO = objCommonRouter.route(objCommonVO);
		Gson json = new Gson() ;
		String ajaxFlag = (String)objMap.get("isAjax") ;
		if(ajaxFlag != null && ajaxFlag.equals("Y")){
			PrintWriter out =  response.getWriter();
			out.print(json.toJson(objCommonVO.getAjaxResp()));
		}else{
			request.setAttribute("COMMON_RESPONSE", json.toJson(objCommonVO));
			if(objCommonVO.getResponsePage() == null ){
				objCommonVO.setResponsePage(JSPReference.COMMON_RESP);
			}
				servletContext.getRequestDispatcher(objCommonVO.getResponsePage()).forward(request, response);
			
		}
		}catch(Exception e ){
			e.printStackTrace();
			servletContext.getRequestDispatcher(JSPReference.COMMON_ERR).forward(request, response);
		}
		/*
		if(objCommonVO.getSessionStatus() == ConfigConstants.SESSION_VALID){
			session.setAttribute("SESSION_STATE", "ON");
		}else{
			
		}
		*/
		
		
	}

}
