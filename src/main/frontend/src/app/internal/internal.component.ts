import {Component, OnInit} from '@angular/core';
import {Person} from "../person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {saveAs} from "file-saver";


@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.sass']
})
export class InternalComponent implements OnInit {

  backendUrl: string = environment.backendUrl;
  person: Person = new Person();
  errorMessage = null;
  year_actual = new Date().getFullYear();
  year_next = this.year_actual+1;
  staticFiles;
  practiceFiles;

  constructor(private http: HttpClient, private _authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.populateGreeting();
    this.getAvailableFiles();
  }


  populateGreeting() {
    this.http.get<Person>(environment.backendUrl + '/auth/user').subscribe(success => {
        this.person = success;
      }, error1 => {
        console.log("error: " + error1.error.message);
      }
    )
  }

  getAvailableFiles() {
    this.http.get(environment.backendUrl + "/api/protected/internal/staticfiles?staticFileCategory=GENERIC").subscribe(sucess => {
      this.staticFiles = sucess;
    }, error1 => {
      console.error(error1);
    });

    this.http.get(environment.backendUrl + "/api/protected/internal/staticfiles?staticFileCategory=PRACTICE").subscribe(sucess => {
      this.practiceFiles = sucess;
    }, error1 => {
      console.error(error1);
    })
  }

  downloadFile(uri: string, type: string, filename: string) {
    this.http.get(environment.backendUrl + uri, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, type, filename));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any, type: string, filename: string) {
    var blob = new Blob([data], {type: type});
    saveAs(blob, filename);


    /*
    var url = window.URL.createObjectURL(blob);
    var pwa = window.open(url);
    if (!pwa || pwa.closed || typeof pwa.closed == 'undefined') {
      alert('Bitte popup-blocker in Ihrem browser deaktivieren.');
    }
    */
  }
}
