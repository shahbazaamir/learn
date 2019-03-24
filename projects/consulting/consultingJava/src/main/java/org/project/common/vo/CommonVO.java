package org.project.common.vo;

import java.util.HashMap;

import org.framework.vo.ValueObject;
import org.project.user.UserVO;

public class CommonVO extends ValueObject{
	private HashMap<String , Object> paramHm ;
	private String sessionId ;
	private int sessionStatus ;
	private UserVO uservo ;
	private boolean userStatus ;
	private String responsePage ;
	private Object ajaxResp ;
	private HashMap<String, Object> commonRespHm ;
	public Object getAjaxResp() {
		return ajaxResp;
	}

	public void setAjaxResp(Object ajaxResp) {
		this.ajaxResp = ajaxResp;
	}

	public String getResponsePage() {
		return responsePage;
	}

	public void setResponsePage(String responsePage) {
		this.responsePage = responsePage;
	}

	public boolean isUserStatus() {
		return userStatus;
	}

	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}
	public UserVO getUservo() {
		return uservo;
	}

	public void setUservo(UserVO uservo) {
		this.uservo = uservo;
	}

	public int getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(int sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public HashMap<String, Object> getParamHm() {
		return paramHm;
	}

	public void setParamHm(HashMap<String, Object> paramHm) {
		this.paramHm = paramHm;
	}

	public HashMap<String, Object> getCommonRespHm() {
		return commonRespHm;
	}

	public void setCommonRespHm(HashMap<String, Object> commonRespHm) {
		this.commonRespHm = commonRespHm;
	}
	
	

}
