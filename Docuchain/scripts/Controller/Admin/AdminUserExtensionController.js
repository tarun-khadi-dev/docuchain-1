var adminUserExtension = angular.module('dapp.AdminUserExtensionController', ['angularUtils.directives.dirPagination']);

adminUserExtension.controller('AdminUserExtensionController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster','$timeout', function ($scope, $window, $location, $state, $rootScope, FunctionalityService, toaster,$timeout) {
    $scope.userProfileId = $window.localStorage.getItem('userId');

    var approveObj;
    var rejectdObj;
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.tabName='tab1'; 
    $scope.pendingArray = [];
    $scope.approveArray = [];
    $scope.rejectedArray = [];
    $scope.loader = false;

    

    $scope.$on('$viewContentLoaded', function () {

        FunctionalityService.getPendingRequest($scope.userProfileId) 
          .then(function (response) {
            if (response.status == 200) {
              console.log("pendingList Response", response);
             $scope.pendingList = response.data.requestUserDTOs;
             angular.forEach ($scope.pendingList, function (value){
                if (value.requestUserStatus == "Approved"){
                  $scope.approveArray.push(value);
                }else if (value.requestUserStatus == "Rejected"){
                  $scope.rejectedArray.push(value);
                }else if (value.requestUserStatus == "Pending"){
                  $scope.pendingArray.push(value);
                }
               });
            }
          }, function (error) { 
            console.log("message :: " + error);
          });
      })

      $scope.userClick = function (userlist) {
        $scope.listUser = userlist;
      }

      $scope.approveUser = function (requestId) {
          console.log("inside approve user::"+requestId);
          approveObj = {
              "requestId":requestId,
              "requestUserStatus":"Approved",
              "loginId":$scope.userProfileId
          }
         
      }
      $scope.rejectedUser = function (requestId) {
        console.log("inside approve user::"+requestId);
         rejectdObj = {
            "requestId":requestId,
            "requestUserStatus":"Rejected",
            "loginId":$scope.userProfileId
        }       
    }

       $scope.approveUserPopup = function (remark){
  $scope.loader = true;

         approveObjpopup = {
          "requestId":approveObj.requestId,
          "requestUserStatus":approveObj.requestUserStatus,
          "loginId":$scope.userProfileId,
          "remarks" : remark
      }
        FunctionalityService.approvelUser(approveObjpopup)
        .then(function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#approve').modal('hide'); 
            $state.reload();
              $timeout(function () {
                toaster.success({ title: response.data.message });
              }, 1000);        
          }
        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
       }

       $scope.rejectedUserPopup = function (remarkReject){
        $scope.loader = true;
        rejectdObjpopup = {
          "requestId":rejectdObj.requestId,
          "requestUserStatus":rejectdObj.requestUserStatus,
          "loginId":$scope.userProfileId,
          "remarks" : remarkReject
      }    
        FunctionalityService.approvelUser(rejectdObjpopup)
        .then(function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#reject').modal('hide');  
            $state.reload();
              $timeout(function () {
                toaster.success({ title: response.data.message });
              }, 1000);         
          }
        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
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
    $scope.showme = function (){
        console.log("inside tab 1"+JSON.stringify($scope.tab));
        $scope.tab = 1;
        //   if (angular.isDefined($scope.pendingArray)){    
        // }else{
        //   toaster.error("no recoeds found");
        // }
      }
      $scope.showApprovel = function (){
        console.log("inside tab 2"+JSON.stringify($scope.tab));
        $scope.tab = 2; 
        // if (angular.isDefined($scope.approveArray)){
         
        // }else{
        //   toaster.error("no recoeds found");
        // }
      }
      $scope.showRejected = function(){
        console.log("inside tab 3"+JSON.stringify($scope.tab));
        $scope.tab = 3;
        // if (angular.isDefined($scope.rejectedArray)){
          
        // }else{
        //   toaster.error("no recoeds found");
        // }
      }
      $scope.approvePopupClear = function (){
        $scope.remark = "";
      }
      $scope.rejectPopupClear = function (){
        $scope.remarkReject = "";
      }
}]);