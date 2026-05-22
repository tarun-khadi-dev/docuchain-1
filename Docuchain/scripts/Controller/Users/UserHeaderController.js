// var userHeader = angular.module('dapp.UserHeaderController', ['$idle']);

// userHeader.controller('UserHeaderController', [
//   '$scope',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   'FunctionalityService',
//   '$idle',
//   'toaster',
//   '$timeout',
//   function (
//     $scope,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     FunctionalityService,
//     $idle,
//     toaster,
//     $timeout
//   ) {
//     $scope.userProfileId = $window.localStorage.getItem('userId');
//     var sessionData = $window.localStorage.getItem('userName');
//     $scope.logoPath = $window.localStorage.getItem('logoPicture');
//     $scope.headerDetails;

//     // --- DARK MODE LOGIC START ---
//     $scope.isDarkMode = $window.localStorage.getItem('theme') === 'dark';

//     // Apply the class on initial load
//     if ($scope.isDarkMode) {
//       document.body.classList.add('dark-mode');
//     }

//     $scope.toggleTheme = function () {
//       $scope.isDarkMode = !$scope.isDarkMode;

//       if ($scope.isDarkMode) {
//         document.body.classList.add('dark-mode');
//         $window.localStorage.setItem('theme', 'dark');
//       } else {
//         document.body.classList.remove('dark-mode');
//         $window.localStorage.setItem('theme', 'light');
//       }
//     };
//     // --- DARK MODE LOGIC END ---

//     $scope.sidebarActiveFun = function () {
//       if ($rootScope.sidebarActive == true) {
//         $rootScope.sidebarActive = false;
//       } else {
//         $rootScope.sidebarActive = true;
//       }
//     };

//     $scope.$on('$userTimeout', function () {
//       $state.go('session');
//     });

//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId, filterByDay: 'Lastweek' };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           $scope.notificationList = response.data.userList;
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });

//     $scope.userDetails = function () {
//       $state.go('dapp.userProfile');
//     };

//     $scope.statusChange = function (changestatus) {
//       $rootScope.statusForFilter = changestatus;
//       $state.reload();
//     };

//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId };
//       FunctionalityService.getDocumentNotificationCount(data).then(
//         function mySuccess(response) {
//           $scope.notificationCountList = response.data.userList;
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });

//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );

//     $scope.notification = function () {
//       var data = { userId: $scope.userProfileId };
//       $state.go('dapp.userNotification');
//       FunctionalityService.setDocumentNotificationViewed(data).then(
//         function mySuccess(response) {
//           $state.go('dapp.userNotification');
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     };

//     $scope.faq = function () {
//       $state.go('dapp.faq');
//     };

//     if (sessionData == null || sessionData == undefined) {
//       $location.path('/login');
//     } else if (sessionData != null) {
//       $scope.storedData = sessionData.loginInfo;
//     }

//     $scope.logout = function () {
//       $window.localStorage.clear();
//       document.body.classList.remove('dark-mode'); // Clear dark mode on logout
//       toaster.pop('success', 'Logout successfully');

//       $timeout(function () {
//         $window.location.href = '/';
//       }, 1000);
//     };

//     $(document).ready(function () {
//       $('#logout').on('click', function () {
//         localStorage.setItem('logout-event', 'logout' + Math.random());
//         return true;
//       });
//     });

//     window.addEventListener(
//       'storage',
//       function (event) {
//         if (event.key == 'logout-event') {
//           $window.localStorage.clear();
//           document.body.classList.remove('dark-mode');
//           toaster.pop('success', 'Logout successfully');
//           setTimeout(function () {
//             $location.path('/');
//           }, 1000);
//           $window.location.reload();
//         }
//       },
//       false
//     );

//     $scope.confirmLogout = function () {
//       Swal.fire({
//         html: `<div class="logout-card">
//                     <div class="logout-icon">
//                         <i class="fa fa-sign-out"></i>
//                     </div>
//                     <h3>Logout</h3>
//                     <p>Are you sure you want to logout?</p>
//                 </div>`,
//         width: 480,
//         showCancelButton: true,
//         confirmButtonText: 'Logout',
//         cancelButtonText: 'Cancel',
//         confirmButtonColor: '#4a7be6',
//       }).then((result) => {
//         if (result.isConfirmed) {
//           $scope.$apply(function () {
//             $scope.logout();
//           });
//         }
//       });
//     };

//     $scope.vesselsInformation = function () {
//       if ($window.localStorage.getItem('roleId') == 3) {
//         $rootScope.selected = 1;
//         $state.go('dapp.userVesselDocumentEBD');
//       } else {
//         $rootScope.selected = 1;
//         $state.go('dapp.userVesselDocument');
//       }
//     };
//   },
// ]);
var userHeader = angular.module('dapp.UserHeaderController', ['$idle']);

userHeader.controller('UserHeaderController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  '$idle',
  'toaster',
  '$timeout',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    $idle,
    toaster,
    $timeout
  ) {
    $scope.userProfileId = $window.localStorage.getItem('userId');
    var sessionData = $window.localStorage.getItem('userName');
    $scope.logoPath = $window.localStorage.getItem('logoPicture');
    $scope.headerDetails;

    // --- DARK MODE LOGIC START ---
    $scope.isDarkMode = $window.localStorage.getItem('theme') === 'dark';

    // Apply the class on initial load
    if ($scope.isDarkMode) {
      document.body.classList.add('dark-mode');
    }

    $scope.toggleTheme = function () {
      $scope.isDarkMode = !$scope.isDarkMode;

      if ($scope.isDarkMode) {
        document.body.classList.add('dark-mode');
        $window.localStorage.setItem('theme', 'dark');
      } else {
        document.body.classList.remove('dark-mode');
        $window.localStorage.setItem('theme', 'light');
      }
    };
    // --- DARK MODE LOGIC END ---

    $scope.sidebarActiveFun = function () {
      if ($rootScope.sidebarActive == true) {
        $rootScope.sidebarActive = false;
      } else {
        $rootScope.sidebarActive = true;
      }
    };

    $scope.$on('$userTimeout', function () {
      $state.go('session');
    });

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Lastweek' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationList = response.data.userList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.userDetails = function () {
      $state.go('dapp.userProfile');
    };

    $scope.statusChange = function (changestatus) {
      $rootScope.statusForFilter = changestatus;
      $state.reload();
    };

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId };
      FunctionalityService.getDocumentNotificationCount(data).then(
        function mySuccess(response) {
          $scope.notificationCountList = response.data.userList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.notification = function () {
      var data = { userId: $scope.userProfileId };
      $state.go('dapp.userNotification');
      FunctionalityService.setDocumentNotificationViewed(data).then(
        function mySuccess(response) {
          $state.go('dapp.userNotification');
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    };

    $scope.faq = function () {
      $state.go('dapp.faq');
    };

    if (sessionData == null || sessionData == undefined) {
      $location.path('/login');
    } else if (sessionData != null) {
      $scope.storedData = sessionData.loginInfo;
    }

    // 🔥 PERFECTED LOGOUT FUNCTION (Matched with SA & Admin) 🔥
    $scope.logout = function () {
      // 1. Wipe absolutely everything from local storage in one go
      $window.localStorage.clear();

      // 2. Remove the logout-event for cross-tab synchronization
      localStorage.removeItem('logout-event');

      // 3. Instantly drop the dark mode class
      document.body.classList.remove('dark-mode');

      // 4. Show success toast
      toaster.pop('success', 'Logout successfully');

      // 5. Redirect smoothly back to the login page after 1.5 seconds (gives toast time to show)
      $timeout(function () {
        $window.location.href = '/';
      }, 1500);
    };

    $(document).ready(function () {
      $('#logout').on('click', function () {
        localStorage.setItem('logout-event', 'logout' + Math.random());
        return true;
      });
    });

    // Cross-tab logout synchronization (Cleaned up to match standard)
    window.addEventListener(
      'storage',
      function (event) {
        if (event.key == 'logout-event') {
          $window.localStorage.clear();
          document.body.classList.remove('dark-mode');
          toaster.pop('success', 'Logout successfully');
          setTimeout(function () {
            $window.location.href = '/';
          }, 1000);
        }
      },
      false
    );

    // 🔥 MODERN SWEETALERT CONFIRM LOGOUT (Matched with SA & Admin) 🔥
    $scope.confirmLogout = function () {
      Swal.fire({
        html: `
      <div class="logout-card-modern">
        <div class="logout-icon-wrapper">
          <i class="fa fa-sign-out"></i>
        </div>
        <h3 class="logout-title">Sign Out</h3>
        <p class="logout-subtitle">Are you sure you want to sign out of your account?</p>
      </div>`,
        width: 400,
        showCancelButton: true,
        confirmButtonText: 'Yes, Sign Out',
        cancelButtonText: 'Cancel',
        buttonsStyling: false, // Turns off default SweetAlert button styles
        reverseButtons: true, // Puts Cancel on the left, Action on the right
        customClass: {
          confirmButton: 'swal-btn-logout-danger',
          cancelButton: 'swal-btn-cancel-simple',
          actions: 'swal-actions-container',
        },
      }).then((result) => {
        if (result.isConfirmed) {
          $scope.$apply(function () {
            $scope.logout();
          });
        }
      });
    };

    $scope.vesselsInformation = function () {
      if ($window.localStorage.getItem('roleId') == 3) {
        $rootScope.selected = 1;
        $state.go('dapp.userVesselDocumentEBD');
      } else {
        $rootScope.selected = 1;
        $state.go('dapp.userVesselDocument');
      }
    };
  },
]);
