package test;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import javax.xml.bind.JAXBException;
import org.junit.Test;

import junit.framework.TestCase;
import test.Order;

public class DatabaseHelper {
	private SessionFactory sessionFactory;

	private static StandardServiceRegistry registry;
	
	protected void setUp() throws Exception {
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
		}
	}

	protected void tearDown() throws Exception {
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void testBasicUsage() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Order order =new Order();
		order.setId("123");
		order.setScriptName("TEST");
		order.setStockPrice(new BigDecimal(100));
		order.setStocks(new BigDecimal(1));
		session.save( order );
		session.getTransaction().commit();
		session.close();
	}
	
	
	@Test
	public void getAllOrder() throws Exception {
		setUp();
		List result =null;
		try{
			Session s = sessionFactory.openSession();
			 result = s.createQuery( "from Order" ).list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println( result);
		tearDown();
	}
}
