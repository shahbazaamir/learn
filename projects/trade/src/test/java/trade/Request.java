package trade;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request implements Serializable {
	
	private String scrip;
	private String buySell;
	private long quantity;
	public String getScrip() {
		return scrip;
	}
	public void setScrip(String scrip) {
		this.scrip = scrip;
	}
	public String getBuySell() {
		return buySell;
	}
	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	

	
	
	
}
