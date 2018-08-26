package bse;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import config.Config;
import constants.Constants;
import mail.SendMail;

public class LoadAnnouncement {

	public static void main(String[] args) {
		System.setProperty(Constants.CHROME_DRIVER_PATH_KEY,Config.getConfig(Constants.CHROME_DRIVER_PATH));
		WebDriver driver =null;
		try{
			driver=new ChromeDriver();
			driver.get("https://www.bseindia.com/corporates/ann.aspx?expandable=0");
			//Thread.sleep(2000);
			Thread.sleep(2000);
			List<String> notices=new ArrayList<String>() ;
			String mailBody="BuyBack Issues";
			int i=0;
			for(WebElement notice :  driver.findElements(By.xpath("//*[contains(text(), 'otice')]"))) {
				notices.add(notice.getText());
				mailBody+="\n";
				mailBody+= (++i + ". ");
				mailBody+=(notice.getText());
			}
			System.out.println(notices);
			SendMail.sendData(mailBody);
			//(driver.findElement(By.xpath("//*[contains(text(), 'Notice')]"))).findElement(By.xpath("..")).findElement(By.xpath("..")).getText();  
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
			if(driver != null) {
				driver.close();
			}
		}
	}
	
}
