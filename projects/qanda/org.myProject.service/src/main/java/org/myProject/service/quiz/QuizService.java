package org.myProject.service.quiz;

public interface QuizService {
	public QuizDetails startQuiz(QuizDetails quizDetails) throws QuizException;
	public QuizDetails nextQuestion(QuizDetails quizDetails) throws QuizException;
	public QuizDetails previousQuestion(QuizDetails quizDetails) throws QuizException;
	public QuizDetails finishQuiz(QuizDetails quizDetails) throws QuizException;
}
