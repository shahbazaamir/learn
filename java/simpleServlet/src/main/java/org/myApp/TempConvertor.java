package org.myApp;

public class TempConvertor
{
	public static String farenhietToCelcius(float farenhiet){
		float celcius =(farenhiet -32 )*5/9;
		return String.valueOf(celcius);
	}
}