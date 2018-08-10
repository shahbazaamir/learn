package org.order.service;
// pull success , push success

import java.io.Serializable;
import java.util.List;

public class OrderDetails<T> implements Serializable {

	private List<T> orderList;

	public List<T> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<T> orderList) {
		this.orderList = orderList;
	}
	
	
	
}
