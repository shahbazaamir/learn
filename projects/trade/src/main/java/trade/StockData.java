package trade;

import java.util.Date;

public class StockData {
	
	private Date time;
	
	private CandleData candleData;
	
	private Stochastic stochastic;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public CandleData getCandleData() {
		return candleData;
	}

	public void setCandleData(CandleData candleData) {
		this.candleData = candleData;
	}

	public Stochastic getStochastic() {
		return stochastic;
	}

	public void setStochastic(Stochastic stochastic) {
		this.stochastic = stochastic;
	}
	
	
}
