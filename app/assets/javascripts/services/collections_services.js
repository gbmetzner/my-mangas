angular.module('collection.services', []).factory('CollectionService', ['$http', function ($http) {

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
            return doPost('/api/collections', publisher);
        },
        edit: function (publisherID) {
            return doGet('/api/collections/' + publisherID + '/edit');
        },
        update: function (publisher) {
            return doPut('/api/collections/' + publisher.id, publisher);
        },
        remove: function (publisherID) {
            return doDelete('/api/collections/' + publisherID);
        },
        paginate: function (publisherFilter) {
            var url = '/api/collections/search?';
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

