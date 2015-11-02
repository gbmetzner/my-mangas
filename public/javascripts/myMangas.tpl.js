(function(module) {
try {
  module = angular.module('myMangas.tpl');
} catch (e) {
  module = angular.module('myMangas.tpl', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('/partials/collections/collection.html',
    '<div class="col-sm-12">\n' +
    '    <div class="col-md-10">\n' +
    '        <form id="collectionForm" name="collectionForm" class="form-horizontal">\n' +
    '            <input type="hidden" name="id" ng-model="collection.id">\n' +
    '            <fieldset>\n' +
    '\n' +
    '                <legend>{{legend}}</legend>\n' +
    '\n' +
    '                <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '                    {{alert.msg}}\n' +
    '                </uib-alert>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="publisher">Publisher</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="publisher" name="publisher" type="text" ng-model="collection.publisher" placeholder="Publisher\'s name"\n' +
    '                               uib-typeahead="publisher for publisher in getPublishers($viewValue)"\n' +
    '                               typeahead-loading="loadingLocations" typeahead-no-results="noResults"\n' +
    '                               class="form-control">\n' +
    '                        <i ng-show="loadingLocations" class="glyphicon glyphicon-refresh"></i>\n' +
    '\n' +
    '                        <div ng-show="noResults">\n' +
    '                            <i class="glyphicon glyphicon-remove"></i> No Results Found\n' +
    '                        </div>\n' +
    '                        <span data-ng-show="collectionForm.publisher.$invalid">Please enter a valid name</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="name">Name</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="name" name="name" type="text" ng-model="collection.name"\n' +
    '                               placeholder="Collection\'s name" class="form-control input-md" required\n' +
    '                               pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                        <span data-ng-show="collectionForm.name.$invalid">Please enter a valid name</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="searchParameter">Search Parameter</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="searchParameter" name="searchParameter" type="text" ng-model="collection.searchParam"\n' +
    '                               placeholder="Search\'s parameter" class="form-control input-md" required\n' +
    '                               pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                        <span data-ng-show="collectionForm.searchParameter.$invalid">Please enter a valid name</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="isComplete">Is Complete</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="isComplete" name="isComplete" type="checkbox" class="btn btn-success"\n' +
    '                               ng-model="collection.isComplete">\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="submit"></label>\n' +
    '\n' +
    '                    <div class="col-md-8">\n' +
    '                        <button id="submit" name="submit" ng-click="save(collection)" class="btn btn-success">\n' +
    '                            Save\n' +
    '                        </button>\n' +
    '                        <button id="reset" name="reset" ng-click="reset()" class="btn btn-warning">Reset</button>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '            </fieldset>\n' +
    '        </form>\n' +
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
  $templateCache.put('/partials/collections/collections.html',
    '<div class="col-sm-12">\n' +
    '    <div class="col-md-10">\n' +
    '\n' +
    '        <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '            {{alert.msg}}\n' +
    '        </uib-alert>\n' +
    '\n' +
    '        <form class="form-horizontal">\n' +
    '            <fieldset>\n' +
    '                <legend>Search:</legend>\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="name">Collection</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="name" name="name" type="text" ng-model="name"\n' +
    '                               placeholder="Collection\'s name" class="form-control input-md">\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="publisher">Publisher</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="publisher" name="publisher" type="text" ng-model="publisher"\n' +
    '                               placeholder="Publisher\'s name" class="form-control input-md">\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </fieldset>\n' +
    '        </form>\n' +
    '\n' +
    '        <fieldset>\n' +
    '            <legend>Collections</legend>\n' +
    '            <table class="table table-striped">\n' +
    '                <thead>\n' +
    '                <tr>\n' +
    '                    <th>Name</th>\n' +
    '                    <th>Publisher</th>\n' +
    '                    <th>Is Complete?</th>\n' +
    '                    <th>Action</th>\n' +
    '                </tr>\n' +
    '                </thead>\n' +
    '                <tbody>\n' +
    '                <tr ng-repeat="item in items">\n' +
    '                    <td>{{item.name}}</td>\n' +
    '                    <td>{{item.publisher}}</td>\n' +
    '                    <td>{{item.isComplete ? Yes : No}}</td>\n' +
    '                    <td>\n' +
    '                        <button ng-click="openRemoveDialog(item)" class="btn btn-danger pull-right"\n' +
    '                                id="btn_delete">Remove\n' +
    '                        </button>\n' +
    '                        <a ng-href="/views/collection/{{item.id}}/edit" class="btn btn-success pull-right"\n' +
    '                           id="btn_update">Edit</a>\n' +
    '                    </td>\n' +
    '                </tr>\n' +
    '                </tbody>\n' +
    '                <tfoot>\n' +
    '                <tr>\n' +
    '                    <td>\n' +
    '                        <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                        class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                        rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                        </uib-pagination>\n' +
    '                    </td>\n' +
    '                </tr>\n' +
    '                </tfoot>\n' +
    '            </table>\n' +
    '        </fieldset>\n' +
    '    </div>\n' +
    '    <script type="text/ng-template" id="removeCollectionTemplate">\n' +
    '        <div class="ngdialog-message">\n' +
    '            <h3>Remove Collection</h3>\n' +
    '\n' +
    '            <p>Are you sure you want to remove the Collection: {{collectionToRemove.name}}?</p>\n' +
    '        </div>\n' +
    '        <div class="ngdialog-buttons">\n' +
    '            <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog()">\n' +
    '                Cancel\n' +
    '            </button>\n' +
    '            <button type="button" class="ngdialog-button ngdialog-button-primary"\n' +
    '                    ng-click="confirm(collectionToRemove.id)">Save\n' +
    '            </button>\n' +
    '        </div>\n' +
    '    </script>\n' +
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
    '    <div class="col-md-10" >\n' +
    '        <form id="publisherForm" name="publisherForm" class="form-horizontal">\n' +
    '            <input type="hidden" name="id" ng-model="publisher.id">\n' +
    '            <fieldset>\n' +
    '\n' +
    '                <legend>{{legend}}</legend>\n' +
    '\n' +
    '                <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '                    {{alert.msg}}\n' +
    '                </uib-alert>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="name">Name</label>\n' +
    '\n' +
    '                    <div class="col-md-4">\n' +
    '                        <input id="name" name="name" type="text" ng-model="publisher.name"\n' +
    '                               placeholder="Publisher\'s name" class="form-control input-md" required\n' +
    '                               pattern=".{2,}" title="Please type at least 2 characters">\n' +
    '                        <span data-ng-show="publisherForm.name.$invalid">Please enter a valid name</span>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="form-group">\n' +
    '                    <label class="col-md-4 control-label" for="submit"></label>\n' +
    '\n' +
    '                    <div class="col-md-8">\n' +
    '                        <button id="submit" name="submit" ng-click="save(publisher)" class="btn btn-success">\n' +
    '                            Save\n' +
    '                        </button>\n' +
    '                        <button id="reset" name="reset" ng-click="reset()" class="btn btn-warning">Reset</button>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '            </fieldset>\n' +
    '        </form>\n' +
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
  $templateCache.put('/partials/publishers/publishers.html',
    '<div class="col-sm-12">\n' +
    '    <div class="col-md-10">\n' +
    '        <div ng-controller="ListPublisherController">\n' +
    '\n' +
    '            <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">\n' +
    '                {{alert.msg}}\n' +
    '            </uib-alert>\n' +
    '\n' +
    '            <form class="form-horizontal">\n' +
    '                <fieldset>\n' +
    '                    <legend>Search:</legend>\n' +
    '                    <div class="form-group">\n' +
    '                        <label class="col-md-4 control-label" for="name">Name</label>\n' +
    '\n' +
    '                        <div class="col-md-4">\n' +
    '                            <input id="name" name="name" type="text" ng-model="name"\n' +
    '                                   placeholder="Publisher\'s name" class="form-control input-md">\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </fieldset>\n' +
    '            </form>\n' +
    '\n' +
    '            <fieldset>\n' +
    '                <legend>Publishers</legend>\n' +
    '                <table class="table table-striped">\n' +
    '                    <thead>\n' +
    '                    <tr>\n' +
    '                        <th>Name</th>\n' +
    '                        <th>Action</th>\n' +
    '                    </tr>\n' +
    '                    </thead>\n' +
    '                    <tbody>\n' +
    '                    <tr ng-repeat="item in items">\n' +
    '                        <td>{{item.name}}</td>\n' +
    '                        <td>\n' +
    '                            <button ng-click="openRemoveDialog(item)" class="btn btn-danger pull-right"\n' +
    '                                    id="btn_delete">Remove\n' +
    '                            </button>\n' +
    '                            <a ng-href="/views/publisher/{{item.id}}/edit" class="btn btn-success pull-right"\n' +
    '                               id="btn_update">Edit</a>\n' +
    '                        </td>\n' +
    '                    </tr>\n' +
    '                    </tbody>\n' +
    '                    <tfoot>\n' +
    '                    <tr>\n' +
    '                        <td>\n' +
    '                            <uib-pagination total-items="bigTotalItems" ng-model="bigCurrentPage" max-size="maxSize"\n' +
    '                                            class="pagination-sm" items-per-page="itemsPerPage" boundary-links="true"\n' +
    '                                            rotate="false" num-pages="numPages" ng-change="pageChanged()">\n' +
    '                            </uib-pagination>\n' +
    '                        </td>\n' +
    '                    </tr>\n' +
    '                    </tfoot>\n' +
    '                </table>\n' +
    '            </fieldset>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '    <script type="text/ng-template" id="removePublisherTemplate">\n' +
    '        <div class="ngdialog-message">\n' +
    '            <h3>Remove Publisher</h3>\n' +
    '            <p>Are you sure you want to remove the Publisher: {{publisherToRemove.name}}</p>\n' +
    '        </div>\n' +
    '        <div class="ngdialog-buttons">\n' +
    '            <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog()">Cancel</button>\n' +
    '            <button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="confirm(publisherToRemove.id)">Save</button>\n' +
    '        </div>\n' +
    '    </script>\n' +
    '</div>');
}]);
})();
