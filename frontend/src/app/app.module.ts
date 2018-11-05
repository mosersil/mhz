import {BrowserModule, enableDebugTools} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {UiModule} from './ui/ui.module';
import {HttpClientModule} from '@angular/common/http';
import {CalendarService} from "./calendar.service";
import { AppRoutingModule } from './app-routing.module';
import { AboutusComponent } from './aboutus/aboutus.component';
import { HomeComponent } from './home/home.component';
import { ContactComponent } from './contact/contact.component';
import { InternalComponent } from './internal/internal.component';
import { SocialComponent } from './social/social.component';
import { CalendarComponent } from './calendar/calendar.component';
import { ShopComponent } from './shop/shop.component';
import { NewsComponent } from './news/news.component';
import { FormsModule }   from '@angular/forms';



@NgModule({
  imports: [
    BrowserModule,
    UiModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
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
    NewsComponent
  ],

  providers: [CalendarService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
