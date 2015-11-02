angular.module('myMangas',
    ['ngRoute',
        'ui.bootstrap',
        'publisher.controllers',
        'publisher.services',
        'publisher.routes',
        'collection.controllers',
        'collection.services',
        'collection.routes']);
angular.module('collection.controllers', ['collection.services', 'publisher.services', 'ngDialog'])
    .controller('NewCollectionController', ['$scope', 'CollectionService', 'PublisherService',
        function ($scope, CollectionService, PublisherService) {

            $scope.alerts = [];

            $scope.getPublishers = function (name) {
                return PublisherService.findByName(name).then(function (response) {
                    return response.data.items.map(function (item) {
                        return item.name;
                    });
                });
            };

            $scope.legend = "Add New Collection";

            $scope.save = function (collection) {
                if ($scope.collectionForm.$valid) {
                    CollectionService.save(collection).then(function (response) {
                        $scope.reset();
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                $scope.collection = {
                    publisher: "",
                    name: "",
                    searchParam: "",
                    isComplete: false
                };
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('ListCollectionController', ['$scope', '$timeout', 'CollectionService', 'ngDialog',
        function ($scope, $timeout, CollectionService, ngDialog) {

            var name = '';

            $scope.alerts = [];

            var timeout;
            $scope.$watch('name', function (newVal) {
                if (newVal) {
                    if (timeout) $timeout.cancel(timeout);
                    timeout = $timeout(function () {
                        name = newVal;
                        alert(name);
                        paginate(1, 0);
                    }, 350);
                }
            });

            $scope.openRemoveDialog = function (collection) {
                $scope.collectionToRemove = collection;
                ngDialog.openConfirm({
                    template: 'removeCollectionTemplate',
                    className: 'ngdialog-theme-default',
                    scope: $scope
                }).then(function (collectionID) {
                    CollectionService.remove(collectionID).then(function (response) {
                        paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }, function () {

                });
            };

            var paginate = function (currentPage, skip) {
                $scope.itemsPerPage = 10;
                $scope.maxSize = 5;
                $scope.bigCurrentPage = currentPage;

                CollectionService.paginate({
                    'name': name,
                    'limit': $scope.itemsPerPage,
                    'skip': skip
                }).then(function (response) {
                    $scope.items = response.data.items;
                    $scope.bigTotalItems = response.data.totalRecords;
                }, function (response) {

                });
            };

            $scope.setPage = function (pageNo) {
                $scope.bigCurrentPage = pageNo;
            };

            $scope.pageChanged = function () {
                paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
            };

            paginate(1, 0);

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('UpdateCollectionController', ['$scope', '$routeParams', 'CollectionService',
        function ($scope, $routeParams, CollectionService) {

            $scope.alerts = [];

            $scope.legend = "Update Collection";

            var oldCollection = null;

            var updateModel = function () {
                CollectionService.edit($routeParams.id).then(function (response) {
                    $scope.collection = response.data.collection;
                    oldCollection = angular.copy($scope.collection);
                }, function (response) {
                    $scope.alerts.push({type: 'warning', msg: response.data.msg});
                });
            };

            updateModel();

            $scope.save = function (collection) {
                if ($scope.collectionForm.$valid) {
                    CollectionService.update(collection).then(function (response) {
                        oldCollection = angular.copy($scope.collection);
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                updateModel();
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };
        }]);
angular.module('publisher.controllers', ['publisher.services', 'ngDialog'])
    .controller('NewPublisherController', ['$scope', 'PublisherService',
        function ($scope, PublisherService) {

            $scope.alerts = [];

            $scope.legend = "Add New Publisher";

            $scope.save = function (publisher) {
                if ($scope.publisherForm.$valid) {
                    PublisherService.save(publisher).then(function (response) {
                        $scope.reset();
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                $scope.publisher = {
                    name: ""
                };
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('ListPublisherController', ['$scope', '$route', '$timeout', 'PublisherService', 'ngDialog',
        function ($scope, $route, $timeout, PublisherService, ngDialog) {

            var name = '';

            $scope.alerts = [];

            var timeout;
            $scope.$watch('name', function (newVal) {
                if (newVal) {
                    if (timeout) $timeout.cancel(timeout);
                    timeout = $timeout(function () {
                        name = newVal;
                        paginate(1, 0);
                    }, 350);
                }
            });

            $scope.openRemoveDialog = function (publisher) {
                $scope.publisherToRemove = publisher;
                ngDialog.openConfirm({
                    template: 'removePublisherTemplate',
                    className: 'ngdialog-theme-default',
                    scope: $scope
                }).then(function (publisherID) {
                    PublisherService.remove(publisherID).then(function (response) {
                        paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }, function () {

                });
            };

            var paginate = function (currentPage, skip) {
                $scope.itemsPerPage = 10;
                $scope.maxSize = 5;
                $scope.bigCurrentPage = currentPage;

                PublisherService.paginate({
                    'name': name,
                    'limit': $scope.itemsPerPage,
                    'skip': skip
                }).then(function (response) {
                    $scope.items = response.data.items;
                    $scope.bigTotalItems = response.data.totalRecords;
                }, function (response) {

                });
            };

            $scope.setPage = function (pageNo) {
                $scope.bigCurrentPage = pageNo;
            };

            $scope.pageChanged = function () {
                paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
            };

            paginate(1, 0);

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('UpdatePublisherController', ['$scope', '$routeParams', 'PublisherService',
        function ($scope, $routeParams, PublisherService) {

            $scope.alerts = [];

            $scope.legend = "Update Publisher";

            var oldPublisher = null;

            var updateModel = function () {
                PublisherService.edit($routeParams.id).then(function (response) {
                    $scope.publisher = response.data.publisher;
                    oldPublisher = angular.copy($scope.publisher);
                }, function (response) {
                    $scope.alerts.push({type: 'warning', msg: response.data.msg});
                });
            };

            updateModel();

            $scope.save = function (publisher) {
                if ($scope.publisherForm.$valid) {
                    PublisherService.update(publisher).then(function (response) {
                        oldPublisher = angular.copy($scope.publisher);
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                updateModel();
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };
        }]);
angular.module('collection.routes', ['collection.controllers'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/views/collection', {
                templateUrl: '/assets/partials/collections/collection.html',
                controller: 'NewCollectionController'
            })
            .when('/views/collections', {
                templateUrl: '/assets/partials/collections/collections.html',
                controller: 'ListCollectionController'
            })
            .when('/views/collection/:id/edit', {
                templateUrl: '/assets/partials/collections/collection.html',
                controller: 'UpdateCollectionController'
            });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }]);
angular.module('publisher.routes', ['publisher.controllers'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/views/publisher', {
                templateUrl: '/assets/partials/publishers/publisher.html',
                controller: 'NewPublisherController'
            })
            .when('/views/publishers', {
                templateUrl: '/assets/partials/publishers/publishers.html',
                controller: 'ListPublisherController'
            })
            .when('/views/publisher/:id/edit', {
                templateUrl: '/assets/partials/publishers/publisher.html',
                controller: 'UpdatePublisherController'
            });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }]);
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


angular.module('publisher.services', []).factory('PublisherService', ['$http', function ($http) {

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
        findByName: function (name) {
            return doGet('/api/publishers/search?name=' + name + '&limit=10&skip=0');
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

