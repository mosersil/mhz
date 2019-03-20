import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {LoginForm} from "./login-form";
import {BehaviorSubject, Observable} from "rxjs";
import * as jwt_decode from 'jwt-decode';
import {LoginResponse} from "./auth/login-response";
import {map} from "rxjs/operators";
import {AuthUser} from "./auth/auth-user";


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<AuthUser>;
  public currentUser: Observable<AuthUser>;


  public getMe() {
    var loginResponse = new LoginResponse();
    loginResponse = JSON.parse(localStorage.getItem('auth'));
    return loginResponse.authUser;
  }

  public getToken(): string {
    if (localStorage.getItem('auth')==null) {return null};
    var retrievedObject = JSON.parse(localStorage.getItem('auth'));
    return (<LoginResponse>retrievedObject).jwt
  }

  public isAuthenticated(): boolean {
    // get the token
    const token = this.getToken();
    // return a boolean reflecting
    // whether or not the token is expired
    return token!=null && !this.isTokenExpired(token);
  }

  public isAdmin(): boolean {
    // get the token
    var retrievedObject = JSON.parse(localStorage.getItem('auth'));
    var authUser = (<LoginResponse>retrievedObject).authUser;
    if (authUser.authRoles.some(it => {
      return it.name==='ADMIN';
    })) {
      return true;
    }
    return false;
  }

  getTokenExpirationDate(token: string): Date {
    const decoded = jwt_decode(token);

    if (decoded.exp === undefined) return null;

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  isTokenExpired(token?: string): boolean {
    if (!token) token = this.getToken();
    if (!token) return true;

    const date = this.getTokenExpirationDate(token);
    if (date === undefined) return false;
    return !(date.valueOf() > new Date().valueOf());
  }



  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<AuthUser>(JSON.parse(localStorage.getItem('me')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): AuthUser {
    return this.currentUserSubject.value;
  }


  login(login_form: LoginForm): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(environment.backendUrl + '/login', login_form)
      .pipe(map(response => {
        // login successful if there's a jwt token in the response
        if (response && response.jwt) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('auth', JSON.stringify(response));
          this.currentUserSubject.next(response.authUser);
        }

        return response;
      }));


  }

  logout() {
    localStorage.removeItem("auth");
    return this.http.get(environment.backendUrl + '/logout');
  }
}
