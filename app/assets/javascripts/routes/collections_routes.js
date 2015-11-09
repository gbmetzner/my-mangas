angular.module('collection.routes', ['collection.controllers'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/views/collection', {
                templateUrl: '/partials/collections/collection.html',
                controller: 'NewCollectionController'
            })
            .when('/views/collections', {
                templateUrl: '/partials/collections/collections.html',
                controller: 'ListCollectionController'
            })
            .when('/views/collection/:id/edit', {
                templateUrl: '/partials/collections/collection.html',
                controller: 'UpdateCollectionController'
            });
    }]);