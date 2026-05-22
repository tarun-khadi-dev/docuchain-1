var adminDashboard = angular.module('dapp.DashBoardController',['angularUtils.directives.dirPagination']);

adminDashboard.controller('DashBoardController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService','toaster','$timeout',function($scope, $window, $location,$state, $rootScope, FunctionalityService,toaster,$timeout){

  $scope.userProfileId = $window.localStorage.getItem('userId');
  $scope.roleId = $window.localStorage.getItem('roleId');
  $scope.selectedCategory = "All";
  $scope.currentPage = 1;
  $scope.viewby = 10;
  $scope.itemsPerPage = $scope.viewby;
  $scope.deleteNotificationList = []; 
  $scope.loader= false;
  
  if ($scope.roleId == 2){
    $scope.categoryList = {
      option1 : "All",
      option2 : "Task Notification",      
      option4 : "UserExtension Notification"
      
  } 
  }else if ($scope.roleId == 3){
    $scope.categoryList = {
      option1 : "All",
      option2 : "Task Notification",
      option3 : "ExpiryDocument Notification",      
      option4 : "Approve Document"
  } 
  
  }else {
    $scope.categoryList = {
      option1 : "All",
      option2 : "Task Notification",
      option3 : "ExpiryDocument Notification", 
      option4 : "UserExtension Notification",     
      option5 : "Approve Document"
  } 
  }
   
    $scope.$on('$viewContentLoaded', function () {
      var data = { "userId": $scope.userProfileId };
      FunctionalityService.getDocumentNotification(data)
        .then(function mySuccess(response) {
          $scope.notificationList = response.data.userList;
          console.log("$scope.notificationList::"+JSON.stringify(response));
        }, function myError(err) {
          console.log("Error response");
        });

    });
       $scope.notification = function (){
        $scope.loader= true;

        var data = { "userId": $scope.userProfileId};
        // $state.go('dapp.notification');
        FunctionalityService.setDocumentNotificationViewed(data)
          .then(function mySuccess(response) {
            $scope.loader= false;
            //$window.location.reload();
            $state.go('dapp.notification');
          }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
      }
      $scope.getNotificationByCategory = function (selectedCategory){
        $scope.loader= true;

        if (selectedCategory != "All"){
          var categorytobackend;
          if (selectedCategory == "Task Notification")
              categorytobackend = "Task page";
          if (selectedCategory == "ExpiryDocument Notification")
              categorytobackend = "Document List Page";  
          if (selectedCategory == "UserExtension Notification")
              categorytobackend = "User request page"; 
          if (selectedCategory == "Approve Document")
              categorytobackend = "Approval page";  
          var data = { "userId": $scope.userProfileId,
                       "category": categorytobackend };
          FunctionalityService.getNotificationByCategory(data)
            .then(function mySuccess(response) {
              $scope.loader= false;

              $scope.notificationList = response.data.userList;
             // $state.go('dapp.notification');
            }, function myError(err) {
              $scope.loader = false;
              console.log("Error response", err);
            });
             }else{
              var data = { "userId": $scope.userProfileId };
              FunctionalityService.getDocumentNotification(data)
                .then(function mySuccess(response) {
                  $scope.loader= false;
                  $scope.notificationList = response.data.userList;
                }, function myError(err) {
                  $scope.loader = false;
                  console.log("Error response", err);
                });
             }
      }
      $scope.checkUncheckAll = function (notificationList) {
        if ($scope.checkall) {
         $scope.checkall = true;
         $scope.deleteNotificationList = notificationList;
        } else {
         $scope.checkall = false;
        }
        angular.forEach($scope.notificationList, function (notification) {
         notification.checked = $scope.checkall;
        });
       };
     
       $scope.updateCheckall = function(){
        
         var userTotal = $scope.notificationList.length;
         var count = 0;
         $scope.deleteNotificationList = [];
         angular.forEach($scope.notificationList, function (item) {
            if(item.checked){
              count++;
              var notifiId = {"notificationId": item.notificationId };
              $scope.deleteNotificationList.push(notifiId);              
            }          
            
         });
     
         if(userTotal == count){
            $scope.checkall = true;
         }else{
            $scope.checkall = false;
         }
        };
    //   $scope.checkAll = function(){
    //     console.log("inside the check all function");
    //     if ($scope.selectedAll) {         
    //       $scope.selectedAll = true;
    //   } else {
    //       $scope.selectedAll = false;     
    //   }    
    //   console.log("value:::"+$scope.selectedAll);
    //   $scope.subCheckBox = undefined;
    //    $scope.subCheckBox = $scope.selectedAll;
    //    console.log("value2::::"+$scope.subCheckBox);
       

    // }
    
    // $scope.deleteEntityNotification = function (id){   
    //   console.log("inside the entity changes method:::"+id+"......."+$scope.subCheckBox);
    //   var notificationId = {"notificationId":id};
    //   console.log("before check"+$scope.deleteNotificationList.length);    
    //   $scope.deleteNotificationList.push(notificationId);
    //   console.log("ready for delete notification:::"+JSON.stringify($scope.deleteNotificationList));
    // }
    $scope.deleteNotification = function (id){

      console.log("inside delete notification:::::"+id);
      $scope.notificationId = id;     
    }
    $scope.deleteNotificationById = function (){
      
      console.log("inside delete notification:::::"+$scope.notificationId);
      var data = {
        "loginId" : $scope.userProfileId,
        "notificationId" : $scope.notificationId
      }
      FunctionalityService.deleteNotification(data)
        .then(function mySuccess(response) {        
        $('#delete').modal('hide');
        $state.reload();
        $timeout(function () {
            toaster.success({ title: response.data.status });
        }, 1000);
          
          
        }, function myError(err) {
          toaster.error({ title: myError.data.status });
          console.log("Error response");
        });
    }
    $scope.deleteAllNotificationBySelect = function (){
      $scope.loader= true;
      FunctionalityService.deleteAllNotification($scope.deleteNotificationList)
      .then(function mySuccess(response) {   
        $scope.loader= false;

      $('#deleteAll').modal('hide');
      $state.reload();
      $timeout(function () {
        toaster.pop('success', response.data.status);
          
      }, 1000);
        
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

  $scope.linkToExpiryDoc = function (shipId){
    $window.localStorage.setItem('shipId',shipId);

    $state.go('dapp.userVesselDocumentEBD');
  }
  $scope.linkToTaskManagement = function (taskId){  
    if ( $scope.roleId == 2)     
    $state.go('dapp.taskManagementByNotifyAdmin', ({'id':taskId}));
    else
    $state.go('dapp.taskManagementByNotifyUser', ({'id':taskId}));
  
  }
  $scope.linkToApproveDocument = function (data){
    $state.go('dapp.userDoumentApproval');
    // console.log("inside linkToApproveDocument ",JSON.stringify(data));
    // $window.sessionStorage.setItem('shipId',data.shipId);            
    // $state.go('dapp.approvedDocumentListByNotify', ({'expiryDocumentId':data.expiryDocumentId})); 
   
  }
  $scope.linkToAdminApproveDoc = function (shipId){  
    $state.go('dapp.adminUserExtension');
    // $window.sessionStorage.setItem('shipId',shipId);      
    // $state.go('dapp.adminRequestApproval');
  }
  $scope.deleteEntityNotification = function (id){
    console.log("inside the entity changes method:::"+id);
    var notificationId = {"notificationId":id};
    var loginid = {"loginId": $scope.userProfileId}
    console.log("before check"+$scope.deleteNotificationList.length);    
    $scope.deleteNotificationList.push(notificationId);
    $scope.deleteNotificationList.push(loginid);
    console.log("ready for delete notification:::"+JSON.stringify($scope.deleteNotificationList));
  }
  $scope.deleteAllNotification = function (list){ 
    console.log("click deleteAll check"+$scope.deleteNotificationList.length);
    if ($scope.deleteNotificationList.length == 0)  
    $scope.deleteNotificationList = list;  
  }
  $scope.deleteAllNotificationBySelect = function (){
    console.log("list for delete::"+JSON.stringify($scope.deleteNotificationList));
    FunctionalityService.deleteAllNotification($scope.deleteNotificationList)
    .then(function mySuccess(response) {   
    $('#deleteAll').modal('hide');
    $state.reload();
    $timeout(function () {
        toaster.success({ title: response.data.status });
    }, 1000);
      
    }, function myError(err) {
      toaster.error({ title: myError.data.status });
      console.log("Error response");
    });
  }
}]);