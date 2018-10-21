package org.myProject.question.serviceImpl;

import org.myProject.service.quiz.QuizDetails;
import org.myProject.service.quiz.QuizException;
import org.myProject.service.quiz.QuizService;

public class QuizServiceImpl implements QuizService{

	public QuizDetails startQuiz(QuizDetails quizDetails) throws QuizException {
		// TODO Auto-generated method stub
		generateTestID(quizDetails);
		
		throw new UnsupportedOperationException();
	}
	private void generateTestID(QuizDetails quizDetails) {
		throw new UnsupportedOperationException();
		
	}
	/**
	 * //get a question
		//check if quetion is not in attemted questions
		//add the question to attempted question
		//get options for the question
		//get answer for the question
	 * @see org.myProject.service.quiz.QuizService#nextQuestion(org.myProject.service.quiz.QuizDetails)
	 */
	public QuizDetails nextQuestion(QuizDetails quizDetails)
			throws QuizException {
		throw new UnsupportedOperationException();
	}

	public QuizDetails previousQuestion(QuizDetails quizDetails)
			throws QuizException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	public QuizDetails finishQuiz(QuizDetails quizDetails) throws QuizException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
