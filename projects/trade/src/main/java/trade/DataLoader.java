package trade;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.google.gson.Gson;

public class DataLoader {
	public Map<String,String> loadData(WebDriver webdriver,String name) {

		Task t=new Task();
		Map<String,String> priceMap=new TreeMap<String,String>();
		for(int i=1;i<=10;i++) {
			String price=t.fetchPrice(webdriver, name);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			priceMap.put(new Date().toString(), price);
		}

		return (priceMap);
	}


	public void loadStockData(WebDriver webdriver,String name)throws AWTException, InterruptedException, ParseException {
		//webdriver.get("https://in.finance..com/quote/NFLX/chart?p=NFLX#eyJpbnRlcnZhbCI6NSwicGVyaW9kaWNpdHkiOjEsInRpbWVVbml0IjoibWludXRlIiwiY2FuZGxlV2lkdGgiOjgsInZvbHVtZVVuZGVybGF5Ijp0cnVlLCJhZGoiOnRydWUsImNyb3NzaGFpciI6dHJ1ZSwiY2hhcnRUeXBlIjoiY2FuZGxlIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuOCwiZGlzcGxheSI6Ik5GTFgiLCJjaGFydE5hbWUiOiJjaGFydCIsInRvcCI6MH0sIuKAjHN0b2NoYXN0aWNz4oCMICgxNCx5KSI6eyJwZXJjZW50IjowLjIsImRpc3BsYXkiOiJTdG9jaCAoMTQpIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJ0b3AiOjM2Ni40MDAwMDAwMDAwMDAwM319LCJzZXRTcGFuIjp7fSwibGluZVdpZHRoIjoyLCJzdHJpcGVkQmFja2dyb3VkIjp0cnVlLCJldmVudHMiOnRydWUsImNvbG9yIjoiIzAwODFmMiIsImN1c3RvbVJhbmdlIjpudWxsLCJzeW1ib2xzIjpbeyJzeW1ib2wiOiJORkxYIiwic3ltYm9sT2JqZWN0Ijp7InN5bWJvbCI6Ik5GTFgifSwicGVyaW9kaWNpdHkiOjEsImludGVydmFsIjo1LCJ0aW1lVW5pdCI6Im1pbnV0ZSIsInNldFNwYW4iOnt9fV0sInN0dWRpZXMiOnsidm9sIHVuZHIiOnsidHlwZSI6InZvbCB1bmRyIiwiaW5wdXRzIjp7ImlkIjoidm9sIHVuZHIiLCJkaXNwbGF5Ijoidm9sIHVuZHIifSwib3V0cHV0cyI6eyJVcCBWb2x1bWUiOiIjMDBiMDYxIiwiRG93biBWb2x1bWUiOiIjRkYzMzNBIn0sInBhbmVsIjoiY2hhcnQiLCJwYXJhbWV0ZXJzIjp7IndpZHRoRmFjdG9yIjowLjQ1LCJjaGFydE5hbWUiOiJjaGFydCJ9fSwi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIjp7InR5cGUiOiJzdG9jaGFzdGljcyIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJTbW9vdGgiOnRydWUsImlkIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwiZGlzcGxheSI6IlN0b2NoICgxNCkifSwib3V0cHV0cyI6eyJGYXN0IjoiIzAwMDAwMCIsIlNsb3ciOiIjRkYwMDAwIn0sInBhbmVsIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwicGFyYW1ldGVycyI6eyJzdHVkeU92ZXJab25lc0VuYWJsZWQiOnRydWUsInN0dWR5T3ZlckJvdWdodFZhbHVlIjo4MCwic3R1ZHlPdmVyQm91Z2h0Q29sb3IiOiIjMDAwMDAwIiwic3R1ZHlPdmVyU29sZFZhbHVlIjoyMCwic3R1ZHlPdmVyU29sZENvbG9yIjoiIzAwMDAwMCIsImNoYXJ0TmFtZSI6ImNoYXJ0In19fX0%3D");
		  webdriver.get("https://in.finance..com/quote/TATASTEEL.NS/chart?p=TATASTEEL.NS#eyJpbnRlcnZhbCI6NSwicGVyaW9kaWNpdHkiOjEsInRpbWVVbml0IjoibWludXRlIiwiY2FuZGxlV2lkdGgiOjgsInZvbHVtZVVuZGVybGF5Ijp0cnVlLCJhZGoiOnRydWUsImNyb3NzaGFpciI6dHJ1ZSwiY2hhcnRUeXBlIjoiY2FuZGxlIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuOCwiZGlzcGxheSI6IlRBVEFTVEVFTC5OUyIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwidG9wIjowfSwi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIjp7InBlcmNlbnQiOjAuMiwiZGlzcGxheSI6IlN0b2NoICgxNCkiLCJjaGFydE5hbWUiOiJjaGFydCIsInRvcCI6MzY2LjQwMDAwMDAwMDAwMDAzfX0sInNldFNwYW4iOnt9LCJsaW5lV2lkdGgiOjIsInN0cmlwZWRCYWNrZ3JvdWQiOnRydWUsImV2ZW50cyI6dHJ1ZSwiY29sb3IiOiIjMDA4MWYyIiwiY3VzdG9tUmFuZ2UiOm51bGwsInN5bWJvbHMiOlt7InN5bWJvbCI6IlRBVEFTVEVFTC5OUyIsInN5bWJvbE9iamVjdCI6eyJzeW1ib2wiOiJUQVRBU1RFRUwuTlMifSwicGVyaW9kaWNpdHkiOjEsImludGVydmFsIjo1LCJ0aW1lVW5pdCI6Im1pbnV0ZSIsInNldFNwYW4iOnt9fV0sInJhbmdlIjp7fSwic3R1ZGllcyI6eyJ2b2wgdW5kciI6eyJ0eXBlIjoidm9sIHVuZHIiLCJpbnB1dHMiOnsiaWQiOiJ2b2wgdW5kciIsImRpc3BsYXkiOiJ2b2wgdW5kciJ9LCJvdXRwdXRzIjp7IlVwIFZvbHVtZSI6IiMwMGIwNjEiLCJEb3duIFZvbHVtZSI6IiNGRjMzM0EifSwicGFuZWwiOiJjaGFydCIsInBhcmFtZXRlcnMiOnsid2lkdGhGYWN0b3IiOjAuNDUsImNoYXJ0TmFtZSI6ImNoYXJ0In19LCLigIxzdG9jaGFzdGljc%2BKAjCAoMTQseSkiOnsidHlwZSI6InN0b2NoYXN0aWNzIiwiaW5wdXRzIjp7IlBlcmlvZCI6MTQsIlNtb290aCI6dHJ1ZSwiaWQiOiLigIxzdG9jaGFzdGljc%2BKAjCAoMTQseSkiLCJkaXNwbGF5IjoiU3RvY2ggKDE0KSJ9LCJvdXRwdXRzIjp7IkZhc3QiOiIjMDAwMDAwIiwiU2xvdyI6IiNGRjAwMDAifSwicGFuZWwiOiLigIxzdG9jaGFzdGljc%2BKAjCAoMTQseSkiLCJwYXJhbWV0ZXJzIjp7InN0dWR5T3ZlclpvbmVzRW5hYmxlZCI6dHJ1ZSwic3R1ZHlPdmVyQm91Z2h0VmFsdWUiOjgwLCJzdHVkeU92ZXJCb3VnaHRDb2xvciI6IiMwMDAwMDAiLCJzdHVkeU92ZXJTb2xkVmFsdWUiOjIwLCJzdHVkeU92ZXJTb2xkQ29sb3IiOiIjMDAwMDAwIiwiY2hhcnROYW1lIjoiY2hhcnQifX19fQ%3D%3D");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		Date d=sdf.parse("201805111525");
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		Thread.sleep(5000);
		int x=1320;
		int y=400;
		WebElement element= webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]"));
		element.click();
		Thread.sleep(2000);
		Robot robot = new Robot();
		List<StockData> candleDataList=new ArrayList<>();
		CandleData candleData=null;
		StockData stockData=null;
		for(int i=1;i<=30;i++) {
			x-=8;
			robot.mouseMove(x,y);
			Thread.sleep(1000);
			candleData=fetchCandleData(webdriver);
			stockData=new StockData();
			if(candleData!=null) {
				stockData.setCandleData(candleData);
				stockData.setTime(cal.getTime());
				cal.add(Calendar.MINUTE, -5);
				candleDataList.add(stockData);
			}
			Thread.sleep(1000);
		}
		Gson gson =new Gson();
		System.out.println( gson.toJson(candleDataList));
	}


	public CandleData fetchCandleData(WebDriver webdriver)  {
		CandleData candleData=new CandleData();

		WebElement element= null;
		try{//*[@id="High"]
			element=webdriver.findElement(By.xpath("//*[@id=\"Open\"]"));
			try{
				candleData.setOpen( element.getText().substring(5));
			}catch(StringIndexOutOfBoundsException e) {
				candleData.setOpen( "0");
			}
			element= webdriver.findElement(By.xpath("//*[@id=\"High\"]"));
			try{
				candleData.setHigh(element.getText().substring(5));
			}catch(StringIndexOutOfBoundsException e) {
				candleData.setOpen( "0");
			}
			element= webdriver.findElement(By.xpath("//*[@id=\"Low\"]"));
			try{
				candleData.setLow(element.getText().substring(4));
			}catch(StringIndexOutOfBoundsException e) {
				candleData.setOpen( "0");
			}
			element= webdriver.findElement(By.xpath("//*[@id=\"Close\"]"));
			try{
				candleData.setClose(element.getText().substring(6));
			}catch(StringIndexOutOfBoundsException e) {
				candleData.setOpen( "0");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			candleData=null;
		}
		return candleData;

	}
	public Map<String,String> loadStoch2(WebDriver webdriver,String name) {

		Map<String,String> priceMap=new TreeMap<String,String>();
		webdriver.get("https://in.finance.yahoo.com/quote/NFLX/chart?p=NFLX#eyJpbnRlcnZhbCI6NSwicGVyaW9kaWNpdHkiOjEsInRpbWVVbml0IjoibWludXRlIiwiY2FuZGxlV2lkdGgiOjgsInZvbHVtZVVuZGVybGF5Ijp0cnVlLCJhZGoiOnRydWUsImNyb3NzaGFpciI6dHJ1ZSwiY2hhcnRUeXBlIjoiY2FuZGxlIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuOCwiZGlzcGxheSI6Ik5GTFgiLCJjaGFydE5hbWUiOiJjaGFydCIsInRvcCI6MH0sIuKAjHN0b2NoYXN0aWNz4oCMICgxNCx5KSI6eyJwZXJjZW50IjowLjIsImRpc3BsYXkiOiJTdG9jaCAoMTQpIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJ0b3AiOjM2Ni40MDAwMDAwMDAwMDAwM319LCJzZXRTcGFuIjp7fSwibGluZVdpZHRoIjoyLCJzdHJpcGVkQmFja2dyb3VkIjp0cnVlLCJldmVudHMiOnRydWUsImNvbG9yIjoiIzAwODFmMiIsImN1c3RvbVJhbmdlIjpudWxsLCJzeW1ib2xzIjpbeyJzeW1ib2wiOiJORkxYIiwic3ltYm9sT2JqZWN0Ijp7InN5bWJvbCI6Ik5GTFgifSwicGVyaW9kaWNpdHkiOjEsImludGVydmFsIjo1LCJ0aW1lVW5pdCI6Im1pbnV0ZSIsInNldFNwYW4iOnt9fV0sInN0dWRpZXMiOnsidm9sIHVuZHIiOnsidHlwZSI6InZvbCB1bmRyIiwiaW5wdXRzIjp7ImlkIjoidm9sIHVuZHIiLCJkaXNwbGF5Ijoidm9sIHVuZHIifSwib3V0cHV0cyI6eyJVcCBWb2x1bWUiOiIjMDBiMDYxIiwiRG93biBWb2x1bWUiOiIjRkYzMzNBIn0sInBhbmVsIjoiY2hhcnQiLCJwYXJhbWV0ZXJzIjp7IndpZHRoRmFjdG9yIjowLjQ1LCJjaGFydE5hbWUiOiJjaGFydCJ9fSwi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIjp7InR5cGUiOiJzdG9jaGFzdGljcyIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJTbW9vdGgiOnRydWUsImlkIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwiZGlzcGxheSI6IlN0b2NoICgxNCkifSwib3V0cHV0cyI6eyJGYXN0IjoiIzAwMDAwMCIsIlNsb3ciOiIjRkYwMDAwIn0sInBhbmVsIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LHkpIiwicGFyYW1ldGVycyI6eyJzdHVkeU92ZXJab25lc0VuYWJsZWQiOnRydWUsInN0dWR5T3ZlckJvdWdodFZhbHVlIjo4MCwic3R1ZHlPdmVyQm91Z2h0Q29sb3IiOiIjMDAwMDAwIiwic3R1ZHlPdmVyU29sZFZhbHVlIjoyMCwic3R1ZHlPdmVyU29sZENvbG9yIjoiIzAwMDAwMCIsImNoYXJ0TmFtZSI6ImNoYXJ0In19fX0%3D");
		WebElement element= webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]"));//*[@id="fin-chartiq"]/div[11]
		Actions builder = new Actions(webdriver); 
		//Robot robot = new Robot();

		//Point location ratingElementDiv[i].getLocation(); 
		//builder.moveToElement(element, 30000, 30000).click();
		builder.moveByOffset(900, 900).click().build().perform();//(webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]"))).click()
		for(int i=1;i<=10;i++) {
			String price=null;
			try{// webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]")).getText()
				element= webdriver.findElement(By.xpath("//*[@id=\"fin-chartiq\"]/div[11]"));
			}catch(Exception e) {
				e.printStackTrace();
			}
			price= element.getText();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			priceMap.put(new Date().toString(), price);
		}

		return (priceMap);
	}
}
