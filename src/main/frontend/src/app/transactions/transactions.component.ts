import {Component, OnInit} from '@angular/core';
import {ShopService} from "../shop.service";
import {environment} from "../../environments/environment";
import {saveAs} from "file-saver";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.sass']
})
export class TransactionsComponent implements OnInit {

  transactions;

  constructor(private _shopService: ShopService, private _http: HttpClient) {
  }

  ngOnInit() {
    this.getMyTransactions();
  }

  getMyTransactions() {
    this._shopService.getMyTransactions().subscribe(success => {
      this.transactions = success;
    }, error1 => console.log(error1))
  }

  downloadReceipt(id: string) {
    this._http.get(environment.backendUrl + "/api/protected/shop/receipt?id="+id, {
      responseType: 'arraybuffer'
    })
      .subscribe(response => this.downLoadFile(response));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any) {
    var blob = new Blob([data]);
    saveAs(blob, 'download.pdf')
  }


}
