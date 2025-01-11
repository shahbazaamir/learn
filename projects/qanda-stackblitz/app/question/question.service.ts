import { EventEmitter, Injectable } from '@angular/core';
import { Headers, Http ,Response} from '@angular/http';
import {  HttpClientModule } from '@angular/common/http';
import {  HttpModule } from '@angular/http';


import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class QuestionService{
	 constructor(private http:Http) {}
	loadQuestions(){
		console.log('load question');
		if(1!=1){
			return [{"test":"pass"}];
		}else{
		
		return this.http.get('http://localhost:8990/question')
		.map(
        (response: Response) => {
          const data = response.json();
          
          return data;
        }
      )
      .catch(
        (error: Response) => {
          return Observable.throw('Something went wrong');
        }
      );
		}
	}
}