package main;

import static constants.Constants.NOTIF_INC;
import static constants.Constants.ONDEMAND_NOTIF_DURATION;

import notif.NotifJob;

public class OndemandJob {

	public static void main(String[] args) {
		NotifJob.startJob(NOTIF_INC, ONDEMAND_NOTIF_DURATION);
	}
}
