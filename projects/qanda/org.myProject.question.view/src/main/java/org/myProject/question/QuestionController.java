package org.myProject.question;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/question2")
public class QuestionController { 
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String printHello() throws QuestionException {
		QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
		//if(questionId==null){
		System.out.println("hello7");
		return questionService.getQuestionDetails();
	}
}
