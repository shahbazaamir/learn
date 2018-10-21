package org.myProject.qAndA;

import java.util.List;

import org.myProject.common.model.Question;

public class QAndAVO {
	private String userSessionId ;
	private List<String> attemptedQuestions;
	private Question nextQuestion;
	private String questionId;
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public List<String> getAttemptedQuestions() {
		return attemptedQuestions;
	}
	public void setAttemptedQuestions(List<String> attemptedQuestions) {
		this.attemptedQuestions = attemptedQuestions;
	}
	public Question getNextQuestion() {
		return nextQuestion;
	}
	public void setNextQuestion(Question nextQuestion) {
		this.nextQuestion = nextQuestion;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
}
