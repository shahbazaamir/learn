package org.myProject.common.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.myProject.common.model.Question;

public class QuestionDAOJPAImpl implements QAndADAO {

	

	@Override
	public Map modifyQuestion(Map m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addQuestion(Question q) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
	      
	      EntityManager entitymanager = emfactory.createEntityManager( );
	      entitymanager.getTransaction().begin();
	      entitymanager.persist(q);
	      entitymanager.getTransaction().commit();
	      entitymanager.close( );
	      emfactory.close( );
	      return true;
	}

	@Override
	public List loadQuestion(String quesionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
