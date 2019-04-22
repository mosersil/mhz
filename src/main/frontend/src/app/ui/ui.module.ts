import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LayoutComponent} from './layout/layout.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {RouterModule} from "@angular/router";
import {AppRoutingModule} from "../app-routing.module";
import {FormsModule} from '@angular/forms';
import {CookieLawModule} from "angular2-cookie-law";

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    AppRoutingModule,
    FormsModule,
    CookieLawModule
  ],
  declarations: [LayoutComponent, HeaderComponent, FooterComponent],
  exports: [LayoutComponent]
})
export class UiModule {
}
