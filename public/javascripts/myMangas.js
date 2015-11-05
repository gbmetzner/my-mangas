angular.module('myMangas',
    ['ngRoute',
        'ui.bootstrap',
        'ngFileUpload',
        'myMangas.tpl',
        'akoenig.deckgrid',
        'publisher.controllers',
        'publisher.services',
        'publisher.routes',
        'collection.controllers',
        'collection.services',
        'collection.routes',
        'manga.controllers',
        'manga.services',
        'manga.routes']);
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
                $scope.type = "Collection";
                $scope.type = collection;
                ngDialog.open({
                    template: '/partials/templates/remove_dialog.html',
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
angular.module('manga.controllers', ['manga.services', 'collection.services', 'ngDialog', 'myMangas.tpl'])
    .controller('NewMangaController', ['$scope', 'MangaService', 'CollectionService',
        function ($scope, MangaService, CollectionService) {

            $scope.alerts = [];

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

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

        }]).controller('ListMangaController', ['$scope', '$timeout', 'MangaService', 'ngDialog',
        function ($scope, $timeout, MangaService, ngDialog) {

            var manga = {
                collection: "",
                name: ""
            };

            $scope.alerts = [];

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
                ngDialog.open({
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

            var paginate = function (currentPage, skip) {
                $scope.itemsPerPage = 10;
                $scope.maxSize = 5;
                $scope.bigCurrentPage = currentPage;

                MangaService.paginate({
                    'manga': manga,
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

        }]).controller('UpdateMangaController', ['$scope', '$routeParams', 'MangaService',
        'CollectionService', 'Upload', function ($scope, $routeParams, MangaService, CollectionService, Upload) {

            $scope.alerts = [];

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

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
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
                    //var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    //console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
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
                       }).then(function (response) {
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
                templateUrl: '/partials/collections/collection.html',
                controller: 'NewCollectionController'
            })
            .when('/views/collections', {
                templateUrl: '/partials/collections/collections.html',
                controller: 'ListCollectionController'
            })
            .when('/views/collection/:id/edit', {
                templateUrl: '/partials/collections/collection.html',
                controller: 'UpdateCollectionController'
            });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }]);
angular.module('manga.routes', ['manga.controllers'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/views/manga', {
                templateUrl: '/partials/mangas/manga.html',
                controller: 'NewMangaController'
            })
            .when('/views/mangas', {
                templateUrl: '/partials/mangas/mangas.html',
                controller: 'ListMangaController'
            })
            .when('/views/manga/:id/edit', {
                templateUrl: '/partials/mangas/manga.html',
                controller: 'UpdateMangaController'
            }).when('/', {
                templateUrl: '/partials/mangas/mangas_deck.html',
                controller: 'DeckMangaController'
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
                templateUrl: '/partials/publishers/publisher.html',
                controller: 'NewPublisherController'
            })
            .when('/views/publishers', {
                templateUrl: '/partials/publishers/publishers.html',
                controller: 'ListPublisherController'
            })
            .when('/views/publisher/:id/edit', {
                templateUrl: '/partials/publishers/publisher.html',
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
        findByName: function (name) {
            return doGet('/api/collections/search?name=' + name + '&limit=10&skip=0');
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
}])
;


angular.module('manga.services', []).factory('MangaService', ['$http', function ($http) {
    var baseURL = '/api/mangas';
    return {
        save: function (manga) {
            return $http.post(baseURL,manga);
        },
        edit: function (mangaID) {
            return $http.get(baseURL + '/' + mangaID + '/edit');
        },
        update: function (manga) {
            return $http.put(baseURL + '/' + manga.id, manga);
        },
        remove: function (mangaID) {
            return $http.delete(baseURL + '/' + mangaID);
        },
        paginate: function (mangaFilter) {
            var url = '/search?';
            if (mangaFilter.limit) {
                url = url + 'limit=' + mangaFilter.limit;
            }
            if (mangaFilter.skip) {
                url = url + '&skip=' + mangaFilter.skip;
            }
            if (mangaFilter.manga.collection) {
                url = url + '&collection=' + mangaFilter.manga.collection;
            }
            if (mangaFilter.manga.name) {
                url = url + '&name=' + mangaFilter.manga.name;
            }
            return $http.get(baseURL + url);
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

