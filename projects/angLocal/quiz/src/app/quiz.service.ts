import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import { AngularFirestore } from '@angular/fire/firestore';

import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/Rx';


@Injectable({
  providedIn: 'root'
})
export class QuizService {
  subjectId: any;
  constructor(private http: Http, private db: AngularFirestore) { }

  private dataSource = new BehaviorSubject<String>("");
  data = this.dataSource.asObservable();

  private questionBs = new BehaviorSubject<String>("");
  questionObs = this.questionBs.asObservable();

  updateQuestion(question) {
    console.log('updateQuestion' + question);
    this.questionBs.next(question);
  }

  updatedDataSelection(subject) {
    console.log('updatedDataSelection');
    this.dataSource.next(subject);
  }

  loadSubject() {
    return this.db.collection('subject'
      //, ref => ref.where('desc', '==', 'Java')
    ).valueChanges();
    // , ref => ref.where('id', '==', questionId)
  }

  loadQuestion() {
    return this.db.collection('question/java/javaupdatedDataSelection');
  }
  loadQuestionsBySub(subjectId) {
    console.log('subjectId::' + subjectId);
    if (subjectId == '') {
      subjectId = 'dummy';
    }
    return this.db.collection('question/' + subjectId + '/one').valueChanges();
  }

  loadQuizQuestionsBySub(subjectId) {
    console.log('subjectId::' + this.subjectId);
    if (this.subjectId == '') {
      this.subjectId = 'dummy';
    }
    return this.db.collection('question/' + this.subjectId + '/one').valueChanges();
  }

  loadSubjectOld() {
    return this.http
      .get('http://localhost:8990/subject/')
      .map(
        (response: Response) => {
          const data = response.json();

          return data;
        }
      )
      .catch(
        (error: Response) => {
          return Observable.throw('Something went wrong');
        }
      );

  }

  loadQuestionsOld() {
    return this.http
      .get('http://localhost:8990/question/')
      .map(
        (response: Response) => {
          const data = response.json();

          return data;
        }
      )
      .catch(
        (error: Response) => {
          return Observable.throw('Something went wrong');
        }
      );

  }

  loadQuestionsBySubOld(subjectId) {
    return this.http
      .get('http://localhost:8990/question/' + subjectId)
      .map(
        (response: Response) => {
          const data = response.json();

          return data;
        }
      )
      .catch(
        (error: Response) => {
          return Observable.throw('Something went wrong');
        }
      );

  }

  loadAnswers(subjectId, questionId) {
    console.log('questionId,subjectId' + questionId + ',' + subjectId);
    if (subjectId == '') {
      subjectId = 'dummy';
    }
    return this.db.collection('question/' + subjectId + '/answer'
      , ref => ref.where('id', '==', questionId)
    ).valueChanges();
    /*	return this.http
      .get('http://localhost:8990/answer/'+questionId+'/'+subjectId)
      .map(
        (response: Response) => {
          const data = response.json();
          
          return data;
        }
      )
      .catch(
        (error: Response) => {
          return Observable.throw('Something went wrong');
        }
      );
      */
  }

  loadAnswersBySub(subjectId ) {
    console.log('  loadAnswersBySub subjectId' +   ',' + subjectId);
    if (this.subjectId == '') {
      this.subjectId = 'dummy';
    }
    return this.db.collection('question/' + this.subjectId + '/answer'   ).valueChanges();
  }

loadOptionsBySub(subjectId ) {
    console.log('  loadOptionsBySub subjectId' +   ',' + subjectId);
    if (this.subjectId == '') {
      this.subjectId = 'dummy';
    }
    return this.db.collection('question/' + this.subjectId + '/options'   ).valueChanges();
  }

  delete(subjectId, questionId) {
    let promise = new Promise((resolve, reject) => {
      let apiURL = 'http://localhost:8990/delete/question/' + subjectId + '/' + questionId;
      this.http
        .get(apiURL)
        .toPromise()
        .then(
          res => {
            // Success
            console.log(res);
            resolve();
          },
          msg => {
            // Error
            reject(msg);
          }
        );
    });
    return promise;
  }

  setSubject(subjectId){
		this.subjectId = subjectId ;
	}
}
