import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../calendar.service";
import {saveAs} from "file-saver";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.sass']
})
export class CalendarComponent implements OnInit {
  backendUrl: string = environment.backendUrl;
  events;
  showOnlyPublic: boolean = true;
  subscribeCalendarQrCode: string = "test"
  subscribe_url: string = environment.backendUrl+'/public/api/ical'

  constructor(private _calendarService: CalendarService) {
  }

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


  downloadIcal() {
    console.log("hallo");
    this._calendarService.downloadCalendarIcal(true);
  }



  public toggleShowOnlyPublic() {
    this.showOnlyPublic = !this.showOnlyPublic;
    this.getEvents();
  }


}
