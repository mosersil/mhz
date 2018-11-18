import {Component, OnInit} from '@angular/core';
import {Person} from "../person";
import {AuthenticationService} from "../authentication.service";
import {environment} from "../../environments/environment";


@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.sass']
})
export class InternalComponent implements OnInit {

  backendUrl: string = environment.backendUrl;
  person: Person = new Person();
  errorMessage = null;

  constructor(private _authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    /*
    this._authenticationService.getMe().subscribe(person => {
      this.person = person
    }, error1 => this.errorMessage=error1)
    */
  }

}
