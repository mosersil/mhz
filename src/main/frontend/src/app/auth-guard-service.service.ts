import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import {AuthenticationService} from "./authentication.service";
import {Person} from "./person";
import {Observable, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(public _authenticationService: AuthenticationService, public router: Router, private http: HttpClient) {
  }


  canActivate(): Observable<boolean> {

    let subject = new Subject<boolean>();

    console.log("check whether user is already authenticated");
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
