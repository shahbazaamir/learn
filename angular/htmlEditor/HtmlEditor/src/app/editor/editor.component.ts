import { Component, OnInit } from '@angular/core';
import { ServiceService } from '../service.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.sass']
})
export class EditorComponent implements OnInit {

  constructor(private service : ServiceService) { }

  ngOnInit() {
  }

  execCom(command){
    document.execCommand(command);
  }

  save(){
    let formData : data ={
     "body" :  document.getElementById('test2').innerHTML ,
    "id" : "1"};
    this.service.save(formData);
  }
}

class data {
  public  body : string;
  public  id : string;

}