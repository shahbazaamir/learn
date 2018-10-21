package org.myProject.common.web;

public class WebRouterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WebRouterException() {
        super();
    }
	public WebRouterException(String message) {
        super(message);
    }

   
    public WebRouterException(String message, Throwable cause) {
        super(message, cause);
    }
}
