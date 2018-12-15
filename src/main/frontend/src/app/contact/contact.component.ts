import {Component, ViewChild} from '@angular/core';
import {ContactResponse} from "../contact-response";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {CaptchaComponent} from "angular-captcha";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.sass']
})
export class ContactComponent {

  model: any = {};
  processing: boolean = false;

  feedback: any;
  error: any;

  @ViewChild(CaptchaComponent) captchaComponent: CaptchaComponent;


  constructor(private http: HttpClient) {
  }

  onSubmit() {
    this.processing = true;

    this.captchaComponent.validateUnsafe((isCaptchaCodeCorrect: boolean) => {
      if (isCaptchaCodeCorrect) {
        var data = {
          name: this.model.name,
          email: this.model.email,
          message: this.model.message,
          captchaCode: this.captchaComponent.captchaCode,
          captchaId: this.captchaComponent.captchaId
        };
        this.http.post<ContactResponse>(environment.backendUrl + '/api/public/contact', data).subscribe(data => {
          this.error=null;
          this.feedback = "Vielen Dank für Ihre Mitteilung";
          setTimeout(function(){
            this.processing = false;
          },15000);

        }, error1 => {
          this.error = error1.error.message;
        });
      } else {
        this.feedback=null;
        this.error = "Der eingegebene Prüfcode ist nicht korrekt.";
        this.processing = false;
      }
    });


  }


}
