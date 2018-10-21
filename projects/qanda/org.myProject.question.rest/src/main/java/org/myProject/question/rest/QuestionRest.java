package org.myProject.question.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;

@Path("/questions")
public class QuestionRest {
	@GET
	@Path("{questionId}")
	  @Produces(MediaType.APPLICATION_JSON)
	  public String getQuestionDetails(@PathParam("questionId") String questionId) throws QuestionException {
	    QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
	    //if(questionId==null){
	    return questionService.getQuestionDetails();
	    //}else{
	    	
	    //}
	}

	
}
