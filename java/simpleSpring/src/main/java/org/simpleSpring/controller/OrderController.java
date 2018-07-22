package org.simpleSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

	@RequestMapping(value="/order",method = RequestMethod.GET)
	public @ResponseBody String loadAllSubject(
			)  {
		return "{'test':'pass'}";
	}
}
