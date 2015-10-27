var myMangas = angular.module('myMangas', ['ui.bootstrap', 'myMangas.services'])
    .controller('NewPublisherController', ['$scope', 'PublisherService',
        function ($scope, PublisherService) {

            $scope.alerts = [];

            $scope.save = function (publisher) {
                if ($scope.publisherForm.$valid) {

                    PublisherService.post(publisher).then(function (response) {
                        $scope.clear();
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });

                }
            };

            $scope.clear = function () {
                $scope.publisher = {
                    name: ""
                }
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('ListPublisherController', ['$scope', '$timeout', 'PublisherService',
        function ($scope, $timeout, PublisherService) {

            var name = '';

            var timeout;
            $scope.$watch('name', function (newVal) {
                if (newVal) {
                    if (timeout) $timeout.cancel(timeout);
                    timeout = $timeout(function () {
                        name = newVal;
                        paginate(1, 0)
                    }, 350);
                }
            });

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

                })
            };

            $scope.setPage = function (pageNo) {
                $scope.bigCurrentPage = pageNo;
            };

            $scope.pageChanged = function () {
                paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
            };

            paginate(1, 0);

        }]).controller('UpdatePublisherController', ['$scope', 'PublisherService',
        function ($scope, PublisherService) {

            $scope.alerts = [];

            $scope.update = function (publisher) {
                alert(publisher);
                //if ($scope.publisherForm.$valid) {
                //
                //    PublisherService.post(publisher).then(function (response) {
                //        $scope.clear();
                //        $scope.alerts.push({type: 'success', msg: response.data.msg});
                //    }, function (response) {
                //        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                //    });
                //
                //}
            };

            $scope.clear = function () {
                $scope.publisher = {
                    name: ""
                }
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]);