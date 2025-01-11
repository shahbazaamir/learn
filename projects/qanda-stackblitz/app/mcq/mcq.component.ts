import { Component, OnInit , Input,ViewChild } from '@angular/core';
//import { MatFormFieldModule } from '@angular/material';

import { QuizServiceService } from './../quiz-service.service';
import { SubjectComponent } from './subject/subject.component';

@Component({
  selector: 'app-mcq',
  templateUrl: './mcq.component.html',
  styleUrls: ['./mcq.component.css'],
   providers:[QuizServiceService]
})
export class MCQComponent implements OnInit  {
	subjectVal;
	ngOnInit() {
		 
	}
	@ViewChild('subjectDropdown')
	subjectComponent: SubjectComponent;

	

	constructor() {


	}
 
	
  
	 

	ngAfterViewChanges(){
		//console.log("ngAfterViewChanged:", this.subjectComponent.subject);
		this.subjectVal = this.subjectComponent.subject;

	}
	ngDoCheck(){
		//console.log('do check mcq');
		
	}

	displayCounter(data){
		//console.log('data');
		//console.log(data);
		this.subjectVal=data;
	}
 
}
