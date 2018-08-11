package org.simpleSpring.controller;

import org.order.Order;
import org.order.service.OrderDetails;
import org.order.service.OrderService;
import org.simpleSpring.bean.BeanFactory;
import org.simpleSpring.bean.FactoryConstants;
import org.simpleSpring.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

	@RequestMapping(value="/order",method = RequestMethod.GET)
	public @ResponseBody String loadAllSubject(
			)  {
		OrderService orderService=(OrderService) BeanFactory.getBean(FactoryConstants.ORDER_SERVICE);
		OrderDetails<Order> orderDetails =orderService.getOrder();
		//orderDetails.
		return Util.gson.toJson(orderDetails);
	}
	
}
