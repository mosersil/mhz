import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Person} from "./person";
import {LoginForm} from "./login-form";
import {Observable, Subject} from "rxjs";
import {reject} from "q";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  forward: string

  constructor(private http: HttpClient) {
  }


  // Uses http.get() to load data from a single API endpoint
  getMe():Observable<Person> {
    if (localStorage.getItem("me") === null) {
      console.log("Could not find something in local storage");
      return this.http.get<Person>(environment.backendUrl + '/auth/user');
    } else {
      console.log("found something in local storage, returning it")
      const person = JSON.parse(localStorage.getItem("me"));
      return person;
    }
  }

  isAuthenticated(): Observable<boolean> {
    let subject = new Subject<boolean>();
    this.getMe().subscribe(response => {
        subject.next(true);
      }, error1 => {
        subject.next(false);
      }, () => {
        subject.complete();
      }
    );
    return subject;
  }

  async login(login_form: LoginForm) {
    console.log("login() called")
    await this.http.post(environment.backendUrl + '/login', login_form).toPromise()
      .then(data => {
      console.log("login executed. All is well...");
    }, error1 => {
      console.log("login failed...");
      //do noting else
    });
  }

  logout() {
    return this.http.get(environment.backendUrl + '/logout');
  }
}
