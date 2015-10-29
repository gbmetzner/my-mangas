angular.module('myMangas.services', []).factory('PublisherService', ['$http', function ($http) {

    var doPost = function (url, publisher) {
        return $http({
            method: 'POST',
            data: publisher,
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    var doPut = function (url, publisher) {
        return $http({
            method: 'PUT',
            data: publisher,
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
        save: function (publisher) {
            return doPost('/api/publishers', publisher);
        },
        edit: function (publisherID) {
            return doGet('/api/publishers/' + publisherID + '/edit');
        },
        update: function (publisher) {
            return doPut('/api/publishers/' + publisher.id, publisher);
        },
        remove: function (publisherID) {
            return doDelete('/api/publishers/' + publisherID);
        },
        paginate: function (publisherFilter) {
            var url = '/api/publishers/search?';
            if (publisherFilter.limit !== null) {
                url = url + 'limit=' + publisherFilter.limit;
            }
            if (publisherFilter.skip !== null) {
                url = url + '&skip=' + publisherFilter.skip;
            }
            if (publisherFilter.name !== "") {
                url = url + '&name=' + publisherFilter.name;
            }
            return doGet(url);
        }
    };
}]);

