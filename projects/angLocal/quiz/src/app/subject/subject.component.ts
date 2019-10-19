import { Component, OnInit, Output } from '@angular/core';
import { QuizService } from '../quiz.service';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.css']
})
export class SubjectComponent implements OnInit {
	subject;
	subjects;
  constructor(private quizService :QuizService) { }
  @Output() valueChange = new EventEmitter();

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
	valueChanged() { // You can give any function name
        //this.counter = this.counter + 1;
        console.log("valueChanged"+this.subject);
		this.valueChange.emit(this.subject);
		this.quizService.updatedDataSelection(this.subject);
    }
}
