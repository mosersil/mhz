import { Component, OnInit } from '@angular/core';
import {Membership} from "../../common/entities/membership";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";


const URL_MEMBERS = environment.backendUrl + "/api/public/members";



@Component({
  selector: 'app-legal',
  templateUrl: './legal.component.html',
  styleUrls: ['./legal.component.scss']
})
export class LegalComponent implements OnInit {


  members_vorstand: Membership[];
  president: Membership[];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getMembers('Vorstand').subscribe(data => {
      this.members_vorstand = data;

      this.members_vorstand.sort((a: Membership, b: Membership) => {
        if (a.person.lastName > b.person.lastName) {
          return 1;
        }
        if (a.person.lastName == b.person.lastName) {
          return 0;
        }
        return -1;
      });

      this.president = this.members_vorstand.filter(item => {
        if (item.function === 'Präsident') {
          return item;
        }
      });
    });
  }

  getMembers(group: string) {
    return this.http.get<Membership[]>(URL_MEMBERS + "?group=" + group);
  }

}
