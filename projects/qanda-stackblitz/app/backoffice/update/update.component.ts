import { Component, OnInit } from '@angular/core';
import { Observable} from 'rxjs';
import { AngularFirestore } from '@angular/fire/firestore';


@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent implements OnInit {
   
   users: Observable<any[]>;
  constructor(private db: AngularFirestore) {

    this.users = db.collection('test1').valueChanges();
 

    console.log(this.users);
  }

  ngOnInit() {
  }

  update() {
    this.db.database().ref('users/' + 'userId').set({
      username: 'name2',
      email: 'email1',
      profile_picture: 'imageUrl'
    });
  }
}