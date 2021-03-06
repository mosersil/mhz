import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../common/services/calendar.service";
import {environment} from "../../environments/environment";
import {NewsService} from "../news.service";
import {Article} from "../common/entities/article";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  events: Event[];
  main_background: string = "url('" + environment.backendUrl + "/public/api/background')";
  hotArticles: Article[];
  now = new Date();
  years_since = (this.now.getFullYear()-1899)

  constructor(private _calendarService: CalendarService, private _newsService: NewsService) {
  }

  ngOnInit() {
    this.getEvents();
    this.getArticle()
  }

  getEvents() {
    this._calendarService.getEvents(3, true).subscribe(
      data => {
        this.events = data
      },
      err => console.error(err)
    );
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotArticles = data;
      },
      //err => console.error(err),
    );
  }

}
