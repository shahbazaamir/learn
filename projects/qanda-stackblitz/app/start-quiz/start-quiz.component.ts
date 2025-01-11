import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SubjectComponent } from '../mcq/subject/subject.component';
import { QuizServiceService } from '../quiz-service.service';

@Component({
  selector: 'app-start-quiz',
  templateUrl: './start-quiz.component.html',
  styleUrls: ['./start-quiz.component.css']
})
export class StartQuizComponent implements OnInit {
  subjectVal: any;
  @ViewChild('subjectDropdown')
	subjectComponent: SubjectComponent;

  constructor(private router: Router, 
    private service : QuizServiceService) { }

  ngOnInit() {
  }
  displayCounter(data){
		//console.log('data');
		//console.log(data);
    this.subjectVal=data;
    this.service.setSubject(data);
    console.log(this.subjectVal);
	}
    
  
  start(){
    let routeToNext = ['/quiz',{'subject':this.subjectVal}];
    this.router.navigate(routeToNext);
  }
}
