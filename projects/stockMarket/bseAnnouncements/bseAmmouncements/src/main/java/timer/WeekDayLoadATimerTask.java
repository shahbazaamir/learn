package timer;



import java.util.Date;
import java.util.TimerTask;

import bse.WeekdayLoadAnnManager;

public class WeekDayLoadATimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("Timer task started at:"+new Date());
        WeekdayLoadAnnManager.loadNext();
        System.out.println("Timer task finished at:"+new Date());
    }

    
}
