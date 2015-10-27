angular.module('myMangas.routes', ['myMangas.controllers'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.
            when('/publishers', {
                templateUrl: '/partials/phone-list.html',
                controller: 'ServicesController'
            });
        $locationProvider.html5Mode(true);
    }]);