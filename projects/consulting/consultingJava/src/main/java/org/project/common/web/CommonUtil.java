package org.project.common.web;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	public HashMap<String , Object > getRequestMap(HttpServletRequest req ){
		Enumeration<String> reqEnum = req.getParameterNames();
		String paramName = null ;
		HashMap<String , Object> objHM = new HashMap<String , Object>(); 
		while(reqEnum.hasMoreElements()){
			paramName = reqEnum.nextElement();
			objHM.put(paramName, req.getParameter(paramName));
		}
		return objHM ;
	}
}
