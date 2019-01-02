package trade;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class LoginTest {

	public void init() {
		//System.setProperty("webdriver.firefox.marionette","F:\\shahbaz\\softwares\\mozilla-selenium-driver\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver","F:\\shahbaz\\softwares\\chrome-driver\\chromedriver.exe");
	}
	
	//@Test
	public void login() {
		init();
		WebDriver driver=null;
	try {
		 driver=new ChromeDriver();
		 //driver = new FirefoxDriver();
		 Login.launch(driver);
		 Request r=(Request) deserialize(new File("tradeRequest.xml"));
		 
	} catch (JAXBException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally { 
		if(driver != null) {
			driver.close();
		}
	}
	}
	
	
	@Test
		public void testClick() {
			init();
			WebDriver driver=null;
		try {
			 driver=new ChromeDriver();
			 //driver = new FirefoxDriver();//https://www.investopedia.com/terms/s/stochasticoscillator.asp
			 driver.get("https://in.finance.yahoo.com/quote/NFLX/chart?p=NFLX#eyJpbnRlcnZhbCI6NSwicGVyaW9kaWNpdHkiOjEsInRpbWVVbml0IjoibWludXRlIiwiY2FuZGxlV2lkdGgiOjgsInZvbHVtZVVuZGVybGF5Ijp0cnVlLCJhZGoiOnRydWUsImNyb3NzaGFpciI6dHJ1ZSwiY2hhcnRUeXBlIjoiY2FuZGxlIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuOCwiZGlzcGxheSI6Ik5GTFgiLCJjaGFydE5hbWUiOiJjaGFydCIsInRvcCI6MH0sIuKAjHN0b2NoYXN0aWNz4oCMICgxNCx5KSI6eyJwZXJjZW50IjowLjIsImRpc3BsYXkiOiJTdG9jaCAoMTQpIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJ0b3AiOjM2Ni40MDAwMDAwMDAwMDAwM319LCJzZXRTcGFuIjp7fSwibGluZVdpZHRoIjoyLCJzdHJpcGVkQmFja2dyb3VkIjp0cnVlLCJldmVudHMiOnRydWUsImNvbG9yIjoiIzAwODFmMiIsImN1c3RvbVJhbmdlIjpudWxsLCJzeW1ib2xzIjpbeyJzeW1ib2wiOiJORkxYIiwic3ltYm9sT2JqZWN0Ijp7InN5bWJvbCI6Ik5GTFgifSwicGVyaW9kaWNpdHkiOjEsImludGVydmFsIjo1LCJ0aW1lVW5pdCI6Im1pbnV0ZSIsInNldFNwYW4iOnt9fV0sInN0dWRpZXMiOnsidm9sIHVuZHIiOnsidHlwZSI6InZvbCB1bmRyIiwiaW5wdXRzIjp7ImlkIjoidm9sIHVuZHIiLCJkaXNwbGF5Ijoidm9sIHVuZHIifSwib3V0cHV0cyI6eyJVcCBWb2x1bWUiOiIjMDBiMDYxIiwiRG93biBWb2x1bWUiOiIjRkYzMzNBIn0sInBhbmVsIjoiY2hhcnQiLCJwYXJhbWV0ZXJzIjp7IndpZHRoRmFjdG9yIjowLjQ1LCJjaGFydE5hbWUiOiJjaGFydCJ9fSwi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIjp7InR5cGUiOiJzdG9jaGFzdGljcyIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJTbW9vdGgiOnRydWUsImlkIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwiZGlzcGxheSI6IlN0b2NoICgxNCkifSwib3V0cHV0cyI6eyJGYXN0IjoiIzAwMDAwMCIsIlNsb3ciOiIjRkYwMDAwIn0sInBhbmVsIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwicGFyYW1ldGVycyI6eyJzdHVkeU92ZXJab25lc0VuYWJsZWQiOnRydWUsInN0dWR5T3ZlckJvdWdodFZhbHVlIjo4MCwic3R1ZHlPdmVyQm91Z2h0Q29sb3IiOiIjMDAwMDAwIiwic3R1ZHlPdmVyU29sZFZhbHVlIjoyMCwic3R1ZHlPdmVyU29sZENvbG9yIjoiIzAwMDAwMCIsImNoYXJ0TmFtZSI6ImNoYXJ0In19fX0%3D");
				
			 ClickByXY click=new ClickByXY();
			 click.click(driver,"//*[@id=\"fin-chartiq\"]/div[11]",145);
			 //Request r=(Request) deserialize(new File("tradeRequest.xml"));
			 System.out.println("done");
		
		
		}finally { 
			if(driver != null) {
				driver.close();
			}
		}
		}
	
	public Object deserialize(File file) throws JAXBException {
	    JAXBContext jaxbContext = JAXBContext.newInstance(Request.class);

	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    Request request = (Request) jaxbUnmarshaller.unmarshal(file);
	    return request;
	}
}
