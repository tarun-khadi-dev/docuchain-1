var adminDashboard = angular.module('dapp.SaAauditLogController',['angularUtils.directives.dirPagination']);

adminDashboard.controller('SaAauditLogController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService',function($scope, $window, $location,$state, $rootScope, FunctionalityService){
     
    $scope.userProfileId = $window.localStorage.getItem('userId');

    $scope.activity;
    $scope.event;
    $scope.tabName='tab2';
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.loader= false;
    
    $scope.$on('$viewContentLoaded', function () { 
        var adminId = {"superAdminId":$scope.userProfileId};
        FunctionalityService.getHistorybysuperAdmin(adminId) 
          .then(function (response) {
            if (response.status == 200) { 
              $scope.historyList = response.data.historyInfo;
              $scope.historyList = $scope.historyList.reverse();
              console.log("$scope.historyList::"+JSON.stringify($scope.historyList));
          }
          }, function (error) {
            console.log("message :: " + error);
          });
      })
    
      $scope.activityFunction = function (activity){
        $scope.activity = activity; 
        if (activity == 'All'){
          $scope.event = 'All';
          var adminId = {"superAdminId":$scope.userProfileId};
          FunctionalityService.getHistorybysuperAdmin(adminId) 
            .then(function (response) {
              if (response.status == 200) { 
                console.log("getHistorybyadmin Response", response);
                $scope.historyList = response.data.historyInfo;
                $scope.historyList = $scope.historyList.reverse();
            }
            }, function (error) {
              console.log("message :: " + error);
            });
        }else {        
            if ( $scope.event != 'All'){
              var data = {
                  "superAdminId":$scope.userProfileId,
                  "selectType":$scope.activity,
                  "objectStatus":$scope.event
                 
                };
            }else {
              var data = {
                  "superAdminId":$scope.userProfileId,
                  "selectType":$scope.activity
                  
                };
            }
            
          FunctionalityService.gethistorybasedSuperAdminonselecttype(data)
          .then(function (response) {
              if (response.status == 200) { 
                console.log("gethistorybasedonselecttype Response", response);
                $scope.historyList = response.data.historyInfo;
                $scope.historyList = $scope.historyList.reverse();
            }
            }, function (error) {
              console.log("message :: " + error);
            });
        
      }  
      }

      $scope.eventFunction = function (event){
        //$scope.loader= true;        
          $scope.event = event;
          if (event == 'All'){
            $scope.historyListNew = [];
            var adminId = {"superAdminId":$scope.userProfileId};
            FunctionalityService.getHistorybysuperAdmin(adminId) 
              .then(function (response) {
                $scope.loader= false;

                if (response.status == 200) {                   
                  angular.forEach(response.data.historyInfo, function(value){
                   if (value.selectType == $scope.activity){
                    $scope.historyListNew.push(value);                    
                   }
                  });                  
                  $scope.historyList = $scope.historyListNew;
                  if ($scope.activity == undefined || $scope.activity == 'All')
                  $scope.historyList = response.data.historyInfo;
              }
              }, function (error) {
                console.log("message :: " + error);
              });
          }else {
            $scope.historyList = '';
          if ($scope.activity != undefined){
              if ( $scope.event != undefined){
                var data = {
                    "superAdminId":$scope.userProfileId,
                    "selectType":$scope.activity,
                    "objectStatus":$scope.event
                   
                  };
              }else {
                var data = {
                    "superAdminId":$scope.userProfileId,
                    "selectType":$scope.activity
                    
                  };
              }
              
            FunctionalityService.gethistorybasedSuperAdminonselecttype(data)
            .then(function (response) {
              $scope.loader= false;

                if (response.status == 200) { 
                  $scope.historyList = response.data.historyInfo;
              }
              }, function (error) {
                console.log("message :: " + error);
              });
          }
        }
      }

      $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        console.log('Page changed to: ' + $scope.currentPage);
    };

    $scope.setItemsPerPage = function (num) {
        $scope.itemsPerPage = num;
        $scope.currentPage = 1; //reset to first page
    }
}]); 