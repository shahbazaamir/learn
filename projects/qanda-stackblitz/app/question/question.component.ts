import { Component, OnInit, Input } from '@angular/core';
import { QuizServiceService } from '../quiz-service.service';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent implements OnInit {
	@Input() inSubject ;
	subjectId ;
	questions;
	questionDD;
  constructor(private quizService :QuizServiceService ) { }

  ngOnInit() {
	  
		this.quizService.loadQuestionsBySub(this.subjectId)
			.subscribe(
				(questions1: any[]) => {
					this.questions = questions1;
          console.log('1');
				},
				(error) => console.log(error)
		);
		this.quizService.data.subscribe(
			(subjectId : String ) =>{
				console.log('subject observable subscribed');
        this.subjectId=subjectId;
				
				this.quizService.loadQuestionsBySub(this.subjectId)
				.subscribe(
					(questions1: any[]) => {
						this.questions = questions1;
            console.log('2');
					},
					(error) => console.log(error)
			);
			} ,
			(error) => console.log(error)
		);
	 
	} ;

	loadAnswers(){
		console.log('loadAnswers');
		this.quizService.updateQuestion(this.questionDD);
	}
}
