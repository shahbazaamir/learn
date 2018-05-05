package trade;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Login {


	public static void main(String[] args) {
		//System.setProperty("webdriver.firefox.marionette","F:\\shahbaz\\softwares\\mozilla-selenium-driver\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver","F:\\shahbaz\\softwares\\chrome-driver\\chromedriver.exe");

		WebDriver driver =null;
		try{
			//driver = new FirefoxDriver();
			driver=new ChromeDriver();
			//comment the above 2 lines and uncomment below 2 lines to use Chrome

			driver.get("https://kite.zerodha.com/");
			System.out.println("waiting...");
			Thread.sleep(10 * 1000);
			System.out.println("executing...");
			
		}finally { 
			if(driver != null) {
				driver.close();
			}
		}
	}
}


}
