package org.myProject.question;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.AnswerDetail;
import org.myProject.service.question.AnswerService;
import org.myProject.service.question.ImageDetail;
import org.myProject.service.question.ImageService;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
public class ImagesController {/*
	@RequestMapping(value="/saveImage",method = RequestMethod.POST )
	   public @ResponseBody ImageDetail saveQuestionDetails(
			   @RequestBody ImageDetail imageDetail) throws QuestionException {
		System.out.println("hello012209090");
		ImageService answerService=(ImageService)BeanFactory.getBean(QuestionConstants.IMAGE_SERVICE);
	    //if(questionId==null){
		System.out.println("hello01209000000088");
		answerService.saveImageDetails(imageDetail);
	    // Gson gson=new Gson();
	     return imageDetail;
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
	   }*/
}
