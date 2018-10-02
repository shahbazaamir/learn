package record;

import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static Properties config= new Properties();
	static {
		InputStream input = null;

		try {

			String filename = "config.properties";
			input = Config.class.getClassLoader().getResourceAsStream(filename);
			config.load(input);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getProp(String key) {
		return config.getProperty(key);
	}
	
}
