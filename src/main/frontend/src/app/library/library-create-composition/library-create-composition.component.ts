import {Component, OnInit} from '@angular/core';
import {LibraryService} from "../../common/services/library.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Composition} from "../../common/entities/composition";
import {CompositionFactory} from "../../common/factories/composition-factory";

@Component({
  selector: 'app-library-create-composition',
  templateUrl: './library-create-composition.component.html',
  styleUrls: ['./library-create-composition.component.sass']
})
export class LibraryCreateCompositionComponent implements OnInit {

  constructor(private ls:LibraryService,
              private route:ActivatedRoute,
              private router:Router) {

  }

  ngOnInit() {
  }

  createComposition(composition: Composition) {
    this.ls.create(composition).subscribe(() => {
      this.router.navigate(['/library']);
    });
  }

  emptyComposition<Composition>() {
    return CompositionFactory.empty();
  }

}
