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
