package notif;

import java.util.Timer;
import java.util.TimerTask;

import bse.WeekdayLoadAnnManager;
import log.LogWrapper;
import timer.WeekDayLoadATimerTask;



public class NotifJob {
	
	public static void startJob(long interval,long duration) {
		WeekdayLoadAnnManager.loadFirstAnn();
		TimerTask timerTask = new WeekDayLoadATimerTask();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, interval );
        LogWrapper.info("TimerTask started");
        
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        	LogWrapper.fatal(e);
        	LogWrapper.fatal("TimerTask interrupted 1");
        }
        timer.cancel();
        LogWrapper.info("TimerTask cancelled");
        WeekdayLoadAnnManager.loadLast();
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
        	LogWrapper.fatal("TimerTask interrupted 2");
        	LogWrapper.fatal(e);
        }
		
	}
}
