import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ScripComponent } from './scrip/scrip.component';
import { NameComponent } from './scrip/name/name.component';
import { NseCodeComponent } from './scrip/nse-code/nse-code.component';
import { OrderComponent } from './order/order.component';
import { QuantityComponent } from './order/quantity/quantity.component';
import { TypeComponent } from './order/type/type.component';
import { PriceComponent } from './order/price/price.component';
import { IdComponent } from './order/id/id.component';

@NgModule({
  declarations: [
    AppComponent,
    ScripComponent,
    NameComponent,
    NseCodeComponent,
    OrderComponent,
    QuantityComponent,
    TypeComponent,
    PriceComponent,
    IdComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
