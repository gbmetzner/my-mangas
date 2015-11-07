angular.module('login.controllers', ['login.services', 'ngDialog', 'myMangas.tpl', 'ngCookies'])
    .controller('LoginController', ['$scope', '$cookies', 'LoginService', 'ngDialog',
        function ($scope, $cookies, LoginService, ngDialog) {

            var logged = false;

            var token = $cookies["XSRF-TOKEN"];

            $scope.form = {};

            if (token) {
                LoginService.logged()
                .then(
                    function(response){
                        $scope.name = response.data.user.name;
                        logged = true;
                },
                    function(response){
                        logged = false;
                });
            }

              var dialog = function(message){

                $scope.message = message;

                ngDialog.open({
                         template: '/partials/templates/info_dialog.html',
                         className: 'ngdialog-theme-default',
                         scope: $scope
                     });
                };

              $scope.isAuthenticated = function(){
                return logged;
              };

              $scope.login = function(loginData){
                if ($scope.form.loginForm.$valid){
                    LoginService.login(loginData).then(function(response){
                        token = response.data.token;
                        $scope.name = response.data.user.name;
                        logged = true;
                    },
                    function(response){
                        logged = false;
                        dialog(response.data.msg);
                    });
                }
              };

              $scope.logout = function(){
                LoginService.logout().then(function(response){
                    dialog(response.data.msg);
                    $scope.name = undefined;
                    logged = false;
                },
                function(response){
                    logged = false;
                });
              };

        }]);