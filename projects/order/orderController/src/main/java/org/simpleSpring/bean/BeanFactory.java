package org.simpleSpring.bean;

import org.simpleSpring.log.LogWrapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {
	
	public static Object getBean(String beanId){
		LogWrapper.debug("beanId is");
		LogWrapper.debug(beanId);
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
