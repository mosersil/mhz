import { Component, OnInit } from '@angular/core';
import {CalendarService} from "../calendar.service";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.sass']
})
export class CalendarComponent implements OnInit {

  events;

  constructor(private _calendarService: CalendarService) {  }

  ngOnInit() {
    this.getEvents();
  }

  getEvents() {
    this._calendarService.getEvents(null).subscribe(
      data => {
        this.events = data
        console.log(data)
      },
      err => console.error(err),
      () => console.log('done loading events: ' + this.events)
    );
  }

}
