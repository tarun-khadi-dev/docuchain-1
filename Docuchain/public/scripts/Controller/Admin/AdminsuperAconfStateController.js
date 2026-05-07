var adminDashboard = angular.module('dapp.AdminsuperAconfStateController', [
  'angularUtils.directives.dirPagination',
]);

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
adminDashboard.controller('AdminsuperAconfStateController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  '$timeout',
  'toaster',
  'FunctionalityService',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    $timeout,
    toaster,
    FunctionalityService
  ) {
    $scope.userProfileId = $window.localStorage.getItem('userId');

    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.role = {};
    $scope.loader = false;
    $scope.addLogoImage;
    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getListVesselType($scope.userProfileId).then(
        function (response) {
          if (response.status == 200) {
            $scope.vesselsTypeList = response.data.vesselsTypeInfos;
          }
        },
        function (error) {
          console.log('message :: ' + error);
        }
      );
    });

    $scope.setPage = function (pageNo) {
      $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
      console.log('Page changed to: ' + $scope.currentPage);
    };

    $scope.setItemsPerPage = function (num) {
      $scope.itemsPerPage = num;
      $scope.currentPage = 1; //reset to first page
    };

    $scope.addVessels = function (role) {
      $scope.loader = true;

      var addRoleAlais = {
        vesselsTypeName: role.aliasName,
        userId: $scope.userProfileId,
      };
      FunctionalityService.addRolesName(addRoleAlais).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#addVesselTypes').modal('hide');
            $timeout(function () {
              toaster.pop('success', response.data.message);
            }, 300);
            $state.reload();
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.closeOrCancel = function () {
      $scope.role.aliasName = '';
    };

    $scope.editVesselspopup = function (vessel) {
      $scope.role.editVesselname = vessel.vesselsTypeName;
      $scope.role.editRoleId = vessel.vesselsTypeId;
    };

    $scope.editVessels = function (role) {
      $scope.loader = true;

      var addRoleAlais = {
        vesselsTypeId: $scope.role.editRoleId,
        vesselsTypeName: $scope.role.editVesselname,
        userId: $scope.userProfileId,
      };
      FunctionalityService.updateVessels(addRoleAlais).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#editVesselTypes').modal('hide');
            $timeout(function () {
              toaster.pop('success', response.data.message);
            }, 300);
            $state.reload();
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.setItemsPerPage = function (num) {
      $scope.itemsPerPage = num;
      $scope.currentPage = 1; //reset to first page
    };

    $scope.thumbnail = {
      // dataUrl: 'adsfas'
    };
    $scope.fileReaderSupported = window.FileReader != null;
    $scope.uploadFile = function (files) {
      if (files != null) {
        var file = files[0];

        if (files[0].size > 2048000) {
          $rootScope.errorFile = document.getElementById(
            'sizeOffile'
          ).innerHTML = 'Picture should be below 1MB';
        } else {
          document.getElementById('sizeOffile').innerHTML = '';
          $rootScope.errorFile = '';
        }

        $scope.addLogoPic = file;

        if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
          $timeout(function () {
            var fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function (e) {
              $timeout(function () {
                $scope.thumbnail.dataUrl = e.target.result;
                $scope.addLogoImage = $scope.thumbnail.dataUrl;
                console.log(
                  '$scope.userData.shipProfilePicPath' + $scope.addLogoImage
                );
              });
            };
          });
        }
      }
    };
    // Find this line in your controller:
    $scope.thumbnail = {};

    // ADD THESE LINES RIGHT AFTER:
    // Fetch logo from localStorage on initial load
    var savedLogo = $window.localStorage.getItem('logoPicture');

    // Check if a logo exists and isn't a string literal of 'undefined' or 'null'
    if (savedLogo && savedLogo !== 'undefined' && savedLogo !== 'null') {
      $scope.thumbnail.dataUrl = savedLogo;
    }

    $scope.fileReaderSupported = window.FileReader != null;
    // ... rest of your code remains the same
    $scope.addLogoFunction = function () {
      $scope.loader = true;
      FunctionalityService.changeLogo(
        $scope.addLogoPic,
        $scope.userProfileId
      ).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            var newLogo = response.data.organizationInfo.logoPicture;
            var cacheBuster = '?t=' + new Date().getTime();
            var updatedLogoUrl = newLogo + cacheBuster;

            // 1. Save to local storage
            $window.localStorage.setItem('logoPicture', updatedLogoUrl);

            // 2. Update the preview on the Add Logo page
            $scope.thumbnail.dataUrl = updatedLogoUrl;

            // 3. SHOUT TO THE REST OF THE APP THAT THE LOGO CHANGED
            $rootScope.$broadcast('logoUpdated', updatedLogoUrl);

            // 4. Show success message
            toaster.pop('success', 'Company logo added successfully!');
          } else {
            toaster.pop('error', response.data.message);
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    // $scope.addLogoFunction = function () {
    //   $scope.loader = true;
    //   FunctionalityService.changeLogo(
    //     $scope.addLogoPic,
    //     $scope.userProfileId
    //   ).then(
    //     function (response) {
    //       $scope.loader = false;
    //       if (response.status == 200 || response.status == 201) {
    //         //$state.reload();
    //         //  $timeout(function () {
    //         //  toaster.clear();
    //         //toaster.pop("success", response.data.message);
    //         // }, 4000);

    //         // $window.location.reload();
    //         //$window.localStorage.setItem('success',response.data.message);
    //         $window.localStorage.setItem(
    //           'logoPicture',
    //           response.data.organizationInfo.logoPicture
    //         );
    //         $window.location.reload();
    //         toaster.pop('success', 'Company logo added successfully!', 5000);
    //       } else {
    //         toaster.pop('error', response.data.message);
    //       }
    //     },
    //     function myError(err) {
    //       $scope.loader = false;
    //       console.log('Error response', err);
    //     }
    //   );
    // };
  },
]);
