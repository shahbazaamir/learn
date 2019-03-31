import { Component, OnInit , Input, Output } from '@angular/core';
import { QuizServiceService } from './../../quiz-service.service';
import { NgModel } from '@angular/forms';
import { EventEmitter } from '@angular/core';



@Component({
	selector: 'app-subject',
	templateUrl: './subject.component.html',
	styleUrls: ['./subject.component.css']
})
export class SubjectComponent implements OnInit {
	subject : string ;
	subjects ;
	@Output() valueChange = new EventEmitter();
	constructor(private quizService :QuizServiceService) { }

	ngOnInit() {
		
		this.quizService.loadSubject()
			.subscribe(
				(subjects1: any[]) => {
					this.subjects = subjects1;
					console.log(subjects1);
				},
				(error) => console.log(error)
			);
	}
	
	 
	
	 loadQuestion($event){
		 console.log("loadQuestion"+this.subject);
	 };
	 valueChanged() { // You can give any function name
        //this.counter = this.counter + 1;
		this.valueChange.emit(this.subject);
		this.quizService.updatedDataSelection(this.subject);
    }
	
}
