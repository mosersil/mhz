import {Component, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";
import {NewsService} from "../../news.service";
import {Person} from "../../person";
import {AuthenticationService} from "../../authentication.service";
import {Observable, Subject, Subscription} from "rxjs";
import {Router} from "@angular/router";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {

  hotarticle = null;

  backendUrl = environment.backendUrl;

  constructor(private _newsService: NewsService, private _authenticationService: AuthenticationService, private router: Router) {
    console.log("backend: " + this.backendUrl);
  }


  ngOnInit() {
    console.log("header: onInit called. Backend: " + this.backendUrl);
    this.getArticle();

  }

  public logout() {
    this._authenticationService.logout();
    this.router.navigate(['home']);
  }

  isAuthenticated() {
    return this._authenticationService.isAuthenticated();
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotarticle = data;
      },
      err => console.error(err),
    );
  }

}
