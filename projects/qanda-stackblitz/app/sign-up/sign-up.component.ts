import { Component, OnInit, SimpleChanges } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  form1 : FormGroup;
  name1 : FormControl;
  constructor() { }
  
  ngOnInit() {
    console.log('ngOnInit');
	  this.form1= new FormGroup({
	  	'name1': new FormControl( this.name1,
	  	[ Validators.required, Validators.minLength(4), Validators.maxLength(10) ]
		
		  )
    });

    console.log(this.form1);
  }

  ngOnChanges(changes : SimpleChanges [] ){
    console.log('ngOnChanges');
  }
  ngDoCheck(){
    //console.log('ngDoCheck');
    
  }
}
