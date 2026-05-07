// var saState = angular.module('dapp.SaConfigStateController', ['angularUtils.directives.dirPagination', 'toaster']);

// saState.controller('SaConfigStateController', ['$scope', '$window', '$location', '$state', '$rootScope', '$timeout', 'toaster', 'FunctionalityService', 'DeletePopup', function ($scope, $window, $location, $state, $rootScope, $timeout, toaster, FunctionalityService, DeletePopup ) {
//     $scope.currentPage = 1;
//     $scope.viewby = 10;
//     $scope.itemsPerPage = $scope.viewby;
//     $scope.userProfileId = $window.localStorage.getItem('userId');

//     $scope.allCountryPortList;
//     $scope.allCountryList;
//     $scope.loader= false;

//     $scope.expandSubMenu = function () {
//         if ($rootScope.subMenuActive == true) {
//             $rootScope.subMenuActive = false;
//         }
//         else {
//             $rootScope.subMenuActive = true;
//             $rootScope.subConfigMenuActive = false;
//         }
//     }
//     $scope.expandConfigSubMenu = function () {
//         if ($rootScope.subConfigMenuActive == true) {
//             $rootScope.subConfigMenuActive = false;
//         }
//         else {
//             $rootScope.subConfigMenuActive = true;
//             $rootScope.subMenuActive = false;
//         }

//     }

//     $scope.countryPortList = function () {
//         $scope.loader= true;

//         FunctionalityService.getCountryStateList($scope.userProfileId)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {

//                     $scope.allCountryPortList = JSON.stringify(response.data.portInfos);
//                     $scope.allCountryPortList = response.data.portInfos;
//                     if ($scope.allCountryPortList == undefined) {
//                         toaster.clear();
//                         toaster.info({ title: "No records found" });
//                     }
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });

//             FunctionalityService.getCountryList($scope.userProfileId)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {

//                     $scope.allCountryList = JSON.stringify(response.data.countryInfos);
//                     $scope.allCountryList = response.data.countryInfos;
//                     if ($scope.allCountryList == undefined) {
//                         toaster.clear();
//                         toaster.info({ title: "No records found" });
//                     }
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });
//             $scope.closeOrCancel = function () {
//                 $state.reload();
//             }
//     }
//     $scope.updateCountryState;
//     $scope.getEditCountryState = function (update) {
//         $scope.updateCountryState = update;
//       }

//     $scope.addCountryState = function () {
//         $scope.loader= true;

//         var addCountryStateData = { "userId": $scope.userProfileId, "countryId": $scope.state.countryId, "portName": $scope.state.portName };
//         FunctionalityService.addCountryState(addCountryStateData)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {
//                     $('#addCountryState').modal('hide');
//                     $state.reload();
//                     $timeout(function () {
//                         toaster.clear();
//                         toaster.success({ title: response.data.message });
//                     }, 1000);

//                 } else {
//                     $('#addCountryState').modal('hide');
//                     toaster.clear();
//                     toaster.error({ title: response.data.message });
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });
//     }

//     $scope.editCountryState = function () {
//         $scope.loader= true;

//         var editCountryStateData = { "userId": $scope.userProfileId, "countryId": $scope.updateCountryState.countryId, "portName": $scope.updateCountryState.portName ,"portId":$scope.updateCountryState.portId};
//         FunctionalityService.editCountryState(editCountryStateData)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {
//                     $('#updateCountryState').modal('hide');
//                     $state.reload();
//                     $timeout(function () {
//                         toaster.clear();
//                         toaster.success({ title: response.data.message });
//                     }, 1000);

//                 } else {
//                     $('#updateCountryState').modal('hide');
//                     toaster.clear();
//                     toaster.error({ title: response.data.message });
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });
//     }

//     $scope.confirmDeleteCountryState = function(state){
//     DeletePopup.confirm(
//         "Delete State",
//         "Are you sure you want to delete this state?",
//         function(){
//             $scope.deleteCountryState(state);
//         }
//     );
// };

// $scope.deleteCountryState = function(state){

//     $scope.loader = true;

//     var deleteCountryStatedata = {
//         portIds: [state.portId],
//         userId: $scope.userProfileId,
//         countryId: state.countryId
//     };

//     FunctionalityService.deleteCountryState(deleteCountryStatedata)
//     .then(function(response){
//         $scope.loader = false;
//         if(response.status == 200 || response.status == 201){
//             $scope.allCountryPortList = $scope.allCountryPortList.filter(function(item){
//                 return item.portId !== state.portId;
//             });

//             toaster.success({ title: response.data.message });

//         }else{
//             toaster.error({ title: response.data.message });
//         }

//     },function(err){
//         $scope.loader = false;
//         console.log("Error response",err);
//     });
// };
// }])
// .directive('customFocus', [function () {
//     var FOCUS_CLASS = "custom-focused"; //Toggle a class and style that!
//     return {
//     restrict: 'A', //Angular will only match the directive against attribute names
//     require: 'ngModel',
//     link: function (scope, element, attrs, ctrl) {
//     ctrl.$focused = false;

//     element.bind('focus', function (evt) {
//     element.addClass(FOCUS_CLASS);
//     scope.$apply(function () { ctrl.$focused = true; });

//     }).bind('blur', function (evt) {
//     element.removeClass(FOCUS_CLASS);
//     scope.$apply(function () { ctrl.$focused = false; });
//     });
//     }
//     }

//     }]);
var saState = angular.module('dapp.SaConfigStateController', [
  'angularUtils.directives.dirPagination',
  'toaster',
]);

saState.controller('SaConfigStateController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  '$timeout',
  'toaster',
  'FunctionalityService',
  'DeletePopup',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    $timeout,
    toaster,
    FunctionalityService,
    DeletePopup
  ) {
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.loader = false;

    // --- CLEANUP HELPER (Fixes stuck scrolling) ---
    function cleanupModal() {
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
      $('body').css('padding-right', '0px');
    }

    // --- BACKGROUND FETCH HELPER ---
    $scope.countryPortList = function () {
      $scope.loader = true;
      // Fetch Ports
      FunctionalityService.getCountryStateList($scope.userProfileId).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.allCountryPortList = response.data.portInfos;
          }
        }
      );

      // Fetch Countries for dropdowns
      FunctionalityService.getCountryList($scope.userProfileId).then(
        function (response) {
          if (response.status == 200 || response.status == 201) {
            $scope.allCountryList = response.data.countryInfos;
          }
        }
      );
    };

    // --- ADD PORT ---
    $scope.addCountryState = function () {
      $scope.loader = true;
      var data = {
        userId: $scope.userProfileId,
        countryId: $scope.state.countryId,
        portName: $scope.state.portName,
      };
      FunctionalityService.addCountryState(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $('#addCountryState').modal('hide');
          cleanupModal();
          // NO TITLE, ONLY MESSAGE
          toaster.pop('success', null, response.data.message);
          $scope.countryPortList(); // Refresh data silently
          $scope.state = {}; // Clear form
        } else {
          toaster.pop('error', null, response.data.message);
        }
      });
    };

    // --- EDIT PORT ---
    $scope.getEditCountryState = function (update) {
      $scope.updateCountryState = angular.copy(update);
    };

    $scope.editCountryState = function () {
      $scope.loader = true;
      var data = {
        userId: $scope.userProfileId,
        countryId: $scope.updateCountryState.countryId,
        portName: $scope.updateCountryState.portName,
        portId: $scope.updateCountryState.portId,
      };
      FunctionalityService.editCountryState(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $('#updateCountryState').modal('hide');
          cleanupModal();
          toaster.pop('success', null, response.data.message);
          $scope.countryPortList(); // Refresh data silently
        } else {
          toaster.pop('error', null, response.data.message);
        }
      });
    };

    // --- DELETE PORT ---
    $scope.confirmDeleteCountryState = function (state) {
      DeletePopup.confirm(
        'Delete Port',
        'Are you sure you want to delete this port?',
        function () {
          $scope.loader = true;
          var data = {
            portIds: [state.portId],
            userId: $scope.userProfileId,
            countryId: state.countryId,
          };
          FunctionalityService.deleteCountryState(data).then(
            function (response) {
              $scope.loader = false;
              if (response.status == 200 || response.status == 201) {
                $scope.allCountryPortList = $scope.allCountryPortList.filter(
                  (item) => item.portId !== state.portId
                );
                toaster.pop('success', null, response.data.message);
              }
            }
          );
        }
      );
    };

    $scope.closeOrCancel = function () {
      $('#addCountryState').modal('hide');
      $('#updateCountryState').modal('hide');
      cleanupModal();
    };

    $scope.expandSubMenu = function () {
      $rootScope.subMenuActive = !$rootScope.subMenuActive;
      if ($rootScope.subMenuActive) $rootScope.subConfigMenuActive = false;
    };

    $scope.expandConfigSubMenu = function () {
      $rootScope.subConfigMenuActive = !$rootScope.subConfigMenuActive;
      if ($rootScope.subConfigMenuActive) $rootScope.subMenuActive = false;
    };
  },
]);
