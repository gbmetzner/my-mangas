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

            var collection = {
                publisher: "",
                name: ""
            };

            $scope.alerts = [];

            var timeout;
            $scope.$watchGroup(['collection.publisher', 'collection.name'], function (newVal) {
                if (newVal[0] || newVal[1]) {
                    if (timeout) $timeout.cancel(timeout);
                    timeout = $timeout(function () {
                        collection = {
                            publisher: newVal[0] ? newVal[0] : '',
                            name: newVal[1] ? newVal[1] : ''
                        };
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
                    'collection': collection,
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