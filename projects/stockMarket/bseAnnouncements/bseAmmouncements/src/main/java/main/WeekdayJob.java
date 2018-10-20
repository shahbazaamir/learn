package main;

import java.util.Timer;
import java.util.TimerTask;

import bse.WeekdayLoadAnnManager;
import log.LogWrapper;
import timer.WeekDayLoadATimerTask;

import static constants.Constants.*;

public class WeekdayJob {
	
	public static void main(String[] args) {
		WeekdayLoadAnnManager.loadFirstAnn();
		TimerTask timerTask = new WeekDayLoadATimerTask();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, NOTIF_INC );
        LogWrapper.info("TimerTask started");
        
        try {
            Thread.sleep(NOTIF_DURATION);
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
