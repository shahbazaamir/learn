package org.myProject.passwordManager;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface  PasswordManagerWS {
	@WebMethod
	public abstract String getPassword(String appId);
}
