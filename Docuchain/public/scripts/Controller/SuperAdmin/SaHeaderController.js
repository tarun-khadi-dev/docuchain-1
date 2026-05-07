// // var saHeader = angular.module('dapp.SaHeaderController', ['$idle']);

// // saHeader.controller('SaHeaderController', [
// //   '$scope',
// //   '$window',
// //   '$location',
// //   '$state',
// //   '$rootScope',
// //   'FunctionalityService',
// //   '$idle',
// //   'toaster',
// //   '$timeout', // <-- ADDED
// //   function (
// //     $scope,
// //     $window,
// //     $location,
// //     $state,
// //     $rootScope,
// //     FunctionalityService,
// //     $idle,
// //     toaster,
// //     $timeout // <-- ADDED
// //   ) {
// //     var sessionData = $window.localStorage.getItem('userName');

// //     $scope.sidebarActiveFun = function () {
// //       if ($rootScope.sidebarActive == true) {
// //         $rootScope.sidebarActive = false;
// //       } else {
// //         $rootScope.sidebarActive = true;
// //       }
// //     };

// //     $scope.$on('$userTimeout', function () {
// //       $state.go('session');
// //     });

// //     if (sessionData == null || sessionData == undefined) {
// //       $location.path('/');
// //     } else if (sessionData != null) {
// //       $scope.storedData = sessionData.loginInfo;
// //     }

// //     $scope.sessionObject = {
// //       firstName: $window.localStorage.getItem('userName'),
// //       lastName: '',
// //       role: $window.localStorage.getItem('role'),
// //       profilePicture: $window.localStorage.getItem('profilePicture'),
// //     };

// //     $scope.userDetails = function () {
// //       $state.go('dapp.saProfile');
// //     };

// //     $scope.logout = function () {
// //       $window.localStorage.removeItem('sessionObject');
// //       $window.localStorage.removeItem('userRole');
// //       $window.localStorage.removeItem('userName');
// //       $window.localStorage.removeItem('userEmail');
// //       $window.localStorage.removeItem('userId');
// //       $window.localStorage.removeItem('roleId');
// //       $window.localStorage.removeItem('role');
// //       $window.localStorage.removeItem('organizationId');
// //       $window.localStorage.removeItem('organizationName');
// //       $window.localStorage.removeItem('profilePicture');
// //       $window.localStorage.removeItem('maxShipCount');
// //       $window.localStorage.removeItem('maxUserCount');
// //       $window.localStorage.removeItem('shipProfileInfos');
// //       $window.localStorage.removeItem('groupShipId');
// //       $window.localStorage.removeItem('groupShipName');
// //       $window.localStorage.removeItem('editId');
// //       $window.localStorage.removeItem('countryName');
// //       $window.localStorage.removeItem('stateName');
// //       $window.localStorage.removeItem('shipId');
// //       $window.localStorage.removeItem('libShipId');
// //       $window.localStorage.removeItem('libshipName');
// //       localStorage.removeItem('logout-event');
// //       $window.localStorage.removeItem('logoPicture');

// //       toaster.pop('success', 'Logout successfully');

// //       // 2. 🔥 Replace setTimeout with $timeout
// //       $timeout(function () {
// //         // 3. 🔥 If you want a real page reload to clear cache/memory, use this:
// //         $window.location.href = '/';
// //         // If your login page needs a hashbang (like '#!/'), use: $window.location.href = '#!/';
// //       }, 1000);
// //     };

// //     $(document).ready(function () {
// //       $('#logout').on('click', function () {
// //         localStorage.setItem('logout-event', 'logout' + Math.random());
// //         return true;
// //       });
// //     });

// //     window.addEventListener(
// //       'storage',
// //       function (event) {
// //         // Keep your existing storage event listener logic here...
// //         // (Truncated for brevity, but leave your code intact here)
// //       },
// //       false
// //     );

// //     $scope.confirmLogout = function () {
// //       Swal.fire({
// //         html: `<div class="logout-card">
// //                     <div class="logout-icon">
// //                         <i class="fa fa-sign-out"></i>
// //                     </div>
// //                     <h3>Logout</h3>
// //                     <p>Are you sure you want to logout?</p>
// //                 </div>`,
// //         width: 480,
// //         showCancelButton: true,
// //         confirmButtonText: 'Logout',
// //         cancelButtonText: 'Cancel',
// //         confirmButtonColor: '#4a7be6',
// //       }).then((result) => {
// //         if (result.isConfirmed) {
// //           // 4. 🔥 Wrap the logout call in $scope.$apply so the Toaster renders
// //           $scope.$apply(function () {
// //             $scope.logout();
// //           });
// //         }
// //       });
// //     };
// //   },
// // ]);

// var saHeader = angular.module('dapp.SaHeaderController', ['$idle']);

// saHeader.controller('SaHeaderController', [
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
//     var sessionData = $window.localStorage.getItem('userName');

//     // --- DARK MODE LOGIC START ---
//     // Check local storage for theme preference, default to false (light mode)
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

//     if (sessionData == null || sessionData == undefined) {
//       $location.path('/');
//     } else if (sessionData != null) {
//       $scope.storedData = sessionData.loginInfo;
//     }

//     $scope.sessionObject = {
//       firstName: $window.localStorage.getItem('userName'),
//       lastName: '',
//       role: $window.localStorage.getItem('role'),
//       profilePicture: $window.localStorage.getItem('profilePicture'),
//     };

//     $scope.userDetails = function () {
//       $state.go('dapp.saProfile');
//     };

//     $scope.logout = function () {
//       // $window.localStorage.removeItem('sessionObject');
//       // $window.localStorage.removeItem('userRole');
//       // $window.localStorage.removeItem('userName');
//       // $window.localStorage.removeItem('userEmail');
//       // $window.localStorage.removeItem('userId');
//       // $window.localStorage.removeItem('roleId');
//       // $window.localStorage.removeItem('role');
//       // $window.localStorage.removeItem('organizationId');
//       // $window.localStorage.removeItem('organizationName');
//       // $window.localStorage.removeItem('profilePicture');
//       // $window.localStorage.removeItem('maxShipCount');
//       // $window.localStorage.removeItem('maxUserCount');
//       // $window.localStorage.removeItem('shipProfileInfos');
//       // $window.localStorage.removeItem('groupShipId');
//       // $window.localStorage.removeItem('groupShipName');
//       // $window.localStorage.removeItem('editId');
//       // $window.localStorage.removeItem('countryName');
//       // $window.localStorage.removeItem('stateName');
//       // $window.localStorage.removeItem('shipId');
//       // $window.localStorage.removeItem('libShipId');
//       // $window.localStorage.removeItem('libshipName');
//       // localStorage.removeItem('logout-event');
//       // $window.localStorage.removeItem('logoPicture');

//       // 1. Wipe absolutely everything from local storage in one go
//       $window.localStorage.clear();

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
//         // Keep your existing storage event listener logic here...
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
//   },
// ]);

var saHeader = angular.module('dapp.SaHeaderController', ['$idle']);

saHeader.controller('SaHeaderController', [
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
    var sessionData = $window.localStorage.getItem('userName');

    // --- DARK MODE LOGIC START ---
    $scope.isDarkMode = $window.localStorage.getItem('theme') === 'dark';

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

    if (sessionData == null || sessionData == undefined) {
      $location.path('/');
    } else if (sessionData != null) {
      $scope.storedData = sessionData.loginInfo;
    }

    $scope.sessionObject = {
      firstName: $window.localStorage.getItem('userName'),
      lastName: '',
      role: $window.localStorage.getItem('role'),
      profilePicture: $window.localStorage.getItem('profilePicture'),
    };

    $scope.userDetails = function () {
      $state.go('dapp.saProfile');
    };

    // // 🔥 PERFECTED LOGOUT FUNCTION 🔥
    // $scope.logout = function () {
    //   // 1. Wipe absolutely everything from local storage in one go
    //   $window.localStorage.clear();

    //   // 2. Remove the logout-event for cross-tab synchronization
    //   localStorage.removeItem('logout-event');

    //   // 3. Instantly drop the dark mode class so the login screen returns to light mode
    //   document.body.classList.remove('dark-mode');

    //   // 4. Show success toast
    //   toaster.pop('success', 'Logout successfully');

    //   // 5. Redirect smoothly back to the login page after 1 second
    //   $timeout(function () {
    //     $window.location.href = '/';
    //   }, 1000);
    // };
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

    // Cross-tab logout synchronization
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

    // $scope.confirmLogout = function () {
    //   Swal.fire({
    //     html: `<div class="logout-card">
    //                 <div class="logout-icon">
    //                     <i class="fa fa-sign-out"></i>
    //                 </div>
    //                 <h3>Logout</h3>
    //                 <p>Are you sure you want to logout?</p>
    //             </div>`,
    //     width: 480,
    //     showCancelButton: true,
    //     confirmButtonText: 'Logout',
    //     cancelButtonText: 'Cancel',
    //     confirmButtonColor: '#4a7be6',
    //   }).then((result) => {
    //     if (result.isConfirmed) {
    //       $scope.$apply(function () {
    //         $scope.logout();
    //       });
    //     }
    //   });
    // };
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
        // showClass: {
        //   popup: 'animate__animated animate__zoomIn animate__faster',
        // },
        // hideClass: {
        //   popup: 'animate__animated animate__zoomOut animate__faster',
        // },

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
  },
]);
