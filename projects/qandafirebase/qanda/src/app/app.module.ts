import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AngularFireModule } from '@angular/fire';
import { AppComponent } from './app.component';
import { HelloComponent } from './hello.component';
import { QuizComponent } from './quiz/quiz.component';
import { SubjectComponent } from './subject/subject.component';
import { QuizService } from './quiz.service';
import {environment} from "../environments/environment";
import { AngularFirestore } from '@angular/fire/firestore';
import {Routes,RouterModule} from '@angular/router';
import { StartQuizComponent } from './start-quiz/start-quiz.component';


const appRoutes:Routes =[
  {path:'start-quiz',component: QuizComponent}
  ];

@NgModule({
  imports:      [ BrowserModule, 
  AngularFireModule.initializeApp(environment.firebase),
  RouterModule.forRoot(appRoutes),
  
  FormsModule ],
  declarations: [ AppComponent, HelloComponent, QuizComponent, SubjectComponent, StartQuizComponent ],
  bootstrap:    [ AppComponent ],
  providers: [QuizService,AngularFirestore]
})
export class AppModule { }
