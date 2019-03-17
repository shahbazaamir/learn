import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import {ReactiveFormsModule} from '@angular/forms';
import {Routes,RouterModule} from '@angular/router';
import { CustDetailComponent } from './cust-detail/cust-detail.component';
import { MCQComponent } from './mcq/mcq.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ScoreComponent } from './cust-detail/score/score.component';
const appRoutes:Routes =[
{path:'cust-detail',component: CustDetailComponent},
{path:'mcq',component: MCQComponent},
{path:'sign-up',component: SignUpComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    CustDetailComponent,
    MCQComponent,
    SignUpComponent,
    ScoreComponent
  ],
  imports: [
    BrowserModule,
	FormsModule,
	ReactiveFormsModule,
	RouterModule.forRoot(appRoutes),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
