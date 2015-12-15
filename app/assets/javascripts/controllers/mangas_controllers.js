angular.module('manga.controllers', ['manga.services', 'collection.services', 'ngDialog', 'myMangas.tpl'])
    .controller('NewMangaController', ['$scope', 'MangaService', 'CollectionService',
        function ($scope, MangaService, CollectionService) {

            $scope.legend = "Add New Manga";

            $scope.showUploadCover = false;

            $scope.getCollections = function (name) {
                return CollectionService.findByName(name).then(function (response) {
                    return response.data.items.map(function (item) {
                        return item.name;
                    });
                });
            };

            $scope.save = function (manga) {
                if ($scope.mangaForm.$valid) {
                    MangaService.save(manga).then(function (response) {
                        $scope.reset();
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                $scope.manga = {
                    collection: "",
                    name: "",
                    number: "",
                    doIHaveIt: false
                };
            };

        }]).controller('ListMangaController', ['$scope', '$timeout', 'MangaService', 'ngDialog',
        function ($scope, $timeout, MangaService, ngDialog) {

            var manga = {
                collection: "",
                name: ""
            };

            var timeout;
            $scope.$watchGroup(['manga.collection', 'manga.name'], function (newVal) {
                if (newVal[0] || newVal[1]) {
                    if (timeout) $timeout.cancel(timeout);
                    timeout = $timeout(function () {
                        manga = {
                            collection: newVal[0] ? newVal[0] : '',
                            name: newVal[1] ? newVal[1] : ''
                        };
                        paginate(1, 0);
                    }, 350);
                }
            });

            $scope.openRemoveDialog = function (manga) {
                $scope.item = manga;
                $scope.type = "Manga";
                ngDialog.openConfirm({
                    template: '/partials/templates/remove_dialog.html',
                    className: 'ngdialog-theme-default',
                    scope: $scope
                }).then(function (mangaID) {
                    MangaService.remove(mangaID).then(function (response) {
                        paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }, function () {

                });
            };

            $scope.updateOwnership = function(manga){
                MangaService.updateOwnership(manga.id, !manga.doIHaveIt).then(function (response) {
                    paginate($scope.bigCurrentPage, $scope.itemsPerPage * ($scope.bigCurrentPage - 1));
                }, function (response) {

                });
            };

            var paginate = function (currentPage, skip) {
                $scope.itemsPerPage = 10;
                $scope.maxSize = 5;
                $scope.bigCurrentPage = currentPage;

                MangaService.paginate({
                    'manga': manga,
                    'limit': $scope.itemsPerPage,
                    'skip': skip
                },false).then(function (response) {
                    $scope.mangas = response.data.items;
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

        }]).controller('UpdateMangaController', ['$scope', '$routeParams', 'MangaService',
        'CollectionService', 'Upload', function ($scope, $routeParams, MangaService, CollectionService, Upload) {

            $scope.showUploadCover = true;

            $scope.legend = "Update Manga";

            $scope.getCollections = function (name) {
                return CollectionService.findByName(name).then(function (response) {
                    return response.data.items.map(function (item) {
                        return item.name;
                    });
                });
            };

            var oldManga = null;

            var updateModel = function () {
                MangaService.edit($routeParams.id).then(function (response) {
                    $scope.manga = response.data.manga;
                    oldManga = angular.copy($scope.manga);
                }, function (response) {
                    $scope.alerts.push({type: 'warning', msg: response.data.msg});
                });
            };

            updateModel();

            $scope.save = function (manga) {
                if ($scope.mangaForm.$valid) {
                    MangaService.update(manga).then(function (response) {
                        oldManga = angular.copy($scope.manga);
                        $scope.alerts.push({type: 'success', msg: response.data.msg});
                    }, function (response) {
                        $scope.alerts.push({type: 'warning', msg: response.data.msg});
                    });
                }
            };

            $scope.reset = function () {
                updateModel();
            };

            $scope.uploadCover = function (file, manga) {
                Upload.upload({
                    url: '/api/mangas/cover/upload?mangaID=' + manga.id + '&directory=' + manga.collection + '&originalname=' + manga.collection + '_' + manga.number,
                    data: {file: file}
                }).then(function (response) {
                    $scope.alerts.push({type: 'success', msg: response.data.msg});
                }, function (response) {
                    $scope.alerts.push({type: 'warning', msg: response.data.msg});
                }, function (evt) {
                });
            };


        }]).controller('DeckMangaController', ['$scope', '$timeout', 'MangaService',
                   function ($scope, $timeout, MangaService) {

                   $scope.mangas = [];

                   var collection = "";

                   var timeout;

                   $scope.$watch('collection', function (newVal) {
                       if (newVal) {
                           if (timeout) $timeout.cancel(timeout);
                           timeout = $timeout(function () {
                                   collection = newVal;
                               paginate(1, 0);
                           }, 350);
                       }
                   });

                   var paginate = function (currentPage, skip) {
                       $scope.itemsPerPage = 20;
                       $scope.maxSize = 5;
                       $scope.bigCurrentPage = currentPage;

                       MangaService.paginate({
                           'manga': {collection: collection, name:""},
                           'limit': $scope.itemsPerPage,
                           'skip': skip
                       },true).then(function (response) {
                           $scope.mangas = response.data.items;
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


                   }]);