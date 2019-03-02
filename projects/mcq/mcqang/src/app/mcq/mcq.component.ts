import { Component, OnInit } from '@angular/core';
import { MatFormFieldModule } from '@angular/material';
import { QuizServiceService } from './../quiz-service.service';

@Component({
  selector: 'app-mcq',
  templateUrl: './mcq.component.html',
  styleUrls: ['./mcq.component.css'],
   providers:[QuizServiceService]
})
export class MCQComponent implements OnInit {

  constructor(private quizService :QuizServiceService ) {


  }

  ngOnInit() {
	  
	  this.quizService.loadQuestions().subscribe(
        (questions: any[]) => {
			this.questions = questions;
			console.log(questions);
		},
        (error) => console.log(error)
      );
	 
  }
var questions;
}
