var adminDashboard = angular.module('dapp.AdminSuperAauditLogController',['angularUtils.directives.dirPagination']);

adminDashboard.controller('AdminSuperAauditLogController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService',function($scope, $window, $location,$state, $rootScope, FunctionalityService){
    
    $scope.userProfileId = $window.localStorage.getItem('userId');

    $scope.activity;
    $scope.event;
    $scope.tabName='tab2';
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.loader = false;

    $scope.$on('$viewContentLoaded', function () { 
        var adminId = {"adminId":$scope.userProfileId};
        FunctionalityService.getHistorybyadmin(adminId) 
          .then(function (response) {
            if (response.status == 200) { 
              $scope.historyList = response.data.historyInfo;
              $scope.historyList = $scope.historyList.reverse();
              console.log("historyinfosss::"+JSON.stringify($scope.historyList));
          }
          }, function (error) {
            console.log("message :: " + error);
          });
      })
    
      $scope.activityFunction = function (activity){
        $scope.loader = true;

        $scope.activity = activity;
        if (activity == 'All'){
          var adminId = {"adminId":$scope.userProfileId};
          FunctionalityService.getHistorybyadmin(adminId) 
            .then(function (response) {
              $scope.loader = false;
              if (response.status == 200) { 
                $scope.historyList = response.data.historyInfo;
                $scope.historyList = $scope.historyList.reverse();
            }
            }, function myError(err) {
              $scope.loader = false;
              console.log("Error response", err);
            });
        } else{
          if ($scope.event != 'All'){
            var data = {
              "adminId":$scope.userProfileId,
              "objectName":$scope.activity,
              "subObjectName":$scope.event
            };
          }else {
            var data = {
              "adminId":$scope.userProfileId,
              "objectName":$scope.activity
            };
          }
          FunctionalityService.gethistorybasedonselecttype(data)
          .then(function (response) {
            $scope.loader = false;

              if (response.status == 200) { 
                $scope.historyList = response.data.historyInfo;
                $scope.historyList = $scope.historyList.reverse();
            }
            }, function myError(err) {
              $scope.loader = false;
              console.log("Error response", err);
            });
        }  
      }

      $scope.eventFunction = function (event){
      //  $scope.loader = true;

          $scope.event = event;
          if (event == 'All'){
            $scope.historyListNew = [];
            var adminId = {"adminId":$scope.userProfileId};
            FunctionalityService.getHistorybyadmin(adminId) 
              .then(function (response) {
                $scope.loader = false;

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
              }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
          }else {
            $scope.historyList = '';          
          if ($scope.activity != undefined){
              var data = {
                "adminId":$scope.userProfileId,
                "objectName":$scope.activity,
                "subObjectName":$scope.event
              };
            FunctionalityService.gethistorybasedonselecttype(data)
            .then(function (response) {
              $scope.loader = false;

                if (response.status == 200) { 
                  $scope.historyList = response.data.historyInfo;
              }
              }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
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