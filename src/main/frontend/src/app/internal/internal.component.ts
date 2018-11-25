import {Component, OnInit} from '@angular/core';
import {Person} from "../person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {saveAs} from "file-saver";
import {RequestOptions, ResponseContentType} from "@angular/http";


@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.sass']
})
export class InternalComponent implements OnInit {

  backendUrl: string = environment.backendUrl;
  person: Person = new Person();
  errorMessage = null;
  year_actual = 2018
  year_next = 2019

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


  downloadFile(uri: string, type: string) {
    this.http.get(environment.backendUrl + uri, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, type));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any, type: string) {
    var blob = new Blob([data], {type: type});

    switch (type) {
      case 'application/vnd.ms-excel':
        saveAs(blob, 'download.xls');
        break;
      case 'application/pdf':
        saveAs(blob, 'download.pdf')
        break;
    }
    /*
    var url = window.URL.createObjectURL(blob);
    var pwa = window.open(url);
    if (!pwa || pwa.closed || typeof pwa.closed == 'undefined') {
      alert('Bitte popup-blocker in Ihrem browser deaktivieren.');
    }
    */
  }
}
