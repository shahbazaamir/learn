package org.myProject.question;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.OptionDetail;
import org.myProject.service.question.OptionService;
import org.myProject.service.question.QuestionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OptionController {
	@RequestMapping(value="/saveOptionDetails",method = RequestMethod.POST )
	   public @ResponseBody OptionDetail saveOptionDetails(
			   @RequestBody OptionDetail optionDetail) throws QuestionException {
		System.out.println("hello option");
		OptionService optionService=(OptionService)BeanFactory.getBean(QuestionConstants.OPTION_SERVICE);
		System.out.println("hello option 012");
		optionService.saveOptionDetails(optionDetail);
	     return optionDetail;
	   }
	
	
	@RequestMapping(value="/option/{subjectId}/{questionId}/{optionId}",method = RequestMethod.GET)
	   public @ResponseBody String loadOptionDetails(@PathVariable String questionId,
			   @PathVariable String subjectId,
			   @PathVariable String optionId
			   ) throws QuestionException {
		OptionService optionService=(OptionService)BeanFactory.getBean(QuestionConstants.OPTION_SERVICE);
	    //if(questionId==null){
		OptionDetail optionDetail=new OptionDetail();
		optionDetail.setQuestionId(questionId);
		optionDetail.setSubjectId(subjectId);
		optionDetail.setOptionId(optionId);
		System.out.println("hello7");
	    return optionService.getOptionDetail(optionDetail);
	   }
}
