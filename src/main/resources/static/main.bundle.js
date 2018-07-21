webpackJsonp(["main"],{

/***/ "./src/$$_lazy_route_resource lazy recursive":
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncatched exception popping up in devtools
	return Promise.resolve().then(function() {
		throw new Error("Cannot find module '" + req + "'.");
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./src/$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./src/app/app-routing.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppRoutingModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__home_home_component__ = __webpack_require__("./src/app/home/home.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__feature_feature_component__ = __webpack_require__("./src/app/feature/feature.component.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var routes = [
    { path: '', pathMatch: 'full', redirectTo: '/home' },
    { path: 'home', component: __WEBPACK_IMPORTED_MODULE_2__home_home_component__["a" /* HomeComponent */] },
    { path: 'feature', component: __WEBPACK_IMPORTED_MODULE_3__feature_feature_component__["a" /* FeatureComponent */] }
];
var AppRoutingModule = (function () {
    function AppRoutingModule() {
    }
    AppRoutingModule.components = [__WEBPACK_IMPORTED_MODULE_2__home_home_component__["a" /* HomeComponent */], __WEBPACK_IMPORTED_MODULE_3__feature_feature_component__["a" /* FeatureComponent */]];
    AppRoutingModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            imports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* RouterModule */].forRoot(routes)],
            exports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* RouterModule */]]
        })
    ], AppRoutingModule);
    return AppRoutingModule;
}());



/***/ }),

/***/ "./src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var AppComponent = (function () {
    function AppComponent() {
    }
    AppComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'app-root',
            template: "<router-outlet></router-outlet>"
        }),
        __metadata("design:paramtypes", [])
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "./src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser__ = __webpack_require__("./node_modules/@angular/platform-browser/esm5/platform-browser.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__("./node_modules/@angular/forms/esm5/forms.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_common_http__ = __webpack_require__("./node_modules/@angular/common/esm5/http.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__app_component__ = __webpack_require__("./src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__app_routing_module__ = __webpack_require__("./src/app/app-routing.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__shared_data_service__ = __webpack_require__("./src/app/shared/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__shared_calendar_service__ = __webpack_require__("./src/app/shared/calendar.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};








var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            imports: [__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser__["a" /* BrowserModule */], __WEBPACK_IMPORTED_MODULE_2__angular_forms__["a" /* FormsModule */], __WEBPACK_IMPORTED_MODULE_3__angular_common_http__["b" /* HttpClientModule */], __WEBPACK_IMPORTED_MODULE_5__app_routing_module__["a" /* AppRoutingModule */]],
            declarations: [__WEBPACK_IMPORTED_MODULE_4__app_component__["a" /* AppComponent */], __WEBPACK_IMPORTED_MODULE_5__app_routing_module__["a" /* AppRoutingModule */].components],
            providers: [__WEBPACK_IMPORTED_MODULE_6__shared_data_service__["a" /* DataService */], __WEBPACK_IMPORTED_MODULE_7__shared_calendar_service__["a" /* CalendarService */]],
            bootstrap: [__WEBPACK_IMPORTED_MODULE_4__app_component__["a" /* AppComponent */]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "./src/app/feature/feature.component.html":
/***/ (function(module, exports) {

module.exports = "<!--Section: Live preview-->\n<section id=\"v-1\">\n\n    <h2 class=\"title text-center text-md-left pt-4 mt-5\">\n        <strong>Blog listing v.1 </strong>\n    </h2>\n\n    <!--Section: Blog v.1-->\n    <section class=\"py-4 text-center text-lg-left\">\n\n        <!--Section heading-->\n        <h1 class=\"h1 font-weight-bold text-center mb-5 pt-4\">Recent posts</h1>\n        <!--Section description-->\n        <p class=\"text-center mb-5 pb-3\">Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur\n            sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>\n\n        <!--Grid row-->\n        <div class=\"row\">\n\n            <!--Grid column-->\n            <div class=\"col-lg-5 col-xl-5 pb-3\">\n                <!--Featured image-->\n                <div class=\"view overlay rounded z-depth-2\">\n                    <img src=\"https://mdbootstrap.com/img/Photos/Others/img%20(27).jpg\" alt=\"Sample image for first version of blog listing\"\n                         class=\"img-fluid\">\n                    <a>\n                        <div class=\"mask rgba-white-slight\"></div>\n                    </a>\n                </div>\n            </div>\n            <!--Grid column-->\n\n            <!--Grid column-->\n            <div class=\"col-lg-7 col-xl-7\">\n                <!--Excerpt-->\n                <a href=\"\" class=\"green-text\">\n                    <h6 class=\"font-weight-bold pb-1\">\n                        <i class=\"fa fa-cutlery\"></i> Food</h6>\n                </a>\n                <h3 class=\"mb-4 font-weight-bold dark-grey-text\">\n                    <strong>This is title of the news</strong>\n                </h3>\n                <p>Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime\n                    placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus et aut officiis debitis\n                    aut rerum.</p>\n                <p>by\n                    <a>\n                        <strong>Carine Fox</strong>\n                    </a>, 19/08/2018</p>\n                <a class=\"btn btn-success btn-md mb-3\">Read more</a>\n            </div>\n            <!--Grid column-->\n\n        </div>\n        <!--Grid row-->\n\n        <hr class=\"mb-5 mt-5 pb-3\">\n\n        <!--Grid row-->\n        <div class=\"row\">\n\n            <!--Grid column-->\n            <div class=\"col-lg-7 col-xl-7 pb-3\">\n                <!--Excerpt-->\n                <a href=\"\" class=\"pink-text\">\n                    <h6 class=\"font-weight-bold pb-1\">\n                        <i class=\"fa fa-image\"></i> Lifestyle</h6>\n                </a>\n                <h3 class=\"mb-4 font-weight-bold dark-grey-text\">\n                    <strong>This is title of the news</strong>\n                </h3>\n                <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti\n                    atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident et\n                    dolorum fuga.</p>\n                <p>by\n                    <a>\n                        <strong>Carine Fox</strong>\n                    </a>, 14/08/2018</p>\n                <a class=\"btn btn-pink btn-md mb-3\">Read more</a>\n            </div>\n            <!--Grid column-->\n\n            <!--Grid column-->\n            <div class=\"col-lg-5 col-xl-5 mb-5\">\n                <!--Featured image-->\n                <div class=\"view overlay rounded z-depth-2\">\n                    <img src=\"https://mdbootstrap.com/img/Photos/Others/img%20(34).jpg\" alt=\"Second image in the first version of blog listing\"\n                         class=\"img-fluid\">\n                    <a>\n                        <div class=\"mask rgba-white-slight\"></div>\n                    </a>\n                </div>\n            </div>\n            <!--Grid column-->\n\n        </div>\n        <!--Grid row-->\n\n        <hr class=\"mb-5 mt-4 pb-3\">\n\n        <!--Grid row-->\n        <div class=\"row pb-5\">\n\n            <!--Grid column-->\n            <div class=\"col-lg-5 col-xl-5 pb-3\">\n                <!--Featured image-->\n                <div class=\"view overlay rounded z-depth-2\">\n                    <img src=\"https://mdbootstrap.com/img/Photos/Others/img (28).jpg\" alt=\"Thrid image in the blog listing.\" class=\"img-fluid\">\n                    <a>\n                        <div class=\"mask rgba-white-slight\"></div>\n                    </a>\n                </div>\n            </div>\n            <!--Grid column-->\n\n            <!--Grid column-->\n            <div class=\"col-lg-7 col-xl-7\">\n                <!--Excerpt-->\n                <a href=\"\" class=\"indigo-text\">\n                    <h6 class=\"font-weight-bold pb-1\">\n                        <i class=\"fa fa-suitcase\"></i> Travels</h6>\n                </a>\n                <h3 class=\"mb-4 font-weight-bold dark-grey-text\">\n                    <strong>This is title of the news</strong>\n                </h3>\n                <p>Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni\n                    dolores eos qui ratione voluptatem sequi nesciunt. Neque porro qui dolorem ipsum quia sit amet, consectetur.</p>\n                <p>by\n                    <a>\n                        <strong>Carine Fox</strong>\n                    </a>, 11/08/2018</p>\n                <a class=\"btn btn-indigo btn-md mb-3\">Read more</a>\n            </div>\n            <!--Grid column-->\n\n        </div>\n        <!--Grid row-->\n\n    </section>\n    <!--Section: Blog v.1-->\n\n</section>\n<!--Section: Live preview-->"

/***/ }),

/***/ "./src/app/feature/feature.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return FeatureComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var FeatureComponent = (function () {
    function FeatureComponent() {
    }
    FeatureComponent.prototype.ngOnInit = function () {
    };
    FeatureComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'feature',
            template: __webpack_require__("./src/app/feature/feature.component.html")
        }),
        __metadata("design:paramtypes", [])
    ], FeatureComponent);
    return FeatureComponent;
}());



/***/ }),

/***/ "./src/app/home/home.component.html":
/***/ (function(module, exports) {

module.exports = "<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\n    <a class=\"navbar-brand\" href=\"#\">Logo</a>\n    <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarSupportedContent\"\n            aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n        <span class=\"navbar-toggler-icon\"></span>\n    </button>\n\n\n    <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n        <ul class=\"navbar-nav mr-auto\">\n            <li class=\"nav-item active\">\n                <a class=\"nav-link\" href=\"#\">Home<span class=\"sr-only\">(current)</span></a>\n            </li>\n            <li class=\"nav-item\">\n                <a class=\"nav-link\" href=\"/feature\">Über uns</a>\n            </li>\n            <li class=\"nav-item dropdown\">\n                <a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"navbarDropdown\" role=\"button\" data-toggle=\"dropdown\"\n                   aria-haspopup=\"true\" aria-expanded=\"false\">\n                    Dropdown\n                </a>\n                <div class=\"dropdown-menu\" aria-labelledby=\"navbarDropdown\">\n                    <a class=\"dropdown-item\" href=\"#\">Action</a>\n                    <a class=\"dropdown-item\" href=\"#\">Another action</a>\n                    <div class=\"dropdown-divider\"></div>\n                    <a class=\"dropdown-item\" href=\"#\">Something else here</a>\n                </div>\n            </li>\n            <li class=\"nav-item\">\n                <a class=\"nav-link disabled\" href=\"#\">Disabled</a>\n            </li>\n        </ul>\n    </div>\n</nav>\n<header class=\"masthead text-white text-center\">\n    <div id=\"myCarousel\" class=\"carousel slide\" data-ride=\"carousel\">\n        <ol class=\"carousel-indicators\">\n            <li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>\n            <li data-target=\"#myCarousel\" data-slide-to=\"1\"></li>\n            <li data-target=\"#myCarousel\" data-slide-to=\"2\"></li>\n        </ol>\n        <div class=\"carousel-inner\" role=\"listbox\">\n            <div class=\"carousel-item active\">\n                <img class=\"d-block w-100\" src=\"../../assets/gruppenfoto.jpg\" alt=\"First slide\">\n                <div class=\"carousel-caption\">\n                    <h3>Second slide</h3>\n                    <p>This is the second slide.</p>\n                </div>\n            </div>\n            <div class=\"carousel-item\">\n                <img class=\"d-block w-100\" src=\"./../assets/sechselaeuten.jpg\" alt=\"Second slide\">\n                <div class=\"carousel-caption\">\n                    <h3>Second slide</h3>\n                    <p>This is the second slide.</p>\n                </div>\n            </div>\n            <div class=\"carousel-item\">\n                <img class=\"d-block w-100\" src=\"./../assets/bandfoto.jpg\" alt=\"Third slide\">\n                <div class=\"carousel-caption\">\n                    <div>Werden Sie noch heute Passivmitglied vom MV Harmonie Zürich-Oberstrass</div>\n                    <div class=\"col-12 col-md-6\">\n                        <button type=\"submit\" class=\"btn btn-block btn-lg btn-primary\">Werden Sie Passivmitglied!</button>\n                    </div>\n                </div>\n            </div>\n        </div>\n        <a class=\"carousel-control-prev\" href=\"#myCarousel\" role=\"button\" data-slide=\"prev\">\n            <span class=\"carousel-control-prev-icon\" aria-hidden=\"true\"></span>\n            <span class=\"sr-only\">Previous</span>\n        </a>\n        <a class=\"carousel-control-next\" href=\"#myCarousel\" role=\"button\" data-slide=\"next\">\n            <span class=\"carousel-control-next-icon\" aria-hidden=\"true\"></span>\n            <span class=\"sr-only\">Next</span>\n        </a>\n    </div>\n</header>\n\n\n\n<div class=\"container\">\n    <div class=\"row\">\n        <div class=\"col-sm-4\">\n            <div class=\"card\">\n                <div class=\"card-block\">\n                    <h4 class=\"card-title\">Anlässe</h4>\n                    <ul *ngIf=\"events\">\n                        <li *ngFor=\"let event of events\">\n                            {{event.date | date : \"mediumDate\"}} : {{event.title}}\n                        </li>\n                    </ul>\n                    <p class=\"card-text\">This is a longer card with supporting text below as a natural lead-in to\n                        additional content. This content is a little bit longer.</p>\n                    <p class=\"card-text\">\n                        <small class=\"text-muted\">Last updated 3 mins ago</small>\n                    </p>\n                </div>\n            </div>\n        </div>\n        <div class=\"col-sm-4\">\n            <div class=\"card\">\n                <div class=\"card-block\">\n                    <h4 class=\"card-title\">Card title</h4>\n                    <p class=\"card-text\">This card has supporting text below as a natural lead-in to additional\n                        content.</p>\n                    <p class=\"card-text\">\n                        <small class=\"text-muted\">Last updated 3 mins ago</small>\n                    </p>\n                </div>\n            </div>\n        </div>\n        <div class=\"col-sm-4\">\n            <div class=\"card\">\n                <div class=\"card-block\">\n                    <h4 class=\"card-title\">Card title</h4>\n                    <p class=\"card-text\">This card has supporting text below as a natural lead-in.\n                        Aliquam codeply mauris arcu, tristique a lobortis vitae, condimentum feugiat justo.</p>\n                    <p class=\"card-text\">\n                        <small class=\"text-muted\">Last updated 3 mins ago</small>\n                    </p>\n                </div>\n            </div>\n        </div>\n    </div>\n\n\n</div>"

/***/ }),

/***/ "./src/app/home/home.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HomeComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__shared_calendar_service__ = __webpack_require__("./src/app/shared/calendar.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var HomeComponent = (function () {
    function HomeComponent(_calendarService) {
        this._calendarService = _calendarService;
    }
    HomeComponent.prototype.ngOnInit = function () {
        this.getEvents();
    };
    HomeComponent.prototype.getEvents = function () {
        var _this = this;
        this._calendarService.getEvents().subscribe(function (data) { _this.events = data; }, function (err) { return console.error(err); }, function () { return console.log('done loading events'); });
    };
    HomeComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'home',
            template: __webpack_require__("./src/app/home/home.component.html")
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__shared_calendar_service__["a" /* CalendarService */]])
    ], HomeComponent);
    return HomeComponent;
}());



/***/ }),

/***/ "./src/app/shared/calendar.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CalendarService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("./node_modules/@angular/common/esm5/http.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var httpOptions = {
    headers: new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpHeaders */]({ 'Content-Type': 'application/json' })
};
var CalendarService = (function () {
    function CalendarService(http) {
        this.http = http;
    }
    // Uses http.get() to load data from a single API endpoint
    CalendarService.prototype.getEvents = function () {
        return this.http.get('/public/api/calendar?max=5');
    };
    CalendarService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["A" /* Injectable */])(),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__angular_common_http__["a" /* HttpClient */]])
    ], CalendarService);
    return CalendarService;
}());



/***/ }),

/***/ "./src/app/shared/data.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var DataService = (function () {
    function DataService() {
    }
    DataService.prototype.getProjectName = function () {
        return 'Angular Bare Bones';
    };
    DataService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["A" /* Injectable */])(),
        __metadata("design:paramtypes", [])
    ], DataService);
    return DataService;
}());



/***/ }),

/***/ "./src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
var environment = {
    production: false
};


/***/ }),

/***/ "./src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("./node_modules/@angular/platform-browser-dynamic/esm5/platform-browser-dynamic.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("./src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("./src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* enableProdMode */])();
}
Object(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */])
    .catch(function (err) { return console.log(err); });


/***/ }),

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("./src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map