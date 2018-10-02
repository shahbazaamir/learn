package org.myProject.passwordManager;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT)
public class PasswordManager {
	@WebMethod
	public String getPassword(@WebParam(name="appName")  String appName){
		return "test";
	}
	
	public static void main(String[] args) {
		new PasswordManager().getPassword("google");
	}
}
