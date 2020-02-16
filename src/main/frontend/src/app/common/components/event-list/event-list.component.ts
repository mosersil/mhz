import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.sass']
})
export class EventListComponent implements OnInit {

  @Input()
  events: Event[];

  constructor() { }

  ngOnInit() {
  }

}
