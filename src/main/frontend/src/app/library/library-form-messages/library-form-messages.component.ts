import {Component, Input} from '@angular/core';
import {AbstractControl} from "@angular/forms";

@Component({
  selector: 'app-library-form-messages',
  templateUrl: './library-form-messages.component.html',
  styleUrls: ['./library-form-messages.component.sass']
})
export class LibraryFormMessagesComponent {

  @Input() control: AbstractControl;
  @Input() controlName: string;


  private allMessages = {
    title: {
      required: 'Ein Titel muss angegeben werden.',
      minlength: 'Der Titel muss mindestens 3 Zeichen haben'
    },
    composers: {
      required: 'Es muss ein Komponist angegeben werden.'
    }
  };

  constructor() { }

  errorsForControl(): string[] {
    const messages = this.allMessages[this.controlName];

    if (
      !this.control ||
      !this.control.errors ||
      !messages ||
      !this.control.dirty
    ) { return null; }

    return Object.keys(this.control.errors)
      .map(err => messages[err]);
  }

}
