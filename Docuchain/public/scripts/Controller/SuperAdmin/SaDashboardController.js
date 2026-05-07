var saDashboard = angular.module('dapp.SaDashboardController',['$idle','angularUtils.directives.dirPagination']);

saDashboard.controller('SaDashboardController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService','$idle',function($scope, $window, $location,$state, $rootScope, FunctionalityService,$idle){
    
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;


    $scope.$on('$viewContentLoaded', function () {  
        FunctionalityService.getStatisticsDetail()
          .then(function (response) {  
            if (response.status == 200) {            
              $scope.organizationdetaillist = response.data.organizationInfos;
            }
          }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          }); 
      })

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

      // Dashboard Stats
    FunctionalityService.getOrganizationTopCount().then(function(response){
        if(response.status == 200){

            console.log("TopCount Response:", response.data);

            $scope.totalOrganization = response.data.organizationInfo.organizationCount;
            $scope.activeCount = response.data.organizationInfo.activeCount;
            $scope.renewalCount = response.data.organizationInfo.renewalCount;
            $scope.expiredCount = response.data.organizationInfo.expiryCount ;
        }
    }, function(error){
        console.log("TopCount Error:", error);
    })
     
}]);