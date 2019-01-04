import {Injectable} from '@angular/core';
import {ShopItem} from "./shop-item";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {AuthenticationService} from "./authentication.service";
import {Router} from "@angular/router";
import {Subject} from "rxjs";
import {ShopTransaction} from "./shop-transaction";

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  cart: ShopItem[] = new Array();
  total: number = 0;
  purchaseId;
  transactionId;

  totalChange: Subject<number> = new Subject<number>();


  constructor(private http: HttpClient,
              private _authenticationService: AuthenticationService,
              private router: Router) {

    this.totalChange.subscribe(value => {
      this.total = value;
      console.log(this.total)
    })
  }


  addItemToCart(product: ShopItem) {
    if (this.cart.length === 0) {
      product.count = 1;
      this.cart.push(product);
    } else {
      var repeat = false;
      for (var i = 0; i < this.cart.length; i++) {
        if (this.cart[i].id === product.id) {
          repeat = true;
          this.cart[i].count += 1;
        }
      }
      if (!repeat) {
        product.count = 1;
        this.cart.push(product);
      }
    }
    var newTotal = this.total + product.price;
    this.totalChange.next(newTotal);
  }


  removeItemFromCart(product: ShopItem) {
    if (product.count > 1) {
      product.count -= 1;
    }
    else if (product.count === 1) {
      var index = this.cart.indexOf(product);
      this.cart.splice(index, 1);
    }

    var newTotal = this.total - product.price;
    this.totalChange.next(newTotal);
  }


  generateOrder() {
    let auth = this._authenticationService;

    var data = [];
    for (let entry of this.cart) {
      var orderLine = {
        count: entry.count,
        shopItemId: entry.id
      };

      data.push(orderLine);
    }


    this.http.post(environment.backendUrl + '/api/public/shop/submitorder', data).subscribe(success => {
      this.purchaseId = success;
      this.router.navigateByUrl("payment");
      /*
      if (this._authenticationService.authenticated) {
        console.log("User is already authenticated - forward to payment");
        this.router.navigateByUrl("payment");
      } else {
        console.log("user seems not to be authenticated yet - forward to loginpage");
        this._authenticationService.forward = "payment";
        this.router.navigateByUrl("/login");
      }
      */
    }, error1 => {
      console.error(error1);
    });
  }


  assignPersonToOrder() {
    var data = {
      id: this.purchaseId
    };
    this.http.post(environment.backendUrl + "/api/protected/shop/prepare_transaction", data).subscribe(result => {
      this.transactionId = result;
    }, error1 => {
      console.log("An error occured: " + error1.error)
      //TODO: error handling
    })
  }


  createPayment(token, name) {
    var data = {
      transactionId: this.transactionId,
      cardholder_name: name,
      token: token.id
    };
    return this.http.post(environment.backendUrl + "/api/protected/shop/createpayment", data);
  }

  createAdvancePayment() {
    var data = {
      transactionId: this.transactionId
    };
    return this.http.post(environment.backendUrl + "/api/protected/shop/createadvancepayment", data);
  }

  getTransactionDetails() {
    return this.http.get(environment.backendUrl + "/api/protected/shop/transaction?id=" + this.purchaseId);
  }

  getMyTransactions() {
    return this.http.get<ShopTransaction[]>(environment.backendUrl + "/api/protected/shop/transactions");
  }


}
