import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {UiModule} from './ui/ui.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CalendarService} from "./calendar.service";
import {AppRoutingModule} from './app-routing.module';
import {AboutusComponent} from './aboutus/aboutus.component';
import {HomeComponent} from './home/home.component';
import {ContactComponent} from './contact/contact.component';
import {InternalComponent} from './internal/internal.component';
import {SocialComponent} from './social/social.component';
import {CalendarComponent} from './calendar/calendar.component';
import {ShopComponent} from './shop/shop.component';
import {NewsComponent} from './news/news.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthenticationService} from "./authentication.service";
import {AuthGuardService} from "./auth-guard-service.service";
import {LoginComponent} from './login/login.component';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {ShopService} from "./shop.service";
import {RegisterComponent} from './register/register.component';
import {ForgotComponent} from './forgot/forgot.component';
import {PaymentComponent} from './payment/payment.component';
import {NgxStripeModule} from 'ngx-stripe';
import {TransactionsComponent} from './transactions/transactions.component';
import {TokenInterceptor} from "./token-interceptor";
import { ForgotLandingComponent } from './forgot-landing/forgot-landing.component';


@NgModule({
  imports: [
    BrowserModule,
    UiModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AngularFontAwesomeModule,
    NgxStripeModule.forRoot('pk_test_D19x4omdZwLxxIlJZuivB41j'),
  ],
  declarations: [
    AppComponent,
    AboutusComponent,
    HomeComponent,
    ContactComponent,
    InternalComponent,
    SocialComponent,
    CalendarComponent,
    ShopComponent,
    NewsComponent,
    LoginComponent,
    RegisterComponent,
    ForgotComponent,
    PaymentComponent,
    TransactionsComponent,
    ForgotLandingComponent,
  ],

  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    CalendarService,
    ShopService,
    AuthenticationService,
    AuthGuardService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
