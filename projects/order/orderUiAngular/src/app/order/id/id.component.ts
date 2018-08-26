import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-id',
  templateUrl: './id.component.html',
  styleUrls: ['./id.component.css']
})
export class IdComponent implements OnInit {
order_id_text="";
  constructor() { }

  ngOnInit() {
  }
	loadId(){
	console.log(this.order_id_text);
	}
  

}
