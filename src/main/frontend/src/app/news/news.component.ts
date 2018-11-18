import {Component, OnInit} from '@angular/core';
import {NewsService} from "../news.service";

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.sass']
})
export class NewsComponent implements OnInit {

  hotarticle = null;

  constructor(private _newsService: NewsService) {
  }

  ngOnInit() {
    this.getArticle();
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotarticle = data;
        console.log(data)
      },
      err => console.error(err),
    );
  }

}
