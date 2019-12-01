import { Component, OnInit } from '@angular/core';
//import { FormControl , FormGroup} from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  statusForm = this.fb.group(
    {
      fname : ['', Validators.required],
       sname :  ['']
    }

  );
  onSubmit() {
  
    console.warn(this.statusForm.value);
  }
  constructor(private fb: FormBuilder) { }

  ngOnInit() {
  }

  updateStatus() {
     
  }

}