package org.myProject.qAndA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.myProject.common.dao.QAndADAO;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.Question;
import org.myProject.common.model.Subject;

import com.google.gson.Gson;

public class QAndAAdapter {
	
	public Map addQuestion(Map m){
		Map m2 =new  HashMap();
		try{
			m2.put("status","true");
			Question q=new Question();
		if(m.get("questionId")!=null)
		q.setQuestionId(((String[])m.get("questionId"))[0]);
		if(m.get("question")!=null)
		q.setQuestion(((String[])m.get("question"))[0]);
		if(m.get("optionA")!=null)
	    q.setOptionA(((String[])m.get("optionA"))[0]);
		if(m.get("optionB")!=null)
		q.setOptionB(((String[])m.get("optionB"))[0]);
		if(m.get("optionC")!=null)
		q.setOptionC(((String[])m.get("optionC"))[0]);
		if(m.get("optionD")!=null)
		q.setOptionD(((String[])m.get("optionD"))[0]);
		if(m.get("explanation")!=null)
		q.setExplanation(((String[])m.get("explanation"))[0]);
		//if(m.get("subject")!=null)
			//q.setSubject(((String[])m.get("subject"))[0]);
		
		QAndA qandq=new QAndA();
		qandq.addQuestion(q);
		m2.put("status","true");
		}catch(Exception e){
			e.printStackTrace();
		}
		return m2;
	}

	public Map addQuestion(String questionId, String question, String optionA, String optionB, String optionC, String optionD, String explanation){
		Map m2 =new  HashMap();
		try{
			m2.put("status","true");
			Question q=new Question();
		q.setQuestionId(questionId);
		q.setQuestion(question);
	    q.setOptionA(optionA);
		q.setOptionB(optionB);
		q.setOptionC(optionC);
		q.setOptionD(optionD);
		q.setExplanation(explanation);
		QAndA qandq=new QAndA();
		qandq.addQuestion(q);
		m2.put("status","true");
		}catch(Exception e){
			e.printStackTrace();
		}
		return m2;
	}
	
	public String loadQuestion(Map m){
		String questionId=null;
		QAndA qAndA =new QAndA();
		Gson   gson=null;
		List l;
		try{
			questionId=((String[]) m.get("questionId"))[0];
			
			l=qAndA.loadQuestion(questionId);
			gson=new Gson();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
		}
		return 	gson.toJson(l);
	}
	public Map addSubject(Map m){
		Map m2 =new  HashMap();
		Subject s=new Subject();
		try{
			if(m.get("subjectCode")!=null)
				s.setSubjectCode(((String[])m.get("subjectCode"))[0]);
			if(m.get("subjectDesc")!=null)
				s.setSubjectDesc(((String[])m.get("subjectDesc"))[0]);
			QAndA qAndA =new QAndA();
			qAndA.addSubject(s);
			
			m2.put("status","true");
		}catch(Exception  e){
			throw new RuntimeException(e);
		}
		return m2;
	}
}
