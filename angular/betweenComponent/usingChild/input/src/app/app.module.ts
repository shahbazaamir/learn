import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ChildcComponent } from './childc/childc.component';
import { FormsModule  } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    ChildcComponent
  ],
  imports: [
    BrowserModule,
	FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
