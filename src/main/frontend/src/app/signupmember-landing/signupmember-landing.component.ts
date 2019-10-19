import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-signupmember-landing',
  templateUrl: './signupmember-landing.component.html',
  styleUrls: ['./signupmember-landing.component.scss']
})
export class SignupmemberLandingComponent implements OnInit {

  model: any = {};

  errormessage: string = null;
  infomessage: string = null;
  token: string = null;
  verified=false;
  passwordset=false;

  constructor(private activatedRoute: ActivatedRoute, private http:HttpClient, public router: Router) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token= params['token'];
    });
  }

  continue() {
    this.router.navigateByUrl("/")
  }

  ngOnInit() {
    this.http.get(environment.backendUrl + '/api/public/registration/verifyEmail?token='+ this.token).subscribe(success => {
      this.infomessage = "Vielen Dank, Ihre E-Mail Adresse wurde erfolgreich verifiziert. Willkommen beim MHZ-Oberstrass!";
      this.verified=true;
    }, error1 => {
      this.errormessage = error1.error.message;
      this.verified=false;
    })
  }

  async onSubmit() {
    var data = {
      token: this.token,
      password: this.model.password,
      passwordConfirmation: this.model.passwordConfirmation
    };
    this.http.post(environment.backendUrl + '/api/public/registration/setPassword', data).subscribe(success => {
      this.errormessage=null;
      this.infomessage = "Ihr Account ist jetzt mit einem Passwort geschützt.";
      this.passwordset=true;
    }, error1 => {
      this.errormessage = error1.error.message;
      this.infomessage = null;
      this.passwordset=false;
    })
  }
}
