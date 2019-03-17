import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-score',
  templateUrl: './score.component.html',
  styleUrls: ['./score.component.css']
})
export class ScoreComponent implements OnInit {
  score : any;
  constructor() { 
  this.score=1;
  }

  ngOnInit() {
  }
updateScore(newScore){
	this.score= newScore;
}
}
