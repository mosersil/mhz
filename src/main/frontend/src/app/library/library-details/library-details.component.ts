import {Component, OnInit} from '@angular/core';
import {Composition} from "../../common/entities/composition";
import {LibraryService} from "../../common/services/library.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from '@angular/common';

@Component({
  selector: 'app-library-details',
  templateUrl: './library-details.component.html',
  styleUrls: ['./library-details.component.scss']
})
export class LibraryDetailsComponent implements OnInit {

  composition: Composition;

  constructor(
    private libraryService: LibraryService,
    private route: ActivatedRoute,
    private _location: Location,
  ) { }

  ngOnInit() {
    const params = this.route.snapshot.paramMap;
    this.libraryService.getComposition(params.get('id')).subscribe(data => {
      this.composition = data;
    })
  }

  backClicked() {
    this._location.back();
  }

  download(id: string) {
    this.libraryService.downloadFile(id);
  }

  playerDownload(id: string) {
    this.libraryService.downloadSample(id);
  }


}
