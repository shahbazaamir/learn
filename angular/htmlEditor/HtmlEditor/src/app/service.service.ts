import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  constructor(private http: HttpClient) { }

  saveFile(formData) {
    let headers = new HttpHeaders();
    //this is the important step. You need to set content type as null
    headers.set('Content-Type', null);
    headers.set('Accept', "multipart/form-data");
    let params = new HttpParams();
    this.http.post('http://localhost:8990/' + 'uploadFile', formData, { params, headers }).subscribe((res) => {
      console.log(res);
    });
  }

  save(formData) {
    let headers = new HttpHeaders();
    //this is the important step. You need to set content type as null
    headers.set('Content-Type', null);
    headers.set('Accept', "text/json");
    let params = new HttpParams();
    this.http.post('http://localhost:8990/' + 'saveHtml', formData, { params, headers }).subscribe((res) => {
      console.log(res);
    });
  }
}
