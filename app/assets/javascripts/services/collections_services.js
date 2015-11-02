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
        paginate: function (collectionFilter) {
            var url = '/api/collections/search?';
            if (collectionFilter.limit !== null) {
                url = url + 'limit=' + collectionFilter.limit;
            }
            if (collectionFilter.skip !== null) {
                url = url + '&skip=' + collectionFilter.skip;
            }
            if (collectionFilter.collection.publisher !== "") {
                url = url + '&publisher=' + collectionFilter.collection.publisher;
            }
            if (collectionFilter.collection.name !== "") {
                url = url + '&name=' + collectionFilter.collection.name;
            }
            return doGet(url);
        }
    };
}]);

