import {Component, OnInit} from '@angular/core';
import {Composition} from "../../common/entities/composition";
import {Subject} from "rxjs";
import {LibraryService} from "../../common/services/library.service";
import {debounceTime, filter, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-library-search',
  templateUrl: './library-search.component.html',
  styleUrls: ['./library-search.component.scss']
})
export class LibrarySearchComponent implements OnInit {

  foundCompositions: Composition[];
  searchTitleKeyUp = new Subject<string>();


  ngOnInit() {
    this.searchTitleKeyUp.pipe(filter(term => term.length >= 3),
      debounceTime(700),
      switchMap(searchTerm => this.libraryService.searchCompositions(searchTerm))
    )
      .subscribe(compositions => {
        this.foundCompositions = compositions
      });
  }


  constructor(private libraryService: LibraryService) {
  }

}
