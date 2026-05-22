var adminDashboard = angular.module('dapp.AdminsuperAconfCountryController',['angularUtils.directives.dirPagination']);

adminDashboard.controller('AdminsuperAconfCountryController',['$scope','$window', '$location','$state', '$rootScope','toaster','$timeout','FunctionalityService',function($scope, $window, $location,$state, $rootScope,toaster, $timeout,FunctionalityService){
    $scope.userProfileId = $window.localStorage.getItem('userId');

    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.role = {};
    $scope.loader = false;

    $scope.$on('$viewContentLoaded', function () { 
        FunctionalityService.getListRolesName($scope.userProfileId) 
          .then(function (response) {
            if (response.status == 200) {
             $scope.roleAliasList = response.data.roleAliasInfos;             
            }
          }, function (error) {
            console.log("message :: " + error);
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

    
    $scope.clearRoleAlias = function (){
      $scope.role.editVesselname = "";
    }
    $scope.addRole = function (role) {
      $scope.loader = true;

        var addRoleAlais = {            
                "vesselsTypeName": role.aliasName,
                "userId":$scope.userProfileId       
        }
        FunctionalityService.addRolesName(addRoleAlais) 
        .then(function (response) {
          $scope.loader = false;

          if (response.status == 200) {
          $('#addVesselTypes').modal('hide');
          $timeout(function () {
            toaster.success({ title: response.data.message });
          }, 300);
          $state.reload();           
          }
        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });

    }

    $scope.editRole = function (role){
      $scope.loader = true;

        var addRoleAlais = {            
            "roleAliasId" : role.editRoleId,
            "roleAliasName": role.editVesselname,
            "roleId":role.editRoleId, 
            "adminId":$scope.userProfileId   
    }
    FunctionalityService.editRole(addRoleAlais) 
    .then(function (response) {
      $scope.loader = false;

      if (response.status == 200) {
      $('#editVesselTypes').modal('hide');
      $timeout(function () {
        toaster.success({ title: response.data.message });
      }, 300);
      $state.reload();           
      }else{        
        toaster.clear();
        toaster.error({ title: response.data.message }); 
        $('#editVesselTypes').modal('hide');
      }
    }, function myError(err) {
      $scope.loader = false;
      console.log("Error response", err);
    });
    }
 
    $scope.editCountry = function (role){
        $scope.role.editVesselname = role.roleAliasName;
        $scope.role.editRoleId = role.roleId;
    }


}]);