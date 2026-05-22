var home = angular.module('dapp.HomeController',[]);

 home.controller('HomeController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService',function($scope, $window, $location,$state, $rootScope, FunctionalityService){

    $scope.sidebarActiveFun=function(){
        if($rootScope.sidebarActive==true){
            $rootScope.sidebarActive=false;
        }
        else{
            $rootScope.sidebarActive=true;
        }
    }

}]);