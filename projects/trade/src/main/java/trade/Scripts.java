package trade;

public enum Scripts {
	HDFCBANK("http://www.moneycontrol.com/india/stockpricequote/banks-private-sector/hdfcbank/HDF01"),
	ICICIBANK("http://www.moneycontrol.com/india/stockpricequote/banks-private-sector/icicibank/ICI02"),
	TATASTEEL("http://www.moneycontrol.com/india/stockpricequote/steel-large/tatasteel/TIS"),
	NETFLIX("https://in.finance.yahoo.com/quote/NFLX/analysis?p=NFLX");
	private Scripts(String url) {
		this.url=url;
	}
	private String url;
	
	public String getUrl() {
		return url;
	}
}
