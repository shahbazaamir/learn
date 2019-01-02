package trade;
import java.awt.Robot;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClickByXY {

	public void click(WebDriver driver,String path,int offset) {


		WebElement ele = driver.findElement(By.xpath(path));

		//JavascriptExecutor js = (JavascriptExecutor) driver;
		try{
			ele.click();
			//js.executeScript("window.scrollTo(100, 200)");
			Robot robot = new Robot();
			robot.mouseMove(400,400);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
