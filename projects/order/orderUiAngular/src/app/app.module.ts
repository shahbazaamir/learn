import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {Routes,RouterModule} from '@angular/router';

import { AppComponent } from './app.component';
import { ScripComponent } from './scrip/scrip.component';
import { NameComponent } from './scrip/name/name.component';
import { NseCodeComponent } from './scrip/nse-code/nse-code.component';
import { OrderComponent } from './order/order.component';
import { QuantityComponent } from './order/quantity/quantity.component';
import { TypeComponent } from './order/type/type.component';
import { PriceComponent } from './order/price/price.component';
import { IdComponent } from './order/id/id.component';
import { OrderPageComponent } from './order-page/order-page.component';
import { OrderServiceService } from './order-service.service';


const appRoutes:Routes =[
{path:'order',component: OrderPageComponent}]


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
    IdComponent,
	OrderPageComponent
  ],
  imports: [
    BrowserModule,
	HttpClientModule,
	RouterModule.forRoot(appRoutes)
  ],
  providers: [OrderServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
