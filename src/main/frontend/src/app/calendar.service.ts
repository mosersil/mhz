import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../environments/environment";
import {saveAs} from "file-saver";


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root',
})
export class CalendarService {

  constructor(private http: HttpClient) {
  }


  getEvents(max, publicOnly) {
    let url = '/public/api/calendar?';

    if (publicOnly) {
      url = url + "publicOnly=true";
    } else {
      url = url + "publicOnly=false";
    }

    if (max == null) {
      //do nothing
    }
    else {
      url = url + "&max=" + max;
    }

    return this.http.get<Event[]>(environment.backendUrl + url);

  }

  getPublicEvents(max) {
    if (max == null) {
      return this.http.get<Event[]>(environment.backendUrl + '/public/api/calendar?publicOnly=true');
    } else {
      return this.http.get<Event[]>(environment.backendUrl + '/public/api/calendar?publicOnly=true&max=' + max);
    }
  }

  downloadCalendarIcal(publicOnly:boolean) {
    this.http.get(environment.backendUrl+'/public/api/ical', {responseType: 'blob'}).subscribe(response => {
      try {
        let isFileSaverSupported = !!new Blob;
      } catch (e) {
        console.log(e);
        return;
      }
      let blob = new Blob([response], { type: 'text/calendar' });
      saveAs(blob, `mhz.ics`);
    });
  }

}
