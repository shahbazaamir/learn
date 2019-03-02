import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import {ReactiveFormsModule} from '@angular/forms';
import {Routes,RouterModule} from '@angular/router';
import { CustDetailComponent } from './cust-detail/cust-detail.component';
import { MCQComponent } from './mcq/mcq.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { MatButtonModule, MatInputModule, MatCardModule } from '@angular/material';
import { QuizServiceService } from './quiz-service.service';
import { HttpModule } from '@angular/http';
import {MatSelectModule} from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

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
    SignUpComponent
  ],
  imports: [
    BrowserModule,
	FormsModule,
	ReactiveFormsModule,
	RouterModule.forRoot(appRoutes),
	MatButtonModule,
    MatInputModule,
    MatCardModule,
	HttpModule,
	MatSelectModule,
	BrowserAnimationsModule
  ],
  providers: [QuizServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
