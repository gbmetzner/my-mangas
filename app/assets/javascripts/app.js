var myMangas = angular.module('myMangas',
    ['ngRoute',
     'ngCookies',
        'ui.bootstrap',
        'ngFileUpload',
        'myMangas.tpl',
        'akoenig.deckgrid',
        'publisher.controllers',
        'publisher.routes',
        'collection.controllers',
        'collection.routes',
        'manga.controllers',
        'manga.routes',
        'login.controllers',
        'login.directives',
        'messages.directives',
        'errors.routes',]).config(function ($provide, $httpProvider, $locationProvider) {

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
     });

  $provide.factory('HttpInterceptor', function ($q, $location) {
    return {

      request: function (config) {
        return config || $q.when(config);
      },

      requestError: function (rejection) {
        return $q.reject(rejection);
      },

      response: function (response) {
        return response || $q.when(response);
      },

      responseError: function (rejection) {
          switch (rejection.status) {
              case 401:
                $location.path('/');
                break;
              case 404:
                $location.path('/404');
                break;
              default: $location.path('/error');
          }
        return $q.reject(rejection);
      }
    };
  });

  $httpProvider.interceptors.push('HttpInterceptor');

});