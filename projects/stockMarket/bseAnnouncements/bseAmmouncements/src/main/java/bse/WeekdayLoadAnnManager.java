package bse;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import config.Config;
import constants.Constants;
import log.LogWrapper;
import mail.SendMail;
import store.Store;

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
				SendMail.sendData(mailBody);
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
			mailBody+="NONE";
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
				Store.notices.add(notice.getText());
				++matches;
				mailBody=formMailBody(mailBody, notice, matches);
			}
		}
		if( matches>0) {
			mailBody+=MAIL_END;
			SendMail.sendData(mailBody);
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


}
