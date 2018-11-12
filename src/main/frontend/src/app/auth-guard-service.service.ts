import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthenticationService} from "./authentication.service";
import {Observable, Subject} from "rxjs";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(public _authenticationService: AuthenticationService, public router: Router) {
  }


  canActivate(): Observable<boolean> {

    let subject = new Subject<boolean>();

    console.log("check whether user is already authenticated");
    this._authenticationService.forward="intra";
    this._authenticationService.getMe().subscribe(response => {
        subject.next(true);
      }, error1 => {
        this.router.navigateByUrl("/login");
        subject.next(false);
      }, () => {
        subject.complete();
      }
    );
    return subject;
  }

}
