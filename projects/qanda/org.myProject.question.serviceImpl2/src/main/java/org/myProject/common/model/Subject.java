package org.myProject.common.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SUBJECT database table.
 * 
 */
@Entity
@Table(name="SUBJECT")
@NamedQuery(name="Subject.findAll", query="SELECT s FROM Subject s")
public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SUBJECT_CODE")
	private String subjectCode;
	

	@Column(name="SUBJECT_DESC")
	private String subjectDesc;

	       
	public Subject() {
	}

	public String getSubjectDesc() {
		return this.subjectDesc;
	}

	public void setSubjectDesc(String subjectDesc) {
		this.subjectDesc = subjectDesc;
	}

	
	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	

}