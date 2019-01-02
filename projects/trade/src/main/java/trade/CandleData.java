package trade;

import java.io.Serializable;

public class CandleData implements Serializable{

	private String high;
	private String low;
	private String open;
	private String close;
	private String volume;
	
	
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	
	
}
