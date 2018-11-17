import {Component, OnInit} from '@angular/core';
import {Person} from "../person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {saveAs} from "file-saver";


@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.sass']
})
export class InternalComponent implements OnInit {

  backendUrl: string = environment.backendUrl;
  person: Person = new Person();
  errorMessage=null;

  constructor(private http: HttpClient, private _authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.populateGreeting();
  }


  populateGreeting() {
    this.http.get<Person>(environment.backendUrl + '/auth/user').subscribe(success => {
        this.person = success;
      }, error1 => {
        console.log("error: " + error1.error.message);
      }
    )
  }

  downloadFile(uri: string) {
    this.http.get(this.backendUrl+uri, {responseType: 'blob'}).subscribe(response => {
      console.log("get successful")
      try {
        let isFileSaverSupported = !!new Blob;
      } catch (e) {
        console.log(e);
        return;
      }
      let blob = new Blob([response], { type: 'image/jpeg' });
      saveAs(blob, `file.png`);
    });
  }



}
