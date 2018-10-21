package org.myProject.common.dao.test;

import org.myProject.common.dao.DAOFactory;
import org.myProject.common.dao.QAndADAO;

public class QuestionDAOTest {
	public void testInsert(){
		QAndADAO objQAndDAO=(QAndADAO)DAOFactory.getDAO(DAOFactory.QUESTION_DAO);
		//objQAndDAO.addQuestion(m);
	}
}
