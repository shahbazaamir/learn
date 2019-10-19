import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { QuizService } from '../quiz.service';
import { SubjectComponent } from '../subject/subject.component';

@Component({
  selector: 'app-start-quiz',
  templateUrl: './start-quiz.component.html',
  styleUrls: ['./start-quiz.component.css']
})
export class StartQuizComponent implements OnInit {
  subjectVal;
  @ViewChild('subjectDropdown')
	subjectComponent: SubjectComponent;
  constructor(private router: Router, 
    private service : QuizService) { }

  ngOnInit() {
  }
  displayCounter(data){
		console.log('data');
		console.log(data);
    this.subjectVal=data;
    this.service.setSubject(data);
    console.log(this.subjectVal);
	}
  start(){
    let routeToNext = ['/quiz' ];
    this.router.navigate(routeToNext);
  }

}
