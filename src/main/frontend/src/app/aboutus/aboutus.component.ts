import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Membership} from "../common/entities/membership";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";


@Component({
  selector: 'app-aboutus',
  templateUrl: './aboutus.component.html',
  styleUrls: ['./aboutus.component.sass']
})

export class AboutusComponent implements OnInit {

  private fragment: string;
  member_count: number = 0;

  now = new Date();
  years_since = (this.now.getFullYear()-1899)

  members_aktivmitglied: Membership[];
  members_vorstand: Membership[];

  president: Membership[];
  cashier: Membership[];
  secretary: Membership[];
  logistics: Membership[];
  librarian: Membership[];
  board_member: Membership[];
  members_conductor: Membership[];
  members_flute: Membership[];
  members_clarinet: Membership[];
  members_sax: Membership[];
  members_trumpet: Membership[];
  members_horn: Membership[];
  members_bariton: Membership[];
  members_trombone: Membership[];
  members_tuba: Membership[];
  members_percussion: Membership[];


  constructor(private _route: ActivatedRoute, private _http: HttpClient) {
  }

  ngOnInit() {
    try {
      document.querySelector('#' + this.fragment).scrollIntoView();
    } catch (e) {
      console.error(e);
    }

    this.getMembers('Aktivmitglied').subscribe(data => {
      this.members_aktivmitglied = data;

      this.members_aktivmitglied.sort((a: Membership, b: Membership) => {
        if (a.person.lastName > b.person.lastName) {
          return 1;
        }
        if (a.person.lastName == b.person.lastName) {
          return 0;
        }
        return -1;
      });

      this.member_count = this.members_aktivmitglied.length;

      this.members_conductor = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Dirigent') {
          return item;
        }
      });

      this.members_flute = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Querflöte' || item.function === 'Flöte' || item.function === 'Oboe' ) {
          return item;
        }
      });


      this.members_clarinet = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Klarinette') {
          return item;
        }
      });


      this.members_sax = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Saxophon') {
          return item;
        }
      });

      this.members_trumpet = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Trompete') {
          return item;
        }
      });


      this.members_horn = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Waldhorn') {
          return item;
        }
      });


      this.members_bariton = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Bariton' || item.function === 'Tenorhorn' || item.function === 'Euphonium') {
          return item;
        }
      });


      this.members_trombone = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Posaune') {
          return item;
        }
      });

      this.members_tuba = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Tuba' || item.function === 'Bass') {
          return item;
        }
      });

      this.members_percussion = this.members_aktivmitglied.filter(item => {
        if (item.function === 'Schlagzeug' || item.function === 'Perkussion') {
          return item;
        }
      });


    });


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
        if (item.function === 'Präsident' || item.function === 'Präsidentin') {
          return item;
        }
      });


      this.cashier = this.members_vorstand.filter(item => {
          if (item.function === 'Kassier' || item.function === 'Kassierin') {
            return item;
          }
        }
      );

      this.secretary = this.members_vorstand.filter(item => {
          if (item.function === 'Aktuar'  || item.function === 'Aktuarin') {
            return item;
          }
        }
      );

      this.librarian = this.members_vorstand.filter(item => {
          if (item.function === 'Notenverwalter'  || item.function === 'Notenverwalterin' || item.function === 'Bibliothekar'  || item.function === 'Bibliothekarin') {
            return item;
          }
        }
      );

      this.logistics = this.members_vorstand.filter(item => {
          if (item.function === 'Materialverwalter'  || item.function === 'Materialverwalterin') {
            return item;
          }
        }
      );

      this.board_member = this.members_vorstand.filter(item => {
          if (item.function === 'Beisitzer'  || item.function === 'Beisitzerin') {
            return item;
          }
        }
      );

    });

  }

  getMembers(group: string
  ) {
    return this._http.get<Membership[]>(environment.backendUrl + "/api/public/members?group=" + group);
  }

}
