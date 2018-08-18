package setup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import atu.testrecorder.ATUTestRecorder;
import config.Config;
import constants.Constants;

public class TestRecordScreen {
	ATUTestRecorder recorder;
	//@Befor
	 public void setup() throws Exception {
	  DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
	  Date date = new Date();
	  //Created object of ATUTestRecorder
	  //Provide path to store videos and file name format.
	  recorder = new ATUTestRecorder("C:\\tmp\\selenium\\","TestVideo-"+dateFormat.format(date),false);
	  //To start video recording.
	  recorder.start();  
	  
	 }
	
	public void init()throws Exception  {
		System.setProperty(Constants.CHROME_DRIVER_PATH_KEY,Config.getConfig(Constants.CHROME_DRIVER_PATH));
		setup();
	}
	
	@Test
	public void setupTest() throws Exception {
		init();
		WebDriver driver =null;
		try{
			driver=new ChromeDriver();
			driver.get("https://www.google.com/");
			Thread.sleep(3000);
		}finally { 
			if(driver != null) {
				driver.close();
				recorder.stop();
			}
		}
	}
}
