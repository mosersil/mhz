import {Component, OnInit} from '@angular/core';
import {NewsService} from "../news.service";
import {CalendarService} from "../common/services/calendar.service";
import {Article} from "../common/entities/article";

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.sass']
})
export class NewsComponent implements OnInit {

  hotArticles: Article[];

  constructor(private _newsService: NewsService, private _calendarService: CalendarService) {
  }

  ngOnInit() {
    this.getArticle();
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotArticles = data;
      },
      err => console.error(err),
    );
  }

  downloadAttachment(id: string, type: string) {
    this._calendarService.downloadAttachment(id, type)
  }

}
