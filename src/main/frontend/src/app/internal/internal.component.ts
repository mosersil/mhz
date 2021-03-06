import {Component, OnInit} from '@angular/core';
import {Person} from "../common/entities/person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {saveAs} from "file-saver";
import {ChangePasswordResponse} from "../change-password-response";
import {IMyDpOptions} from 'mydatepicker';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Composition} from "../common/entities/composition";
import {LibraryService} from "../common/services/library.service";


@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.sass']
})
export class InternalComponent implements OnInit {

  backendUrl: string = environment.backendUrl;
  person: Person = new Person();
  infoMessage = null;
  errorMessage = null;
  year_actual = new Date().getFullYear();
  year_next = this.year_actual + 1;
  staticFiles;
  internalFiles;
  practiceFiles;
  model: any = {};
  modelBirthDate: any = {};
  processing: boolean = false;
  passwordchange_feedback: string;
  changePasswordSuccess: boolean;
  error_currentpassword: boolean;
  error_currentpassword_msg: string;
  error_newpassword: boolean;
  error_newpassword_msg: string;
  error_confirmpassword: boolean;
  error_confirmpassword_msg: string;
  today = new Date();
  closeResult: string;
  currentCompositions: Composition[];


  constructor(private libraryService: LibraryService, private http: HttpClient, private _authenticationService: AuthenticationService, private modalService: NgbModal) {
  }

  ngOnInit() {
    this.reloadData();
    this.getAvailableFiles();
    this.libraryService.current().subscribe(data => {
      this.currentCompositions=data;
    });
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      //this.errorMessage = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }


  reloadData() {
    this.http.get<Person>(environment.backendUrl + '/auth/user').subscribe(success => {
        this.person = null;
        this.model.person = null;
        this.person = success;
        console.log(this.person);
        let clonePerson = Object.assign({}, this.person);
        this.model.person = clonePerson;
        this.setDate(new Date(this.person.birthDate));
      }, error1 => {
        console.log("error: " + error1.error.message);
      }
    )
  }

  public myDatePickerOptions: IMyDpOptions = {
    alignSelectorRight: true,
    showInputField: true,
    disableHeaderButtons: false,
    editableDateField: false,
    dateFormat: "dd.mm.yyyy",

    disableSince: {year: this.today.getFullYear(), month: this.today.getMonth() + 1, day: this.today.getDate() + 1}
  };


  setDate(theDate: Date): void {
    // for example set date: 09.10.2018, value of year, month and day must be number
    this.modelBirthDate = {date: {year: theDate.getFullYear(), month: theDate.getMonth() + 1, day: theDate.getDate()}};
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

  onAddressDataChangeSubmit() {
    this.processing = true;

    var data = {
      gender: this.model.person.gender,
      company: this.model.person.company,
      title: this.model.person.title,
      firstName: this.model.person.firstName,
      lastName: this.model.person.lastName,
      address1: this.model.person.address1,
      address2: this.model.person.address2,
      zip: this.model.person.zip,
      city: this.model.person.city
    };

    this.http.post(environment.backendUrl + '/api/protected/internal/address', data).subscribe(success => {
      this.errorMessage = null;
      this.infoMessage = "Ihre Adressdaten wurden erfolgreich geändert";
      this.reloadData();
    }, error1 => {
      this.errorMessage = error1.error.message;
    })
    this.processing = false;
    this.modalService.dismissAll();
  }

  onContactDataChangeSubmit() {
    this.processing = true;

    var data = {
      mobile: this.model.person.mobile,
      landline: this.model.person.landline,
      email: this.model.person.email,
    };

    this.http.post(environment.backendUrl + '/api/protected/internal/contactdetails', data).subscribe(success => {
      this.errorMessage = null;
      this.infoMessage = "Ihre Kontaktdaten wurden erfolgreich geändert";
      this.reloadData();
    }, error1 => {
      this.errorMessage = error1.error.message;
    })
    this.processing = false;
    this.modalService.dismissAll();
  }


  onBirthdayChangeSubmit() {
    this.processing = true;

    if (this.modelBirthDate != null) {
      var data = {
        year: this.modelBirthDate.date.year,
        month: this.modelBirthDate.date.month,
        day: this.modelBirthDate.date.day
      };

      this.http.post(environment.backendUrl + '/api/protected/internal/birthday', data).subscribe(success => {
        this.errorMessage = null;
        this.infoMessage = "Ihr Geburtsdatum wurde erfolgreich geändert";
        this.reloadData();
      }, error1 => {
        this.errorMessage = error1.error.message;
      })
    }
    this.processing = false;
    this.modalService.dismissAll();
  }


  onPWChangeSubmit() {
    this.processing = true;
    var data = {
      currentPassword: this.model.currentPassword,
      newPassword: this.model.newPassword,
      confirmPassword: this.model.confirmPassword,
    };

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
      if (success === null || success.errors.length < 1) {
        this.passwordchange_feedback = "Das neue Passwort ist ab sofort aktiv.";
        this.changePasswordSuccess = true;
      }
      else {
        success.errors.forEach(it => {
          if (it.errorContext === "currentPassword") {
            this.error_currentpassword = true;
            this.error_currentpassword_msg = it.errorMessage;
          }
          if (it.errorContext === "newPassword") {
            this.error_newpassword = true;
            this.error_newpassword_msg = it.errorMessage;
          }
          if (it.errorContext === "confirmPassword") {
            this.error_confirmpassword = true;
            this.error_confirmpassword_msg = it.errorMessage;
          }
        })
      }
      ;

      this.processing = false;

    }, error1 => {
      console.log(error1);
    });


  }

}
