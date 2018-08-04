package org.order.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service.GetOrderRequest;
import io.spring.guides.gs_producing_web_service.GetOrderResponse;
import io.spring.guides.gs_producing_web_service.Order;

@Endpoint
public class OrderEndPoint {
	private static final String NAMESPACE_URI = "http://localhost:8090/order-web-service";

	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOrderResponse")
	@ResponsePayload
	public GetOrderResponse getOrder( @RequestPayload GetOrderRequest  orderResponse) {
		GetOrderResponse resp=new GetOrderResponse();
		
		Order value=new Order();
		value.setExchangeId("1");
		value.setPrice(11);
		resp.setOrder(value);
		return resp;
	}

}
