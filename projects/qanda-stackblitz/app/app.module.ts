import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';
import { CustDetailComponent } from './cust-detail/cust-detail.component';
import { MCQComponent } from './mcq/mcq.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { MatButtonModule, MatInputModule, MatCardModule } from '@angular/material';
import { QuizServiceService } from './quiz-service.service';
import { HttpModule } from '@angular/http';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SubjectComponent } from './mcq/subject/subject.component';
import { AnswerComponent } from './mcq/answer/answer.component';
import { QuestionComponent } from './question/question.component';
import { DemoMaterialModule } from './material-module';
import { BackofficeComponent } from './backoffice/backoffice.component';
import { UpdateComponent } from './backoffice/update/update.component';
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { environment } from '../environments/environment';
import { StartQuizComponent } from './start-quiz/start-quiz.component';
import { QuizComponent } from './quiz/quiz.component';



const appRoutes: Routes = [
  { path: 'cust-detail', component: CustDetailComponent },
  { path: 'mcq', component: MCQComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'app-backoffice', component: BackofficeComponent },
  {path:'start-quiz',component: StartQuizComponent},
  {path:'quiz',component: QuizComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    CustDetailComponent,
    MCQComponent,
    SignUpComponent,
    SubjectComponent,
    AnswerComponent,
    QuestionComponent,
    BackofficeComponent,
    UpdateComponent,
    StartQuizComponent,
    QuizComponent

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
    BrowserAnimationsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
    DemoMaterialModule
  ],
  providers: [QuizServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
