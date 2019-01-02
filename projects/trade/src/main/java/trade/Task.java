package trade;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class Task {

	public String fetchPrice2(WebDriver webdriver,String name) {
		Scripts s=Scripts.valueOf(name);
		
		webdriver.get(s.getUrl());
		//Nse_Prc_tick
		WebElement element= webdriver.findElement(By.id("Nse_Prc_tick"));
		return element.getText();
	}
	
	public String fetchPrice(WebDriver webdriver,String name) {
		Scripts s=Scripts.valueOf(name);
		webdriver.get(s.getUrl());
		WebElement element=null;//*[@id="fin-chartiq"]/div[11]
		try{// webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]")).getText()
			 element= webdriver.findElement(By.xpath("//*[@id=\"quote-header-info\"]/div[3]/div/div/span[1]"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return element.getText();
	}
	
	public String fetchStoch(WebDriver webdriver,String name) {
		Scripts s=Scripts.valueOf(name);
		webdriver.get(s.getUrl());
		WebElement element=null;//*[@id="fin-chartiq"]/div[11]
		try{// webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]")).getText()
			 element= webdriver.findElement(By.xpath("//*[@id=\"quote-header-info\"]/div[3]/div/div/span[1]"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return element.getText();
	}
}
