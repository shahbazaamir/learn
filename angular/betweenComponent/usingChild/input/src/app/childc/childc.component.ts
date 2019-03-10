import { Component, OnInit,Input } from '@angular/core';

@Component({
  selector: 'app-childc',
  templateUrl: './childc.component.html',
  styleUrls: ['./childc.component.css']
})
export class ChildcComponent implements OnInit {
	@Input()
	childName : string ;
	constructor() { 
		
	}

	ngOnInit() {
	}

}
