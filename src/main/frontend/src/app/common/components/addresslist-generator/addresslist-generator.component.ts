import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
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
    { id: "Aktivmitglied", name: "Aktivmitglieder" },
    { id: "Passivmitglied", name: "Passivmitglieder" },
    { id: "Ehrenmitglied", name: "Ehrenmitglieder" },
    { id: "Freimitglied", name: "Freimitglieder" },
    { id: "Sonstige", name: "Sonstige" }
  ];
  myForm: FormGroup;


  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit() {
    this.myForm = this.fb.group( {
      organizations: new FormArray([])
    });
    this.addCheckboxes();
  }

  private addCheckboxes() {
    this.organizationData.forEach((o, i) => {
      const control = new FormControl(i === 0); // if first item set to true, else false
      (this.myForm.controls.organizations as FormArray).push(control);
    });
    console.log(this.myForm.controls);
  }

  submit() {
    const selectedorganizations = this.myForm.value.organizations
      .map((v, i) => v ? this.organizationData[i].id : null)
      .filter(v => v !== null);
    console.log(selectedorganizations);
    this.http.get(environment.backendUrl + "/api/protected/internal/addresslist?organization="+encodeURIComponent(selectedorganizations)+"&format=XLS", {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response, 'application/vnd.ms-excel', "download.xls"));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any, type: string, filename: string) {
    var blob = new Blob([data], {type: type});
    saveAs(blob, filename);
  }

}
