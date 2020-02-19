import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import localeDe from '@angular/common/locales/de-CH';
import {AppComponent} from './app.component';
import {UiModule} from './ui/ui.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CalendarService} from "./common/services/calendar.service";
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
import {ShopService} from "./shop.service";
import {RegisterComponent} from './register/register.component';
import {ForgotComponent} from './forgot/forgot.component';
import {PaymentComponent} from './payment/payment.component';
import {NgxStripeModule} from 'ngx-stripe';
import {TransactionsComponent} from './transactions/transactions.component';
import {TokenInterceptor} from "./token-interceptor";
import {ForgotLandingComponent} from './forgot-landing/forgot-landing.component';
import {QRCodeModule} from "angularx-qrcode";
import {NgxPopperModule} from "ngx-popper";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {GalleryModule} from "@ngx-gallery/core";
import {BotDetectCaptchaModule} from "angular-captcha";
import {NgxSpinnerModule} from "ngx-spinner";
import {registerLocaleData} from "@angular/common";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {MyDatePickerModule} from 'mydatepicker';
import {InternationalPhoneNumberModule} from "ngx-international-phone-number";
import {SignupmemberComponent} from './signupmember/signupmember.component';
import {SignupmemberLandingComponent} from './signupmember-landing/signupmember-landing.component';
import {LegalComponent} from './pages/legal/legal.component';
import {RegistrationComponent} from './common/components/registration/registration.component';
import {AddresslistGeneratorComponent} from './common/components/addresslist-generator/addresslist-generator.component';
import {RouterModule} from "@angular/router";
import {MatDatepicker} from "@angular/material/datepicker";
import {MatSlider} from "@angular/material/slider";


registerLocaleData(localeDe, 'de');


@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    UiModule,
    HttpClientModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    QRCodeModule,
    NgxPopperModule,
    GalleryModule.withConfig({dots: true, thumbPosition: "top", imageSize: "contain"}),
    NgxStripeModule.forRoot('pk_test_D19x4omdZwLxxIlJZuivB41j'),
    BotDetectCaptchaModule.forRoot({
        captchaEndpoint: '/botdetectcaptcha'
      }
    ),
    NgxSpinnerModule,
    NgbModule,
    MyDatePickerModule,
    InternationalPhoneNumberModule
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
    SignupmemberComponent,
    SignupmemberLandingComponent,
    LegalComponent,
    RegistrationComponent,
    AddresslistGeneratorComponent
  ],

  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }, {
    provide: LOCALE_ID, useValue: 'de'
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
