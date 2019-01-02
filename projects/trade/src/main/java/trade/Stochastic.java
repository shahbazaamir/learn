package trade;

import java.io.Serializable;

public class Stochastic implements Serializable{
	private String high14;
	private String low14;
	private String currentClose;
	private double percentK;
	private double slowPercentK;
	private double percentD;
	
	
	private double cMinusL;
	private double hMinusL;
	
	
	public double getCMinusL() {
		return cMinusL;
	}
	public void setCMinusL(double cMinusL) {
		this.cMinusL = cMinusL;
	}
	public double getHMinusL() {
		return hMinusL;
	}
	public void setHMinusL(double hMinusL) {
		this.hMinusL = hMinusL;
	}
	public String getHigh14() {
		return high14;
	}
	public void setHigh14(String high14) {
		this.high14 = high14;
	}
	public String getLow14() {
		return low14;
	}
	public void setLow14(String low14) {
		this.low14 = low14;
	}
	public String getCurrentClose() {
		return currentClose;
	}
	public void setCurrentClose(String currentClose) {
		this.currentClose = currentClose;
	}
	public double getPercentK() {
		return percentK;
	}
	public void setPercentK(double percentK) {
		this.percentK = percentK;
	}
	public double getSlowPercentK() {
		return slowPercentK;
	}
	public void setSlowPercentK(double slowPercentK) {
		this.slowPercentK = slowPercentK;
	}
	public double getPercentD() {
		return percentD;
	}
	public void setPercentD(double percentD) {
		this.percentD = percentD;
	}
	
}
