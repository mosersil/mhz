import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Person} from "./person";
import {LoginForm} from "./login-form";
import {Observable, Subject} from "rxjs";
import {LoginResponse} from "./login-response";

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
      console.log("success: " + success);
        this.me_change.next(success);
        this.authenticated_change.next(true);
      }, error1 => {
      console.log("error: " + error1.error.message);
        this.me_change.next(null);
        this.authenticated_change.next(false);
      }
    )
  }


  login(login_form: LoginForm): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(environment.backendUrl + '/login', login_form)
  }

  logout() {
    return this.http.get(environment.backendUrl + '/logout');
  }
}
