angular.module('manga.services', []).factory('MangaService', ['$http', function ($http) {
    var baseURL = '/api/mangas';
    return {
        save: function (manga) {
            return $http.post(baseURL,manga);
        },
        edit: function (mangaID) {
            return $http.get(baseURL + '/' + mangaID + '/edit');
        },
        update: function (manga) {
            return $http.put(baseURL + '/' + manga.id, manga);
        },
        remove: function (mangaID) {
            return $http.delete(baseURL + '/' + mangaID);
        },
        updateOwnership: function(mangaID, doIHaveIt){
            return $http.put(baseURL + '/ownership/' + mangaID, doIHaveIt);
        },
        paginate: function (mangaFilter) {
            var url = '/search?';
            if (mangaFilter.limit) {
                url = url + 'limit=' + mangaFilter.limit;
            }
            if (mangaFilter.skip) {
                url = url + '&skip=' + mangaFilter.skip;
            }
            if (mangaFilter.manga.collection) {
                url = url + '&collection=' + mangaFilter.manga.collection;
            }
            if (mangaFilter.manga.name) {
                url = url + '&name=' + mangaFilter.manga.name;
            }
            return $http.get(baseURL + url);
        }
    };
}]);

