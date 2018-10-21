package org.myProject.common.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.LoginUser;
import org.myProject.common.util.ProjectUtil;
import org.myProject.logger.Logger;

public class LoginDAO {
	public boolean login(String userId,String password){
		
	    boolean isValid=false;
	    LoginUser objLoginUser = new LoginUser();
	    Session session =DatabaseHelper.getSessionFactory().getCurrentSession(); //factory.getCurrentSession();
	    Transaction t = session.beginTransaction();
	    Query query=session.createQuery(" from LoginUser where userId= :userId and password = :password) ");
	    query.setString("userId", userId);
	    query.setString("password", password);
	    List<LoginUser> l=query.list();
	    if(l.size() >0){
	    	isValid=true;
	    	Logger.log("login successful");
	    }else{
	    	Logger.log("login fail");
	    }
	    DatabaseHelper.closeResources(session);
	    return isValid;
	}
}
