import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {ActivatedRoute, Router} from '@angular/router';

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

  async onSubmit() {
    await this._authenticationService.login(this.model).subscribe(data => {
      if (data.errorCode === 0) {
        localStorage.setItem("jwt", data.jwt);
        this._authenticationService.authenticated_change.next(true);
        this.router.navigateByUrl(this.returnUrl);
        return
      } else {
        this.error = data.message;
      }
    }, error1 => {
      this.error = "Etwas lief nun total schief... bitte versuchen Sie es später noch einmal"
    });
  }

  ngOnInit() {
    console.log("OnInit called");
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

}
