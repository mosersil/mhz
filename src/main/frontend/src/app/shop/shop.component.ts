import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {ShopItem} from "../shop-item";
import {ShopService} from "../shop.service";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.sass']
})
export class ShopComponent implements OnInit {

  shopItems;

  constructor(private _shopService: ShopService, private http: HttpClient) { }

  ngOnInit() {
    this.http.get(environment.backendUrl+"/api/public/shop/offering").subscribe(data => {
      console.log("shopItems: " + data);
      this.shopItems=data;
    });
  }

  getCart() {
    return this._shopService.cart;
  }


  addItemToCart(product: ShopItem) {
    this._shopService.addItemToCart(product);
  }

  removeItemFromCart(product: ShopItem) {
    this._shopService.removeItemFromCart(product);
  }


  submitOrder() {
    this._shopService.generateOrder()
  }


  get total(): number {
    return this._shopService.total;
  }

}
