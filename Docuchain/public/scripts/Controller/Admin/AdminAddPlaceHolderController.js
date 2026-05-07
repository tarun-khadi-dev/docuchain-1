var saPlaceHolder = angular.module('dapp.AdminAddPlaceHolderController', [
  'ui.select',
  'ngSanitize',
  'angularUtils.directives.dirPagination',
  'toaster',
]);

saPlaceHolder
  .controller('AdminAddPlaceHolderController', [
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
      $scope.organizationName =
        $window.localStorage.getItem('organizationName');
      $scope.allPlaceHolderList;
      $scope.loader = false;
      $scope.vessel = {};

      $scope.$on('$viewContentLoaded', function () {
        $scope.loader = true;

        FunctionalityService.getSubscriptionList($scope.userProfileId).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $scope.allSubscriptionList = JSON.stringify(
                response.data.subscriptionInfos
              );
              $scope.allSubscriptionList = response.data.subscriptionInfos;
              console.log(
                'inside placeholder:',
                JSON.stringify($scope.allSubscriptionList)
              );
              if ($scope.allSubscriptionList == undefined) {
                toaster.info({ title: 'No records found' });
              }
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      });

      //Expiry certificate type start
      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getVessellist(
          $window.localStorage.getItem('userId')
        ).then(function (response) {
          if (response.status == 201 || response.status == 200) {
            $scope.users = response.data.shipProfileList;
            console.log('$scope.users', JSON.stringify($scope.users));
          } else if (response.status == 206) {
            toaster.pop('error', response.data.message);
          }
        });
      });

      $scope.organizationChanges = function (organization) {
        console.log('second inside', organization);
        $scope.orgName = organization.organizationName;
      };

      // delete popup
      $scope.confirmDeletePlaceHolder = function (placeholder) {
        DeletePopup.confirm(
          'Delete Placeholder',
          'Are you sure you want to delete this placeholder?',
          function () {
            $scope.deletePlaceHolder(placeholder);
          }
        );
      };

      $scope.expandSubMenu = function () {
        if ($rootScope.subMenuActive == true) {
          $rootScope.subMenuActive = false;
        } else {
          $rootScope.subMenuActive = true;
          $rootScope.subConfigMenuActive = false;
        }
      };

      $scope.expandConfigSubMenu = function () {
        if ($rootScope.subConfigMenuActive == true) {
          $rootScope.subConfigMenuActive = false;
        } else {
          $rootScope.subConfigMenuActive = true;
          $rootScope.subMenuActive = false;
        }
      };

      $scope.placeHolderList = function () {
        $scope.loader = true;
        var dataid = { userId: $scope.userProfileId };
        console.log('data is ::', dataid);
        FunctionalityService.getPlaceHolderListByorganizatinId(dataid).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $scope.allPlaceHolderList = JSON.stringify(
                response.data.documentHolderList
              );
              $scope.allPlaceHolderList = response.data.documentHolderList;
              console.log(
                '$scope.allPlaceHolderList',
                JSON.stringify($scope.allPlaceHolderList)
              );
              if ($scope.allPlaceHolderList == undefined) {
                toaster.clear();
                toaster.info({ title: 'No records found' });
              }
            } else {
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      // ==========================================
      // NEW: Select All & Clear All Functionality for Vessels
      // ==========================================

      $scope.selectAllVessels = function () {
        if ($scope.users && $scope.users.length > 0) {
          // Initialize vessel object if undefined
          if (!$scope.vessel) $scope.vessel = {};

          // Copy all vessels from $scope.users to the selected array
          $scope.vessel.selectedVessel = angular.copy($scope.users);
        }
      };

      $scope.clearAllVessels = function () {
        if ($scope.vessel) {
          // Empty the selection array
          $scope.vessel.selectedVessel = [];
        }
      };

      // ==========================================
      // END OF NEW FUNCTIONALITY
      // ==========================================

      $scope.addPlaceHolder = function () {
        $scope.loader = true;
        console.log(
          '$selected vessel:',
          JSON.stringify($scope.vessel.selectedVessel)
        );
        $scope.vesselIds = [];
        angular.forEach($scope.vessel.selectedVessel, function (item) {
          $scope.vesselIds.push(item.id);
        });

        var addPlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderName: $scope.placeholdername,
          documentHolderDescription: $scope.placeholderdescription,
          organizationName: $scope.organizationName,
          vesselIds: $scope.vesselIds,
          type: 'Standard',
        };

        FunctionalityService.addPlaceHolder(addPlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;

            // 1. Check for True Success (200 or 201)
            if (response.status == 201 || response.status == 200) {
              $('#addplaceholder').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 1000);

              // 2. EXPLICITLY check for 206 (Your custom backend error status)
            } else if (response.status == 206) {
              // Show the error, but DO NOT close the modal
              toaster.clear();
              toaster.error({ title: response.data.message });

              // 3. Catch-all for anything else returning "Failure"
            } else if (response.data && response.data.status === 'Failure') {
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
            toaster.clear();
            toaster.error({ title: 'Server error occurred' });
          }
        );
      };

      $scope.clearAddPlaceholder = function () {
        $scope.placeholdername = '';
        $scope.placeholderdescription = '';
        $scope.placeholderfilename = '';

        // Also clear the multi-select when the main Clear button is clicked
        if ($scope.vessel) {
          $scope.vessel.selectedVessel = [];
        }
      };

      $scope.updatePlaceHolder;

      // --- REPLACED FUNCTION START ---
      $scope.getEditPlaceHolder = function (update) {
        // 1. Assign the update object to scope
        $scope.updatePlaceHolder = update;

        // 2. Logic to Find Vessel Name Manually from $scope.users
        if ($scope.users && $scope.users.length > 0) {
          // Try to find the ID property. It could be 'vesselId' or 'shipId'
          var vesselIdToFind = update.vesselId || update.shipId;

          // If the ID is missing, check if it is inside an array (e.g. vesselIds)
          if (
            !vesselIdToFind &&
            update.vesselIds &&
            update.vesselIds.length > 0
          ) {
            vesselIdToFind = update.vesselIds[0];
          }

          if (vesselIdToFind) {
            // Find the ship object in the list where the ID matches
            var foundShip = $scope.users.find(function (ship) {
              return ship.id === vesselIdToFind;
            });

            // If found, assign the name to 'shipName' so the UI can display it
            if (foundShip) {
              $scope.updatePlaceHolder.shipName = foundShip.shipName;
            } else {
              $scope.updatePlaceHolder.shipName = 'Vessel Not Found';
            }
          } else {
            $scope.updatePlaceHolder.shipName = 'No Linked Vessel';
          }
        }
      };
      // --- REPLACED FUNCTION END ---

      $scope.editPlaceHolder = function () {
        $scope.loader = true;

        var updatePlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderId: $scope.updatePlaceHolder.documentHolderId,
          documentHolderName: $scope.updatePlaceHolder.documentHolderName,
          documentHolderDescription:
            $scope.updatePlaceHolder.documentHolderDescription,
          documentFileNumber: $scope.updatePlaceHolder.documentFileNumber,
          organizationName: $scope.updatePlaceHolder.organizationName,
        };
        FunctionalityService.editPlaceHolder(updatePlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $('#updateplaceholder').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 1000);
            } else {
              $('#updateplaceholder').modal('hide');
              $state.reload();
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.deletePlaceHolder = function (placeholder) {
        $scope.loader = true;
        var deletePlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderId: placeholder.documentHolderId,
        };
        FunctionalityService.deletePlaceHolder(deletePlaceHolderData).then(
          function (response) {
            $scope.loader = false;
            if (response.data.status === 'Success') {
              $scope.placeHolderList(); // refresh data
              toaster.success({ title: response.data.message });
            } else {
              toaster.error({ title: response.data.message });
            }
          },
          function (err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.closeAddplaceholder = function () {
        $('#addplaceholder').modal('hide');
        $state.reload();
      };
      $scope.clsoedUpdateplaceHolder = function () {
        $('#updateplaceholder').modal('hide');
        $state.reload();
      };

      $scope.resetUpdateplaceHolder = function () {
        $scope.updatePlaceholdeForm.$setPristine();
        $scope.updatePlaceHolder.documentHolderName = '';
        $scope.updatePlaceHolder.documentHolderDescription = '';
        $scope.updatePlaceHolder.documentFileNumber = '';
        $scope.certificateType = '';
        $scope.expirydescription = '';
      };

      $scope.resetExpiryplaceHolder = function () {
        $scope.certificateType = '';
        $scope.expirydescription = '';
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
