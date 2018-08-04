import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {Routes,RouterModule} from '@angular/router';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';

const appRoutes:Routes =[
{path:'home',component: HomeComponent}
];

@NgModule({
  declarations: [
    AppComponent,
   
    HomeComponent
  ],
  imports: [
    BrowserModule,
	HttpModule,
	RouterModule.forRoot(appRoutes)
  ],
  
  bootstrap: [AppComponent]
})
export class AppModule { }
