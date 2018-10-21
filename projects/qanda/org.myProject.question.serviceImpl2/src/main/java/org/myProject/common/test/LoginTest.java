package org.myProject.common.test;

import org.myProject.common.dao.LoginDAO;

public class LoginTest {
public static void main(String[] args) {
	LoginDAO loginDAO=new LoginDAO();
	boolean isValid=loginDAO.login("shahbaz", "shahbaz");
	System.out.println(isValid);
}
}
