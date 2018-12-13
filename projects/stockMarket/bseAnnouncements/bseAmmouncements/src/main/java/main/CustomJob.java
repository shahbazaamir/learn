package main;


import notif.NotifJob;

public class CustomJob {
	
	public static void main(String[] args) {
		
	
	NotifJob.startJob(Long.parseLong(args[0]), Long.parseLong(args[1]));
	}
}
