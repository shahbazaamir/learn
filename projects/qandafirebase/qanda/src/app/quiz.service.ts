import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AngularFirestore } from '@angular/fire/firestore';


@Injectable()
export class QuizService {
  constructor(private db: AngularFirestore) {

  }
  //firebase.initializeApp(environment.firebaseConfig);

  

  getSubject(){
    return this.db.collection('/subject').valueChanges();
  }
}
