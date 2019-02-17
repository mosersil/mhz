import {Component, OnInit} from '@angular/core';
import {Person} from "../person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {saveAs} from "file-saver";
import {ChangePasswordResponse} from "../change-password-response";


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
  year_next = this.year_actual + 1;
  staticFiles;
  internalFiles;
  practiceFiles;
  model: any = {};
  processing: boolean = false;
  passwordchange_feedback: string;
  changePasswordSuccess: boolean;
  error_currentpassword: boolean;
  error_currentpassword_msg: string;
  error_newpassword: boolean;
  error_newpassword_msg: string;
  error_confirmpassword: boolean;
  error_confirmpassword_msg: string;


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

    this.http.get(environment.backendUrl + "/api/protected/internal/staticfiles?staticFileCategory=INTERNAL_DOC").subscribe(sucess => {
      this.internalFiles = sucess;
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
  }


  onPWChangeSubmit() {
    this.processing = true;

    var data = {
      currentPassword: this.model.currentPassword,
      newPassword: this.model.newPassword,
      confirmPassword: this.model.confirmPassword,
    };
    this.http.post<ChangePasswordResponse>(environment.backendUrl + '/api/protected/auth/changepassword', data).subscribe(success => {
      console.log(success);
      this.error_newpassword = false;
      this.error_confirmpassword = false;
      this.error_currentpassword = false;
      if (success===null || success.errors.length<1) {
        this.passwordchange_feedback = "Das neue Passwort ist ab sofort aktiv.";
        this.changePasswordSuccess = true;
      }
      else {
        success.errors.forEach( it => {
          if (it.errorContext==="currentPassword") {
            this.error_currentpassword = true;
            this.error_currentpassword_msg = it.errorMessage;
          }
          if (it.errorContext==="newPassword") {
            this.error_newpassword = true;
            this.error_newpassword_msg = it.errorMessage;
          }
          if (it.errorContext==="confirmPassword") {
            this.error_confirmpassword = true;
            this.error_confirmpassword_msg = it.errorMessage;
          }
        })
      };

      this.processing = false;

    }, error1 => {
      console.log(error1);
    });


  }

}
