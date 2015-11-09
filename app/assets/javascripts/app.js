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
        'errors.routes',]).config(function ($provide, $httpProvider, $locationProvider) {

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
     });

  // Intercept http calls.
  $provide.factory('HttpInterceptor', function ($q) {
    return {
      // On request success
      request: function (config) {
        // console.log(config); // Contains the data about the request before it is sent.

        // Return the config or wrap it in a promise if blank.
        return config || $q.when(config);
      },

      // On request failure
      requestError: function (rejection) {
            alert(rejection.status);
          switch (rejection.status) {
              case 404:
                $location.path('/404');
                break;
              default: $location.path('/error');
          }

        return $q.reject(rejection);
      },

      // On response success
      response: function (response) {
        // console.log(response); // Contains the data from the response.

        // Return the response or promise.
        return response || $q.when(response);
      },

      // On response failure
      responseError: function (rejection) {
        // console.log(rejection); // Contains the data about the error.

        // Return the promise rejection.
        return $q.reject(rejection);
      }
    };
  });

  // Add the interceptor to the $httpProvider.
  $httpProvider.interceptors.push('HttpInterceptor');

});