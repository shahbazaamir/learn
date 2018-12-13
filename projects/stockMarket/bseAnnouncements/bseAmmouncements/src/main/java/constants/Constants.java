package constants;

public final class Constants {

	private Constants(){
		
	}
	
	public static final String CHROME_DRIVER_PATH="chromeDriverPath";
	public static final String CHROME_DRIVER_PATH_KEY="webdriver.chrome.driver";
	
	public static final String FIREFOX_DRIVER_PATH="firefoxDriverPath";
	public static final String FIREFOX_DRIVER_PATH_KEY="webdriver.firefox.marionette";
	
	public static final String SCREENSHOT_PATH_KEY="screenshot.path";
	public static final String EMAIL_FROM_ADD = "fromEmail";
	public static final String EMAIL_FROM_DATA = "fromEmailData";
	public static final String EMAIL_TO_ADD = "toEmail";
	
	
	public static final long SECOND = 1000;
	public static final long MINUTE = 60000;
	public static final long HOUR = 3600000;
	
	public static final long NOTIF_INC = MINUTE;
	public static final long WEEKDAY_NOTIF_DURATION =8*HOUR;
	public static final long ONDEMAND_NOTIF_DURATION =1*MINUTE;
	public static final String SEARCH_TEXT [] = {"buy","Buy","BUY","dividend","Dividend"};
	public static final String SEARCH_TEXT_BUY [] = {"buy","Buy","BUY"};
	public static final String SEARCH_TEXT_DIV [] = {"dividend","Dividend"};
	
	public static final String BSE_CORP_ANN_URL="bseCorpAnnUrl";
	
	public static final String NEXT_LINK_XPATH="//*[@id=\"ctl00_ContentPlaceHolder1_lblNext\"]/a";
	
	public static final String MAIL_HEADING="<h1>Buyback Issues</h1><table>";
	public static final String NUMBER_ROW_START="<tr style='background-color: #C6F399'><td>";
	public static final String NUMBER_ROW_END="</td></tr>";
	public static final String LEADING_TR_START="<tr style='background-color: #A2F6A6'>";
	public static final String LEADING_TR_END="</tr>";
	public static final String LAGGING_TR_START="<tr style='background-color: #C5CBF3'>";
	public static final String LAGGING_TR_END="</tr>";
	public static final String CURRENT_TR_START="<tr style='background-color: #10EFDE'>";
	public static final String CURRENT_TR_END="</tr>";
	public static final String ADDITIONAL_TR_START="<tr style='background-color: #F990A5'>";
	public static final String ADDITIONAL_TR_END="</tr>";
	public static final String MAIL_SUBJECT="Dividend and Buyback Alert";
	public static final String MAIL_SUBJECT_BUY="BuyBack Alert";
	public static final String MAIL_SUBJECT_DIV="Dividend Alert";
	
	
	public static final String MAIL_END="</table>";
	public static final String BLANK_STRING="";
	
	public static final String NEXT_LINE="\n";
	
	public static final int MAX_PAGES =200;
	
}
