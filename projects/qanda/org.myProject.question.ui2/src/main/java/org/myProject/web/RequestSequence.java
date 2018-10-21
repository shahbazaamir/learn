package org.myProject.web;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestSequence {
	private static int nextSequence;
	public static String getNextSequence(){
		NumberFormat nf= NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumIntegerDigits(4);
		Date d=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
			d=new Date();
		String currMill=sdf.format(d);
		return currMill+nf.format(++nextSequence);
	}
	
}
