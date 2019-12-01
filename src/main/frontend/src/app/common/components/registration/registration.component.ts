import {Component, OnInit, ViewChild} from '@angular/core';
import {CaptchaComponent} from "angular-captcha";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Location} from "@angular/common";
import {NgxSpinnerService} from "ngx-spinner";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  model: any = {};
  processing: boolean = false;
  isHuman: boolean = true;

  errormessage: string = null;
  infomessage: string = null;
  registrationSubmitted: boolean = false;

  @ViewChild(CaptchaComponent, {static: true})
  captchaComponent: CaptchaComponent;

  constructor(private http: HttpClient, private router: Router, private location: Location, private spinner: NgxSpinnerService,) {
  }

  ngOnInit() {
  }

  async onSubmit() {
    console.log("on submit called");

    this.processing = true;
    this.spinner.show();

    this.captchaComponent.validateUnsafe((isCaptchaCodeCorrect: boolean) => {
      if (isCaptchaCodeCorrect) {
        this.isHuman = true;

        var data = {
          gender: this.model.gender,
          firstName: this.model.firstName,
          lastName: this.model.lastName,
          email: this.model.email,
          address1: this.model.address1,
          zip: this.model.zip,
          city: this.model.city,
          password: this.model.password,
          password_confirmation: this.model.passwordconfirm,
          preferredChannel: this.model.preferredChannel,
          captchaCode: this.captchaComponent.captchaCode,
          captchaId: this.captchaComponent.captchaId
        };
        this.http.post(environment.backendUrl + '/api/public/registration/registerPerson', data).subscribe(success => {
          this.errormessage = null;
          this.spinner.hide();
          this.processing = false;
          this.registrationSubmitted=true;
        }, error1 => {
          this.errormessage = error1.error.message;
          this.spinner.hide();
          this.infomessage = null;
          this.isHuman = false;
          this.processing = false;
        })
      } else {
        this.isHuman=false;
        this.spinner.hide();
        this.processing = false;
      }
    });
  }

}
