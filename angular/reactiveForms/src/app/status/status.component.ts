import { Component, OnInit } from '@angular/core';
import { FormControl , FormGroup} from '@angular/forms';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  statusForm = new FormGroup(
    {
      fname : new FormControl(''),
       sname : new FormControl('')
    }

  );
  onSubmit() {
  
    console.warn(this.statusForm.value);
  }
  constructor() { }

  ngOnInit() {
  }

  updateStatus() {
     
  }

}