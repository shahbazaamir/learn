import { Component } from '@angular/core';
import { AppService } from './service/app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'helpUI';
content ;
  constructor(private appService : AppService ){}

  ngOnInit(){
    this.appService.loadContent().subscribe(
      res => {
        console.log(res);
        this.content = res ;

    });
  
  }

  ngOnDestroy(){

  }
}
