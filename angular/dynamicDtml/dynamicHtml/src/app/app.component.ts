import { Component } from '@angular/core';
import { AppService } from './service/app.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private title = 'DynamicHtml';
  private content ;
  private loadcontent =false;
  
  private s :Subscription;
  private s1 :Subscription;

  constructor(private appService : AppService ){}

  ngOnInit(){
    this.s =this.appService.loadContent().subscribe(
      (res:any )=> {
        console.log(res);
        this.content = res._body ;
        this.loadcontent = true;
    });
  
  }

  load(name : string ){
    this.s1 =this.appService.loadContent().subscribe(
      (res:any )=> {
        console.log(res);
        this.content = res._body ;
        this.loadcontent = true;
    });
  }
  ngOnDestroy(){
    if(this.s) this.s.unsubscribe();
    if(this.s1) this.s1.unsubscribe();
  }
}
