import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mcq';
  person : Person ;
  constructor	(){
	  this.person={
		name:"shahbaz",
			age:29
	  };
  }
}


export class Person{
	constructor(
		public name : string ,
		public age : number
	){
		
	}
	
}