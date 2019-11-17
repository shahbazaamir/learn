import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-score',
  templateUrl: './score.component.html',
  styleUrls: ['./score.component.css']
})
export class ScoreComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }
  start(){
    let routeToNext = ['/start-quiz' ];
    this.router.navigate(routeToNext);
  }

}
