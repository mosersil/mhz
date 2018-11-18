import {Component, OnInit} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.sass']
})
export class ForgotComponent implements OnInit {

  model: any = {};
  infomessage: string;
  errormessage: string;

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
  }

  onSubmit() {
    var data = {
      email: this.model.email,
      username: this.model.email
    };

    this.http.post(environment.backendUrl + '/auth/initPwReset', data).subscribe(success => {
      this.infomessage = "Eine E-Mail mit weiteren Anweisungen wurde an Ihre E-Mail Adresse gesandt. Bitte prüfen Sie Ihren E-Mail Posteingang und befolgen Sie die weiteren Anweisungen";
    }, error1 => {
      this.errormessage = error1.error.message;
    })
  }

}
