import {Component, OnInit} from '@angular/core';
import {NewsService} from "../news.service";
import {CalendarService} from "../calendar.service";

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.sass']
})
export class NewsComponent implements OnInit {

  hotarticle = null;

  constructor(private _newsService: NewsService, private _calendarService: CalendarService) {
  }

  ngOnInit() {
    this.getArticle();
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotarticle = data;
      },
      err => console.error(err),
    );
  }

  downloadAttachment(id: string, type: string) {
    this._calendarService.downloadAttachment(id, type)
  }

}
