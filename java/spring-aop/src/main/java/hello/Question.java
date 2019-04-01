package hello;

import org.springframework.data.annotation.Id;

public class Question {


	@Id
	public String id;


	
	private String subjectId;
	private String questionId;
	private String questionDesc;
	public Question(){

	}

	public Question(String subjectId, String questionId,
			String questionDesc) {
		super();
		this.subjectId = subjectId;
		this.questionId = questionId;
		this.questionDesc = questionDesc;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

