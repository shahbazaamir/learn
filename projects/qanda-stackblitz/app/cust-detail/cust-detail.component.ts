import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cust-detail',
  templateUrl: './cust-detail.component.html',
  styleUrls: ['./cust-detail.component.css']
})
export class CustDetailComponent implements OnInit {

  
person : Person ;
  constructor	(){
	  this.person={
		name:"",
			dob:0
	  };
  }
  
  ngOnInit() {
  }
}


export class Person{
	constructor(
		public name : string ,
		public dob : number
	){
		
	}
	
}