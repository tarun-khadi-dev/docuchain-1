var Login = angular.module('dapp.LoginController', ['toaster']);

Login.controller('LoginController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'toaster',
  'FunctionalityService',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    toaster,
    FunctionalityService
  ) {
    $scope.loader = false;

    if (localStorage.length != 0) {
      if (localStorage.roleId == '2') {
        $state.go('dapp.admindashboard');
      }
      if (localStorage.roleId == '1') {
        $state.go('dapp.saDashboard');
      } else if (
        localStorage.roleId == '3' ||
        localStorage.roleId == '4' ||
        localStorage.roleId == '5' ||
        localStorage.roleId == '6'
      ) {
        $state.go('dapp.userDashboard');
      }
    } else {
      $location.path('/');
    }
    $scope.loginAdmin = function (login) {
      $scope.loader = true;

      toaster.clear();
      if (localStorage.length == 0) {
        FunctionalityService.loginSubmit(login).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              console.log('login response success', response);
              $scope.message = response.data.userInfos;

              $window.localStorage.setItem(
                'sessionObject',
                JSON.stringify($scope.message)
              );
              $window.localStorage.setItem(
                'userRole',
                $scope.message.businessCategory
              );
              $window.localStorage.setItem('userName', $scope.message.userName);
              $window.localStorage.setItem('userEmail', $scope.message.mail);
              $window.localStorage.setItem('userId', $scope.message.userId);
              $window.localStorage.setItem('roleId', $scope.message.roleId);
              $window.localStorage.setItem('role', $scope.message.role);
              $window.localStorage.setItem(
                'organizationId',
                $scope.message.organizationId
              );
              $window.localStorage.setItem(
                'organizationName',
                $scope.message.organizationName
              );
              $window.localStorage.setItem(
                'profilePicture',
                $scope.message.profilePicture
              );
              $window.localStorage.setItem(
                'maxShipCount',
                $scope.message.maxShipCount
              );
              $window.localStorage.setItem(
                'maxUserCount',
                $scope.message.maxUserCount
              );
              $window.localStorage.setItem(
                'shipProfileInfos',
                $scope.message.shipProfileInfos
              );
              $window.localStorage.setItem(
                'logoPicture',
                $scope.message.logoPicture
              );
              window.addEventListener(
                'storage',
                function (event) {
                  //alert(event.key)
                  if (event.key != 'logout-event') {
                    if (localStorage.roleId == '2') {
                      // $state.go('dapp.admindashboard');
                    }
                    if (localStorage.roleId == '1') {
                      // $state.go('dapp.saDashboard');
                    } else if (
                      localStorage.roleId == '3' ||
                      localStorage.roleId == '4' ||
                      localStorage.roleId == '5' ||
                      localStorage.roleId == '6'
                    ) {
                      // $state.go('dapp.userDashboard');
                    }
                  } else {
                  }
                },
                false
              );
              if ($scope.message.roleId == '2') {
                $state.go('dapp.admindashboard');
              }
              if ($scope.message.roleId == '1') {
                $state.go('dapp.saDashboard');
              } else if (
                $scope.message.roleId == '3' ||
                $scope.message.roleId == '4' ||
                $scope.message.roleId == '5' ||
                $scope.message.roleId == '6'
              ) {
                $state.go('dapp.userDashboard');
              }
              // toaster.pop('success', response.data.message);
            } else if (response.status == 206) {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      } else {
      }
    };

    $scope.forgotPass = function (fpass) {
      $scope.loader = true;

      FunctionalityService.forgotpassword(fpass).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            toaster.pop('success', response.data.message);
            setTimeout(function () {
              $state.go('dapp.login');
            }, 3000);
          } else if (response.status == 206) {
            toaster.pop('error', response.data.message);
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    // listen to storage event
    window.addEventListener(
      'storage',
      function (event) {
        // alert(event.key)
        if (event.key == 'sessionObject') {
          localStorage.removeItem('logout-event');

          if (localStorage.roleId == '2') {
            // $state.go('dapp.admindashboard');
          }
          if (localStorage.roleId == '1') {
            // $state.go('dapp.saDashboard');
          } else if (
            localStorage.roleId == '3' ||
            localStorage.roleId == '4' ||
            localStorage.roleId == '5' ||
            localStorage.roleId == '6'
          ) {
            // $state.go('dapp.userDashboard');
          }
        } else {
        }
      },
      false
    );
  },
]);
