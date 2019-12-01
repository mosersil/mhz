import {Component, Input} from '@angular/core';
import {Composition} from "../../common/entities/composition";

@Component({
  selector: 'app-library-list',
  templateUrl: './library-list.component.html',
  styleUrls: ['./library-list.component.scss']
})
export class LibraryListComponent {

  @Input() compositions: Composition[];

  constructor() { }


}
