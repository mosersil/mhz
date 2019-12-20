import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {saveAs} from "file-saver";
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-addresslist-generator',
  templateUrl: './addresslist-generator.component.html',
  styleUrls: ['./addresslist-generator.component.sass']
})
export class AddresslistGeneratorComponent implements OnInit {



  organizationData = [
    { id: "Aktivmitglied", name: "Aktivmitglieder", selected: true},
    { id: "Passivmitglied", name: "Passivmitglieder", selected: false },
    { id: "Ehrenmitglied", name: "Ehrenmitglieder", selected: false },
    { id: "Freimitglied", name: "Freimitglieder", selected: false },
    { id: "Sonstige", name: "Sonstige", selected: false }
  ];

  minimumOneSelected: boolean = true;

  form: FormGroup;



  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit() {
    this.form = this.fb.group( {
      organizations: this.buildOrganizations(),
      format: ['PDF', [Validators.required]]
    });
    this.form.controls['organizations'].valueChanges.subscribe(value => {
      this.minimumOneSelected = value.some(function (item) {
        return item;
      });
    })
  }

  get organizations() {
    return this.form.get('organizations');
  }

  buildOrganizations() {
    const organizations = this.organizationData.map(organization => {
      return this.fb.control(organization.selected);
    });
    return this.fb.array(organizations);
  }

  submit() {
    const format = this.form.value.format;
    const selectedorganizations = this.form.value.organizations
      .map((v, i) => v ? this.organizationData[i].id : null)
      .filter(v => v !== null);

    this.http.get(environment.backendUrl + "/api/protected/internal/addresslist?organization="+encodeURIComponent(selectedorganizations)+"&format="+format, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.saveFile(response, format));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  saveFile(data: any, format: string) {
    switch (format) {
      case "PDF": {
        var blob = new Blob([data], {type: "application/pdf"});
        saveAs(blob, "download.pdf");
        break;
      }
      case "XLS": {
        var blob = new Blob([data], {type: "application/vnd.ms-excel"});
        saveAs(blob, "download.xls");
        break;
      }

    }

  }

}
