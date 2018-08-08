package org.simpleSpring.bean;

import java.io.InputStream;
import java.util.Properties;


public class FactoryConstants {
	private static Properties factory= new Properties();
	static {
		Properties factory= new Properties();
		InputStream input = null;

		try {

			String filename = "factory.properties";
			input = FactoryConstants.class.getClassLoader().getResourceAsStream(filename);
			factory.load(input);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final String ORDER_SERVICE=getProp("orderService");
	
	private static String getProp(String key) {
		return factory.getProperty(key);
	}
	
}
