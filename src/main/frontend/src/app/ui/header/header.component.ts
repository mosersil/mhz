import {Component, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";
import {NewsService} from "../../news.service";
import {AuthenticationService} from "../../authentication.service";
import {Router} from "@angular/router";
import {Role} from "../../role";
import {Person} from "../../person";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {

  hotarticle = null;

  backendUrl = environment.backendUrl;

  constructor(private _newsService: NewsService, private _authenticationService: AuthenticationService, private router: Router) {
  }


  ngOnInit() {
    this.getArticle();
  }

  public logout() {
    this._authenticationService.logout();
    this.router.navigate(['home']);
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
