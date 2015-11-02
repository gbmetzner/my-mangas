angular.module('manga.routes', ['manga.controllers'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/views/manga', {
                templateUrl: '/assets/partials/mangas/manga.html',
                controller: 'NewMangaController'
            })
            .when('/views/mangas', {
                templateUrl: '/assets/partials/mangas/mangas.html',
                controller: 'ListMangaController'
            })
            .when('/views/manga/:id/edit', {
                templateUrl: '/assets/partials/mangas/manga.html',
                controller: 'UpdateMangaController'
            });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }]);