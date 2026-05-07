// var companyProfile = angular.module('dapp.SaCompanyProfileController', [
//   'angularUtils.directives.dirPagination',
//   'toaster',
//   'moment-picker',
//   '720kb.tooltips',
// ]);

// companyProfile.controller('SaCompanyProfileController', [
//   '$scope',
//   '$stateParams',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   '$timeout',
//   'toaster',
//   'FunctionalityService',
//   'DeletePopup',
//   function (
//     $scope,
//     $stateParams,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     $timeout,
//     toaster,
//     FunctionalityService,
//     DeletePopup
//   ) {
//     // $scope.tabName = tab1;
//     $scope.currentPage = 1;
//     $scope.viewby = 10;
//     $scope.itemsPerPage = $scope.viewby;
//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );

//     $scope.allOrganizationList;
//     $scope.loader = false;

//     $scope.expandSubMenu = function () {
//       if ($rootScope.subMenuActive == true) {
//         $rootScope.subMenuActive = false;
//       } else {
//         $rootScope.subMenuActive = true;
//         $rootScope.subConfigMenuActive = false;
//       }
//     };

//     $scope.expandConfigSubMenu = function () {
//       if ($rootScope.subConfigMenuActive == true) {
//         $rootScope.subConfigMenuActive = false;
//       } else {
//         $rootScope.subConfigMenuActive = true;
//         $rootScope.subMenuActive = false;
//       }
//     };

//     $scope.organizationList = function () {
//       $scope.loader = true;

//       FunctionalityService.getOrganizationList(
//         $scope.sessionObject.userId
//       ).then(
//         function mySuccess(response) {
//           $scope.loader = false;

//           if (response.status == 201 || response.status == 200) {
//             $scope.allOrganizationList = JSON.stringify(
//               response.data.organizationInfos
//             );
//             $scope.allOrganizationList = response.data.organizationInfos;
//             if ($scope.allOrganizationList == undefined) {
//               toaster.pop('info', 'No records found');
//             }
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.changeStatusOrganization = function (organization) {
//       $scope.loader = true;

//       $scope.status;
//       if (organization.isStatusActive == 1) {
//         $scope.status = 0;
//       } else {
//         $scope.status = 1;
//       }
//       var changeStatusOrganization = {
//         organizationId: organization.organizationId,
//         userId: $scope.sessionObject.userId,
//         isStatusActive: $scope.status,
//       };
//       FunctionalityService.changeStatusOrganization(
//         changeStatusOrganization
//       ).then(
//         function mySuccess(response) {
//           $scope.loader = false;

//           if (response.status == 201 || response.status == 200) {
//             toaster.clear();
//             toaster.success({ title: response.data.message });
//             $timeout(function () {
//               $state.reload();
//             }, 5000);
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
//     $scope.changeOrgDualApprovel = function (organization) {
//       $scope.loader = true;

//       $scope.isDualApprovalstatus;
//       if (organization.isDualApprovalActive == 1) {
//         $scope.isDualApprovalstatus = 0;
//       } else {
//         $scope.isDualApprovalstatus = 1;
//       }
//       var changeStatusOrganization = {
//         organizationId: organization.organizationId,
//         userId: $scope.sessionObject.userId,
//         isDualApprovalActive: $scope.isDualApprovalstatus,
//       };
//       FunctionalityService.changeOrgDualApprovel(changeStatusOrganization).then(
//         function mySuccess(response) {
//           $scope.loader = false;

//           if (response.status == 201 || response.status == 200) {
//             toaster.clear();
//             toaster.success({ title: response.data.message });
//             $timeout(function () {
//               $state.reload();
//             }, 2000);
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

//     $scope.changeSaveInBlockchain = function (organization) {
//       $scope.loader = true;

//       var changeStatusOrganization = {
//         organizationId: organization.organizationId,
//         userId: $scope.sessionObject.userId,
//         isSaveInBlockchain: organization.isSaveInBlockchain == 1 ? 0 : 1,
//       };

//       FunctionalityService.changeSaveInBlockchain(
//         changeStatusOrganization
//       ).then(
//         function mySuccess(response) {
//           $scope.loader = false;
//           toaster.clear();
//           if (response.status == 201 || response.status == 200) {
//             toaster.success({
//               title: response.data.message,
//             });
//             $timeout(function () {
//               $state.reload();
//             }, 2000);
//           } else {
//             toaster.error({
//               title: response.data.message,
//             });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//           $state.reload();
//         }
//       );
//     };

//     $scope.confirmDeleteOrganization = function (orgId) {
//       DeletePopup.confirm(
//         'Delete Organization',
//         'Are you sure you want to delete this organization?',
//         function () {
//           $scope.deleteOrganization(orgId);
//         }
//       );
//     };

//     $scope.updateOrganizationId;
//     $scope.getUpdateId = function (organization) {
//       $scope.updateOrganizationId = organization.organizationId;
//       $state.go('dapp.saCompanyProfileupdate', {
//         userId: $scope.sessionObject.userId,
//         organizationId: organization.organizationId,
//       });
//     };
//     $scope.viewOrganization;
//     $scope.viewOrganizationById = function (organization) {
//       $scope.viewOrganization = organization.organizationId;
//       $state.go('dapp.saCompanyProfileView', {
//         organizationId: organization.organizationId,
//       });
//     };

//     $scope.deleteOrganization = function (orgId) {
//       $scope.loader = true;
//       var data = {
//         userId: $scope.sessionObject.userId,
//         organizationId: orgId,
//       };
//       FunctionalityService.deleteOrganization(data).then(function (response) {
//         $scope.loader = false;
//         if (response.status == 201 || response.status == 200) {
//           toaster.success({ title: response.data.message });
//           setTimeout(function () {
//             $state.reload();
//           }, 1000);
//         } else {
//           toaster.error({ title: response.data.message });
//         }
//       });
//     };
//   },
// ]);

var companyProfile = angular.module('dapp.SaCompanyProfileController', [
  'angularUtils.directives.dirPagination',
  'toaster',
  'moment-picker',
  '720kb.tooltips',
]);

companyProfile.controller('SaCompanyProfileController', [
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
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.allOrganizationList;
    $scope.loader = false;

    $scope.expandSubMenu = function () {
      if ($rootScope.subMenuActive == true) {
        $rootScope.subMenuActive = false;
      } else {
        $rootScope.subMenuActive = true;
        $rootScope.subConfigMenuActive = false;
      }
    };

    $scope.expandConfigSubMenu = function () {
      if ($rootScope.subConfigMenuActive == true) {
        $rootScope.subConfigMenuActive = false;
      } else {
        $rootScope.subConfigMenuActive = true;
        $rootScope.subMenuActive = false;
      }
    };

    $scope.organizationList = function () {
      $scope.loader = true;

      FunctionalityService.getOrganizationList(
        $scope.sessionObject.userId
      ).then(
        function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            $scope.allOrganizationList = JSON.stringify(
              response.data.organizationInfos
            );
            $scope.allOrganizationList = response.data.organizationInfos;
            if ($scope.allOrganizationList == undefined) {
              toaster.pop('info', 'No records found');
            }
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.changeStatusOrganization = function (organization) {
      $scope.loader = true;

      $scope.status;
      if (organization.isStatusActive == 1) {
        $scope.status = 0;
      } else {
        $scope.status = 1;
      }
      var changeStatusOrganization = {
        organizationId: organization.organizationId,
        userId: $scope.sessionObject.userId,
        isStatusActive: $scope.status,
      };
      FunctionalityService.changeStatusOrganization(
        changeStatusOrganization
      ).then(
        function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            toaster.clear();
            toaster.success({ title: response.data.message });
            $timeout(function () {
              $state.reload();
            }, 5000); // Wait 5 seconds
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

    $scope.changeOrgDualApprovel = function (organization) {
      $scope.loader = true;

      $scope.isDualApprovalstatus;
      if (organization.isDualApprovalActive == 1) {
        $scope.isDualApprovalstatus = 0;
      } else {
        $scope.isDualApprovalstatus = 1;
      }
      var changeStatusOrganization = {
        organizationId: organization.organizationId,
        userId: $scope.sessionObject.userId,
        isDualApprovalActive: $scope.isDualApprovalstatus,
      };
      FunctionalityService.changeOrgDualApprovel(changeStatusOrganization).then(
        function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            toaster.clear();
            toaster.success({ title: response.data.message });
            $timeout(function () {
              $state.reload();
            }, 5000); // Wait 5 seconds
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

    $scope.changeSaveInBlockchain = function (organization) {
      $scope.loader = true;

      var changeStatusOrganization = {
        organizationId: organization.organizationId,
        userId: $scope.sessionObject.userId,
        isSaveInBlockchain: organization.isSaveInBlockchain == 1 ? 0 : 1,
      };

      FunctionalityService.changeSaveInBlockchain(
        changeStatusOrganization
      ).then(
        function mySuccess(response) {
          $scope.loader = false;
          toaster.clear();
          if (response.status == 201 || response.status == 200) {
            toaster.success({
              title: response.data.message,
            });
            $timeout(function () {
              $state.reload();
            }, 5000); // Wait 5 seconds
          } else {
            toaster.error({
              title: response.data.message,
            });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
          $state.reload();
        }
      );
    };

    $scope.confirmDeleteOrganization = function (orgId) {
      DeletePopup.confirm(
        'Delete Organization',
        'Are you sure you want to delete this organization?',
        function () {
          $scope.deleteOrganization(orgId);
        }
      );
    };

    $scope.updateOrganizationId;
    $scope.getUpdateId = function (organization) {
      $scope.updateOrganizationId = organization.organizationId;
      $state.go('dapp.saCompanyProfileupdate', {
        userId: $scope.sessionObject.userId,
        organizationId: organization.organizationId,
      });
    };
    $scope.viewOrganization;
    $scope.viewOrganizationById = function (organization) {
      $scope.viewOrganization = organization.organizationId;
      $state.go('dapp.saCompanyProfileView', {
        organizationId: organization.organizationId,
      });
    };

    $scope.deleteOrganization = function (orgId) {
      $scope.loader = true;
      var data = {
        userId: $scope.sessionObject.userId,
        organizationId: orgId,
      };
      FunctionalityService.deleteOrganization(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 201 || response.status == 200) {
          toaster.success({ title: response.data.message });
          setTimeout(function () {
            $state.reload();
          }, 5000); // Wait 5 seconds
        } else {
          toaster.error({ title: response.data.message });
        }
      });
    };
  },
]);
