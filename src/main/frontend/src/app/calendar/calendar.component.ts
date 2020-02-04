import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../common/services/calendar.service";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.sass']
})
export class CalendarComponent implements OnInit {
  backendUrl: string = environment.backendUrl;
  events : Event[];
  showOnlyPublic: boolean = true;
  subscribe_url: string = environment.backendUrl + '/downloads/calendar/ical'

  constructor(private _calendarService: CalendarService) {
  }

  ngOnInit() {
    this.getEvents();
  }

  private getEvents() {
    this._calendarService.getEvents(null, this.showOnlyPublic).subscribe(
      data => {
        this.events = data
      },
      err => console.error(err),
    );
  }


  downloadIcal() {
    this._calendarService.downloadCalendarIcal(true);
  }

  downloadAttachment(id: string, type: string) {
    this._calendarService.downloadAttachment(id, type)
  }


  public toggleShowOnlyPublic() {
    this.showOnlyPublic = !this.showOnlyPublic;
    this.getEvents();
  }


}
