package org.myProject.common.dao;

import org.hibernate.Session;
import org.myProject.common.dao.util.DatabaseHelper;

public class GenericDAO<T>{
	public T select(T t,String id){
		Session s =DatabaseHelper.getSessionFactory().getCurrentSession();
		
		s.load(t.getClass(), id);
		return null;
	}
}
