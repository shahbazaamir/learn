package org.myProject.service;

import org.junit.Test;
import org.myProject.question.serviceImpl.QuestionServiceImpl;
import org.myProject.question.serviceImpl.QuestionServiceSampleImpl;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;

public class QuestionServiceGetTest {
	@Test
	public void getQuestionDetails() throws QuestionException{
		QuestionDetail questionDetail=new QuestionDetail();
		QuestionServiceSampleImpl questionService=new QuestionServiceSampleImpl();
		questionService.getQuestionDetails();
	}
}
