import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from "./authentication.service";
import {Observable, Subject} from "rxjs";
import {state} from "@angular/animations";
import {el} from "@angular/platform-browser/testing/src/browser_util";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(public _authenticationService: AuthenticationService, public router: Router) {
    this._authenticationService.getMe();
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    this._authenticationService.getMe();
    console.log(this._authenticationService.authenticated)
    if (this._authenticationService.authenticated) {
      console.log("user is authenticated")
      return true;
    } else {
      console.log("user is not authenticated")
      this.router.navigate(['login'], {queryParams: {returnUrl: state.url}});
      return false;
    }
  }

}
