import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {ContactForm} from "./contact-form";
import {catchError} from "rxjs/operators";
import {stringify} from "querystring";


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


  constructor(private http: HttpClient) { }

  sendContactForm(contact_form: ContactForm) {

    //alert('SUCCESS!! :-)\n\n' + JSON.stringify(contact_form))
    return this.http.post(environment.backendUrl+'/api/contact', contact_form);
  }
}
