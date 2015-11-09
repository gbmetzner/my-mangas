angular.module('publisher.routes', ['publisher.controllers'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/views/publisher', {
                templateUrl: '/partials/publishers/publisher.html',
                controller: 'NewPublisherController'
            })
            .when('/views/publishers', {
                templateUrl: '/partials/publishers/publishers.html',
                controller: 'ListPublisherController'
            })
            .when('/views/publisher/:id/edit', {
                templateUrl: '/partials/publishers/publisher.html',
                controller: 'UpdatePublisherController'
            });
    }]);