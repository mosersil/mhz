import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from "../../../environments/environment";
import {saveAs} from "file-saver";


const CALENDAR_API_URL = environment.backendUrl + '/public/api/calendar';
const ICAL_API_URL = environment.backendUrl + '/public/api/ical';

@Injectable({
  providedIn: 'root',
})
export class CalendarService {

  constructor(private http: HttpClient) {
  }


  getEvents(max, publicOnly: boolean) {
    let params = new HttpParams().set('publicOnly', String(publicOnly));

    if (max != null) {
      params = params.append("max", String(max));
    }

    return this.http.get<Event[]>(CALENDAR_API_URL, {params: params});

  }

  downloadCalendarIcal(publicOnly: boolean) {
    this.http.get(ICAL_API_URL, {responseType: 'blob'}).subscribe(response => {
      try {
        let isFileSaverSupported = !!new Blob;
      } catch (e) {
        console.log(e);
        return;
      }
      let blob = new Blob([response], {type: 'text/calendar'});
      saveAs(blob, `mhz.ics`);
    });
  }


  downloadAttachment(id: string, type: string) {
    this.http.get(environment.backendUrl + "/public/api/download?id=" + id, {
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
