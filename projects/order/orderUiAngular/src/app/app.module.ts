import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {Routes,RouterModule} from '@angular/router';

import { AppComponent } from './app.component';
import { QuantityComponent } from './order/quantity/quantity.component';
import { TypeComponent } from './order/type/type.component';
import { PriceComponent } from './order/price/price.component';
import { IdComponent } from './order/id/id.component';
import { OrderPageComponent } from './order-page/order-page.component';
import { OrderComponent } from './order/order.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AlertModule } from 'ngx-bootstrap';
import { FormsModule } from '@angular/forms';
import { ScripComponent } from './scrip/scrip.component';
import { NameComponent } from './scrip/name/name.component';
import { NseCodeComponent } from './scrip/nse-code/nse-code.component';
import { OrderService } from './order/order-service';
const appRoutes:Routes =[
{path:'order',component: OrderPageComponent}]


@NgModule({
  declarations: [
    AppComponent,
    OrderComponent,
    
    QuantityComponent,
    TypeComponent,
    PriceComponent,
    IdComponent,
	OrderPageComponent,
	ScripComponent,
	NameComponent,
	NseCodeComponent
  ],
  imports: [
    BrowserModule,
	AlertModule.forRoot(),
	HttpClientModule,
	FormsModule,
	RouterModule.forRoot(appRoutes)
  ],
  providers: [OrderService],
  bootstrap: [AppComponent]
})
export class AppModule { }
