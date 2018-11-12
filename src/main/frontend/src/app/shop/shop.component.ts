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
  total: number = 0;

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
    if (this._shopService.cart.length === 0) {
      product.count = 1;
      this._shopService.cart.push(product);
    } else {
      var repeat = false;
      for (var i = 0; i < this._shopService.cart.length; i++) {
        if (this._shopService.cart[i].id === product.id) {
          repeat = true;
          this._shopService.cart[i].count += 1;
        }
      }
      if (!repeat) {
        product.count = 1;
        this._shopService.cart.push(product);
      }
    }
    this._shopService.total += product.price;
    this.total=this._shopService.total;
  }

  removeItemFromCart(product: ShopItem) {
    if (product.count > 1) {
      product.count -= 1;
    }
    else if (product.count === 1) {
      var index = this._shopService.cart.indexOf(product);
      this._shopService.cart.splice(index, 1);
    }

    this._shopService.total -= product.price;
    this.total=this._shopService.total;
  }


  submitOrder() {
    this._shopService.generateOrder()
  }

}
