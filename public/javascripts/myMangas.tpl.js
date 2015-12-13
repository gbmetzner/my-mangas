(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/collections/collection.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>{{legend}}</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="collectionForm" name="collectionForm" class="form-horizontal">\n' +
    '\n' +
    '            <input type="hidden" name="id" data-ng-model="collection.id">\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="publisher" class="col-sm-2 control-label">Publisher</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="publisher" name="publisher" type="text" data-ng-model="collection.publisher"\n' +
    '                           placeholder="Publisher\'s name" required class="form-control"\n' +
    '                           uib-typeahead="publisher for publisher in getPublishers($viewValue)"\n' +
    '                           typeahead-loading="loadingLocations" typeahead-no-results="noResults">\n' +
    '                    <i data-ng-show="loadingLocations" class="glyphicon glyphicon-refresh"></i>\n' +
    '\n' +
    '                    <div data-ng-show="noResults">\n' +
    '                        <i class="glyphicon glyphicon-remove"></i> No Results Found\n' +
    '                    </div>\n' +
    '                    <span data-ng-show="collectionForm.publisher.$invalid">\n' +
    '                        <small>Please enter a valid publisher</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="name" class="col-sm-2 control-label">Name</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="name" name="name" type="text" data-ng-model="collection.name"\n' +
    '                           placeholder="Collection\'s name" required class="form-control"\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="collectionForm.name.$invalid">\n' +
    '                        <small>Please enter a valid name</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="searchParameter" class="col-sm-2 control-label">Search Parameter</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="searchParameter" name="searchParameter" type="text"\n' +
    '                           data-ng-model="collection.searchParam" class="form-control"\n' +
    '                           placeholder="Search\'s parameter" required\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="collectionForm.searchParameter.$invalid">\n' +
    '                        <small>Please enter a valid param</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="isComplete" class="col-sm-2 control-label">Is Complete?</label>\n' +
    '                <div class="checkbox">\n' +
    '                    <label>\n' +
    '                        <input id="isComplete" name="isComplete" type="checkbox"\n' +
    '                               data-ng-model="collection.isComplete" data-ng-true-value="true"\n' +
    '                               data-ng-false-value="false">\n' +
    '                    </label>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="submit" class="col-sm-3 control-label"></label>\n' +
    '\n' +
    '                <div class="col-md-6">\n' +
    '                    <button id="submit" name="submit" data-ng-click="save(collection)" class="btn btn-primary">\n' +
    '                        Submit\n' +
    '                    </button>\n' +
    '                    <button id="reset" name="reset" data-ng-click="reset()" class="btn btn-default">\n' +
    '                        Reset\n' +
    '                    </button>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '\n' +
    '    </fieldset>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/collections/collections.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Search</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="collectionsForm" name="collectionsForm" class="form-horizontal">\n' +
    '            <div class="form-group">\n' +
    '                <label for="publisher" class="col-sm-2 control-label">Publisher</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="publisher" name="publisher" type="text" data-ng-model="collection.publisher"\n' +
    '                           placeholder="Publisher\'s name" class="form-control">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '            <div class="form-group">\n' +
    '                <label for="collection" class="col-sm-2 control-label">Collection</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="collection" name="collection" type="text" data-ng-model="collection.name"\n' +
    '                           placeholder="Collection\'s name" class="form-control">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Collections</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <table class="table table-striped table-bordered text-center">\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th class="text-center">Name</th>\n' +
    '                <th class="text-center">Publisher</th>\n' +
    '                <th class="text-center">Is Complete?</th>\n' +
    '                <th class="text-center">Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody class="text-center">\n' +
    '            <tr data-ng-repeat="collection in collections">\n' +
    '                <td>{{collection.name}}</td>\n' +
    '                <td>{{collection.publisher}}</td>\n' +
    '                <td width="15%">{{collection.isComplete ? \'Yes\' : \'No\'}}</td>\n' +
    '                <td width="20%">\n' +
    '                    <a data-ng-href="/views/collection/{{collection.id}}/edit" id="update" class="btn btn-primary">\n' +
    '                        Edit\n' +
    '                    </a>\n' +
    '                    <button data-ng-click="openRemoveDialog(collection)" id="delete" class="btn btn-warning">\n' +
    '                        Remove\n' +
    '                    </button>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td colspan="4">\n' +
    '                    <div class="text-center">\n' +
    '                        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                        class="pagination" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                        rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                        </uib-pagination>\n' +
    '                    </div>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tfoot>\n' +
    '        </table>\n' +
    '    </fieldset>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/errors/404.html',
    '<div class="container-fluid">\n' +
    '    <div class="hero-unit text-center">\n' +
    '        <h1>Ops!\n' +
    '            <small><font face="Tahoma" color="red">Page Not Found :(</font></small>\n' +
    '        </h1>\n' +
    '        <br/>\n' +
    '\n' +
    '        <p>The page you requested could not be found, either contact your webmaster or try again. Use your\n' +
    '            browsers <b>Back</b> button to navigate to the page you have prevously come from</p>\n' +
    '\n' +
    '        <p><b>Or you could just press this neat little button:</b></p>\n' +
    '        <a href="#" data-ng-href="/" class="btn btn-large btn-info"><i class="icon-home icon-white"></i> Take Me to My Mangas</a>\n' +
    '    </div>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/login/logged.html',
    '<ul class="nav navbar-nav navbar-right">\n' +
    '    <li>\n' +
    '        <p class="navbar-text">Welcome <b>{{name}}</b>!</p>\n' +
    '    </li>\n' +
    '    <li>\n' +
    '        <a href="#" role="button" class="btn btn-default" data-ng-click="logout()">Log out</a>\n' +
    '    </li>\n' +
    '</ul>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/login/not_logged.html',
    '<form id="loginForm" name="form.loginForm" class="navbar-form navbar-right" >\n' +
    '    <div class="form-group">\n' +
    '        <input id="username" type="text" class="form-control" name="username" placeholder="Username"\n' +
    '               data-ng-model="loginData.username" required>\n' +
    '    </div>\n' +
    '    <div class="form-group">\n' +
    '        <input id="password" type="password" class="form-control" name="password" placeholder="Password"\n' +
    '               data-ng-model="loginData.password" required>\n' +
    '    </div>\n' +
    '    <button class="btn btn-default" data-ng-click="login(loginData)">Sign In</button>\n' +
    '</form>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/mangas/manga.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>{{legend}}</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="mangaForm" name="mangaForm" class="form-horizontal">\n' +
    '            <input type="hidden" name="id" data-ng-model="manga.id">\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="collection" class="col-sm-2 control-label">Collection</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="collection" name="collection" type="text" data-ng-model="manga.collection"\n' +
    '                           placeholder="Collection\'s name" required\n' +
    '                           uib-typeahead="collection for collection in getCollections($viewValue)"\n' +
    '                           typeahead-loading="loadingCollections" typeahead-no-results="noResults"\n' +
    '                           class="form-control">\n' +
    '                    <i data-ng-show="loadingCollections" class="glyphicon glyphicon-refresh"></i>\n' +
    '\n' +
    '                    <div data-ng-show="noResults">\n' +
    '                        <i class="glyphicon glyphicon-remove"></i> No Results Found\n' +
    '                    </div>\n' +
    '                    <span data-ng-show="mangaForm.collection.$invalid">\n' +
    '                        <small>Please enter a valid collection</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="name" class="col-sm-2 control-label">Name</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="name" name="name" type="text" data-ng-model="manga.name"\n' +
    '                           placeholder="Manga\'s name" required class="form-control"\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="mangaForm.name.$invalid">\n' +
    '                        <small>Please enter a valid name</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="number" class="col-sm-2 control-label">Number</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="number" name="number" type="number" data-ng-model="manga.number"\n' +
    '                           placeholder="Manga\'s number" required class="form-control"\n' +
    '                           title="Please type at least 1 characters" min="1">\n' +
    '                    <span data-ng-show="mangaForm.number.$invalid">\n' +
    '                       <small>Please enter a valid number</small>\n' +
    '                    </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="doIHaveIt" class="col-sm-2 control-label">Do I have it?</label>\n' +
    '\n' +
    '                <div class="checkbox">\n' +
    '                    <label>\n' +
    '                        <input id="doIHaveIt" name="doIHaveIt" type="checkbox"\n' +
    '                               data-ng-model="manga.doIHaveIt" data-ng-true-value="true"\n' +
    '                               data-ng-false-value="false">\n' +
    '                    </label>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div data-ng-show="{{showUploadCover}}" class="form-group">\n' +
    '                <label for="cover" class="col-sm-2 control-label">Cover</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="cover" type="file" data-ngf-select data-ng-model="cover" name="cover"\n' +
    '                           class="form-control"\n' +
    '                           accept="image/jpeg" data-ngf-max-size="2MB">\n' +
    '                </div>\n' +
    '                <button data-ng-disabled="!mangaForm.$valid" data-ng-click="uploadCover(cover, manga)">Submit</button>\n' +
    '            </div>\n' +
    '\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="submit" class="col-sm-3 control-label"></label>\n' +
    '\n' +
    '                <div class="col-md-6">\n' +
    '                    <button id="submit" name="submit" data-ng-click="save(manga)" class="btn btn-primary">\n' +
    '                        Save\n' +
    '                    </button>\n' +
    '                    <button id="reset" name="reset" data-ng-click="reset()" class="btn btn-default">\n' +
    '                        Reset\n' +
    '                    </button>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/mangas/mangas.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Search</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="mangasForm" name="mangasForm" class="form-horizontal">\n' +
    '            <div class="form-group">\n' +
    '                <label for="collection" class="col-sm-2 control-label">Collection</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="collection" name="collection" type="text" data-ng-model="manga.collection"\n' +
    '                           placeholder="Collection\'s name" class="form-control">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="name" class="col-sm-2 control-label">Name</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="name" name="name" type="text" data-ng-model="manga.name"\n' +
    '                           placeholder="Manga\'s name" class="form-control">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Mangas</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '        <table class="table table-striped table-bordered text-center">\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th class="text-center">Collection</th>\n' +
    '                <th class="text-center">Name</th>\n' +
    '                <th class="text-center">Number</th>\n' +
    '                <th class="text-center">Do I have it?</th>\n' +
    '                <th class="text-center">Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody class="text-center">\n' +
    '            <tr data-ng-repeat="manga in mangas">\n' +
    '                <td>{{manga.collection}}</td>\n' +
    '                <td>{{manga.name}}</td>\n' +
    '                <td>{{manga.number}}</td>\n' +
    '                <td>\n' +
    '                    <a role="button" href="#" data-ng-click="updateOwnership(manga)">\n' +
    '                        <span class="glyphicon glyphicon-{{manga.doIHaveIt ? \'ok green-glyphicon\' : \'remove red-glyphicon\'}}"></span>\n' +
    '                    </a>\n' +
    '                </td>\n' +
    '                <td>\n' +
    '                    <a data-ng-href="/views/manga/{{manga.id}}/edit" id="update" class="btn btn-primary">\n' +
    '                        Edit\n' +
    '                    </a>\n' +
    '                    <button data-ng-click="openRemoveDialog(manga)" id="delete" class="btn btn-warning">\n' +
    '                        Remove\n' +
    '                    </button>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td colspan="5">\n' +
    '                    <div class="text-center">\n' +
    '                        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                        class="pagination" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                        rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                        </uib-pagination>\n' +
    '                    </div>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            <tr>\n' +
    '                <td colspan="5" class="text-center">\n' +
    '                    <span>Total of {{bigTotalItems}} mangas</span>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tfoot>\n' +
    '        </table>\n' +
    '    </fieldset>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/mangas/mangas_deck.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Search</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '        <form id="mangasDeckForm" name="mangasDeckForm" class="form-horizontal">\n' +
    '            <div class="form-group">\n' +
    '                <label for="collection" class="col-sm-2 control-label">Collection</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="collection" class="form-control" name="collection" type="text"\n' +
    '                           data-ng-model="collection"\n' +
    '                           placeholder="Collection\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Latest Publications</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '        <div class="text-center">\n' +
    '            <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                            class="pagination" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                            rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '            </uib-pagination>\n' +
    '        </div>\n' +
    '        <div>\n' +
    '            <div deckgrid class="deckgrid" cardTemplate="/partials/mangas/templates/card_deck.html"\n' +
    '                 source="mangas">\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </fieldset>\n' +
    '    <div class="text-center">\n' +
    '        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                        class="pagination" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                        rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '        </uib-pagination>\n' +
    '    </div>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/publishers/publisher.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>{{legend}}</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="publisherForm" name="publisherForm" class="form-horizontal">\n' +
    '            <input type="hidden" name="id" data-ng-model="publisher.id">\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="name" class="col-sm-2 control-label">Name</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="name" name="name" type="text" data-ng-model="publisher.name"\n' +
    '                           class="form-control" placeholder="Publisher\'s name"\n' +
    '                           required pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="publisherForm.name.$invalid"><small>Please enter a valid name</small></span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div class="form-group">\n' +
    '                <label for="submit" class="col-sm-3 control-label"></label>\n' +
    '\n' +
    '                <div class="col-md-6">\n' +
    '                    <button id="submit" name="submit" data-ng-click="save(publisher)" class="btn btn-primary">\n' +
    '                        Submit\n' +
    '                    </button>\n' +
    '                    <button id="reset" name="reset" data-ng-click="reset()" class="btn btn-default">Reset</button>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/publishers/publishers.html',
    '<div class="col-sm-12">\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Search</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <form id="publishersForm" name="publishersForm" class="form-horizontal">\n' +
    '            <div class="form-group">\n' +
    '                <label for="name" class="col-sm-2 control-label">Name</label>\n' +
    '\n' +
    '                <div class="col-sm-3">\n' +
    '                    <input id="name" name="name" type="text" data-ng-model="name"\n' +
    '                           placeholder="Publisher\'s name" class="form-control">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>\n' +
    '            <h3>\n' +
    '                <small>Publishers</small>\n' +
    '            </h3>\n' +
    '        </legend>\n' +
    '\n' +
    '        <table class="table table-striped table-bordered text-center">\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th class="text-center">Name</th>\n' +
    '                <th class="text-center">Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody class="text-center">\n' +
    '            <tr data-ng-repeat="publisher in publishers">\n' +
    '                <td width="70%">{{publisher.name}}</td>\n' +
    '                <td width="30%">\n' +
    '                    <a id="update" data-ng-href="/views/publisher/{{publisher.id}}/edit" class="btn btn-primary">\n' +
    '                        Edit\n' +
    '                    </a>\n' +
    '                    <button id="delete" class="btn btn-warning" data-ng-click="openRemoveDialog(publisher)">\n' +
    '                        Remove\n' +
    '                    </button>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td colspan="2">\n' +
    '                    <div class="text-center">\n' +
    '                        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                        class="pagination" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                        rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                        </uib-pagination>\n' +
    '                    </div>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tfoot>\n' +
    '        </table>\n' +
    '\n' +
    '    </fieldset>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/templates/info_dialog.html',
    '<div class="ngdialog-message">\n' +
    '    <h3>\n' +
    '        <small>Info!</small>\n' +
    '    </h3>\n' +
    '\n' +
    '    <p>{{message}}</p>\n' +
    '</div>\n' +
    '<div class="ngdialog-buttons">\n' +
    '    <button type="button" class="ngdialog-button ngdialog-button-secondary" data-ng-click="closeThisDialog()">\n' +
    '        Ok\n' +
    '    </button>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/templates/messages.html',
    '<div id="messages" class="messages center-block text-center">\n' +
    '    <uib-alert data-ng-repeat="alert in alerts" dismiss-on-timeout=\'3000\' type="{{alert.type}}"\n' +
    '               close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '</div>');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/templates/remove_dialog.html',
    '<div class="ngdialog-message">\n' +
    '    <h3>\n' +
    '        <small>Remove {{type}}</small>\n' +
    '    </h3>\n' +
    '\n' +
    '    <p>Are you sure you want to remove the item: {{item.name}}?</p>\n' +
    '</div>\n' +
    '<div class="ngdialog-buttons">\n' +
    '    <button type="button" class="ngdialog-button ngdialog-button-secondary" data-ng-click="closeThisDialog()">\n' +
    '        Cancel\n' +
    '    </button>\n' +
    '    <button type="button" class="ngdialog-button ngdialog-button-primary"\n' +
    '            data-ng-click="confirm(item.id)">Save\n' +
    '    </button>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/mangas/templates/card_deck.html',
    '<div class="text-center">\n' +
    '    <a id="btnUpdate" data-ng-href="/views/manga/{{card.id}}/edit">\n' +
    '        <h3>{{card.collection}} # {{card.number}}</h3>\n' +
    '        <div class="thumbnail">\n' +
    '            <div class="text-left" >\n' +
    '                <span class="glyphicon glyphicon-{{card.doIHaveIt ? \'ok green-glyphicon\' : \'remove red-glyphicon\'}}"></span>\n' +
    '            </div>\n' +
    '            <img src="" class="portrait img-responsive" alt="Image" data-ng-src="{{card.publicLink}}"/>\n' +
    '        </div>\n' +
    '    </a>\n' +
    '</div>');
}]);
})();
