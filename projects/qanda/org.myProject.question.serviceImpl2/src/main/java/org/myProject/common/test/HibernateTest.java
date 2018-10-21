package org.myProject.common.test;


import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.myProject.common.model.ApplicationInfo;


public class HibernateTest {
	public static void testHbn(){
		System.out.println("start test");
		Configuration cfg=new AnnotationConfiguration();  
	    cfg.configure("hibernate.cfg.xml");
	    SessionFactory factory=cfg.buildSessionFactory();  
	    Session session=factory.openSession();  
	    
	    Transaction t=session.beginTransaction();  
	    ApplicationInfo ai = new ApplicationInfo();
	    ai.setIdentifier("a1");
	    ai.setEventId("testHiber1");
	    ai.setEventDesc("something");
	    session.persist(ai);
	    t.commit();
	    session.close();
	    System.out.println("finish test");
	    }
}
