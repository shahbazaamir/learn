package org.myProject.errorAnalyser;

/**
 * Please configure database table Error_Details
 * @author shahbaz.aamir
 *
 */
public class ErrorAnalyser {
	public static final String DB_DOWN="Exception executing isMaster command on localhost/127.0.0.1:27017: java.io.IOException: couldn't connect to [localhost/127.0.0.1:27017] bc:java.net.ConnectException: Connection refused: connectException executing isMaster command on localhost/127.0.0.1:27017: java.io.IOException: couldn't connect to [localhost/127.0.0.1:27017] bc:java.net.ConnectException: Connection refused: connectException executing isMaster command on localhost/127.0.0.1:27017: java.io.IOException: couldn't connect to [localhost/127.0.0.1:27017] bc:java.net.ConnectException: Connection refused: connectException executing isMaster command on localhost/127.0.0.1:27017: java.io.IOException: couldn't connect to [localhost/127.0.0.1:27017] bc:java.net.ConnectException: Connection refused: connectException executing isMaster command on localhost/127.0.0.1:27017: java.io.IOException: couldn't connect to [localhost/127.0.0.1:27017] bc:java.net.ConnectException: Connection refused: connect";
	public String getErrorReason(ErrorDetails errorDetails){
		if(errorDetails.getExceptionMessageRegex().matches(DB_DOWN)){
			return "DB Down";
		}
		return null;
	}
}
