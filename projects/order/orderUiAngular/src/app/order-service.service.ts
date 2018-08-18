import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Headers, Http ,Response} from '@angular/http';

import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {

  constructor(private http: HttpClient) { }
  
  getOrders(){
		
		
		
		return this.http.get('http://localhost:8090/org.myProject.question.view3-0.0.1-SNAPSHOT/question2')
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
