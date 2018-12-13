package main;

import notif.NotifJob;
import static constants.Constants.*;

public class WeekdayJob {
public static void main(String[] args) {
	//NotifJob notifJob=new NotifJob();
	NotifJob.startJob(NOTIF_INC, WEEKDAY_NOTIF_DURATION);
}
}
