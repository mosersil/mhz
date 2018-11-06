import { Component, OnInit } from '@angular/core';
import {environment} from "../../../environments/environment";
import {NewsService} from "../../news.service";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {

  hotarticle=null;

  backendUrl = environment.backendUrl;

  constructor(private _newsService: NewsService) { }

  ngOnInit() {
    this.getArticle();
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotarticle=data;
        console.log(data)
      },
      err => console.error(err),
    );
  }

}
