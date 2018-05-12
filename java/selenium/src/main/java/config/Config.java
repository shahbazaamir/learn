package config;

import java.io.IOException;
import java.util.Properties;

public final class Config {

	private static Properties props ;
	
	
	private Config() {}
	static {
		try {
			props =new Properties();
			props.load(Config.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getConfig(String key) {
		return props.getProperty(key);
	}
}
