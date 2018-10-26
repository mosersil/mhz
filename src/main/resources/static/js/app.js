'use strict'
var demoApp = angular
    .module('demo', ["ngRoute", "ngCookies"]);

demoApp.factory('ShopService', function () {
    return {
        cart: [],
        total: 0,
        currentStep: 1
    };
});


demoApp.factory('AuthenticationService', function ($http) {

    return {
        isAuthenticated: function () {
            return $http.get('/auth/user').then(
                function (success) {
                    return true;
                },
                function (error) {
                    return false;
                });
        }
    }


    /*
    this.isAuthenticated = function () {
        var authenticated = false

        $http.get('/auth/user').then(
            function (success) {
                authenticated = true;
                return authenticated;
            },
            function (error) {
                if (error.status == 401) {
                    authenticated = false;
                }
                if (error.status == 403) {
                    authenticated = false;
                }
                authenticated = false;
                return authenticated;
            });
    }
    */
});


demoApp.controller("calendar_controller", function ($scope, $http) {
    $http.get("/public/api/calendar")
        .then(function (response) {
            $scope.events = response.data;
        });
});

demoApp.controller("news_controller", function ($scope, $http) {
    $http.get("/public/api/article")
        .then(function (response) {
            if (response != null) {
                $scope.news_show = true;
                $scope.news_title = response.data.title;
                $scope.news_text = response.data.text;
            }
        });
});

demoApp.controller("form_controller", function ($scope, $http) {

    $scope.formData = {};
    $scope.contact_form_display = true;

    $scope.processForm = function () {
        $http({
            method: 'POST',
            url: '/api/contact',
            data: $.param($scope.formContact),  // pass in data as strings
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
        }).then(function (success) {
            if (success.data.success) {
                $scope.contact_message_class = "alert alert-success"
                $scope.contact_message_text = "Vielen Dank!";
                $scope.contact_message_display = true;
                $scope.contact_form_display = false;
            } else {
                $scope.contact_message_class = "alert alert-danger"
                $scope.contact_message_text = success.data.errorDetails;
                $scope.contact_message_display = true;
                $scope.contact_form_display = true;

            }
        });
    }
});

demoApp.controller("intralogin_controller", function ($scope, $http, $location) {
    $scope.submit = function () {

        var data = {
            username: $scope.username,
            password: $scope.password
        };


        $http.post('/login', data).then(function (success) {
            if (success.data.errorCode == 0) {
                console.log("Login succcessful");
                $location.path("/intra");
            } else {
                $scope.errorMessage = success.data.message;
                console.log("Login failed " + success.data.message);
            }
        });

    };
});

demoApp.controller("intra_controller", function ($scope, $http, $location, $filter) {


    $http.get('/internal/api/user').then(
        function (response) {

            $scope.firstName = response.data.firstName;
            $scope.lastName = response.data.lastName;

            var now = new Date();
            $scope.year_actual = (new Date()).getFullYear();
            $scope.year_next = (new Date()).getFullYear() + 1;


            var isAdmin = $filter('filter')(response.data.user.roles, {'type': "ADMIN"});

            console.log("Admin: " + isAdmin);

            if (isAdmin != "") {
                $scope.isAdmin = true;
            }


        },
        function (error) {
            if (error.status == 401) {
                $location.path("/intralogin")
            }
            if (error.status == 403) {
                console.log("Forbidden access")
            }
        }
    );
});


demoApp.controller("shop_controller", function ($scope, $http, $httpParamSerializerJQLike, ShopService, AuthenticationService) {

    $scope.cart = ShopService.cart;
    $scope.total = ShopService.total;
    $scope.currentStep = null;


    // Check the currentStep value in EVERY DIGEST to see if it's changed.
    // And, if so, update the local view-model.
    $scope.$watch(
        function checkCurrentStep() {
            return (ShopService.currentStep);
        },
        function handleStepChange(newValue, oldValue) {
            $scope.currentStep = newValue;
        }
    );


    $http.get("/public/api/shopitems")
        .then(function (response) {
            $scope.shopitems = response.data;
        });


    // Callback for the PayFrame
    var payFrameCallback = function (error) {
        if (error) {
            // Frame could not be loaded, check error object for reason.
            console.log(error.apierror, error.message);
        } else {
            // Frame was loaded successfully and is ready to be used.
            console.log("PayFrame successfully loaded");
            $("#payment-form").show(300);
        }
    }

    var submit = function (event) {
        paymill.createTokenViaFrame({
            amount_int: $scope.total * 100,
            currency: 'CHF'
        }, function (error, result) {
            // Handle error or process result.
            if (error) {
                // Token could not be created, check error object for reason.
                console.log(error.apierror, error.message);
            } else {
                // Token was created successfully and can be sent to backend.
                console.log("token has been created")
                var form = $("#payment-form");
                var token = result.token;
                form.append("<input type='hidden' name='token' value='" + token + "'/>");
                form.get(0).submit();
            }
        });

        return false;
    }


    angular.element(document).ready(function () {
        paymill.embedFrame('credit-card-fields', {
            lang: 'de'
        }, payFrameCallback);
        $("#payment-form").submit(submit);
    });

    $scope.nextStep = function () {
        ShopService.currentStep += 1;
    }

    $scope.previousStep = function () {
        ShopService.currentStep -= 1;
    }


    $scope.addItemToCart = function (product) {

        if ($scope.cart.length === 0) {
            product.count = 1;
            $scope.cart.push(product);

        } else {
            var repeat = false;
            for (var i = 0; i < $scope.cart.length; i++) {
                if ($scope.cart[i].id === product.id) {
                    repeat = true;
                    $scope.cart[i].count += 1;
                }
            }
            if (!repeat) {
                product.count = 1;
                $scope.cart.push(product);
            }
        }
        $scope.total += parseFloat(product.price);

    }

    $scope.removeItemCart = function (product) {

        if (product.count > 1) {
            product.count -= 1;
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() + 1);
            $scope.cart = $cookies.getObject('cart');
        }
        else if (product.count === 1) {
            var index = $scope.cart.indexOf(product);
            $scope.cart.splice(index, 1);
            expireDate = new Date();
            expireDate.setDate(expireDate.getDate() + 1);
        }

        $scope.total -= parseFloat(product.price);

    };


    $scope.submitOrder = function () {
        var data = [];
        angular.forEach(ShopService.cart, function (value, key) {
            var orderLine = {
                count: value.count,
                shopItemId: value.id
            };

            data.push(orderLine);
        });

        $http({
            url: '/public/api/shopinit',
            method: 'POST',
            data: data,
            headers: {
                'Content-Type': 'application/json'
            }

        }).then(function (success) {
            $scope.purchaseId = success.data;
            ShopService.currentStep += 1;

            AuthenticationService.isAuthenticated().then(function (value) {
                if (value === true) {
                    console.log("User is authenticated. Go to step 3 immediately");
                    ShopService.currentStep += 1;
                }
                else {
                    console.log("User is not yet authenticated. Display login")
                }
            })
        });
    }

    $scope.registerPerson = function () {
        var data = {
            gender: $scope.register_gender,
            firstName: $scope.register_firstname,
            lastName: $scope.register_lastname,
            email: $scope.register_email,
            password: $scope.register_password,
            confirm: $scope.register_password_confirm
        }

        $http({
            url: '/public/api/registerPerson',
            method: 'POST',
            data: data,
            headers: {
                'Content-Type': 'application/json'
            }

        }).then(function (success) {
            ShopService.currentStep += 1;
        });
    }

    $scope.submitLogin = function () {
        var data = {
            username: $scope.username,
            password: $scope.password
        };
        $http.post('/login', data).then(function (success) {
            if (success.data.errorCode == 0) {

                var data = {
                    id: $scope.purchaseId
                };

                $http({
                    url: '/internal/shop/authorizeOrder',
                    method: 'POST',
                    data: data,
                    headers: {
                        'Content-Type': 'application/json'
                    }

                }).then(function (success) {
                    ShopService.currentStep += 1;
                });
            } else {
                $scope.errorMessage = success.data.message;
                console.log("Login failed " + success.data.message);
            }
        });
    };

    $scope.submitPersonalData = function () {
        console.log($scope.purchaseId + " " + $scope.client.name)

        var data = {
            email: $scope.client.email,
            id: $scope.purchaseId
        };

        $http({
            url: '/public/api/shopclient',
            method: 'POST',
            data: data,
            headers: {
                'Content-Type': 'application/json'
            }

        }).then(function (success) {
            ShopService.currentStep += 1;
        });

    }


});

demoApp.controller("AppCtrl", function ($scope, $http, $location) {
    $scope.title = "John Doe";
});


demoApp.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "main.html",
        })
        .when("/intralogin", {
            templateUrl: "login.html",
        })
        .when("/shop", {
            templateUrl: "shop.html",
        })
        .when("/shop-login", {
            templateUrl: "shop-login.html",
        })
        .when("/shop-transactions", {
            templateUrl: "shop-transactions.html",
        })
        .when("/intra", {
            templateUrl: "intra.html",
        });
});