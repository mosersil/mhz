import {Component, OnInit} from '@angular/core';
import {Composition} from "../../common/entities/composition";
import {LibraryService} from "../../common/services/library.service";
import {Repertoire} from "../../common/entities/repertoire";

@Component({
  selector: 'app-library-home',
  templateUrl: './library-home.component.html',
  styleUrls: ['./library-home.component.scss']
})
export class LibraryHomeComponent implements OnInit {

  repertoires: Repertoire[];
  currentCompositions: Composition[];


  ngOnInit() {

    this.libraryService.getRepertoires().subscribe(data => {
      this.repertoires=data;
    });

    this.libraryService.current().subscribe(data => {
      this.currentCompositions=data;
    });
  }


  constructor(private libraryService: LibraryService) {
  }

}
