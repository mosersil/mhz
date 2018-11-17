import { Component, OnInit } from '@angular/core';
import {CalendarService} from "../calendar.service";
import {el} from "@angular/platform-browser/testing/src/browser_util";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.sass']
})
export class CalendarComponent implements OnInit {

  events;
  showOnlyPublic: boolean = true;

  constructor(private _calendarService: CalendarService) {  }

  ngOnInit() {
    this.getEvents();
  }

  getEvents() {
      this._calendarService.getEvents(null, this.showOnlyPublic).subscribe(
        data => {
          this.events = data
        },
        err => console.error(err),
      );
  }

  public toggleShowOnlyPublic() {
    this.showOnlyPublic=!this.showOnlyPublic;
    this.getEvents();
  }


}
