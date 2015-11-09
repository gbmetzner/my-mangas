angular.module('publisher.controllers', ['publisher.services', 'ngDialog'])
    .controller('NewPublisherController', ['$scope', 'PublisherService',
        function ($scope, PublisherService) {

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

        }]).controller('ListPublisherController', ['$scope', '$route', '$timeout', 'PublisherService', 'ngDialog',
        function ($scope, $route, $timeout, PublisherService, ngDialog) {

            var name = '';

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
                $scope.item = publisher;
                $scope.type = "Publisher";

                ngDialog.openConfirm({
                    template: '/partials/templates/remove_dialog.html',
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
                    $scope.publishers = response.data.items;
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

        }]).controller('UpdatePublisherController', ['$scope', '$routeParams', 'PublisherService',
        function ($scope, $routeParams, PublisherService) {

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
        }]);