import {Component, Input, OnInit} from '@angular/core';
import {Composition} from "../../common/entities/composition";
import {AuthenticationService} from "../../authentication.service";

@Component({
  selector: 'app-library-list',
  templateUrl: './library-list.component.html',
  styleUrls: ['./library-list.component.scss']
})
export class LibraryListComponent implements OnInit {

  isAdmin: boolean = false;

  @Input() compositions: Composition[];

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.isAdmin = this.authenticationService.isAdmin();
  }


}
