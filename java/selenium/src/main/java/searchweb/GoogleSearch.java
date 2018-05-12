package searchweb;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class GoogleSearch {
	public String query(WebDriver webdriver,String name) {
		//String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
		//webdriver.findElement(By.linkText("https://www.google.co.in/search?q="+name)).sendKeys(selectLinkOpeninNewTab);
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
		webdriver.findElement(By.linkText("https://www.google.co.in/search?q="+name)).sendKeys(selectLinkOpeninNewTab);
		//webdriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "n");
		//ArrayList<String> tabs2 = new ArrayList<String> (webdriver.getWindowHandles());
		//webdriver.switchTo().window(tabs2.get(1));
		//webdriver.navigate().to("https://www.google.co.in/search?q="+name);
		WebElement element= webdriver.findElement(By.tagName("h3"));
		return element.getText();
	}
}
