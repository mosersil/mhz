import { Component, OnInit } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-forgot-landing',
  templateUrl: './forgot-landing.component.html',
  styleUrls: ['./forgot-landing.component.sass']
})
export class ForgotLandingComponent implements OnInit {

  model: any = {};

  errormessage: string = null;
  infomessage: string = null;
  token: string = null;
  forward: string = null;

  constructor(private activatedRoute: ActivatedRoute, private http:HttpClient, public router: Router) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token= params['token'];
      this.forward = params['forward'];
    });
  }

  continue() {
    this.router.navigate([this.forward]);
  }

  ngOnInit() {
  }

  async onSubmit() {
    console.log("on submit called");

    var data = {
      password: this.model.password,
      password_confirmation: this.model.passwordconfirm,
      token: this.token
    };

    this.http.post(environment.backendUrl + '/auth/redeemToken', data).subscribe(success => {
      this.infomessage = "Ihr Passwort wurde erfolgreich zurückgesetzt. Sie können Sich nun einloggen";
    }, error1 => {
      this.errormessage = error1.error.message;
    })
  }
}
