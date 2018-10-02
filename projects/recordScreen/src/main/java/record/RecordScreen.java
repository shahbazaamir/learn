package record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import atu.testrecorder.ATUTestRecorder;


public class RecordScreen {
	static ATUTestRecorder recorder;
	 public static void start() throws Exception {
	  DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
	  Date date = new Date();
	  //Created object of ATUTestRecorder
	  //Provide path to store videos and file name format.
	  recorder = new ATUTestRecorder("C:\\tmp\\selenium\\","TestVideo-"+dateFormat.format(date),false);
	  //To start video recording.
	  recorder.start();  
	  
	 }
	
	
	
	
	public static void stop() throws Exception {
		
		
		recorder.stop();
			
	}
}
