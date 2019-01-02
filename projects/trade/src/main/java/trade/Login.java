package trade;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Login {


	public static void launch(WebDriver driver) throws InterruptedException {
		


			driver.get("https://kite.zerodha.com/");
			System.out.println("waiting...");
			Thread.sleep(10 * 1000);
			System.out.println("executing...");
			
		
	}
}



