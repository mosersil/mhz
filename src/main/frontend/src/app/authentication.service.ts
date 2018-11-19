import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Person} from "./person";
import {LoginForm} from "./login-form";
import {Observable, Subject} from "rxjs";
import {LoginResponse} from "./login-response";
import * as jwt_decode from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  forward: string

  me: Person;
  authenticated: boolean
  me_change: Subject<Person> = new Subject<Person>();
  authenticated_change: Subject<boolean> = new Subject<boolean>()

  constructor(private http: HttpClient) {
    this.me_change.subscribe(value => {
      this.me = value;
    });
    this.authenticated_change.subscribe(value => {
      this.authenticated = value;
    })
    this.getMe();
  }


  // Uses http.get() to load data from a single API endpoint
  async getMe() {
    this.http.get<Person>(environment.backendUrl + '/auth/user').subscribe(success => {
        this.me_change.next(success);
        this.authenticated_change.next(true);
      }, error1 => {
      console.log("error: " + error1.error.message);
        this.me_change.next(null);
        this.authenticated_change.next(false);
      }
    )
  }

  public getToken(): string {
    return localStorage.getItem('jwt');
  }

  public isAuthenticated(): boolean {
    // get the token
    const token = this.getToken();
    // return a boolean reflecting
    // whether or not the token is expired
    return !this.isTokenExpired(token);
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


  login(login_form: LoginForm): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(environment.backendUrl + '/login', login_form)
  }

  logout() {
    localStorage.removeItem("jwt");
    return this.http.get(environment.backendUrl + '/logout')
  }
}
