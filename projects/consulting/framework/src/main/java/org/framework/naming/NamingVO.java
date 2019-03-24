package org.framework.naming;

import org.framework.vo.ValueObject;

public class NamingVO extends ValueObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contextFactory ;
	private String contextUrl;
	private String userName ;
	private String password ;
	private boolean contextPropRequired ;
	public boolean isContextPropRequired() {
		return contextPropRequired;
	}
	public void setContextPropRequired(boolean contextPropRequired) {
		this.contextPropRequired = contextPropRequired;
	}
	public String getContextFactory() {
		return contextFactory;
	}
	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}
	public String getContextUrl() {
		return contextUrl;
	}
	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
