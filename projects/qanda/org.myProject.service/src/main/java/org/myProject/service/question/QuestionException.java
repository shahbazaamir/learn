package org.myProject.service.question;

public class QuestionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
