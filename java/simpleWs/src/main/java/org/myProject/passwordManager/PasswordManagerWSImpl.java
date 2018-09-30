package org.myProject.passwordManager;

import javax.jws.WebService;

@WebService(endpointInterface="org.myProject.passwordManager.PasswordManagerWS",serviceName="PasswordManagerWS")
public class PasswordManagerWSImpl implements PasswordManagerWS{

	public String getPassword(String appId) {
		return "test";
	}

}
