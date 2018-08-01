'use strict'
var demoApp = angular
    .module('demo', ["ngRoute", "ngCookies"]);


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
                $scope.errorMessage=success.data.message;
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


            var isAdmin = $filter('filter')(response.data.user.roles, {'type':"ADMIN"});

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
        .when("/intra", {
            templateUrl: "intra.html",
        });
});