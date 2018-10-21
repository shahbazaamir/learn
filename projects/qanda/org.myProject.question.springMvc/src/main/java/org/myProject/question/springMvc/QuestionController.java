package org.myProject.question.springMvc;

import org.myProject.service.question.QuestionDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionController {

	@RequestMapping(value = "/saveQuestion", method = RequestMethod.POST)
	public @ResponseBody String saveQuestion(@RequestBody QuestionDetail questionDetail){
		return "{\"status\":\"success\"";
	}
	
	@RequestMapping(value = "/org.myProject.question.view/Question", method = RequestMethod.GET)
	public @ResponseBody String getQuestion(@RequestBody QuestionDetail questionDetail){
		return "{\"status\":\"success\"}";
	}
}
