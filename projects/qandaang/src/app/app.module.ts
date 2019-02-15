import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {Routes,RouterModule} from '@angular/router';

import { AppComponent } from './app.component';
import { QuestionComponent } from './question/question.component';
import { HomeComponent } from './home/home.component';
import { SecuritiesComponent } from './securities/securities.component';
import { BuyComponent } from './securities/buy/buy.component';
import { BuypriceComponent } from './securities/buyprice/buyprice.component';
import { SellpriceComponent } from './securities/sellprice/sellprice.component';
import { StocksComponent } from './securities/stocks/stocks.component';
import { ChangeComponent } from './securities/change/change.component';
import { BrokerageComponent } from './securities/brokerage/brokerage.component';
import { ProfitComponent } from './securities/profit/profit.component';
import { NetprofitComponent } from './securities/netprofit/netprofit.component';
import { RecidentialstatusComponent } from './securities/recidentialstatus/recidentialstatus.component';
import { ScripComponent } from './securities/scrip/scrip.component';
import { ProjectsComponent } from './projects/projects.component';
import { HttpModule } from '@angular/http';
import { QuestionService } from './question/question.service';
import { SubjectComponent } from './subject/subject.component';

const appRoutes:Routes =[
{path:'home',component: HomeComponent},
{path:'securities',component: SecuritiesComponent},
{path:'projects',component: ProjectsComponent},
{path:'question',component: QuestionComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    QuestionComponent,
    HomeComponent,
    SecuritiesComponent,
    BuyComponent,
    BuypriceComponent,
    SellpriceComponent,
    StocksComponent,
    ChangeComponent,
    BrokerageComponent,
    ProfitComponent,
    NetprofitComponent,
    RecidentialstatusComponent,
    ScripComponent,
    ProjectsComponent,
    SubjectComponent
  ],
  imports: [
    BrowserModule,
	HttpModule,
	RouterModule.forRoot(appRoutes)
  ],
  providers: [QuestionService],
  bootstrap: [AppComponent]
})
export class AppModule { }
