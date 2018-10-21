package org.myProject.errorAnalyser;

import java.io.Serializable;

public class ErrorDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMessageRegex;
	private String exceptionClass;
	private Throwable cause;
	private String errorReason;
	private String errorType;
	public String getExceptionMessageRegex() {
		return exceptionMessageRegex;
	}
	public void setExceptionMessageRegex(String exceptionMessageRegex) {
		this.exceptionMessageRegex = exceptionMessageRegex;
	}
	public String getExceptionClass() {
		return exceptionClass;
	}
	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
	public Throwable getCause() {
		return cause;
	}
	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
}
