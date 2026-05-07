var userExtension = angular.module('dapp.UserUserExtensionController', [
  'ui.select',
  'ngSanitize',
]);

userExtension
  .controller('UserUserExtensionController', [
    '$scope',
    '$window',
    '$location',
    '$state',
    '$rootScope',
    'toaster',
    '$timeout',
    'FunctionalityService',
    function (
      $scope,
      $window,
      $location,
      $state,
      $rootScope,
      toaster,
      $timeout,
      FunctionalityService
    ) {
      $scope.sessionObject = JSON.parse(
        $window.localStorage.getItem('sessionObject')
      );

      var url;
      var requestUser = {};
      $scope.loader = false;
      $rootScope.selected = 4;

      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getShipList($scope.sessionObject.userId).then(
          function (response) {
            if (response.status == 200) {
              $scope.shipList = response.data.shipProfileList;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      });

      $scope.$on('$viewContentLoaded', function () {
        if ($scope.sessionObject.roleId == 4) url = '/user/techmanager/list/';
        if ($scope.sessionObject.roleId == 5)
          url = '/user/commercialmanager/list/';
        FunctionalityService.getuserList($scope.sessionObject.userId, url).then(
          function (response) {
            if (response.status == 200) {
              $scope.userList = response.data.technicalManagerInfos;
              if ($scope.userList == undefined)
                $scope.userList = response.data.commercialManagerInfos;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      });

      // ==========================================
      // NEW: Select All & Clear All Functionality
      // ==========================================

      $scope.selectAllUsers = function () {
        if ($scope.userList && $scope.userList.length > 0) {
          // Initialize requestUser if undefined
          if (!$scope.requestUser) $scope.requestUser = {};

          // Copy all users to the selected array
          $scope.requestUser.selectedUsers = angular.copy($scope.userList);

          // Auto-update the "Number of Users" input to match the selected count
          $scope.requestUser.noOfUser = $scope.userList.length;
        }
      };

      $scope.clearAllUsers = function () {
        if ($scope.requestUser) {
          // Empty the selection array
          $scope.requestUser.selectedUsers = [];

          // Clear the "Number of Users" input
          $scope.requestUser.noOfUser = null;
        }
      };

      // ==========================================
      // END OF NEW FUNCTIONALITY
      // ==========================================

      $scope.createRequestUser = function (requestUser) {
        $scope.loader = true;
        $scope.selectedUsers = [];
        $scope.shipIdCreate;
        if (requestUser.selectedUsers.length != requestUser.noOfUser) {
          $scope.loader = false;
          toaster.error({ title: 'Please select matching  no of user ' });
          return;
        }
        angular.forEach(requestUser.selectedUsers, function (val) {
          $scope.selectedUsers.push(val.userName);
        });
        var requestUserObj = {
          shipId: requestUser.selectedShip,
          numberOfUser: requestUser.noOfUser,
          userList: $scope.selectedUsers,
          remarks: requestUser.remark,
          uploadUserId: $scope.sessionObject.userId,
        };
        FunctionalityService.addRequestUser(requestUserObj).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200) {
              $state.reload();
              $timeout(function () {
                toaster.success({ title: response.data.message });
              }, 1000);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.clearForm = function () {
        //   $scope.requestUser.selectedShip = "";
        //  $scope.requestUser.selectedUsers = "";
        //   $scope.requestUser.remark = "";
        $state.reload();
      };
    },
  ])
  .directive('customFocus', [
    function () {
      var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
      return {
        restrict: 'A', //Angular will only match the directive against attribute names
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
          ctrl.$focused = false;

          element
            .bind('focus', function (evt) {
              element.addClass(FOCUS_CLASS);
              scope.$apply(function () {
                ctrl.$focused = true;
              });
            })
            .bind('blur', function (evt) {
              element.removeClass(FOCUS_CLASS);
              scope.$apply(function () {
                ctrl.$focused = false;
              });
            });
        },
      };
    },
  ]);
