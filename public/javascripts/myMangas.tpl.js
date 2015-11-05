(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/collections/collection.html',
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>{{legend}}</legend>\n' +
    '\n' +
    '        <form id="collectionForm" name="collectionForm">\n' +
    '\n' +
    '            <input type="hidden" name="id" ng-model="collection.id">\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="publisher">Publisher</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="publisher" name="publisher" type="text" ng-model="collection.publisher"\n' +
    '                           placeholder="Publisher\'s name"\n' +
    '                           uib-typeahead="publisher for publisher in getPublishers($viewValue)"\n' +
    '                           typeahead-loading="loadingLocations" typeahead-no-results="noResults">\n' +
    '                    <i ng-show="loadingLocations" class="glyphicon glyphicon-refresh"></i>\n' +
    '\n' +
    '                    <div ng-show="noResults">\n' +
    '                        <i class="glyphicon glyphicon-remove"></i> No Results Found\n' +
    '                    </div>\n' +
    '                    <span data-ng-show="collectionForm.publisher.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="name">Name</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="name" name="name" type="text" ng-model="collection.name"\n' +
    '                           placeholder="Collection\'s name" required\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="collectionForm.name.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="searchParameter">Search Parameter</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="searchParameter" name="searchParameter" type="text" ng-model="collection.searchParam"\n' +
    '                           placeholder="Search\'s parameter" required\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="collectionForm.searchParameter.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="isComplete">Is Complete</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="isComplete" name="isComplete" type="checkbox"\n' +
    '                           ng-model="collection.isComplete" ng-true-value="true" ng-false-value="false">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <button id="submit" name="submit" ng-click="save(collection)">\n' +
    '                    Save\n' +
    '                </button>\n' +
    '                <button id="reset" name="reset" ng-click="reset()">Reset</button>\n' +
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
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>Search</legend>\n' +
    '\n' +
    '        <form>\n' +
    '            <div>\n' +
    '                <label for="publisher">Publisher</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="publisher" name="publisher" type="text" ng-model="collection.publisher"\n' +
    '                           placeholder="Publisher\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '            <div>\n' +
    '                <label for="collection">Collection</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="collection" name="collection" type="text" ng-model="collection.name"\n' +
    '                           placeholder="Collection\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>Collections</legend>\n' +
    '        <table>\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th>Name</th>\n' +
    '                <th>Publisher</th>\n' +
    '                <th>Is Complete?</th>\n' +
    '                <th>Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody>\n' +
    '            <tr ng-repeat="item in items">\n' +
    '                <td>{{item.name}}</td>\n' +
    '                <td>{{item.publisher}}</td>\n' +
    '                <td>{{item.isComplete ? \'Yes\' : \'No\'}}</td>\n' +
    '                <td>\n' +
    '                    <button ng-click="openRemoveDialog(item)"\n' +
    '                            id="btn_delete">Remove\n' +
    '                    </button>\n' +
    '                    <a ng-href="/views/collection/{{item.id}}/edit"\n' +
    '                       id="btn_update">Edit</a>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td>\n' +
    '                    <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                    class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                    rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                    </uib-pagination>\n' +
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
  $templateCache.put('/partials/mangas/manga.html',
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>{{legend}}</legend>\n' +
    '\n' +
    '        <form id="mangaForm" name="mangaForm">\n' +
    '            <input type="hidden" name="id" ng-model="manga.id">\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="collection">Collection</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="collection" name="collection" type="text" ng-model="manga.collection"\n' +
    '                           placeholder="Collection\'s name" required\n' +
    '                           uib-typeahead="collection for collection in getCollections($viewValue)"\n' +
    '                           typeahead-loading="loadingCollections" typeahead-no-results="noResults"\n' +
    '                    >\n' +
    '                    <i ng-show="loadingCollections" class="glyphicon glyphicon-refresh"></i>\n' +
    '\n' +
    '                    <div ng-show="noResults">\n' +
    '                        <i class="glyphicon glyphicon-remove"></i> No Results Found\n' +
    '                    </div>\n' +
    '                    <span data-ng-show="mangaForm.collection.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="name">Name</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="name" name="name" type="text" ng-model="manga.name"\n' +
    '                           placeholder="Manga\'s name" required\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="mangaForm.name.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="number">Number</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="number" name="number" type="number" ng-model="manga.number"\n' +
    '                           placeholder="Manga\'s number" required\n' +
    '                           title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="mangaForm.number.$invalid">Please enter a valid number</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="doIHaveIt">Do I have it?</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="doIHaveIt" name="doIHaveIt" type="checkbox"\n' +
    '                           ng-model="manga.doIHaveIt" ng-true-value="true" ng-false-value="false">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div data-ng-show="{{showUploadCover}}">\n' +
    '                <label for="cover">Cover</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="cover" type="file" ngf-select ng-model="cover" name="cover"\n' +
    '                           accept="image/*" ngf-max-size="2MB">\n' +
    '                </div>\n' +
    '                <button ng-disabled="!mangaForm.$valid" ng-click="uploadCover(cover, manga)">Submit</button>\n' +
    '            </div>\n' +
    '\n' +
    '\n' +
    '            <div>\n' +
    '                <button id="submit" name="submit" ng-click="save(manga)">\n' +
    '                    Save\n' +
    '                </button>\n' +
    '                <button id="reset" name="reset" ng-click="reset()">Reset</button>\n' +
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
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>Search</legend>\n' +
    '\n' +
    '        <form>\n' +
    '            <div>\n' +
    '                <label for="collection">Collection</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="collection" name="collection" type="text" ng-model="manga.collection"\n' +
    '                           placeholder="Collection\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '            <div>\n' +
    '                <label for="name">Name</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="name" name="name" type="text" ng-model="manga.name"\n' +
    '                           placeholder="Manga\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>Mangas</legend>\n' +
    '        <table>\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th>Name</th>\n' +
    '                <th>Collection</th>\n' +
    '                <th>Number</th>\n' +
    '                <th>Do I have it?</th>\n' +
    '                <th>Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody>\n' +
    '            <tr ng-repeat="item in items">\n' +
    '                <td>{{item.name}}</td>\n' +
    '                <td>{{item.collection}}</td>\n' +
    '                <td>{{item.number}}</td>\n' +
    '                <td>{{item.doIHaveIt ? \'Yes\' : \'No\'}}</td>\n' +
    '                <td>\n' +
    '                    <button ng-click="openRemoveDialog(item)"\n' +
    '                            id="btn_delete">Remove\n' +
    '                    </button>\n' +
    '                    <a ng-href="/views/manga/{{item.id}}/edit"\n' +
    '                       id="btn_update">Edit</a>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td>\n' +
    '                    <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                    class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                    rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                    </uib-pagination>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            <tr>\n' +
    '                <td><span>Total of {{bigTotalItems}} mangas</span></td>\n' +
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
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>Search</legend>\n' +
    '        <form>\n' +
    '            <div>\n' +
    '                <label for="collection">Collection</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="collection" name="collection" type="text" ng-model="collection"\n' +
    '                           placeholder="Collection\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>Editions</legend>\n' +
    '\n' +
    '        <div>\n' +
    '            <div deckgrid class="deckgrid" cardTemplate="/partials/mangas/templates/card_deck.html"\n' +
    '                 source="mangas">\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </fieldset>\n' +
    '    <div>\n' +
    '        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                        class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
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
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>{{legend}}</legend>\n' +
    '\n' +
    '        <form id="publisherForm" name="publisherForm">\n' +
    '            <input type="hidden" name="id" ng-model="publisher.id">\n' +
    '\n' +
    '            <div>\n' +
    '                <label for="name">Name</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="name" name="name" type="text" ng-model="publisher.name"\n' +
    '                           placeholder="Publisher\'s name" required\n' +
    '                           pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                    <span data-ng-show="publisherForm.name.$invalid">Please enter a valid name</span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '            <div>\n' +
    '                <button id="submit" name="submit" ng-click="save(publisher)">\n' +
    '                    Save\n' +
    '                </button>\n' +
    '                <button id="reset" name="reset" ng-click="reset()">Reset</button>\n' +
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
    '<div>\n' +
    '\n' +
    '    <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '        {{alert.msg}}\n' +
    '    </uib-alert>\n' +
    '\n' +
    '    <fieldset>\n' +
    '\n' +
    '        <legend>Search:</legend>\n' +
    '\n' +
    '        <form>\n' +
    '            <div>\n' +
    '                <label for="name">Name</label>\n' +
    '\n' +
    '                <div>\n' +
    '                    <input id="name" name="name" type="text" ng-model="name"\n' +
    '                           placeholder="Publisher\'s name">\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '        </form>\n' +
    '    </fieldset>\n' +
    '\n' +
    '    <fieldset>\n' +
    '        <legend>Publishers</legend>\n' +
    '\n' +
    '        <table>\n' +
    '            <thead>\n' +
    '            <tr>\n' +
    '                <th>Name</th>\n' +
    '                <th>Action</th>\n' +
    '            </tr>\n' +
    '            </thead>\n' +
    '            <tbody>\n' +
    '            <tr ng-repeat="item in items">\n' +
    '                <td>{{item.name}}</td>\n' +
    '                <td>\n' +
    '                    <button ng-click="openRemoveDialog(item)"\n' +
    '                            id="btn_delete">Remove\n' +
    '                    </button>\n' +
    '                    <a ng-href="/views/publisher/{{item.id}}/edit"\n' +
    '                       id="btn_update">Edit</a>\n' +
    '                </td>\n' +
    '            </tr>\n' +
    '            </tbody>\n' +
    '            <tfoot>\n' +
    '            <tr>\n' +
    '                <td>\n' +
    '                    <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                    class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                    rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                    </uib-pagination>\n' +
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
  $templateCache.put('/partials/templates/remove_dialog.html',
    '<div class="ngdialog-message">\n' +
    '    <h3>Remove {{type}}</h3>\n' +
    '\n' +
    '    <p>Are you sure you want to remove the item: {{item.name}}?</p>\n' +
    '</div>\n' +
    '<div class="ngdialog-buttons">\n' +
    '    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog()">\n' +
    '        Cancel\n' +
    '    </button>\n' +
    '    <button type="button" class="ngdialog-button ngdialog-button-primary"\n' +
    '            ng-click="confirm(item.id)">Save\n' +
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
    '<div class="cover">\n' +
    '    <a id="btnUpdate" ng-href="/views/manga/{{card.id}}/edit">\n' +
    '        <h2>{{card.collection}} # {{card.number}}</h2>\n' +
    '    </a>\n' +
    '\n' +
    '    <div class="thumbnail">\n' +
    '        <img src="" class="portrait" alt="Image" data-ng-src="{{card.publicLink}}"/>\n' +
    '    </div>\n' +
    '    <div class="text-left">\n' +
    '        <span class="glyphicon glyphicon-{{card.doIHaveIt ? \'ok\' : \'remove\'}}" aria-hidden="true"></span>\n' +
    '    </div>\n' +
    '</div>');
}]);
})();
