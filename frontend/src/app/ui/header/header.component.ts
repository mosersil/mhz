import { Component, OnInit } from '@angular/core';
import {environment} from "../../../environments/environment";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {

  backendUrl = environment.backendUrl;

  constructor() { }

  ngOnInit() {
  }

}
