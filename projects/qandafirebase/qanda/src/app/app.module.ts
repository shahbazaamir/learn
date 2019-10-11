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


@NgModule({
  imports:      [ BrowserModule, 
  AngularFireModule.initializeApp(environment.firebase),
  FormsModule ],
  declarations: [ AppComponent, HelloComponent, QuizComponent, SubjectComponent ],
  bootstrap:    [ AppComponent ],
  providers: [QuizService,AngularFirestore]
})
export class AppModule { }
