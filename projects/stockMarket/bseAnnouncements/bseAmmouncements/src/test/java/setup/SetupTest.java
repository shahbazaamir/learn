package setup;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import config.Config;
import constants.Constants;

public class SetupTest {
	public void init() {
		System.setProperty(Constants.CHROME_DRIVER_PATH_KEY,Config.getConfig(Constants.CHROME_DRIVER_PATH));
	}
	
	@Test
	public void moveMouse() {
		init();
		WebDriver driver =null;
		try{
			driver=new ChromeDriver();
			driver.get("https://www.google.com/");
			
		} finally { 
			if(driver != null) {
				driver.close();
			}
		}
	}
}
