package org.myProject.question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.google.gson.Gson;

@Controller
public class QuestionSaveController{ /*
	@RequestMapping(value="/saveQuestion",method = RequestMethod.POST ,consumes="application/json" ,produces="application/json" )
	   public @ResponseBody String saveQuestion(
			   @RequestBody QuestionDetail questionDetail) throws QuestionException {
		QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
	    //if(questionId==null){
		System.out.println("hello0");
	     questionService.saveQuestionDetails(questionDetail);
	     Gson gson=new Gson();
	     return gson.toJson(questionDetail);
	   }*/
	@RequestMapping(value="/question/{subjectId}/{questionId}",method = RequestMethod.GET)
		   public @ResponseBody String printHello(@PathVariable String questionId,
				   @PathVariable String subjectId
				   ) throws QuestionException {
			QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
		    //if(questionId==null){
			QuestionDetail questionDetail=new QuestionDetail();
			questionDetail.setQuestionId(questionId);
			questionDetail.setSubjectId(subjectId);
			System.out.println("hello7");
		    return questionService.getQuestionDetail(questionDetail);
		   }
	
	@RequestMapping(value="/question/{subjectId}",method = RequestMethod.GET)
	   public @ResponseBody String getAllQuestions(
			   @PathVariable String subjectId
			   ) throws QuestionException {
		QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
	    //if(questionId==null){
		
		System.out.println("hello87");
	    return questionService.getQuestionDetailsBySubject(subjectId);
	   }
	
	@RequestMapping(value="/saveQuestionDetails",method = RequestMethod.POST )
	   public @ResponseBody QuestionDetail saveQuestionDetails(
			   @RequestBody QuestionDetail questionDetail) throws QuestionException {
		System.out.println("hello0122");
		QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
	    //if(questionId==null){
		System.out.println("hello012");
	     questionService.saveQuestionDetails(questionDetail);
	    // Gson gson=new Gson();
	     return questionDetail;
	   }
	
	@RequestMapping(value="/saveQuestionData",method = RequestMethod.POST )
	   public  @ResponseBody QuestionDetail saveQuestionData(
			   @RequestParam("subjectId") String subjectId ,
			   @RequestParam("questionId") String questionId ,
			   @RequestParam("questionDesc") String questionDesc) throws QuestionException {
		System.out.println("hello01223");
		QuestionService questionService=(QuestionService)BeanFactory.getBean(QuestionConstants.QUESTION_SERVICE);
	    //if(questionId==null){
		System.out.println("hello0124");
		QuestionDetail questionDetail=new QuestionDetail(subjectId, questionId, questionDesc);
		questionService.saveQuestionDetails(questionDetail);
		return questionDetail;
	}
	/*
	@RequestMapping(value="/showView",method = RequestMethod.GET )
	   public  ModelAndView  showView(
			   @RequestParam("viewName") String viewName) {
		ModelAndView modelAndView  =new ModelAndView (viewName);
		return modelAndView;
	}*/

	
}
		