package org.order;



import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;

public class OrderDao {
	
	public int addOrder(String id,String scripName, int price, int quantity) {
		try{
				Session s = DatabaseHelper.sessionFactory.openSession();
				s.beginTransaction();
				Order order =new Order();
				order.setId(id);
				order.setScriptName(scripName);
				order.setStockPrice(new BigDecimal(price));
				order.setStocks(new BigDecimal(quantity));
				s.save( order );
				s.getTransaction().commit();
				s.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public List<Order> getAllOrder() {
		List result =null;
		try{
			Session s = DatabaseHelper.sessionFactory.openSession();
			 result = s.createQuery( "from Order" ).list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (List<Order>)result;
	}
	
	

}
