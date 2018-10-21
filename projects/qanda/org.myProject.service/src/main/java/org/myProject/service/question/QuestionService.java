package org.myProject.service.question;

public interface QuestionService {
	public void saveQuestionDetails(QuestionDetail questionDetail) throws QuestionException;
	public String getQuestionDetails( ) throws QuestionException;
	public String getQuestionDetail( QuestionDetail questionDetail) throws QuestionException;
	public boolean isQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException ;
	public boolean updateQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException;
	public String getQuestionDetailsBySubject( String subjectId)
			throws QuestionException ;
	
	
}
