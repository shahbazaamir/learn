package org.myProject.service.question;

import java.io.Serializable;

public class QuestionDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subjectId;
	private String questionId;
	private String questionDesc;
	public QuestionDetail(){
		
	}
	
	public QuestionDetail(String subjectId, String questionId,
			String questionDesc) {
		super();
		this.subjectId = subjectId;
		this.questionId = questionId;
		this.questionDesc = questionDesc;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionDesc() {
		return questionDesc;
	}
	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}
	
	
}
