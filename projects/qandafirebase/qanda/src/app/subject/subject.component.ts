import { Component, OnInit } from '@angular/core';
import {QuizService} from '../quiz.service';

@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.css']
})
export class SubjectComponent implements OnInit {
  subjects ;
  constructor( private quizService : QuizService) { }

  ngOnInit() {
    this.quizService.getSubject().subscribe(
      (subjects1: any[]) => {
					this.subjects = subjects1;
					console.log(subjects1);
				},
				(error) => console.log(error)
    );
  }

}