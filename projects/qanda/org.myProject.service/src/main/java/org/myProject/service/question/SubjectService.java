package org.myProject.service.question;

public interface SubjectService {

	
	public void saveSubjectDetails(SubjectDetail subjectDetail) throws QuestionException;
	public String getSubjectDetail( SubjectDetail subjectDetail) throws QuestionException;
	public String getAllSubject( ) throws QuestionException;
}
