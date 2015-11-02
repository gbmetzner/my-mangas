angular.module('manga.services', []).factory('MangaService', ['$http', function ($http) {

    var doPost = function (url, manga) {
        return $http({
            method: 'POST',
            data: manga,
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    var doPut = function (url, manga) {
        return $http({
            method: 'PUT',
            data: manga,
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    var doGet = function (url) {
        return $http({
            method: 'GET',
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    var doDelete = function (url) {
        return $http({
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    return {
        save: function (manga) {
            alert(manga.name);
            return doPost('/api/mangas', manga);
        },
        edit: function (mangaID) {
            return doGet('/api/mangas/' + mangaID + '/edit');
        },
        update: function (manga) {
            return doPut('/api/mangas/' + manga.id, manga);
        },
        remove: function (mangaID) {
            return doDelete('/api/mangas/' + mangaID);
        },
        paginate: function (mangaFilter) {
            var url = '/api/mangas/search?';
            if (mangaFilter.limit !== null) {
                url = url + 'limit=' + mangaFilter.limit;
            }
            if (mangaFilter.skip !== null) {
                url = url + '&skip=' + mangaFilter.skip;
            }
            if (mangaFilter.manga.collection !== "") {
                url = url + '&collection=' + mangaFilter.manga.collection;
            }
            if (mangaFilter.manga.name !== "") {
                url = url + '&name=' + mangaFilter.manga.name;
            }
            return doGet(url);
        }
    };
}]);

