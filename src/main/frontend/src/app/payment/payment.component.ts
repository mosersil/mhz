import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ShopService} from "../shop.service";
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Element as StripeElement, Elements, ElementsOptions, StripeService} from "ngx-stripe";
import {Router} from "@angular/router";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.sass']
})
export class PaymentComponent implements OnInit {
  @ViewChild('cardInfo') cardInfo: ElementRef;

  elements: Elements
  card: StripeElement
  readyForPayment = false;
  error: string = null;
  stripeTest: FormGroup;

// optional parameters
  elementsOptions: ElementsOptions = {
    locale: 'de'
  };


  constructor(private _shopService: ShopService, private http: HttpClient, private fb: FormBuilder, private stripeService: StripeService, private router: Router) {
  }

  get total(): number {
    return this._shopService.total;
  }

  ngOnInit() {
    //console.log("total= " + this._shopService.total);
    //this.total = this._shopService.total;

    this._shopService.assignPersonToOrder();

    this.stripeTest = this.fb.group({
      name: ['', [Validators.required]]
    });
    this.stripeService.elements(this.elementsOptions)
      .subscribe(elements => {
        this.elements = elements;
        // Only mount the element the first time
        if (!this.card) {
          this.card = this.elements.create('card', {
            style: {
              base: {
                iconColor: '#666EE8',
                color: '#31325F',
                lineHeight: '40px',
                fontWeight: 300,
                fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
                fontSize: '18px',
                '::placeholder': {
                  color: '#CFD7E0'
                }
              }
            }
          });
          this.card.mount('#card-element');
        }
      });

  }

  buy() {
    const name = this.stripeTest.get('name').value;
    this.stripeService
      .createToken(this.card, {name})
      .subscribe(result => {
        if (result.token) {
          // Use the token to create a charge or a customer
          // https://stripe.com/docs/charges
          console.log(result.token);
          this._shopService.createPayment(result.token, name).subscribe(success => {
            this.router.navigate(['transactions']);
          }, error1 => {
            console.log("oops a daisy! " + error1);
            this.error = error1.error.message;
          })

        } else if (result.error) {
          this.error = result.error.message;
          console.log(result.error.message);
        }
      });
  }

  getCart() {
    return this._shopService.cart;
  }

}
