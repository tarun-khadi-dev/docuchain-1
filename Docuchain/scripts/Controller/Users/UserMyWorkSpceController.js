var userMyWorkspace = angular.module('dapp.UserMyWorkSpceController', []);

userMyWorkspace.controller('UserMyWorkSpceController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService
  ) {
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.groupVesselList = [];
    $scope.currentPage = 1;
    $scope.groupVesselListLength = $scope.groupVesselList.length;
    $scope.loader = false;
    $rootScope.selected = 3;

    //This method is used to get all ShipList
    $scope.getAllShipList = function () {
      $scope.loader = true;

      var data = {
        userId: $scope.sessionObject.userId,
        roleId: $scope.sessionObject.roleId,
      };
      // FunctionalityService.getVesselProfileList(data).then(
      //   function (response) {
      //     $scope.loader = false;

      //     if (response.status == 200) {
      //       $scope.groupVesselList = response.data.shipProfileList;
      //       $scope.groupVesselListLength = $scope.groupVesselList.length;
      //     }
      //   },
      //   function myError(err) {
      //     $scope.loader = false;
      //     console.log('Error response', err);
      //   }
      // );
      FunctionalityService.getVesselProfileList(data).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            // 1. Store the list in a temporary variable
            var list = response.data.shipProfileList;

            // 2. Sort the list numerically by officialNo (IMO Number)
            // This ensures 100 comes before 101
            list.sort(function (a, b) {
              return parseInt(a.officialNo) - parseInt(b.officialNo);
            });

            // 3. Assign the sorted list to the scope variables
            $scope.groupVesselList = list;
            $scope.groupVesselListLength = $scope.groupVesselList.length;

            console.log('Group Vessel List Sorted:', $scope.groupVesselList);
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    //This used to redirect to grouo list page
    $scope.groupList = function (ship) {
      $window.localStorage.removeItem('groupShipId');
      $window.localStorage.setItem('groupShipId', ship.id);
      $window.localStorage.setItem('groupShipName', ship.shipName);
      $state.go('dapp.userMyWorkspaceList');
    };
  },
]);
