// var manageSubScription = angular.module(
//   'dapp.saSubscrptionManageAdminController',
//   ['angularUtils.directives.dirPagination', 'toaster']
// );
// manageSubScription.controller('saSubscrptionManageAdminController', [
//   '$scope',
//   '$stateParams',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   '$timeout',
//   'toaster',
//   'FunctionalityService',
//   function (
//     $scope,
//     $stateParams,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     $timeout,
//     toaster,
//     FunctionalityService
//   ) {
//     $scope.currentPage = 1;
//     $scope.viewby = 10;
//     $scope.itemsPerPage = $scope.viewby;
//     $scope.allSubscriptionAdminList = [];
//     $scope.loader = false;

//     // $scope.userId= $stateParams.userId;
//     $scope.subscriptionAdminList = function () {
//       $scope.loader = true;

//       FunctionalityService.subscriptionAdminList(
//         $stateParams.organizationId
//       ).then(
//         function mySuccess(response) {
//           $scope.loader = false;

//           if (response.status == 201 || response.status == 200) {
//             //$scope.allSubscriptionAdminList = JSON.stringify(response.data.adminInfos);
//             $scope.allSubscriptionAdminList = response.data.adminInfos;
//             if ($scope.allSubscriptionAdminList == undefined) {
//               toaster.clear();
//               toaster.info('info', 'No records found');
//             }
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.checkPassword = function (password, confirmPassword) {
//       if (password != confirmPassword) {
//         $scope.IsMatch = true;
//         $scope.isDisabled = true;
//       } else if (password == confirmPassword) {
//         $scope.IsMatch = false;
//         $scope.newpassword = password;
//         $scope.isDisabled = false;
//       }
//     };

//     $scope.changeStatusUserLists = function (status) {
//       $scope.loader = true;

//       var data = status.userId;
//       FunctionalityService.getchangeStatusOfUser(data).then(
//         function mySuccess(response) {
//           if (response.status == 200 || response.status == 201) {
//             $scope.loader = false;

//             toaster.clear();
//             toaster.success({ title: response.data.message });
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.userInfo = [];
//     $scope.clearCreateAdmin = function () {
//       $scope.user = [];
//     };

//     $scope.createOrganizationAdmin = function (user) {
//       $scope.loader = true;
//       if (user.password != user.confirmPassword) {
//         $scope.loader = false;
//         toaster.pop('error', 'Password does not match');
//       } else {
//         var userdata = {
//           organizationId: $stateParams.organizationId,
//           userId: $stateParams.userId,
//           userInfo: $scope.user,
//         };
//         FunctionalityService.addSubAdmin(userdata).then(
//           function mySuccess(response) {
//             $scope.loader = false;

//             if (response.status == 200 || response.status == 201) {
//               $('#addNewUser').modal('hide');
//               $timeout(function () {
//                 toaster.clear();
//                 toaster.success({ title: response.data.message });
//               }, 300);
//               $state.reload();
//             } else {
//               $('#addNewUser').modal('hide');
//               toaster.clear();
//               toaster.error({ title: response.data.message });
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       }
//     };
//     $scope.editAdminDetail = [];
//     $scope.editAdmin = function (admin) {
//       $scope.editAdminDetail = admin;
//     };
//     $scope.resPass = [];
//     $scope.passowrdret = function (admin) {
//       $scope.resPass = admin;
//     };

//     $scope.updateAdmin = function () {
//       $scope.loader = true;
//       var userdata = {
//         userId: $scope.editAdminDetail.userId,
//         firstName: $scope.editAdminDetail.firstName,
//         lastName: $scope.editAdminDetail.lastName,
//         mail: $scope.editAdminDetail.mail,
//         userName: $scope.editAdminDetail.userName,
//       };
//       FunctionalityService.editProfileData(userdata).then(
//         function mySuccess(response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#manageAdminEdit').modal('hide');
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//             }, 300);
//             $state.reload();
//           } else {
//             $('#manageAdminEdit').modal('hide');
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.delete;
//     $scope.deleteSubAdminId = function (admin) {
//       $scope.delete = admin.userId;
//     };

//     $scope.deleteSubAdmin = function () {
//       $scope.loader = true;

//       var deleteOrganizationdata = {
//         superAdminId: $stateParams.userId,
//         adminId: $scope.delete,
//       };
//       FunctionalityService.deleteAdmin(deleteOrganizationdata).then(
//         function mySuccess(response) {
//           $scope.loader = false;
//           if (response.status == 201 || response.status == 200) {
//             $('#delete').modal('hide');
//             $state.reload();
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//             }, 1000);
//           } else {
//             $('#delete').modal('hide');
//             $state.reload();
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.clearResetPassword = function () {
//       $scope.resPass.password = '';
//       $scope.resPass.confirmPassword = '';
//     };

//     $scope.resetPasswordAdmin = function (resPass) {
//       $scope.loader = true;
//       if (resPass.password != resPass.confirmPassword) {
//         $scope.loader = false;
//         toaster.pop('error', 'Password does not match');
//       } else {
//         var deleteOrganizationdata = {
//           adminId: $stateParams.userId,
//           userId: $scope.resPass.userId,
//           password: $scope.resPass.password,
//         };
//         FunctionalityService.resetPasswordAdmin(deleteOrganizationdata).then(
//           function mySuccess(response) {
//             $scope.loader = false;

//             if (response.status == 201 || response.status == 200) {
//               $('#resetPassword').modal('hide');
//               $state.reload();
//               $timeout(function () {
//                 toaster.clear();
//                 toaster.success({ title: response.data.message });
//               }, 1000);
//             } else {
//               $('#resetPassword').modal('hide');
//               $state.reload();
//               toaster.clear();
//               toaster.error({ title: response.data.message });
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       }
//     };

//     $scope.resetSubAdminEdit = function () {
//       $scope.edit.$setPristine();
//       $scope.editAdminDetail.firstName = '';
//       $scope.editAdminDetail.lastName = '';
//       $scope.editAdminDetail.mail = '';
//     };

//     $scope.closeadminEdit = function () {
//       $('#manageAdminEdit').modal('hide');
//       $state.reload();
//     };

//     $scope.closeResetPassword = function () {
//       $('#resetPassword').modal('hide');
//       $state.reload();
//     };

//     $scope.clsoeManageAdminaddnew = function () {
//       $('#manageAdminaddnew').modal('hide');
//       $state.reload();
//     };
//   },
// ]);

var manageSubScription = angular.module(
  'dapp.saSubscrptionManageAdminController',
  ['angularUtils.directives.dirPagination', 'toaster']
);
manageSubScription.controller('saSubscrptionManageAdminController', [
  '$scope',
  '$stateParams',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  '$timeout',
  'toaster',
  'FunctionalityService',
  'DeletePopup',

  function (
    $scope,
    $stateParams,
    $window,
    $location,
    $state,
    $rootScope,
    $timeout,
    toaster,
    FunctionalityService,
    DeletePopup
  ) {
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.allSubscriptionAdminList = [];
    $scope.loader = false;

    // $scope.userId= $stateParams.userId;
    $scope.subscriptionAdminList = function () {
      $scope.loader = true;

      FunctionalityService.subscriptionAdminList(
        $stateParams.organizationId
      ).then(
        function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            //$scope.allSubscriptionAdminList = JSON.stringify(response.data.adminInfos);
            $scope.allSubscriptionAdminList = response.data.adminInfos;
            if ($scope.allSubscriptionAdminList == undefined) {
              toaster.clear();
              toaster.info('info', 'No records found');
            }
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.checkPassword = function (password, confirmPassword) {
      if (password != confirmPassword) {
        $scope.IsMatch = true;
        $scope.isDisabled = true;
      } else if (password == confirmPassword) {
        $scope.IsMatch = false;
        $scope.newpassword = password;
        $scope.isDisabled = false;
      }
    };

    $scope.changeStatusUserLists = function (status) {
      $scope.loader = true;

      var data = status.userId;
      FunctionalityService.getchangeStatusOfUser(data).then(
        function mySuccess(response) {
          if (response.status == 200 || response.status == 201) {
            $scope.loader = false;

            toaster.clear();
            toaster.success({ title: response.data.message });
          } else {
            toaster.clear();
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.userInfo = [];
    $scope.clearCreateAdmin = function () {
      $scope.user = [];
    };

    $scope.createOrganizationAdmin = function (user) {
      $scope.loader = true;
      if (user.password != user.confirmPassword) {
        $scope.loader = false;
        toaster.pop('error', 'Password does not match');
      } else {
        var userdata = {
          organizationId: $stateParams.organizationId,
          userId: $stateParams.userId,
          userInfo: $scope.user,
        };
        FunctionalityService.addSubAdmin(userdata).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 200 || response.status == 201) {
              $('#addNewUser').modal('hide');
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 300);
              $state.reload();
            } else {
              $('#addNewUser').modal('hide');
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      }
    };
    $scope.editAdminDetail = [];
    $scope.editAdmin = function (admin) {
      $scope.editAdminDetail = admin;
    };
    $scope.resPass = [];
    $scope.passowrdret = function (admin) {
      $scope.resPass = admin;
    };

    $scope.updateAdmin = function () {
      $scope.loader = true;
      var userdata = {
        userId: $scope.editAdminDetail.userId,
        firstName: $scope.editAdminDetail.firstName,
        lastName: $scope.editAdminDetail.lastName,
        mail: $scope.editAdminDetail.mail,
        userName: $scope.editAdminDetail.userName,
      };
      FunctionalityService.editProfileData(userdata).then(
        function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $('#manageAdminEdit').modal('hide');
            $timeout(function () {
              toaster.clear();
              toaster.success({ title: response.data.message });
            }, 300);
            $state.reload();
          } else {
            $('#manageAdminEdit').modal('hide');
            toaster.clear();
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.delete;
    $scope.deleteSubAdminId = function (admin) {
      console.log('Delete clicked:', admin);
      $scope.delete = admin.userId;
      DeletePopup.confirm(
        'Delete Admin',
        'Are you sure you want to delete this admin?',
        function () {
          $scope.$apply(function () {
            $scope.deleteSubAdmin();
          });
        }
      );
    };

    $scope.deleteSubAdmin = function () {
      $scope.loader = true;

      var deleteOrganizationdata = {
        superAdminId: $stateParams.userId,
        adminId: $scope.delete,
      };
      FunctionalityService.deleteAdmin(deleteOrganizationdata).then(
        function mySuccess(response) {
          $scope.loader = false;
          if (response.status == 201 || response.status == 200) {
            $('#delete').modal('hide');
            $state.reload();
            $timeout(function () {
              toaster.clear();
              toaster.success({ title: response.data.message });
            }, 1000);
          } else {
            $('#delete').modal('hide');
            $state.reload();
            toaster.clear();
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.clearResetPassword = function () {
      $scope.resPass.password = '';
      $scope.resPass.confirmPassword = '';
    };

    $scope.resetPasswordAdmin = function (resPass) {
      $scope.loader = true;
      if (resPass.password != resPass.confirmPassword) {
        $scope.loader = false;
        toaster.pop('error', 'Password does not match');
      } else {
        var deleteOrganizationdata = {
          adminId: $stateParams.userId,
          userId: $scope.resPass.userId,
          password: $scope.resPass.password,
        };
        FunctionalityService.resetPasswordAdmin(deleteOrganizationdata).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $('#resetPassword').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 1000);
            } else {
              $('#resetPassword').modal('hide');
              $state.reload();
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      }
    };

    $scope.resetSubAdminEdit = function () {
      $scope.edit.$setPristine();
      $scope.editAdminDetail.firstName = '';
      $scope.editAdminDetail.lastName = '';
      $scope.editAdminDetail.mail = '';
    };

    $scope.closeadminEdit = function () {
      $('#manageAdminEdit').modal('hide');
      $state.reload();
    };

    $scope.closeResetPassword = function () {
      $('#resetPassword').modal('hide');
      $state.reload();
    };

    $scope.clsoeManageAdminaddnew = function () {
      $('#manageAdminaddnew').modal('hide');
      $state.reload();
    };
  },
]);
