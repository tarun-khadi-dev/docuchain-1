var sessionExpiry = angular.module('dapp.SessionExpiredController', []);

sessionExpiry.controller('SessionExpiredController', [
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

    if ($scope.sessionObject != null) {
      window.localStorage.clear();
      $window.localStorage.removeItem('sessionObject');
      $scope.clearSession = function () {
        $window.location.reload();
        $location.path('/');
      };
    }
  },
]);
