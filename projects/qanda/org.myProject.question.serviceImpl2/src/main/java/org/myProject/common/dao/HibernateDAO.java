package org.myProject.common.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.myProject.common.dao.util.DatabaseHelper;

public abstract class HibernateDAO {
	public boolean save(Object o){
		boolean result = false;
	    Session session=null;
	    try{
	    	session=DatabaseHelper.getSessionFactory().getCurrentSession();  
	    
	    Transaction t=session.beginTransaction();  
	    
	    session.save(o);
	    t.commit();
	    result=true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    	
	    }finally{
	    	DatabaseHelper.closeResources(session);
	    }
	    return result;
	}
}
