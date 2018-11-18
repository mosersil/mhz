import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../calendar.service";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  events;
  backend: string = environment.backendUrl;
  main_background: string = "url('"+environment.backendUrl+"/public/api/background')";

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
