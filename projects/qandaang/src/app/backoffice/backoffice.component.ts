import { Component, OnInit, SimpleChanges } from '@angular/core';
import { QuizServiceService } from '../quiz-service.service';

@Component({
  selector: 'app-backoffice',
  templateUrl: './backoffice.component.html',
  styleUrls: ['./backoffice.component.css']
})
export class BackofficeComponent implements OnInit {
  subjectId ;
  questions;
  displayedColumns : string[] = ['subjectId','questionId', 'questionDesc','id'];
  constructor(private quizService :QuizServiceService) { }
  testClick( questionId ){
    console.log('hello');
	console.log(this.subjectId);
    console.log(questionId);
    this.quizService.delete(this.subjectId,questionId);
  }
  ngOnInit() {
    this.quizService.data.subscribe(
			(subjectId : String ) =>{
				this.subjectId=subjectId;
				
				this.quizService.loadQuestionsBySub(this.subjectId)
				.subscribe(
					(questions1: any[]) => {
						this.questions = questions1;
				
					},
					(error) => console.log(error)
			);
			} ,
			(error) => console.log(error)
		);
  }
  ngOnChanges(changes: SimpleChanges) {
    console.log('changes');
    console.log(changes);
    
  }
}
