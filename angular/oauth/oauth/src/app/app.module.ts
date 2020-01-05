import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { environment } from '../environments/environment';


import { AppComponent } from './app.component';
import { HelloComponent } from './hello.component';
import { ScripComponent } from './scrip/scrip.component';
import { TradeComponent } from './trade/trade.component';
import { BuyComponent } from './trade/buy/buy.component';
import { AuthService } from './auth.service';
import { HeaderComponent } from './header/header.component';



@NgModule({
  imports:      [ BrowserModule, FormsModule ,
  AngularFireModule.initializeApp(environment.firebase),
  AngularFirestoreModule, // imports firebase/firestore, only needed for database features
  AngularFireAuthModule 
  ],
  declarations: [ AppComponent, HelloComponent, ScripComponent, TradeComponent, BuyComponent, HeaderComponent ],
  bootstrap:    [ AppComponent ],
  providers: [AuthService]
})

 
export class AppModule { }
