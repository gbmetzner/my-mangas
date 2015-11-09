angular.module('manga.routes', ['manga.controllers'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/views/manga', {
                templateUrl: '/partials/mangas/manga.html',
                controller: 'NewMangaController'
            })
            .when('/views/mangas', {
                templateUrl: '/partials/mangas/mangas.html',
                controller: 'ListMangaController'
            })
            .when('/views/manga/:id/edit', {
                templateUrl: '/partials/mangas/manga.html',
                controller: 'UpdateMangaController'
            }).when('/', {
                templateUrl: '/partials/mangas/mangas_deck.html',
                controller: 'DeckMangaController'
            });
    }]);