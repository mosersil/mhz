import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../calendar.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  events;

  constructor(private _calendarService: CalendarService) {
  }

  ngOnInit() {
    this.getEvents();
  }

  getEvents() {
    this._calendarService.getPublicEvents(5).subscribe(
      data => {
        this.events = data
        console.log(data)
      },
      err => console.error(err),
      () => console.log('done loading events: ' + this.events)
    );
  }

}
