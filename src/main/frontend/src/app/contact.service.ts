import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";


const headerDict = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Access-Control-Allow-Headers': 'Content-Type'
}

const requestOptions = {
  headers: new Headers(headerDict),
};

@Injectable({
  providedIn: 'root'
})
export class ContactService {


  constructor(private http: HttpClient) {
  }


}
