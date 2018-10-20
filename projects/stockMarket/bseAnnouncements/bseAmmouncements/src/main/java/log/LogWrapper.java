package log;

public class LogWrapper {
	public static final boolean isDebug=false;
	
	public static void fatal(Exception e) {
		e.printStackTrace();
	}
	
	public static void fatal(Object o) {
		System.out.println(o);
	}
	
	public static void info(Object o) {
		System.out.println(o);
	}
	
	
	public static void debug(Object o) {
		System.out.println(o);
	}
	
}
