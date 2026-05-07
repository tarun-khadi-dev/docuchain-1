var userDashboard = angular.module('dapp.UserDashboardController', [
  'angularUtils.directives.dirPagination',
  '$idle',
]);

userDashboard.controller('UserDashboardController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'toaster',
  '$timeout',
  'FunctionalityService',
  '$idle',
  'DeletePopup',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    toaster,
    $timeout,
    FunctionalityService,
    $idle,
    DeletePopup
  ) {
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.roleId = $scope.sessionObject.roleId;
    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.profilePictures = $window.localStorage.getItem('profilePicture');
    $scope.shipProfileInfos = $window.localStorage.getItem('shipProfileInfos');
    $scope.notificationId = null;
    $scope.deleteNotificationList = [];
    $scope.currentPage = 1;
    $scope.shipProfileList = [];
    $scope.shipProfileListLength = $scope.shipProfileList.length;
    $scope.sidebarList = [];
    $scope.editShow = true;
    $scope.resetShow = false;
    $scope.profilePicture = null;
    $scope.loader = false;
    $rootScope.markers = [];
    $rootScope.selected;

    // Initialize a variable to track which filter is clicked
    $scope.selectedDocFilter = '';

    // ========================================================
    // NEW: Dashboard Task Counts & URL Redirection Logic
    // ========================================================
    $scope.goToTaskTab = function (tabId) {
      $window.localStorage.setItem('activeTaskTab', tabId);
      $state.go('dapp.userTasks');
    };

    // 1. New function to handle the Stat Card clicks
    $scope.focusOnTable = function (filterType) {
      $scope.selectedDocFilter = filterType;

      // Set the filter in local storage so the next page knows what to load
      if (filterType) {
        $window.localStorage.setItem('redirectFilterStatus', filterType);
      } else {
        $window.localStorage.removeItem('redirectFilterStatus');
      }

      // Smooth scroll to the table
      $timeout(function () {
        var tableElement = document.getElementById('vesselStatisticsTable');
        if (tableElement) {
          tableElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }, 100);
    };

    // 2. Update the existing openEBDState function
    $scope.openEBDState = function (vessel) {
      $window.localStorage.removeItem('libShipId');
      $window.localStorage.removeItem('libshipName');

      $window.localStorage.setItem('libShipId', vessel.id);
      $window.localStorage.setItem('libshipName', vessel.vesselsName);

      // Ensure the selected filter is passed to the next page
      if ($scope.selectedDocFilter) {
        $window.localStorage.setItem(
          'redirectFilterStatus',
          $scope.selectedDocFilter
        );
      } else {
        $window.localStorage.removeItem('redirectFilterStatus');
      }

      $state.go('dapp.userVesselDocumentEBD');
    };

    var latitudehome = 1.3521;
    var longitutehome = 103.8198;

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

    $scope.redirectToEBDWithFilter = function (status) {
      if (status) {
        $window.localStorage.setItem('redirectFilterStatus', status);
      } else {
        $window.localStorage.removeItem('redirectFilterStatus');
      }

      if ($scope.shipProfileList && $scope.shipProfileList.length > 0) {
        $window.localStorage.setItem('libShipId', $scope.shipProfileList[0].id);
        $window.localStorage.setItem(
          'libshipName',
          $scope.shipProfileList[0].shipName ||
            $scope.shipProfileList[0].vesselsName
        );
      }

      $state.go('dapp.userVesselDocumentEBD');
    };

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

    $rootScope.select = function (index) {
      $rootScope.selected = index;
    };

    $scope.thumbnail = {};
    if ($scope.profilePictures && $scope.profilePictures !== 'undefined') {
      $scope.thumbnail.dataUrl = $scope.profilePictures;
    }

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

    FunctionalityService.getDashboardTopCount($scope.userProfileId).then(
      function (response) {
        if (response.data && response.data.shipProfileList.length > 0) {
          var data = response.data.shipProfileList[0];
          $scope.vesselsName = data.shipName || data.vesselsName || '';
          $scope.vesselsCount = data.vesselsCount || data.totalCount || 0;
          $scope.activeCount = data.activeCount || 0;
          $scope.renewalCount = data.renewelCount || 0;
          $scope.expiredCount = data.expiryedCount || 0;
          $scope.missingCount = data.missingCount || 0;
        }
      },
      function (error) {
        console.log('User TopCount Error:', error);
      }
    );

    $scope.openNotification = function (openlink) {
      if (openlink == 'GeoLocationUpload') $state.go('dapp.userDashboard');
      if (openlink == 'New Document ') $state.go('dapp.userDoumentApproval');
      if (openlink == 'Task ') $state.go('dapp.userTasks');
    };

    $scope.openTask = function () {
      $state.go('dapp.userTasks');
    };

    $scope.userDetails = function () {
      $state.go('dapp.userProfile');
      $scope.editShow = true;
      $scope.resetShow = false;
    };

    $scope.editProfile = function () {
      $scope.editShow = true;
      $scope.resetShow = false;
      $state.reload();
    };

    $scope.notificationTypeFilter = function (notificationType) {
      $scope.notificationListFielter = [];
      $scope.notificationListLastweekFielter = [];
      $scope.notificationListLastmonthFielter = [];
      $scope.notificationListOlderFielter = [];

      angular.forEach($scope.notificationList, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListLastweek, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListLastweekFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListLastmonth, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListLastmonthFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListOlder, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListOlderFielter.push(item);
        }
      });
      if (notificationType === '') {
        $scope.notificationListOlderFielter = $scope.notificationListOlder;
        $scope.notificationListLastmonthFielter =
          $scope.notificationListLastmonth;
        $scope.notificationListLastweekFielter =
          $scope.notificationListLastweek;
        $scope.notificationListFielter = $scope.notificationList;
      }
    };

    $scope.snoozeOption = function (snooze, notifId) {
      $scope.loader = true;
      var data = { snooze: snooze, notificationId: notifId };
      FunctionalityService.snoozeUpdate(data).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            $scope.resetShow = false;
            $scope.editShow = true;
            setTimeout(function () {
              $state.reload();
            }, 1000);
          } else {
            toaster.pop('error', response.data.message);
            $scope.resetShow = true;
            $scope.editShow = false;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.updateProfileSubmit = function (data) {
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
        ).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200 || response.status == 201) {
              toaster.pop('success', response.data.message);
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
                'profilePicture',
                $scope.message.profilePicture
              );

              $scope.resetShow = false;
              $scope.editShow = false;
              setTimeout(function () {
                $window.location.reload();
              }, 1000);
            } else {
              toaster.pop('error', response.data.message);
              $scope.editShow = true;
              $scope.resetShow = false;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      }
    };

    $scope.resetPassword = function () {
      $scope.resetShow = true;
      $scope.editShow = false;
    };

    $scope.resetPasswordSubmit = function (data) {
      $scope.loader = true;
      toaster.clear();

      var resetData = {
        userId: $scope.userProfileId,
        currentPassword: data.currentPassword,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };

      FunctionalityService.resetPswd(resetData).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            $scope.resetShow = false;
            $scope.editShow = true;
            setTimeout(function () {
              $state.reload();
            }, 1000);
          } else {
            toaster.pop('error', response.data.message);
            $scope.resetShow = true;
            $scope.editShow = false;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.geoLocationlist(
        $window.localStorage.getItem('userId')
      ).then(
        function (response) {
          if (response.status == 200) {
            $rootScope.geoLocationlistUser = response.data.userList;
          }
        },
        function (error) {}
      );
    });

    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getQuestionAndAnswer().then(
        function (response) {
          if (response.status == 200) {
            $scope.getQuestionAndAnswer = response.data.faqInfos;
          }
        },
        function (error) {}
      );
    });

    function getRandomArbitrary(min, max) {
      return Math.random() * (max - min) + min;
    }

    $scope.getAllVesselListAndCount = function () {
      $scope.loader = true;
      $scope.sessionObject = JSON.parse(
        $window.localStorage.getItem('sessionObject')
      );
      var userId = $scope.sessionObject.userId;
      FunctionalityService.getViewShipProfile(userId).then(
        function mySuccess(response) {
          $scope.loader = false;
          if (response.status == 200) {
            $scope.shipProfileList = response.data.shipProfileList;
            $scope.shipProfileListLength = $scope.shipProfileList.length;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    // $scope.shipMasterSidebar = [
    //   {
    //     id: 1,
    //     url: 'dapp.userDashboard',
    //     color: 'imgclrs clr972323',
    //     status: 'active',
    //     name: 'Dashboard',
    //     imgName: 'dashboardIcon.png',
    //   },
    //   {
    //     id: 2,
    //     url: 'dapp.userVesselDocumentEBD',
    //     color: 'imgclrs clrff8830',
    //     status: 'active',
    //     name: 'Vessel Documents',
    //     imgName: 'vesselDocumentIcon.png',
    //   },
    //   {
    //     id: 3,
    //     url: 'dapp.userDoumentApproval',
    //     color: 'imgclrs clr187e03',
    //     status: 'active',
    //     name: 'Document Approval',
    //     imgName: 'documentApprovalIcon.png',
    //   },
    //   {
    //     id: 4,
    //     url: 'dapp.userMyWorkspaceList',
    //     color: 'imgclrs clr004283',
    //     status: 'active',
    //     name: 'My Workspace',
    //     imgName: 'workspaceIcon.png',
    //   },
    //   {
    //     id: 5,
    //     url: 'dapp.userTasks',
    //     color: 'imgclrs clr8b0ce8',
    //     status: 'active',
    //     name: 'Tasks',
    //     imgName: 'taskIcon.png',
    //   },
    // ];
    $scope.shipMasterSidebar = [
      {
        id: 1,
        url: 'dapp.userDashboard',
        icon: 'fa fa-home',
        status: 'active',
        name: 'Dashboard',
      },
      {
        id: 2,
        url: 'dapp.userVesselDocumentEBD',
        icon: 'fa fa-ship',
        status: 'active',
        name: 'Vessel Documents',
      },
      {
        id: 3,
        url: 'dapp.userDoumentApproval',
        icon: 'fa fa-check-square-o',
        status: 'active',
        name: 'Document Approval',
      },
      {
        id: 4,
        url: 'dapp.userMyWorkspaceList',
        icon: 'fa fa-briefcase',
        status: 'active',
        name: 'My Workspace',
      },
      {
        id: 5,
        url: 'dapp.userTasks',
        icon: 'fa fa-tasks',
        status: 'active',
        name: 'Tasks',
      },
    ];

    $scope.otherUserSidebar = [
      {
        id: 1,
        url: 'dapp.userDashboard',
        icon: 'fa fa-home',
        status: 'active',
        name: 'Dashboard',
      },
      {
        id: 2,
        url: 'dapp.userVesselDocument',
        icon: 'fa fa-ship',
        status: 'active',
        name: 'Vessel Documents',
      },
      {
        id: 3,
        url: 'dapp.userDoumentApproval',
        icon: 'fa fa-check-square-o',
        status: 'active',
        name: 'Document Approval',
      },
      {
        id: 4,
        url: 'dapp.userMyWorkspace',
        icon: 'fa fa-briefcase',
        status: 'active',
        name: 'My Workspace',
      },
      {
        id: 5,
        url: 'dapp.userUserExtension',
        icon: 'fa fa-puzzle-piece',
        status: 'active',
        name: 'User Extension',
      },
      {
        id: 6,
        url: 'dapp.userTasks',
        icon: 'fa fa-tasks',
        status: 'active',
        name: 'Tasks',
      },
    ];

    if ($scope.sessionObject.roleId === 3) {
      $scope.sidebarList = $scope.shipMasterSidebar;
    } else {
      $scope.sidebarList = $scope.otherUserSidebar;
    }
    // Clears dashboard filters when navigating via sidebar
    $scope.clearFilterAndGo = function (stateName) {
      $window.localStorage.removeItem('redirectFilterStatus');
      $state.go(stateName);
    };

    $scope.clearGeoLocation = function () {
      $scope.latitude = '';
      $scope.longitute = '';
    };

    $scope.addGeoLocation = function () {
      $scope.loader = true;
      angular.forEach($scope.sessionObject.shipProfileInfos, function (val) {
        $scope.shipId = val.id;
      });
      var addGeoObject = {
        latitude: $scope.latitude,
        longitute: $scope.longitute,
        userId: $scope.sessionObject.userId,
        shipId: $scope.shipId,
      };
      FunctionalityService.addGeoLocation(addGeoObject).then(
        function mySuccess(response) {
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

    $scope.openEBDState = function (vessel) {
      $window.localStorage.removeItem('libShipId');
      $window.localStorage.removeItem('libshipName');
      $window.localStorage.setItem('libShipId', vessel.id);
      $window.localStorage.setItem('libshipName', vessel.vesselsName);
      $state.go('dapp.userVesselDocumentEBD');
    };

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

    $scope.checkAll = function () {
      if ($scope.selectedAll) {
        $scope.selectedAll = true;
      } else {
        $scope.selectedAll = false;
      }
      $scope.subCheckBox = $scope.selectedAll;
    };

    $scope.deleteAllNotificationBySelect = function () {
      var userDto = { userId: $window.localStorage.getItem('userId') };
      FunctionalityService.deleteAllNotification(userDto).then(
        function mySuccess(response) {
          $state.reload();
          $timeout(function () {
            toaster.success({ title: response.data.status });
          }, 1000);
        },
        function myError(err) {
          toaster.error({ title: myError.data.status });
          console.log('Error response');
        }
      );
    };

    $scope.confirmDeleteAll = function () {
      DeletePopup.confirm(
        'Delete All Notifications',
        'Are you sure you want to delete all notifications?',
        function () {
          $scope.deleteAllNotificationBySelect();
        }
      );
    };

    $scope.confirmDeleteNotification = function (id) {
      $scope.notificationId = id;
      DeletePopup.confirm(
        'Delete Notification',
        'Are you sure you want to delete this notification?',
        function () {
          $timeout(function () {
            $scope.deleteNotificationById();
          });
        }
      );
    };

    $scope.deleteNotificationById = function () {
      var notificationId = {
        notificationId: $scope.notificationId,
      };
      FunctionalityService.deleteNotification(notificationId).then(
        function (response) {
          toaster.success({ title: response.data.status });
          $state.reload();
        },
        function (err) {
          console.log('Error response', err);
        }
      );
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
    // Clears filters and forces a fresh reload of the state
    $scope.clearFilterAndGo = function (stateName) {
      $window.localStorage.removeItem('redirectFilterStatus');
      $state.go(stateName, {}, { reload: true });
    };
  },
]);
