import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
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
import {ForgotComponent} from "./forgot/forgot.component";
import {RegisterComponent} from "./register/register.component";
import {PaymentComponent} from "./payment/payment.component";
import {TransactionsComponent} from "./transactions/transactions.component";
import {ForgotLandingComponent} from "./forgot-landing/forgot-landing.component";
import {SignupmemberComponent} from "./signupmember/signupmember.component";
import {SignupmemberLandingComponent} from "./signupmember-landing/signupmember-landing.component";
import {LibraryDetailsComponent} from "./library/library-details/library-details.component";
import {LibraryHomeComponent} from "./library/library-home/library-home.component";
import {LibraryEditComponent} from "./library/library-edit/library-edit.component";
import {LibrarySearchComponent} from "./library/library-search/library-search.component";
import {LibraryCreateCompositionComponent} from "./library/library-create-composition/library-create-composition.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'news', component: NewsComponent},
  {path: 'calendar', component: CalendarComponent},
  {path: 'about_us', component: AboutusComponent},
  {path: 'social', component: SocialComponent},
  {path: 'shop', component: ShopComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'login', component: LoginComponent},
  {path: 'forgot', component: ForgotComponent},
  {path: 'resetpass', component: ForgotLandingComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'signup', component: SignupmemberComponent},
  {path: 'signuplanding', component: SignupmemberLandingComponent},
  {path: 'payment', component: PaymentComponent, canActivate: [AuthGuard]},
  {path: 'intra', component: InternalComponent, canActivate: [AuthGuard]},
  {path: 'transactions', component: TransactionsComponent, canActivate: [AuthGuard]},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'app', redirectTo: '/app'},
  {path: 'library', component: LibraryHomeComponent},
  {path: 'library/archive', component: LibrarySearchComponent},
  {path: 'library/create', component: LibraryCreateCompositionComponent},
  {path: 'library/composition/edit/:id', component: LibraryEditComponent},
  {path: 'library/composition/:id', component: LibraryDetailsComponent},
  {path: '**', component: HomeComponent},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
