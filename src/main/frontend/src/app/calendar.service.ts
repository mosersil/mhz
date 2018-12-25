import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../environments/environment";
import {saveAs} from "file-saver";
import {Person} from "./person";


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


  downloadAttachment(id: string, type: string) {
    this.http.get(environment.backendUrl + "/public/api/download?id="+id, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, type));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any, type: string) {
    var blob = new Blob([data], {type: type});

    switch (type) {
      case 'application/vnd.ms-excel':
        saveAs(blob, 'download.xls');
        break;
      case 'application/pdf':
        saveAs(blob, 'download.pdf')
        break;
    }
  }
}
