import { Component, OnInit , ViewChild,SimpleChanges} from '@angular/core';
import {ScoreComponent} from './../cust-detail/score/score.component';

@Component({
  selector: 'app-mcq',
  templateUrl: './mcq.component.html',
  styleUrls: ['./mcq.component.css']
})
export class MCQComponent implements OnInit {
  feedback : any ;
  
  @ViewChild(ScoreComponent)
  scoreComponent : ScoreComponent ;
  
  constructor() { 
    this.feedback = '5';
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log('changes.feedback.currentValue');
    console.log(changes.feedback.currentValue);
    console.log('changes.feedback.previousValue');
    console.log(changes.feedback.previousValue);
  }
  ngOnInit() {
  }
	checkAnswer($event){
		this.feedback = 6;
		console.log($event.data );
	}
	ngAfterViewInit(){
		console.log('view init'+this.scoreComponent.score );
	}
	
	ngAfterViewChecked(){
		console.log('view checked'+this.scoreComponent.score );
	}
}
