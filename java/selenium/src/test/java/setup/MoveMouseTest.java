package setup;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import config.Config;
import constants.Constants;
import move.mouse.MoveMouse;

public class MoveMouseTest {
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
			MoveMouse mm=new MoveMouse();
			mm.moveTo(500, 500);
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally { 
			if(driver != null) {
				driver.close();
			}
		}
	}
}
