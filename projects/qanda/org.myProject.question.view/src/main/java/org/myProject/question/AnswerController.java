package org.myProject.question;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.AnswerDetail;
import org.myProject.service.question.AnswerService;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnswerController {
	@RequestMapping(value="/saveAnswerDetails",method = RequestMethod.POST )
	   public @ResponseBody AnswerDetail saveQuestionDetails(
			   @RequestBody AnswerDetail answerDetail) throws QuestionException {
		System.out.println("hello0122");
		AnswerService answerService=(AnswerService)BeanFactory.getBean(QuestionConstants.ANSWER_SERVICE);
	    //if(questionId==null){
		System.out.println("hello012");
		answerService.saveAnswerDetails(answerDetail);
	    // Gson gson=new Gson();
	     return answerDetail;
	   }
	
	
	@RequestMapping(value="/answer/{subjectId}/{questionId}/{answerId}",method = RequestMethod.GET)
	   public @ResponseBody String printHello(@PathVariable String questionId,
			   @PathVariable String subjectId,
			   @PathVariable String answerId
			   ) throws QuestionException {
		AnswerService answerService=(AnswerService)BeanFactory.getBean(QuestionConstants.ANSWER_SERVICE);
	    //if(questionId==null){
		AnswerDetail answerDetail=new AnswerDetail();
		answerDetail.setQuestionId(questionId);
		answerDetail.setSubjectId(subjectId);
		answerDetail.setAnswerId(answerId);
		System.out.println("hello7");
	    return answerService.getAnswerDetail(answerDetail);
	   }
}
