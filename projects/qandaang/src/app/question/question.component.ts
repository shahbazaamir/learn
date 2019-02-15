import { Component, OnInit } from '@angular/core';
import { QuestionService } from './question.service';


@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css'],
  providers:[QuestionService]
})
export class QuestionComponent implements OnInit { 

  constructor(private questionService:QuestionService) { }
 
  
		ngOnInit() {
	  
	  this.questionService.loadQuestions().subscribe(
        (questions: any[]) => {this.questions = questions;console.log(questions);},
        (error) => console.log(error)
      );
	 
  }
  
  var questions;
}
