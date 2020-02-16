import {Component, Input, OnInit} from '@angular/core';
import {Event} from "../../entities/event";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.sass']
})
export class EventDetailsComponent {

  @Input()
  event: Event;

  constructor() {
  }

}