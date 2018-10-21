package org.myProject.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.myProject.common.constants.ProjectConfiguration;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.Question;
import org.myProject.common.util.ProjectUtil;
import org.myProject.logger.Logger;
import org.myProject.qAndA.QAndAVO;

import com.google.gson.Gson;

public class QuestionDAOHibImpl extends HibernateDAO implements QAndADAO{
	
	public Question fetchNextQuestion(List<String> attemptedQuestion){
		Configuration cfg=ProjectUtil.getInstance().getConfiguration();
	    SessionFactory factory=cfg.buildSessionFactory();  
	    Session session=factory.openSession();  
	    
	    Transaction t=session.beginTransaction();  
	    //Question q =new Question();
	    //q.setQuestionId("1");
	    Query query=session.createQuery(" from Question where questionId not in (:attemptedQuestions) ");
	    query.setParameterList("attemptedQuestions", attemptedQuestion);
	    List<Question> results= query.list();
	    return results.get(0);
	}
	
	@Override
	public boolean addQuestion(Question q){
		return save(q);
	   
	}
	@Override
	public Map modifyQuestion(Map m){
		Map m2 =new  HashMap();
	    Session session=null;
	    try{
	    	session=DatabaseHelper.getSessionFactory().getCurrentSession();  
	    	Transaction t =session.beginTransaction();
			Query query=session.createQuery(" from Question where questionId = :questionId ");
			query.setString("questionId",((String[]) m.get("questionId"))[0]);
			List l=query.list();
			Question q = null ;
			if(l.size() ==1){
				q = (Question)l.get(0);
			
			    //Question q =new Question();
			    q.setQuestionId(((String[])m.get("questionId"))[0]);
			    q.setQuestion(((String[])m.get("question"))[0]);
			    q.setOptionA(((String[])m.get("optionA"))[0]);
			    q.setOptionB(((String[])m.get("optionB"))[0]);
			    q.setOptionC(((String[])m.get("optionC"))[0]);
			    q.setOptionD(((String[])m.get("optionD"))[0]);
			    q.setExplanation(((String[])m.get("explanation"))[0]);
			    q.setAnswer(((String[])m.get("answer"))[0]);
			    t.commit();
			    m2.put("status", "success");
			}else{
				m2.put("status", "failure");
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    	
	    }finally{
	    	DatabaseHelper.closeResources(session);
	    }
	    return m2;
	   
	}
	@Override
	public List loadQuestion(String questionId ){
		Session s =null;
		List l=null;
		Gson gson = null;
		try{
			s=DatabaseHelper.getSessionFactory().getCurrentSession();
			Transaction t =s.beginTransaction();
			Query query=s.createQuery(" from Question where questionId = :questionId ");
			Logger.log("questionId");
			Logger.log(questionId);
			query.setString("questionId",questionId);
			l=query.list();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DatabaseHelper.closeResources(s);
		}
		return 	l;
	}
	public String loadAllSubject(Map m){
		Session s =null;
		List l=null;
		Gson gson = null;
		try{
			s=DatabaseHelper.getSessionFactory().getCurrentSession();
			Transaction t =s.beginTransaction();
			Query query=s.createQuery(" from Subject ");
			query.setFirstResult(0);
			query.setMaxResults(10);
			l=query.list();
			gson=new Gson();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DatabaseHelper.closeResources(s);
		}
		return 	gson.toJson(l);
	}
	public Question fetchNextQuestion(String questionId){
		//DAOFactory dAOFactory
		Configuration cfg=ProjectUtil.getInstance().getConfiguration();
	    SessionFactory factory=cfg.buildSessionFactory();  
	    Session session=factory.openSession();  
	    
	    Transaction t=session.beginTransaction();  
	    //Question q =new Question();
	    //q.setQuestionId("1");
	    Query query=session.createQuery(" from Question where questionId = :questionId ");
	    query.setString("questionId", questionId);
	    List<Question> results= query.list();
	    //qAndAVO.setQue
	    return results.get(0);
	}
	public Question fetchNextQuestion(){
		Configuration cfg=ProjectUtil.getInstance().getConfiguration();
	    SessionFactory factory=cfg.buildSessionFactory();  
	    Session session=factory.openSession();  
	    
	    Transaction t=session.beginTransaction();  
	    //Question q =new Question();
	    //q.setQuestionId("1");
	    Query query=session.createQuery(" from Question ");
	    query.setFirstResult(0);
	    query.setMaxResults(1); 
	    List<Question> results= query.list();
	    return results.get(0);
	}
}
