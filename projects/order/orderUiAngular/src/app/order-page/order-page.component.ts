import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';


@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.css']
})
export class OrderPageComponent implements OnInit {

  @Input() order_id_text : string;
  constructor() { }
   
  ngOnInit() {
  }
	onKey(){
		console.log(order_id_text.value);
	}
}
