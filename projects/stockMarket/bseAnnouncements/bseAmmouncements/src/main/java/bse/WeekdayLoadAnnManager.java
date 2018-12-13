package bse;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import config.Config;
import constants.Constants;
import log.LogWrapper;
import mail.SendMail;
import store.Store;
import util.CommonUtil;

import static constants.Constants.*;
import static log.LogWrapper.isDebug;
public class WeekdayLoadAnnManager {



	public static void loadFirstAnn() {
		System.setProperty(CHROME_DRIVER_PATH_KEY,Config.getConfig(CHROME_DRIVER_PATH));
		try{
			Store.driver=new ChromeDriver();
			Store.driver.get(Config.getConfig(BSE_CORP_ANN_URL));
			String mailBody=MAIL_HEADING;
			int matches=0;
			for(int pages=1;pages<=MAX_PAGES;pages++) {
				for(String searchText:SEARCH_TEXT) {
					for(WebElement notice :  Store.driver.findElements(By.xpath("//*[contains(text(), '"+searchText+"')]"))) {
						if(isDebug)LogWrapper.debug("notice:"+notice.getText());
						if(BLANK_STRING.equals(notice.getText())) continue;
						if(Store.notices.contains(notice.getText()))continue;
						String filename=takeSnapShot(Store.driver,notice);
						SendMail.sendData(searchText,filename,CommonUtil.getSubject(searchText));
						Store.notices.add(notice.getText());
						++matches;
						mailBody=formMailBody(mailBody, notice, matches);
					}
				}
				try {
					Store.driver.findElement(By.xpath(NEXT_LINK_XPATH)).click();
					Thread.sleep(2000);
				}catch(Exception e) {
					e.printStackTrace();
					pages=MAX_PAGES+1;
				}
			}
			if( matches>0) {
				mailBody+=MAIL_END;
				LogWrapper.info(mailBody);
				SendMail.sendData(mailBody,MAIL_SUBJECT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static String formMailBody(String mailBody,WebElement notice,int matches) {
		mailBody+=  ( NUMBER_ROW_START+matches + ". "+NUMBER_ROW_END)    ;
		mailBody+=LAGGING_TR_START;
		List<WebElement> list=notice.findElements(By.xpath("..//preceding-sibling::tr"));
		if(list.size()>0) {
			String preceding=list.get(list.size()-1).getAttribute("innerHTML");
			mailBody+=preceding;
		}else{
			mailBody+="<td>NONE</td>";
		}
		mailBody+=LAGGING_TR_END;
		mailBody+=CURRENT_TR_START;
		mailBody+=((notice.findElement(By.xpath(".."))).getAttribute("innerHTML"));
		mailBody+=CURRENT_TR_END;
		List<WebElement> list2=notice.findElements(By.xpath("..//following-sibling::tr"));
		if(list2.size()>0) {
			String leading=list2.get(0).getAttribute("innerHTML");
			mailBody+=LEADING_TR_START;
			mailBody+=leading;
			mailBody+=LEADING_TR_END;
		}
		if(list2.size()>1) {
			String additional=list2.get(1).getAttribute("innerHTML");
			mailBody+=ADDITIONAL_TR_START;
			mailBody+=additional;
			mailBody+=ADDITIONAL_TR_END;
		}
		return mailBody;
	}

	public static void loadNext() {
		Store.driver.navigate().refresh();
		String mailBody=MAIL_HEADING;
		int matches=0;
		for(String searchText:SEARCH_TEXT) {
			for(WebElement notice :  Store.driver.findElements(By.xpath("//*[contains(text(), '"+searchText+"')]"))) {
				if(isDebug)LogWrapper.debug("notice:"+notice.getText());

				if(BLANK_STRING.equals(notice.getText())) continue;
				if(Store.notices.contains(notice.getText()))continue;
				try {
					String filename=takeSnapShot(Store.driver,notice);
					SendMail.sendData(searchText,filename,CommonUtil.getSubject(searchText));
				}catch(Exception e) {
					LogWrapper.fatal("exception while sending screenshot");
					LogWrapper.fatal(e);
				}
				Store.notices.add(notice.getText());
				++matches;
				mailBody=formMailBody(mailBody, notice, matches);
			}
		}
		if( matches>0) {
			mailBody+=MAIL_END;
			SendMail.sendData(mailBody,MAIL_SUBJECT);
		}
	}

	public static void loadLast() {
		try {
			if(Store.driver != null) {
				Store.driver.close();
			}
		} catch (Exception e) {
			LogWrapper.fatal("exception while closing driver");
			LogWrapper.fatal(e);
		} 
	}
	public static String takeSnapShot(WebDriver webdriver, WebElement element) throws Exception{

		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		String filename=Config.getConfig(SCREENSHOT_PATH_KEY)+ "Buyback"+   dateFormat.format(date)+".jpg";
		Actions actions = new Actions(webdriver);
		actions.moveToElement(element);
		actions.perform();
		TakesScreenshot scrShot =((TakesScreenshot)webdriver);


		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);


		File DestFile=new File(filename);


		FileUtils.copyFile(SrcFile, DestFile);
		return filename;

	}

}
