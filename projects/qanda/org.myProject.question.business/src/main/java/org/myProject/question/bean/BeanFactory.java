package org.myProject.question.bean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {
	
	public static Object getBean(String beanId){
		return getApplicationContext(beanId);
	}
	private static ConfigurableApplicationContext context;
	private static Object getApplicationContext(String beanId) {
		if(context == null){
			context =  new ClassPathXmlApplicationContext("application-context.xml");
			context.start();
		}
		return context.getBean(beanId);
	}
	
	 
}
