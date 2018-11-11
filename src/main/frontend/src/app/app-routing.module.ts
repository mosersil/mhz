import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AppComponent} from "./app.component";
import {AboutusComponent} from "./aboutus/aboutus.component";
import {HomeComponent} from "./home/home.component";
import {ContactComponent} from "./contact/contact.component";
import {NewsComponent} from "./news/news.component";
import {CalendarComponent} from "./calendar/calendar.component";
import {SocialComponent} from "./social/social.component";
import {ShopComponent} from "./shop/shop.component";
import {InternalComponent} from "./internal/internal.component";
import {AuthGuardService as AuthGuard} from "./auth-guard-service.service";
import {LoginComponent} from "./login/login.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'news', component: NewsComponent },
  { path: 'calendar', component: CalendarComponent },
  { path: 'about_us', component: AboutusComponent },
  { path: 'social', component: SocialComponent },
  { path: 'shop', component: ShopComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'login', component: LoginComponent },
  { path: 'intra', component: InternalComponent, canActivate: [AuthGuard] }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
