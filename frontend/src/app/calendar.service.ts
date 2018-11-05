import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../environments/environment";


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root',
})
export class CalendarService {

  constructor(private http: HttpClient) {
  }

  // Uses http.get() to load data from a single API endpoint
  getEvents(max) {

    if (max==null) {
      return this.http.get<Event[]>(environment.backendUrl+'/public/api/calendar');
    } else {
      return this.http.get<Event[]>(environment.backendUrl+'/public/api/calendar?max='+max);
    }

  }

}
