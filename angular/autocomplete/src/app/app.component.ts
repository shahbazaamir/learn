import { Component } from '@angular/core';



@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: [ './app.component.css' ]
})
export class AppComponent  {
  name = 'Angular';
  list =['hello','hi','foo','bar'];
  final;
  test;
  test1(){
   
    this.final = [];
    for( let item of this.list){
      if(item.indexOf(this.test) != -1){
        this.final.push(item);
      }
    }

  }
  setItem(item){
      this.test =item;
  }
 
  clear(){
  //  this.final=[];
  }
}
