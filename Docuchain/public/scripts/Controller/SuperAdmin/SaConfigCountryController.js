// var saCountry = angular.module('dapp.SaConfigCountryController', ['angularUtils.directives.dirPagination','toaster']);

// saCountry.controller('SaConfigCountryController', ['$scope', '$window', '$location', '$state', '$rootScope','$timeout','toaster', 'FunctionalityService','DeletePopup', function ($scope, $window, $location, $state, $rootScope,$timeout,toaster, FunctionalityService, DeletePopup) {
//     $scope.currentPage = 1;
//     $scope.viewby = 10;
//     $scope.itemsPerPage = $scope.viewby;
//     $scope.userProfileId = $window.localStorage.getItem('userId');

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

//     $scope.countryList = function () {
//         $scope.loader= true;

//         FunctionalityService.getCountryList($scope.userProfileId)
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

//     }

//     $scope.addCountry = function () {
//         $scope.loader= true;

//         var addcountrydata = { "adminId": $scope.userProfileId, "countryName": $scope.country.countryName, "countryCode": $scope.country.countryCode };
//         FunctionalityService.addCountry(addcountrydata)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {
//                     $('#countryadd').modal('hide');
//                     $state.reload();
//                     $timeout(function () {
//                        toaster.clear();
//                         toaster.success({ title: response.data.message });
//                     }, 1000);

//                 } else {
//                     $('#countryadd').modal('hide');
//                     toaster.clear();
//                     toaster.error({ title: response.data.message });
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });
//     }
//     $scope.closeOrCancel = function () {
//        // $state.reload();
//        $scope.country.countryName = "";
//        $scope.country.countryCode = "";

//     }
//     $scope.updateCountry ;
//     $scope.getUpdateCountry = function (update) {
//         $scope.updateCountry = update;
//       }

//     $scope.editCountry = function () {
//         $scope.loader= true;

//         var editcountrydata = { "countryId": $scope.updateCountry.countryId,"adminId": $scope.userProfileId, "countryName":  $scope.updateCountry.countryName, "countryCode":  $scope.updateCountry.countryCode };
//         FunctionalityService.editCountry(editcountrydata)
//             .then(function mySuccess(response) {
//                 $scope.loader= false;

//                 if (response.status == 201 || response.status == 200) {
//                     $('#editCountry').modal('hide');
//                     $state.reload();
//                     $timeout(function () {
//                         toaster.clear();
//                         toaster.success({ title: response.data.message });
//                     }, 1000);

//                 } else {
//                     $('#editCountry').modal('hide');
//                     toaster.clear();
//                     toaster.error({ title: response.data.message });
//                 }
//             }, function myError(err) {
//                 $scope.loader = false;
//                 console.log("Error response", err);
//               });
//     }

//     $scope.confirmDeleteCountry = function(country){
//     DeletePopup.confirm(
//         "Delete Country",
//         "Are you sure you want to delete this country?",
//         function(){
//             $scope.deleteCountry(country);
//         }
//     );
// };

// $scope.deleteCountry = function(country){
//     $scope.loader = true;

//     var deleteCountrydata = {
//         countryIds: [country.countryId],
//         adminId: $scope.userProfileId
//     };

//     FunctionalityService.deleteCountry(deleteCountrydata)
//     .then(function(response){
//         $scope.loader = false;
//         if(response.status == 200 || response.status == 201){
//             $scope.allCountryList = $scope.allCountryList.filter(function(item){
//                 return item.countryId !== country.countryId;
//             });
//             toaster.success({ title: response.data.message });
//         }else{
//             toaster.error({ title: response.data.message });
//         }
//     }, function(err){
//         $scope.loader = false;
//         console.log("Error response", err);
//     });
// };

//     $scope.closeEditCountry = function () {
//          $state.reload();
//      }

//      $scope.resetEditCountry = function() {
//         $scope.editCountryForm.$setPristine();
//         $scope.updateCountry.countryName="";
//        $scope.updateCountry.countryCode="" ;
//     };
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
var saCountry = angular.module('dapp.SaConfigCountryController', [
  'angularUtils.directives.dirPagination',
  'toaster',
]);

saCountry.controller('SaConfigCountryController', [
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
    $scope.countryList = function () {
      $scope.loader = true;
      FunctionalityService.getCountryList($scope.userProfileId).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.allCountryList = response.data.countryInfos;
          }
        },
        function (err) {
          $scope.loader = false;
        }
      );
    };

    // --- ADD COUNTRY ---
    $scope.addCountry = function () {
      $scope.loader = true;
      var data = {
        adminId: $scope.userProfileId,
        countryName: $scope.country.countryName,
        countryCode: $scope.country.countryCode,
      };
      FunctionalityService.addCountry(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $('#countryadd').modal('hide');
          cleanupModal();
          // NO TITLE, ONLY MESSAGE
          toaster.pop('success', null, response.data.message);
          $scope.countryList(); // Refresh data in background
          $scope.closeOrCancel();
        } else {
          toaster.pop('error', null, response.data.message);
        }
      });
    };

    // --- EDIT COUNTRY ---
    $scope.getUpdateCountry = function (update) {
      $scope.updateCountry = angular.copy(update); // Use copy to prevent list changing before save
    };

    $scope.editCountry = function () {
      $scope.loader = true;
      var data = {
        countryId: $scope.updateCountry.countryId,
        adminId: $scope.userProfileId,
        countryName: $scope.updateCountry.countryName,
        countryCode: $scope.updateCountry.countryCode,
      };
      FunctionalityService.editCountry(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $('#editCountry').modal('hide');
          cleanupModal();
          toaster.pop('success', null, response.data.message);
          $scope.countryList(); // Refresh data in background
        } else {
          toaster.pop('error', null, response.data.message);
        }
      });
    };

    // --- DELETE COUNTRY ---
    $scope.confirmDeleteCountry = function (country) {
      DeletePopup.confirm('Delete Country', 'Are you sure?', function () {
        $scope.loader = true;
        FunctionalityService.deleteCountry({
          countryIds: [country.countryId],
          adminId: $scope.userProfileId,
        }).then(function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.allCountryList = $scope.allCountryList.filter(
              (item) => item.countryId !== country.countryId
            );
            toaster.pop('success', null, response.data.message);
          }
        });
      });
    };

    $scope.closeOrCancel = function () {
      $scope.country = { countryName: '', countryCode: '' };
    };

    $scope.closeEditCountry = function () {
      $('#editCountry').modal('hide');
      cleanupModal();
    };

    $scope.resetEditCountry = function () {
      $scope.updateCountry.countryName = '';
      $scope.updateCountry.countryCode = '';
    };
  },
]);
