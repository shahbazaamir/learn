package org.myProject.common.dao;

import org.hibernate.Session;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.Person;

public class PersonDAO {
	public Person select(String personId){
		Session s =DatabaseHelper.getSessionFactory().getCurrentSession();
		Person p  =(Person)s.load(Person.class, personId);
		DatabaseHelper.closeResources(s);
		return p;
	}
}
