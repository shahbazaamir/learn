   
   package org.myProject.service.question;

import java.io.Serializable;

public class TopicDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String topicId;
	private List<String> parent;
	private List<String> child;
	private String topicDesc;
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
  }
