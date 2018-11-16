import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {environment} from "../../../environments/environment";
import {NewsService} from "../../news.service";
import {Person} from "../../person";
import {AuthenticationService} from "../../authentication.service";
import {Observable, Subject, Subscription} from "rxjs";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {

  hotarticle = null;


  person$: Observable<Person>;


  backendUrl = environment.backendUrl;

  constructor(private _newsService: NewsService, private _authenticationService: AuthenticationService) {
    console.log("backend: " + this.backendUrl);
  }

  subject = new Subject<boolean>();

  ngOnInit() {
    console.log("header: onInit called. Backend: " + this.backendUrl);
    this.getArticle();

  }

  public logout() {
    this._authenticationService.logout();
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
