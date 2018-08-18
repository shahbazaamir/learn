package org.order.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderService {

	public OrderDetails getOrder();
	public OrderDetails saveOrder(OrderDetails orderDetails);
}
