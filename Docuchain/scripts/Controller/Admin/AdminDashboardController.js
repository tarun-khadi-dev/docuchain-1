var adminDashboard = angular.module('dapp.AdminDashboardController', [
  'angularUtils.directives.dirPagination',
]);

// File Input Directive
adminDashboard.directive('fileInput', [
  '$parse',
  function ($parse) {
    return {
      restrict: 'A',
      link: function (scope, ele, attrs) {
        ele.bind('change', function () {
          $parse(attrs.fileInput).assign(scope, ele[0].files);
          scope.$apply();
        });
      },
    };
  },
]);

adminDashboard.controller('AdminDashboardController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  'toaster',
  '$timeout',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    toaster,
    $timeout
  ) {
    if (localStorage.length == 0) {
      $location.path('/');
    }

    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );
    $scope.userProfileId = Number($window.localStorage.getItem('userId'));

    // Initialization
    $scope.currentPage = 1;
    $scope.viewby = 5;
    $scope.itemsPerPage = $scope.viewby;
    $scope.shipProfileList = [];
    $scope.vesselPortsList = [];
    $scope.portsCount = 0;
    $scope.editShow = true;
    $scope.resetShow = false;
    $scope.approveArray = [];
    $scope.rejectedArray = [];
    $rootScope.pendingArray = [];
    $rootScope.geoLocationlist = [];
    $rootScope.markers = [];
    $scope.loader = false;
    $scope.thumbnail = {};
    $scope.profilePicture = null;

    var latitudehome = 1.3521;
    var longitutehome = 103.8198;

    $rootScope.active = $scope.sessionObject.subscriptionExpireStatus;

    // =========================================================================
    // PROFILE IMAGE UPLOAD & PREVIEW LOGIC
    // =========================================================================
    // =========================================================================
    // LOAD LOGO GLOBALLY FOR SIDEBAR AND HEADER
    // =========================================================================
    var savedLogo = $window.localStorage.getItem('logoPicture');
    if (savedLogo && savedLogo !== 'undefined' && savedLogo !== 'null') {
      // Use $rootScope so the Header HTML can access this variable too!
      $rootScope.logoPath = savedLogo;
    }

    // Listen for the broadcast from AddLogo page
    $rootScope.$on('logoUpdated', function (event, newLogoUrl) {
      $rootScope.logoPath = newLogoUrl;
    });
    // =========================================================================
    $scope.uploadFiledp = function (files) {
      if (files && files.length > 0) {
        var file = files[0];
        var errorSpan = document.getElementById('sizeOffile');

        if (file.size > 1048576) {
          if (errorSpan) errorSpan.innerText = 'Picture should be below 1MB';
          $rootScope.errorFile = 'Picture should be below 1MB';
          return;
        } else {
          if (errorSpan) errorSpan.innerText = '';
          $rootScope.errorFile = '';
        }

        var reader = new FileReader();
        reader.onload = function (e) {
          $scope.$apply(function () {
            if (!$scope.thumbnail) {
              $scope.thumbnail = {};
            }
            $scope.thumbnail.dataUrl = e.target.result;
            $scope.profilePicture = file;
          });
        };
        reader.readAsDataURL(file);
      }
    };

    // =========================================================================
    // DASHBOARD & NAVIGATION LOGIC
    // =========================================================================
    FunctionalityService.getDashboardTopCount($scope.userProfileId).then(
      function (response) {
        if (response.data != null && response.data.shipProfileList.length > 0) {
          var data = response.data.shipProfileList[0];
          $scope.userCount = data.userCount || 0;
          $scope.vesselsCount = data.vesselsCount || 0;
          $scope.activeCount = data.activeCount || 0;
          $scope.renewalCount = data.renewelCount || 0;
          $scope.expiredCount = data.expiryedCount || 0;
          $scope.missingCount = data.missingCount || 0;

          $scope.getVesselPortsData(1);
        }
      },
      function (error) {
        console.log('Admin TopCount Error:', error);
      }
    );

    $scope.getVesselPortsData = function (vesselId) {
      $scope.loader = true;
      FunctionalityService.getVesselPorts(vesselId).then(
        function (response) {
          $scope.loader = false;
          if (response.data != null) {
            $scope.portsCount = response.data.portCount || 0;
            $scope.vesselPortsList = response.data.portList || [];
          }
        },
        function (error) {
          $scope.loader = false;
          console.error('Error fetching ports:', error);
        }
      );
    };

    $scope.userDashboard = function () {
      $state.go('dapp.adminUsers');
    };

    $scope.vesselsInformation = function () {
      $state.go('dapp.adminVessels');
    };

    $scope.editProfile = function () {
      $scope.editShow = true;
      $scope.resetShow = false;
      $state.reload();
    };

    $scope.getPendingCount = function () {
      $scope.loader = true;
      FunctionalityService.getPendingRequest($scope.userProfileId).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200) {
            $scope.pendingList = response.data.requestUserDTOs;
            angular.forEach($scope.pendingList, function (value) {
              if (value.requestUserStatus == 'Approved') {
                $scope.approveArray.push(value);
              } else if (value.requestUserStatus == 'Rejected') {
                $scope.rejectedArray.push(value);
              } else if (value.requestUserStatus == 'Pending') {
                $rootScope.pendingArray.push(value);
              }
            });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );

      FunctionalityService.getTaskAssignedToUser($scope.userProfileId).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.taskAssignedToUser = response.data.taskAssignedToUser;
            $scope.taskAssignedToUsernew = [];
            angular.forEach($scope.taskAssignedToUser, function (value) {
              if (
                value.taskStatus != 'Completed' &&
                value.taskStatus != 'Rejected'
              ) {
                $scope.taskAssignedToUsernew.push(value);
              }
            });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.geoLocationlist($scope.sessionObject.userId).then(
        function (response) {
          if (response.status == 200) {
            $rootScope.geoLocationlist = response.data.userList;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    });

    $scope.editProfileSubmit = function (data) {
      $scope.loader = true;
      toaster.clear();

      if ($rootScope.errorFile === 'Picture should be below 1MB') {
        $scope.loader = false;
        toaster.pop('error', $rootScope.errorFile);
      } else {
        $rootScope.errorFile = '';
        var updateData = {
          userId: $scope.userProfileId,
          firstName: data.firstName,
          lastName: data.lastName,
          mail: data.mail,
        };

        FunctionalityService.editProfileData(
          updateData,
          $scope.profilePicture
        ).then(function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            $window.localStorage.setItem(
              'sessionObject',
              JSON.stringify(response.data.userInfos)
            );
            setTimeout(function () {
              $window.location.reload();
            }, 1000);
          }
        });
      }
    };

    $scope.resetPassword = function () {
      $scope.resetShow = true;
      $scope.editShow = false;
    };

    $scope.resetPasswordSubmit = function (data) {
      $scope.loader = true;
      var resetData = {
        userId: $scope.userProfileId,
        currentPassword: data.currentPassword,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };

      FunctionalityService.resetPswd(resetData).then(function (response) {
        $scope.loader = false;
        if (response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);
        }
      });
    };

    $scope.getAllVesselListAndCount = function () {
      $scope.loader = true;
      FunctionalityService.getViewShipProfile($scope.userProfileId).then(
        function mySuccess(response) {
          $scope.loader = false;
          if (response.data != null) {
            $scope.shipProfileList = response.data.shipProfileList;
          }
        }
      );
    };

    var configStates = [
      'dapp.superAconfCountry',
      'dapp.superAconfState',
      'dapp.addLogo',
      'dapp.adminPlaceholder',
    ];

    if (configStates.indexOf($state.current.name) !== -1) {
      $scope.subConfigMenuActive = true;
    } else {
      $scope.subConfigMenuActive = false;
    }

    $scope.expandConfigSubMenu = function () {
      $scope.subConfigMenuActive = !$scope.subConfigMenuActive;
    };

    $scope.sidebarActiveFun = function () {
      $rootScope.sidebarActive = !$rootScope.sidebarActive;
    };

    $scope.routrReload = function () {
      $state.reload();
    };

    // ========================================================
    // NEW: Dashboard Task Counts & URL Redirection Logic
    // ========================================================
    $scope.taskAssignedByYouCount = 0;
    $scope.taskAssignedToYouCount = 0;

    FunctionalityService.getTaskAssignedByUser($scope.userProfileId).then(
      function (response) {
        if (response.data && response.data.taskAssignedByUser) {
          $scope.taskAssignedByYouCount =
            response.data.taskAssignedByUser.length;
        }
      }
    );

    FunctionalityService.getTaskAssignedToUser($scope.userProfileId).then(
      function (response) {
        if (response.data && response.data.taskAssignedToUser) {
          $scope.taskAssignedToYouCount =
            response.data.taskAssignedToUser.length;
        }
      }
    );

    // ========================================================
    // NEW: Dashboard Task Counts & URL Redirection Logic
    // ========================================================
    $scope.taskAssignedByYouCount = 0;
    $scope.taskAssignedToYouCount = 0;

    FunctionalityService.getTaskAssignedByUser($scope.userProfileId).then(
      function (response) {
        if (response.data && response.data.taskAssignedByUser) {
          $scope.taskAssignedByYouCount =
            response.data.taskAssignedByUser.length;
        }
      }
    );

    FunctionalityService.getTaskAssignedToUser($scope.userProfileId).then(
      function (response) {
        if (response.data && response.data.taskAssignedToUser) {
          $scope.taskAssignedToYouCount =
            response.data.taskAssignedToUser.length;
        }
      }
    );

    // When clicked, save the tabName to local storage and redirect
    $scope.goToTaskTab = function (tabId) {
      $window.localStorage.setItem('activeTaskTab', tabId);
      $state.go('dapp.adminTasks');
    };
    // ========================================================
  },
]);
