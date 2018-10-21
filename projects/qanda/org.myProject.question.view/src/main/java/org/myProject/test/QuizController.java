package org.myProject.test;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.question.bean.TestConstants;
import org.myProject.service.question.AnswerDetail;
import org.myProject.service.question.AnswerService;
import org.myProject.service.question.QuestionException;
import org.myProject.service.quiz.QuizDetails;
import org.myProject.service.quiz.QuizException;
import org.myProject.service.quiz.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuizController {
	@RequestMapping(value="/saveAnswerDetails",method = RequestMethod.POST )
	   public @ResponseBody QuizDetails getNextQuestion(
			   @RequestBody QuizDetails quizDetails) throws QuestionException {
		System.out.println("hello0122");
		QuizService answerService=(QuizService)BeanFactory.getBean(TestConstants.TEST_SERVICE);
	    //if(questionId==null){
		System.out.println("hello012");
		try {
			quizDetails =answerService.nextQuestion(quizDetails);
		} catch (QuizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // Gson gson=new Gson();
	     return quizDetails;
	   }
	
}
