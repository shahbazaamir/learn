package org.myProject.qAndA;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.myProject.common.dao.DAOFactory;
import org.myProject.common.dao.QAndADAO;
import org.myProject.common.dao.SubjectDAO;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.Question;
import org.myProject.common.model.Subject;

import com.google.gson.Gson;

public class QAndA {
	
	public boolean addQuestion(Question q){
		boolean status=false;           
	    try{
	    	QAndADAO dao=(QAndADAO)DAOFactory.getDAO(DAOFactory.QUESTION_DAO);
	    	dao.addQuestion(q);
	    	status=true;
	    
	    }catch(Exception e){
	    	e.printStackTrace();
	    	
	    }
	    return status;
	   
	}	
	
	public List loadQuestion(String questionId){
		List l=null;
		try{
			QAndADAO dao=(QAndADAO)DAOFactory.getDAO(DAOFactory.QUESTION_DAO);
			l=dao.loadQuestion(questionId);
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
		}
		return 	l;
	}

	public void addSubject(Subject s) {
		SubjectDAO subjectDAO=( SubjectDAO)DAOFactory.getDAO(DAOFactory.SUBJECT_DAO);;
		subjectDAO.save(s);
	}
	
}
