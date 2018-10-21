package org.myProject.question;

import org.myProject.question.bean.BeanFactory;
import org.myProject.question.bean.QuestionConstants;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;
import org.myProject.service.question.SubjectDetail;
import org.myProject.service.question.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class SubjectController {
	@RequestMapping(value="/subject/{subjectId}",method = RequestMethod.GET)
	public @ResponseBody String loadSubject(
			@PathVariable String subjectId
			) throws QuestionException {
		SubjectService subjectService=(SubjectService)BeanFactory.getBean(QuestionConstants.SUBJECT_SERVICE);
		//if(questionId==null){
		SubjectDetail subjectDetail=new SubjectDetail();
		subjectDetail.setSubjectId(subjectId);
		System.out.println("hello77");
		return subjectService.getSubjectDetail(subjectDetail);
	}

	@RequestMapping(value="/subject",method = RequestMethod.GET)
	public @ResponseBody String loadAllSubject(
			) throws QuestionException {
		SubjectService subjectService=(SubjectService)BeanFactory.getBean(QuestionConstants.SUBJECT_SERVICE);
		//if(questionId==null){
		System.out.println("hello777");
		return subjectService.getAllSubject();
	}

	@RequestMapping(value="/saveSubjectDetails",method = RequestMethod.POST )
	public @ResponseBody SubjectDetail  saveSubjectDetails(
			@RequestBody SubjectDetail subjectDetail) throws QuestionException {
		System.out.println("hello01244342");
		SubjectService subjectService=(SubjectService)BeanFactory.getBean(QuestionConstants.SUBJECT_SERVICE);
		//if(questionId==null){
		System.out.println("hello03412");
		subjectService.saveSubjectDetails(subjectDetail);
		// Gson gson=new Gson();
		return subjectDetail;
	}

}
