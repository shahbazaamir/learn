package org.myProject.question;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mvc")
public class HelloController { 
	@RequestMapping(value="/hello",method = RequestMethod.GET)
	   public @ResponseBody String printHello() {
	      //model.addAttribute("message", "Hello Spring MVC Framework!");
		System.out.println("hellllo111");
	      return "{\"hello\":\"hello\"}";
	   }
}
