import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Router} from "@angular/router";
import {Location} from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent implements OnInit {

  model: any = {};

  errormessage: string = null;
  infomessage: string = null;

  constructor(private http: HttpClient, private router: Router, private location: Location) {
  }

  ngOnInit() {
  }

  async onSubmit() {
    console.log("on submit called");

    var data = {
      gender: this.model.gender,
      firstName: this.model.firstName,
      lastName: this.model.lastName,
      email: this.model.email,
      address1: this.model.address1,
      zip: this.model.zip,
      city: this.model.city,
      password: this.model.password,
      password_confirmation: this.model.passwordconfirm
    };

    this.http.post(environment.backendUrl + '/api/public/registration/registerPerson', data).subscribe(success => {
      this.infomessage="Ihr Benutzer account wurde erfolgreich angelegt. Sie können Sich nun einloggen";
    }, error1 => {
      this.errormessage = error1.error.message;
    })
  }

  goBack() {
    this.location.back();
  }
}
