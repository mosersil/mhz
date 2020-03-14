import {Component, Input, OnInit} from '@angular/core';
import {Composition} from "../../common/entities/composition";
import {LibraryService} from "../../common/services/library.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from '@angular/common';
import {AuthenticationService} from "../../authentication.service";

@Component({
  selector: 'app-library-details',
  templateUrl: './library-details.component.html',
  styleUrls: ['./library-details.component.scss']
})
export class LibraryDetailsComponent implements OnInit {

  @Input() composition: Composition;
  isAdmin: boolean = false;

  constructor(
    private libraryService: LibraryService,
    private route: ActivatedRoute,
    private _location: Location,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    const params = this.route.snapshot.paramMap;
    this.libraryService.getComposition(params.get('id')).subscribe(data => {
      this.composition = data;
    });

    this.isAdmin = this.authenticationService.isAdmin();
  }

  backClicked() {
    this._location.back();
  }

  download(id: string) {
    this.libraryService.downloadFile(id);
  }

}
