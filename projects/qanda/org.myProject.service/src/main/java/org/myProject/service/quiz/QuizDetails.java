package org.myProject.service.quiz;

import java.io.Serializable;

public class QuizDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6576240235338588315L;
	private String testId;
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}

}
