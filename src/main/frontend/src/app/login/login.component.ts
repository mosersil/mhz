import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  model: any = {};

  feedback: any

  constructor(private _authenticationService: AuthenticationService, public router: Router) {
  }

  async onSubmit() {
    console.log("on submit called");
    await this._authenticationService.login(this.model);
    this.router.navigateByUrl(this._authenticationService.forward);
  }

  ngOnInit() {
    console.log("OnInit called");
  }

}
