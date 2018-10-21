package org.myProject.question.model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
	@RequestMapping(value="/default",method=RequestMethod.GET)
	public @ResponseBody DefaultRequest defaultAction(@RequestBody DefaultRequest defaultRequest){
		 defaultRequest.setSubjectId("pass");
		 return defaultRequest;
	}
	
	@RequestMapping(value="/default/{id}",method=RequestMethod.GET)
	public @ResponseBody DefaultRequest getDefault(@PathVariable String id ){
		DefaultRequest defaultRequest=new DefaultRequest();
		defaultRequest.setQuestionId(id);
		defaultRequest.setSubjectId("pass");
		 return defaultRequest;
	}
	
	@RequestMapping(value="/saveDefault/",method=RequestMethod.POST)
	public @ResponseBody DefaultRequest saveDefault(@RequestBody DefaultRequest defaultRequest ){
		defaultRequest.setSubjectId("pass");
		 return defaultRequest;
	}
}
