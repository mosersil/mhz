import { Component, OnInit } from '@angular/core';
import {ContactForm} from "../contact-form";
import {ContactService} from "../contact.service";
import {RequestOptions} from "@angular/http";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.sass']
})
export class ContactComponent implements OnInit {

  model: any = {};

  feedback: any

  constructor(private _contactService: ContactService) { }

  onSubmit() {
    this._contactService.sendContactForm(this.model).subscribe(data => {this.feedback = data},
      err => console.error(err))
    console.log("test");
  }

  ngOnInit() {
  }

}
