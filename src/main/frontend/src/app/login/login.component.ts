import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {ActivatedRoute, Router} from '@angular/router';
import {first} from "rxjs/operators";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  model: any = {};
  error: string;
  returnUrl: string;

  constructor(private _authenticationService: AuthenticationService, private route: ActivatedRoute, public router: Router) {
  }

  onSubmit() {
    this._authenticationService.login(this.model).pipe(first()).subscribe(data => {
      this.router.navigateByUrl(this.returnUrl);
    }, error => {
      this.error = error.error.message;
    });
  }

  ngOnInit() {
    console.log("OnInit called");
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
}
