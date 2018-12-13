package util;

import static constants.Constants.*;

import java.util.Arrays;

public class CommonUtil {

	public static String getSubject(String searchText) {
		if(Arrays.asList( SEARCH_TEXT_BUY).contains(searchText)) {
			return MAIL_SUBJECT_BUY;
		}else {
			return MAIL_SUBJECT_DIV;
		}
		
	}
}
