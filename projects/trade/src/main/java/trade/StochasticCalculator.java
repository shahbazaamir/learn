package trade;

import java.util.List;


public class StochasticCalculator {
	public void calcPercentK(List<CandleData> candleDataList,CandleData candleDataCurrent) {
		Stochastic s=new Stochastic();
		for(CandleData candleData:candleDataList) {
			if(Double.parseDouble(s.getHigh14()) < Double.parseDouble(candleData.getHigh())){
				s.setHigh14(candleData.getHigh());
			}
			if(Double.parseDouble(s.getLow14()) < Double.parseDouble(candleData.getLow())){
				s.setLow14(candleData.getLow());
			}
		}	
		s.setCurrentClose(candleDataCurrent.getClose());
		double cMinusL =(Double.parseDouble(candleDataCurrent.getClose()) - Double.parseDouble(s.getLow14()));
		double hMinusL =(Double.parseDouble(s.getHigh14()) - Double.parseDouble(s.getLow14()));
		double percentK=100*cMinusL/hMinusL;
		s.setCMinusL(cMinusL);
		s.setHMinusL(hMinusL);
		s.setPercentK(percentK);
	}
	
	public void  calcSlowK(List<Stochastic> stochList,Stochastic sCurrent) {
		double cMinusLSum = 0;
		double hMinusLSum =0;
		for(Stochastic s:stochList) {
			cMinusLSum +=s.getCMinusL();
			hMinusLSum +=s.getHMinusL();
		}
		double slowPercentK=cMinusLSum/hMinusLSum;
		sCurrent.setSlowPercentK(slowPercentK);
	}
	
	public void calcPercentD(List<Stochastic> stochList,Stochastic sCurrent) {
		double percentD=0;
		for(Stochastic s:stochList) {
			percentD+=s.getSlowPercentK();
		}
		sCurrent.setPercentD(percentD);
	}
	
}
