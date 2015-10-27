angular.module('myMangas.services', []).factory('PublisherService', ['$http', function ($http) {

    var doPost = function (publisher) {
        return $http({
            method: 'POST',
            data: publisher,
            headers: {'Content-Type': 'application/json'},
            url: '/publishers'
        });
    };
    var doGet = function (url) {
        return $http({
            method: 'GET',
            headers: {'Content-Type': 'application/json'},
            url: url
        });
    };
    return {
        post: function (publisher) {
            return doPost(publisher);
        },
        edit: function (publisherID) {
            return doGet('/publisher/' + publisherID + '/edit');
        },
        paginate: function (publisherFilter) {
            var url = '/publishers/search?';
            if (publisherFilter.limit != null) {
                url = url + 'limit=' + publisherFilter.limit
            }
            if (publisherFilter.skip != null) {
                url = url + '&skip=' + publisherFilter.skip
            }
            if (publisherFilter.name != "") {
                url = url + '&name=' + publisherFilter.name
            }
            return doGet(url);
        }
    };
}])
;

