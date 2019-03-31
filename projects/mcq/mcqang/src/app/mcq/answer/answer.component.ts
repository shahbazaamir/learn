import { Component, OnInit,Input } from '@angular/core';
import { QuizServiceService } from './../../quiz-service.service';

@Component({
	selector: 'app-answer',
	templateUrl: './answer.component.html',
	styleUrls: ['./answer.component.css']
})
export class AnswerComponent implements OnInit {
	answers;
	@Input()
	subjectId : string ;
	@Input()
	questionId : string ;
	
	subjectId1;

	constructor(private quizService :QuizServiceService) { }
	ngOnInit() { 
		this.quizService.data.subscribe(
			(subject) => {
				console.log('subject : '+subject);
				this.subjectId1=subject;
			} ,
			(error) => console.log(error)
		);
		
		this.quizService.questionObs.subscribe(
			(question )=>{
				console.log('question : '+question);
				this.quizService.loadAnswers(this.subjectId1,question)
				.subscribe(
					(data1: any[]) => {
						this.answers = data1;
						console.log(data1);
					},
					(error) => console.log(error)
				);
			} ,
			(error) => console.log(error));
		
	}
	 

}
