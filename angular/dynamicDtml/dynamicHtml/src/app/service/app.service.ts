import { Injectable } from "@angular/core";
import { Headers, Http, Response } from '@angular/http';

@Injectable({
    providedIn: 'root'
  })
export class AppService {

    constructor(private http: Http ) { }

    loadContent(){
        return this.http
        .get('http://localhost:8990/loadFile/b5510b8d-c766-48bd-808d-54c2e6e6d1c2');
    }

    loadContentByName(name : string){
        return this.http
        .get('http://localhost:8990/loadFile/'+name);
    }
}