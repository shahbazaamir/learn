package trade;

import java.awt.AWTException;
import java.io.File;
import java.text.ParseException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DataLoaderTest {

	public void init() {
		//System.setProperty("webdriver.firefox.marionette","F:\\shahbaz\\softwares\\mozilla-selenium-driver\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver","F:\\softwares\\chrome-driver\\chromedriver.exe");
	}
	
	@Test
	public void testData() {
		init();
		WebDriver driver=null;
		String name="NETFLIX";
		try {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized"); 
			driver=new ChromeDriver();
			driver.manage().window().maximize();
			 //driver = new FirefoxDriver();
			 DataLoader dl=new DataLoader();
			// Map<String,String> m= 
			 try {
					
					dl.loadStockData(driver,name);
				
			} catch (AWTException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //System.out.println(m);
 catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
		}finally { 
			if(driver != null) {
				driver.close();
			}
		}
	}
}
