import {Component, ViewChild} from '@angular/core';
import {ContactResponse} from "../contact-response";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {CaptchaComponent} from "angular-captcha";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.sass']
})
export class ContactComponent {

  model: any = {};
  processing: boolean = false;
  isHuman: boolean = true;

  feedback: any;
  error: any;

  @ViewChild(CaptchaComponent) captchaComponent: CaptchaComponent;


  constructor(private http: HttpClient, private spinner: NgxSpinnerService) {
  }

  onSubmit() {
    this.processing = true;
    this.spinner.show();

    this.captchaComponent.validateUnsafe((isCaptchaCodeCorrect: boolean) => {
      if (isCaptchaCodeCorrect) {
        this.isHuman=true;
        var data = {
          name: this.model.name,
          email: this.model.email,
          message: this.model.message,
          captchaCode: this.captchaComponent.captchaCode,
          captchaId: this.captchaComponent.captchaId
        };
        this.http.post<ContactResponse>(environment.backendUrl + '/api/public/contact', data).subscribe(data => {
          this.error=null;
          this.spinner.hide();
          this.feedback = "Vielen Dank für Ihre Mitteilung";
          this.processing = false;

        }, error1 => {
          this.error = error1.error.message;
        });
      } else {
        this.spinner.hide();
        this.error="Der eingegebene Prüfcode stimmt nicht überein."
        this.feedback=null;
        this.isHuman=false;
        this.processing = false;
      }
    });


  }


}
