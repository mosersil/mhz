import {Component} from '@angular/core';
import {ContactResponse} from "../contact-response";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.sass']
})
export class ContactComponent {

  model: any = {};

  feedback: any;
  error: any;

  constructor(private http: HttpClient) {
  }

  onSubmit() {
    var data = {
      name: this.model.name,
      email: this.model.email,
      message: this.model.message
    };
    this.http.post<ContactResponse>(environment.backendUrl + '/api/contact', data).subscribe(data => {
      if (data.success) {
        this.feedback = "Vielen Dank für Ihre Mitteilung";
      } else {
        this.error = data.errorDetails
      }
    }, error1 => {
      this.error = error1.error.message;
    })
  }


}
