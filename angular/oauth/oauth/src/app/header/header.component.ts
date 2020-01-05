import { Component, OnInit } from '@angular/core';

import { AuthService } from '../auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
loginFunc;
  constructor(private authService: AuthService) { }
  action ="Login1";
  ngOnInit() {
  }
  login() {
    this.loginFunc = this.authService.doGoogleLogin();
    this.loginFunc.then(function (res) {
      console.log("Login done success");
      console.log( this.action);
      console.log(res);
      // this.action ="Log out";
    }).catch(function () {
      console.log("Login failed");
    });
  }
}