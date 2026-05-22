// var superAdminDashboard = angular.module('dapp.superAdminDashboardController', ['angularUtils.directives.dirPagination']);
// superAdminDashboard.directive("fileInput", ['$parse', function ($parse) {
//   return {
//     restrict: 'A',
//     link: function (scope, ele, attrs) {
//       ele.bind('change', function () {
//         $parse(attrs.fileInput).
//           assign(scope, ele[0].files)
//         scope.$apply()
//       });
//     }
//   }
// }]);
// superAdminDashboard.controller('superAdminDashboardController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster', function ($scope, $window, $location, $state, $rootScope, FunctionalityService, toaster) {
//   $scope.currentPage = 1;
//   $scope.viewby = 10;
//   $scope.itemsPerPage = $scope.viewby;

//     $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));
//     $scope.userProfileId = $window.localStorage.getItem('userId');
//     $scope.profilePictures = $window.localStorage.getItem('profilePicture');
//     $scope.editShow = true;
//     $scope.resetShow = false;
//     $scope.profilePicture;
//     $scope.loader= false;

//     $scope.expandSubMenu = function () {
//       $rootScope.subConfigMenuActive = false;
//       if ($rootScope.subMenuActive == true) {
//           $rootScope.subMenuActive = false;
//       }
//       else {
//           $rootScope.subMenuActive = true;
//           $rootScope.subConfigMenuActive = false
//       }

//   }

//   $scope.expandConfigSubMenu = function () {
//     $rootScope.subMenuActive = false;
//       if ($rootScope.subConfigMenuActive == true) {
//           $rootScope.subConfigMenuActive = false;
//       }
//       else {
//           $rootScope.subConfigMenuActive = true;
//           $rootScope.subMenuActive = false;
//       }

//   }
//   $scope.liDashboardClick=function(){
//     $rootScope.subConfigMenuActive = false;
//     $rootScope.subMenuActive = false;
//   }

//   $scope.liAduitTrailClick=function(){
//     $rootScope.subConfigMenuActive = false;
//     $rootScope.subMenuActive = false;
//   }
//   $scope.deactiveConfigMenu=function(){
//     $rootScope.subConfigMenuActive = false;
//    // $rootScope.subMenuActive = false;
//   }

//   $scope.deactivesubMenuActive=function(){
//    // $rootScope.subConfigMenuActive = false;
//     $rootScope.subMenuActive = false;
//   }

//     $scope.thumbnail = {
//       dataUrl: ''
//    };
//    if($scope.profilePictures != "undefined"){
//      $scope.thumbnail.dataUrl = $scope.profilePictures;

//    }
//  else{
//    $scope.thumbnail = {
//      dataUrl: "undefined"
//   };

//  }
//    $scope.thumbnail.dataUrl = $scope.profilePictures;

//    $scope.fileReaderSupported = window.FileReader != null;
//    $scope.uploadFiledp = function (files) {
//      if (files != null) {
//        var file = files[0];
//        if (files[0].size > 2048000) {
//          $rootScope.errorFile = document.getElementById('sizeOffile').innerHTML = "Picture should be below 1MB";
//        }
//        else {
//          document.getElementById('sizeOffile').innerHTML = "";
//          $rootScope.errorFile = "";
//        }
//        $scope.profilePicture = file;
//        if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
//          setTimeout(function () {
//            var fileReader = new FileReader();
//            fileReader.readAsDataURL(file);
//            fileReader.onload = function (e) {
//              setTimeout(function () {
//                $scope.thumbnail.dataUrl = e.target.result;
//                $scope.profilePictures = $scope.thumbnail.dataUrl;
//              });
//            }
//          });
//        }
//      }

//    };

//   $scope.userDetails = function () {
//     $state.go('dapp.saProfile');
//     $scope.editShow = true;
//     $scope.resetShow = false;
//   }

//   $scope.editProfile = function () {
//     $scope.editShow = true;
//     $scope.resetShow = false;
//     $state.reload();

//   }

//   $scope.editProfileSubmit = function (data) {
//     $scope.loader= true;

//     toaster.clear();
//     if ($rootScope.errorFile === "Picture should be below 1MB") {
//       $scope.loader= false;
//       toaster.pop('error', $rootScope.errorFile);

//     }
//     else {
//       $rootScope.errorFile = '';
//     $scope.editShow = true;
//     var data = {"userId": $scope.userProfileId,"firstName": data.firstName,"lastName": data.lastName,"mail": data.mail}

//     FunctionalityService.editProfileData(data, $scope.profilePicture).then(function (response) {
//       $scope.loader= false;

//       if (response.status == 200 || response.status == 201) {
//         toaster.pop("success", response.data.message);
//         $scope.message = response.data.userInfos;

//         $window.localStorage.setItem('sessionObject',JSON.stringify($scope.message));
//         $window.localStorage.setItem('userRole',$scope.message.businessCategory);
//         $window.localStorage.setItem('userName',$scope.message.userName);
//         $window.localStorage.setItem('userEmail',$scope.message.mail);
//         $window.localStorage.setItem('userId',$scope.message.userId);
//         $window.localStorage.setItem('roleId',$scope.message.roleId);
//         $window.localStorage.setItem('role',$scope.message.role);
//         $window.localStorage.setItem('organizationId',$scope.message.organizationId);
//         $window.localStorage.setItem('profilePicture',$scope.message.profilePicture);

//         $scope.resetShow = false;
//         $scope.editShow = true;
//         setTimeout(function(){
//        $window.location.reload();
//         }, 1000)       }
//       else {
//         toaster.pop("error", response.data.message);
//         $scope.editShow = true;
//         $scope.resetShow = false;
//       }
//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });

//   }}
//   $scope.resetPassword = function () {
//     $scope.resetShow = true;
//     $scope.editShow = false;
//   }
//   $scope.resetPasswordSubmit = function (data) {
//     $scope.loader= true;

//     toaster.clear();
//     var data =
//       {
//         "userId": $scope.userProfileId,
//         "currentPassword": data.currentPassword,
//         "password": data.password,
//         "confirmPassword": data.confirmPassword
//       }

//     FunctionalityService.resetPswd(data).then(function (response) {
//       $scope.loader= false;

//       if (response.status == 200 || response.status == 201) {
//         toaster.pop("success", response.data.message);
//         $scope.resetShow = false;
//         $scope.editShow = true;
//         setTimeout(function () {
//           $state.reload();
//         }, 1000);
//       }
//       else {
//         toaster.pop("error", response.data.message);
//         $scope.resetShow = true;
//         $scope.editShow = false;

//       }
//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });
//   }

//   $scope.expandSubMenu = function () {
//     if ($rootScope.subMenuActive == true) {
//         $rootScope.subMenuActive = false;
//     }
//     else {
//         $rootScope.subMenuActive = true;
//     }

// }

// $scope.expandConfigSubMenu = function () {
//     if ($rootScope.subConfigMenuActive == true) {
//         $rootScope.subConfigMenuActive = false;
//     }
//     else {
//         $rootScope.subConfigMenuActive = true;
//     }

// }

// }]);
var superAdminDashboard = angular.module('dapp.superAdminDashboardController', [
  'angularUtils.directives.dirPagination',
]);

superAdminDashboard.directive('fileInput', [
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

superAdminDashboard.controller('superAdminDashboardController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  'toaster',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    toaster
  ) {
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;

    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );
    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.profilePictures = $window.localStorage.getItem('profilePicture');
    $scope.editShow = true;
    $scope.resetShow = false;
    $scope.profilePicture = null;
    $scope.loader = false;

    $scope.expandSubMenu = function () {
      $rootScope.subConfigMenuActive = false;
      if ($rootScope.subMenuActive == true) {
        $rootScope.subMenuActive = false;
      } else {
        $rootScope.subMenuActive = true;
        $rootScope.subConfigMenuActive = false;
      }
    };

    $scope.expandConfigSubMenu = function () {
      $rootScope.subMenuActive = false;
      if ($rootScope.subConfigMenuActive == true) {
        $rootScope.subConfigMenuActive = false;
      } else {
        $rootScope.subConfigMenuActive = true;
        $rootScope.subMenuActive = false;
      }
    };

    $scope.liDashboardClick = function () {
      $rootScope.subConfigMenuActive = false;
      $rootScope.subMenuActive = false;
    };

    $scope.liAduitTrailClick = function () {
      $rootScope.subConfigMenuActive = false;
      $rootScope.subMenuActive = false;
    };

    $scope.deactiveConfigMenu = function () {
      $rootScope.subConfigMenuActive = false;
    };

    $scope.deactivesubMenuActive = function () {
      $rootScope.subMenuActive = false;
    };

    // =========================================================================
    // PROFILE IMAGE UPLOAD & PREVIEW LOGIC
    // =========================================================================
    $scope.thumbnail = {};

    $scope.uploadFiledp = function (files) {
      if (files && files.length > 0) {
        var file = files[0];
        var errorSpan = document.getElementById('sizeOffile');

        // Validate File Size (Max 1MB)
        if (file.size > 1048576) {
          if (errorSpan) errorSpan.innerText = 'Picture should be below 1MB';
          $rootScope.errorFile = 'Picture should be below 1MB';
          return;
        } else {
          if (errorSpan) errorSpan.innerText = '';
          $rootScope.errorFile = '';
        }

        // Read the file to create a base64 Data URL for the live preview
        var reader = new FileReader();
        reader.onload = function (e) {
          $scope.$apply(function () {
            // Crucial: Tells Angular to update the UI
            if (!$scope.thumbnail) {
              $scope.thumbnail = {};
            }
            $scope.thumbnail.dataUrl = e.target.result;
            $scope.profilePicture = file; // Store actual file for the update API call
          });
        };
        reader.readAsDataURL(file);
      }
    };
    // =========================================================================

    $scope.userDetails = function () {
      $state.go('dapp.saProfile');
      $scope.editShow = true;
      $scope.resetShow = false;
    };

    $scope.editProfile = function () {
      $scope.editShow = true;
      $scope.resetShow = false;
      $state.reload();
    };

    $scope.editProfileSubmit = function (data) {
      $scope.loader = true;
      toaster.clear();

      if ($rootScope.errorFile === 'Picture should be below 1MB') {
        $scope.loader = false;
        toaster.pop('error', $rootScope.errorFile);
      } else {
        $rootScope.errorFile = '';
        $scope.editShow = true;
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
              $scope.editShow = true;
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
  },
]);
