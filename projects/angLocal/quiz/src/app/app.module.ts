import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {Routes , RouterModule} from '@angular/router';
import { AppComponent } from './app.component';
import { QuizComponent } from './quiz/quiz.component';
import { SubjectComponent } from './subject/subject.component';
import { StartQuizComponent } from './start-quiz/start-quiz.component';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { QuizService } from './quiz.service';
import { AngularFireModule } from '@angular/fire';
import { environment } from 'src/environments/environment';
import { AngularFirestore } from '@angular/fire/firestore';
import { ScoreComponent } from './score/score.component';

const appRoutes : Routes = [
  { path: 'start-quiz'  , component : StartQuizComponent},
{ path: 'quiz' , component : QuizComponent},
{ path: 'score' , component : ScoreComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    QuizComponent,
    SubjectComponent,
    StartQuizComponent,
    ScoreComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    AngularFireModule.initializeApp(environment.firebase),
    FormsModule,
    HttpModule
  ],
  providers: [QuizService,AngularFirestore],
  bootstrap: [AppComponent]
})
export class AppModule { }
