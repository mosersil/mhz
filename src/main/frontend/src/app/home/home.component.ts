import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../calendar.service";
import {environment} from "../../environments/environment";
import {NewsService} from "../news.service";
import {Article} from "../article";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  events;
  main_background: string = "url('" + environment.backendUrl + "/public/api/background')";
  hotArticle: any;

  constructor(private _calendarService: CalendarService, private _newsService: NewsService) {
  }

  ngOnInit() {
    this.getEvents();
    this.getArticle()
  }

  getEvents() {
    this._calendarService.getPublicEvents(5).subscribe(
      data => {
        this.events = data
      },
      err => console.error(err),
      () => console.log('done loading events: ' + this.events)
    );
  }


  getArticle() {
    this._newsService.getArticle().subscribe(
      data => {
        this.hotArticle = data;
      },
      //err => console.error(err),
    );
  }

}
