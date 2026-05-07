// var adminDashboard = angular.module('dapp.AdminHeaderController', ['$idle']);

// // 1. 🔥 Inject $timeout here
// adminDashboard.controller('AdminHeaderController', [
//   '$scope',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   'FunctionalityService',
//   '$idle',
//   'toaster',
//   '$timeout', // <-- ADDED
//   function (
//     $scope,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     FunctionalityService,
//     $idle,
//     toaster,
//     $timeout // <-- ADDED
//   ) {
//     $scope.userProfileId = $window.localStorage.getItem('userId');
//     $scope.logoPath = $window.localStorage.getItem('logoPicture');
//     var sessionData = $window.localStorage.getItem('userName');
//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );
//     $scope.loader = false;

//     $scope.userDetails = function () {
//       $state.go('dapp.adminProfile');
//     };

//     $scope.sidebarActiveFun = function () {
//       if ($rootScope.sidebarActive == true) {
//         $rootScope.sidebarActive = false;
//       } else {
//         $rootScope.sidebarActive = true;
//       }
//     };

//     //This method is used for idle session timeout
//     $scope.$on('$userTimeout', function () {
//       $state.go('session');
//     });
//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           console.info('getDocumentNOtification', response);
//           $scope.notificationList = response.data.userList;
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });
//     // var sessionData = JSON.parse(sessionStorage.getItem("names"));
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

//     $scope.logout = function () {
//       // 1. Completely wipe EVERYTHING in Local Storage (including 'theme')
//       $window.localStorage.clear();

//       // 2. Remove the dark mode class from the body instantly
//       document.body.classList.remove('dark-mode');

//       // 3. Show success toast
//       toaster.pop('success', 'Logout successfully');

//       // 4. Redirect to login page and force a clean reload
//       $timeout(function () {
//         $window.location.href = '/';
//       }, 1000);
//     };

//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId, filterByDay: 'Renewel' };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           $scope.notificationList = response.data.userList;
//           $scope.notificationListFielter = $scope.notificationList;
//           console.info(
//             'response.data.userList:',
//             JSON.stringify($scope.notificationListFielter)
//           );
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });
//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId, filterByDay: 'Lastweek' };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           $scope.notificationListLastweek = response.data.userList;
//           $scope.notificationListLastweekFielter =
//             $scope.notificationListLastweek;
//           console.info(
//             'response.data.userListLastweek:',
//             JSON.stringify(response.data.userList)
//           );
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });
//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId, filterByDay: 'Lastmonth' };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           $scope.notificationListLastmonth = response.data.userList;
//           $scope.notificationListLastmonthFielter =
//             $scope.notificationListLastmonth;
//           console.info(
//             'response.data.userListLastmonth:',
//             JSON.stringify(response.data.userList)
//           );
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });
//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId, filterByDay: 'Older' };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           $scope.notificationListOlder = response.data.userList;
//           $scope.notificationListOlderFielter = $scope.notificationListOlder;
//           console.info(
//             'response.data.userListOlder:',
//             JSON.stringify(response.data.userList)
//           );
//         },
//         function myError(err) {
//           console.log('Error response');
//         }
//       );
//     });

//     $scope.$on('$viewContentLoaded', function () {
//       FunctionalityService.getOrganizationUserList($scope.userProfileId).then(
//         function mySuccess(response) {
//           if (response.status == 201 || response.status == 200) {
//             $scope.adminUserList = JSON.stringify(response.data.getUserList);
//             $scope.adminUserList = response.data.getUserList;
//             if ($scope.adminUserList == undefined) {
//               // toaster.clear();
//               // toaster.info({ title: "No records found" });
//             }
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     });
//     $scope.$on('$viewContentLoaded', function () {
//       var data = { userId: $scope.userProfileId };
//       FunctionalityService.getDocumentNotification(data).then(
//         function mySuccess(response) {
//           console.info('getDocumentNotification', response);
//           $scope.notificationList = response.data.userList;
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     });
//     $scope.notification = function () {
//       $scope.loader = true;
//       var data = { userId: $scope.userProfileId };
//       //$state.go('dapp.notification');
//       FunctionalityService.setDocumentNotificationViewed(data).then(
//         function mySuccess(response) {
//           $scope.loader = false;
//           //$window.location.reload();
//           $state.go('dapp.notificationadmin');
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.userDashboard = function () {
//       console.log('inside the user dashboard');
//       $state.go('dapp.adminUsers');
//     };

//     $scope.vesselsInformation = function () {
//       console.log('inside the user dashboard vessels');
//       $state.go('dapp.adminVessels');
//     };
//     if (sessionData == null || sessionData == undefined) {
//       $location.path('/');
//     } else if (sessionData != null) {
//       $scope.storedData = sessionData.loginInfo;
//     }

//     // listen to storage event
//     $scope.logout = function () {
//       $window.localStorage.removeItem('sessionObject');
//       $window.localStorage.removeItem('userRole');
//       $window.localStorage.removeItem('userName');
//       $window.localStorage.removeItem('userEmail');
//       $window.localStorage.removeItem('userId');
//       $window.localStorage.removeItem('roleId');
//       $window.localStorage.removeItem('role');
//       $window.localStorage.removeItem('organizationId');
//       $window.localStorage.removeItem('organizationName');
//       $window.localStorage.removeItem('profilePicture');
//       $window.localStorage.removeItem('maxShipCount');
//       $window.localStorage.removeItem('maxUserCount');
//       $window.localStorage.removeItem('shipProfileInfos');
//       $window.localStorage.removeItem('groupShipId');
//       $window.localStorage.removeItem('groupShipName');
//       $window.localStorage.removeItem('editId');
//       $window.localStorage.removeItem('countryName');
//       $window.localStorage.removeItem('stateName');
//       $window.localStorage.removeItem('shipId');
//       $window.localStorage.removeItem('libShipId');
//       $window.localStorage.removeItem('libshipName');
//       $window.localStorage.removeItem('logoPicture');
//       localStorage.removeItem('logout-event');

//       toaster.pop('success', 'Logout successfully');

//       // 2. 🔥 Replace setTimeout with $timeout and use window.location
//       $timeout(function () {
//         $window.location.href = '/';
//       }, 1000);
//     };

//     $(document).ready(function () {
//       $('#logout').on('click', function () {
//         // change logout-event and therefore send an event
//         localStorage.setItem('logout-event', 'logout' + Math.random());
//         return true;
//       });
//     });

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
//           // 3. 🔥 Wrap the logout call in $scope.$apply
//           $scope.$apply(function () {
//             $scope.logout();
//           });
//         }
//       });
//     };

//     window.addEventListener(
//       'storage',
//       function (event) {
//         if (event.key == 'logout-event') {
//           $window.localStorage.removeItem('sessionObject');
//           $window.localStorage.removeItem('userRole');
//           $window.localStorage.removeItem('userName');
//           $window.localStorage.removeItem('userEmail');
//           $window.localStorage.removeItem('userId');
//           $window.localStorage.removeItem('roleId');
//           $window.localStorage.removeItem('role');
//           $window.localStorage.removeItem('organizationId');
//           $window.localStorage.removeItem('organizationName');
//           $window.localStorage.removeItem('profilePicture');
//           $window.localStorage.removeItem('maxShipCount');
//           $window.localStorage.removeItem('maxUserCount');
//           $window.localStorage.removeItem('shipProfileInfos');
//           $window.localStorage.removeItem('groupShipId');
//           $window.localStorage.removeItem('groupShipName');
//           $window.localStorage.removeItem('editId');
//           $window.localStorage.removeItem('countryName');
//           $window.localStorage.removeItem('stateName');
//           $window.localStorage.removeItem('shipId');
//           $window.localStorage.removeItem('libShipId');
//           $window.localStorage.removeItem('libshipName');
//           localStorage.removeItem('logout-event');
//           $window.localStorage.removeItem('logoPicture');

//           toaster.pop('success', 'Logout successfully');
//           setTimeout(function () {
//             $location.path('/');
//           }, 2000);
//           $window.location.reload();
//         } else if (event.key != 'logout-event') {
//           if (localStorage.roleId == '2') {
//             // $state.go('dapp.admindashboard');
//           }
//           if (localStorage.roleId == '1') {
//             // $state.go('dapp.saDashboard');
//           } else if (
//             localStorage.roleId == '3' ||
//             localStorage.roleId == '4' ||
//             localStorage.roleId == '5' ||
//             localStorage.roleId == '6'
//           ) {
//             //$state.go('dapp.userDashboard');
//           }
//         }
//       },
//       false
//     );
//   },
// ]);

var adminDashboard = angular.module('dapp.AdminHeaderController', ['$idle']);

adminDashboard.controller('AdminHeaderController', [
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
    $scope.logoPath = $window.localStorage.getItem('logoPicture');
    var sessionData = $window.localStorage.getItem('userName');

    // Safety check for sessionObject parsing
    var rawSession = $window.localStorage.getItem('sessionObject');
    if (rawSession && rawSession !== 'undefined') {
      $scope.sessionObject = JSON.parse(rawSession);
    }

    $scope.loader = false;

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

    $scope.userDetails = function () {
      $state.go('dapp.adminProfile');
    };

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
      var data = { userId: $scope.userProfileId };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationList = response.data.userList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

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

    // 🔥 PERFECTED LOGOUT FUNCTION 🔥
    $scope.logout = function () {
      // 1. Wipe absolutely everything from local storage
      $window.localStorage.clear();

      // 2. Remove the dark mode class instantly
      document.body.classList.remove('dark-mode');

      // 3. Show success toast
      toaster.pop('success', 'Logout successfully');

      // 4. Force hard redirect to login page
      $timeout(function () {
        $window.location.href = '/';
      }, 1000);
    };

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Renewel' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationList = response.data.userList;
          $scope.notificationListFielter = $scope.notificationList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Lastweek' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListLastweek = response.data.userList;
          $scope.notificationListLastweekFielter =
            $scope.notificationListLastweek;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Lastmonth' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListLastmonth = response.data.userList;
          $scope.notificationListLastmonthFielter =
            $scope.notificationListLastmonth;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Older' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListOlder = response.data.userList;
          $scope.notificationListOlderFielter = $scope.notificationListOlder;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getOrganizationUserList($scope.userProfileId).then(
        function mySuccess(response) {
          if (response.status == 201 || response.status == 200) {
            $scope.adminUserList = response.data.getUserList;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    });

    $scope.notification = function () {
      $scope.loader = true;
      var data = { userId: $scope.userProfileId };
      FunctionalityService.setDocumentNotificationViewed(data).then(
        function mySuccess(response) {
          $scope.loader = false;
          $state.go('dapp.notificationadmin');
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.userDashboard = function () {
      $state.go('dapp.adminUsers');
    };

    $scope.vesselsInformation = function () {
      $state.go('dapp.adminVessels');
    };

    if (sessionData == null || sessionData == undefined) {
      $location.path('/');
    } else if (sessionData != null) {
      $scope.storedData = sessionData.loginInfo;
    }

    $(document).ready(function () {
      $('#logout').on('click', function () {
        localStorage.setItem('logout-event', 'logout' + Math.random());
        return true;
      });
    });

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

    // Cross-tab logout synchronization
    // 🔥 MODERN SWEETALERT CONFIRM LOGOUT (Matched with SA) 🔥
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
  },
]);
