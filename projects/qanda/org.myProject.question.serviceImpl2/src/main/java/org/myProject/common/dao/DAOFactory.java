package org.myProject.common.dao;

import org.myProject.common.constants.ProjectConfiguration;
import org.myProject.common.constants.ProjectConstants;

public class DAOFactory {
	public static final int EXPENSE_DAO =1;
	public static final int QUESTION_DAO =2;
	public static final int MASTER_DAO =3;
	public static final int SUBJECT_DAO = 4;
	/**
	 * returns null if unable to get DAO.
	 * @param id
	 * @return
	 */
	public static Object getDAO(int id){
		Object o=null;
		try{
		switch (id){
		case EXPENSE_DAO :{
			if(ProjectConstants.PROJECT_ORM_TYPE_HIBERNATE==ProjectConfiguration.PROJECT_ORM_TYPE){
				o=new ExpenseDAOHibImpl();
			
			}else{
				o=null;
			}
		}
		break;
		case QUESTION_DAO:{
			if(ProjectConstants.PROJECT_ORM_TYPE_HIBERNATE==ProjectConfiguration.PROJECT_ORM_TYPE){
				o=new QuestionDAOHibImpl();
			}else{
				o=null;
			}
		}
		break;
		case MASTER_DAO:{
			if(ProjectConstants.PROJECT_ORM_TYPE_HIBERNATE==ProjectConfiguration.PROJECT_ORM_TYPE){
				o=new MasterDAOHibImpl();
			}else{
				o=null;
			}
		}
		case SUBJECT_DAO:{
			if(ProjectConstants.PROJECT_ORM_TYPE_HIBERNATE==ProjectConfiguration.PROJECT_ORM_TYPE){
				o=new SubjectDAOHibImpl();
			}else{
				o=null;
			}
		}
		break;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return o;
	}
}
