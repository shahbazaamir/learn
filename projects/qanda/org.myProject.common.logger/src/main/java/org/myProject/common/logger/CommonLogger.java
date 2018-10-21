package org.myProject.common.logger;

public class CommonLogger {
	public static boolean isInfo(){
		return true;
	}
	public static void info(Object o){
		System.out.println(o);
	}
	public static void errorTrace(Throwable e){
		e.printStackTrace();
	}
}
