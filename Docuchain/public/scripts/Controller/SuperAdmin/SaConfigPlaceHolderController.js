// // var saPlaceHolder = angular.module('dapp.SaConfigPlaceHolderController', [
// //   'angularUtils.directives.dirPagination',
// //   'toaster',
// // ]);

// // saPlaceHolder
// //   .controller('SaConfigPlaceHolderController', [
// //     '$scope',
// //     '$window',
// //     '$location',
// //     '$state',
// //     '$rootScope',
// //     '$timeout',
// //     'toaster',
// //     'FunctionalityService',
// //     'DeletePopup',
// //     function (
// //       $scope,
// //       $window,
// //       $location,
// //       $state,
// //       $rootScope,
// //       $timeout,
// //       toaster,
// //       FunctionalityService,
// //       DeletePopup
// //     ) {
// //       $scope.currentPage = 1;
// //       $scope.viewby = 10;
// //       $scope.itemsPerPage = $scope.viewby;
// //       $scope.userProfileId = $window.localStorage.getItem('userId');
// //       $scope.allPlaceHolderList;
// //       $scope.loader = false;
// //       //Expiry certificate type start
// //       $scope.$on('$viewContentLoaded', function () {
// //         FunctionalityService.getExpirycertificateList().then(
// //           function (response) {
// //             if (response.status == 200) {
// //               console.log(
// //                 '$scope.expirycertificateList::' + JSON.stringify(response)
// //               );
// //               $scope.expirycertificateList =
// //                 response.data.expiryCertificateTypeDTOs;
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       });
// //       $scope.$on('$viewContentLoaded', function () {
// //         $scope.loader = true;

// //         FunctionalityService.getSubscriptionList($scope.userProfileId).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;

// //             if (response.status == 201 || response.status == 200) {
// //               $scope.allSubscriptionList = JSON.stringify(
// //                 response.data.subscriptionInfos
// //               );
// //               $scope.allSubscriptionList = response.data.subscriptionInfos;
// //               console.log(
// //                 'inside placeholder:',
// //                 JSON.stringify($scope.allSubscriptionList)
// //               );
// //               if ($scope.allSubscriptionList == undefined) {
// //                 toaster.info({ title: 'No records found' });
// //               }
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       });
// //       $scope.organizationChanges = function (organization) {
// //         console.log('second inside', organization);
// //         $scope.orgName = organization.organizationName;
// //       };
// //       $scope.addCertificateType = function () {
// //         $scope.loader = true;
// //         var certificateDate = {
// //           certificateType: $scope.certificateType,
// //           expiryHolderDescription: $scope.expirydescription,
// //         };
// //         FunctionalityService.addCertificateType(certificateDate).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;
// //             if (response.status == 201 || response.status == 200) {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 toaster.clear();
// //                 toaster.success({ title: response.data.message });
// //               }, 1000);
// //             } else {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               toaster.clear();
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       $scope.confirmDeletePlaceHolder = function (placeholder) {
// //         DeletePopup.confirm(
// //           'Delete Placeholder',
// //           'Are you sure you want to delete this placeholder?',
// //           function () {
// //             $scope.deletePlaceHolder(placeholder);
// //           }
// //         );
// //       };

// //       $scope.deletePlaceHolder = function (placeholder) {
// //         $scope.loader = true;
// //         var deletePlaceHolderData = {
// //           userId: $scope.userProfileId,
// //           documentHolderId: placeholder.documentHolderId,
// //         };
// //         FunctionalityService.deletePlaceHolder(deletePlaceHolderData).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.status == 200 || response.status == 201) {
// //               $scope.allPlaceHolderList = $scope.allPlaceHolderList.filter(
// //                 function (item) {
// //                   return item.documentHolderId !== placeholder.documentHolderId;
// //                 }
// //               );
// //               toaster.success({ title: response.data.message });
// //             } else {
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function (err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };
// //       $scope.confirmDeleteCertificate = function (certificate) {
// //         DeletePopup.confirm(
// //           'Delete Certificate',
// //           'Are you sure you want to delete this certificate?',
// //           function () {
// //             $scope.deleteCertificate(certificate);
// //           }
// //         );
// //       };

// //       $scope.deleteCertificate = function (certificate) {
// //         $scope.loader = true;
// //         var deleteData = {
// //           certificateTypeId: certificate.certificateTypeId,
// //         };
// //         FunctionalityService.deleteCertificateType(deleteData).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.status == 200 || response.status == 201) {
// //               // remove deleted certificate from list
// //               $scope.expirycertificateList =
// //                 $scope.expirycertificateList.filter(function (item) {
// //                   return (
// //                     item.certificateTypeId !== certificate.certificateTypeId
// //                   );
// //                 });
// //               toaster.success({ title: response.data.message });
// //             } else {
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function (err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       $scope.getEditCertificate = function (data) {
// //         $scope.editCertificate = data;
// //       };
// //       $scope.resetUpdateCertificate = function () {
// //         var updateData = {
// //           certificateType: $scope.editCertificate.certificateType,
// //           expiryHolderDescription:
// //             $scope.editCertificate.expiryHolderDescription,
// //           certificateTypeId: $scope.editCertificate.certificateTypeId,
// //         };
// //         FunctionalityService.updateCertificateType(updateData).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;
// //             if (response.status == 201 || response.status == 200) {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 toaster.clear();
// //                 toaster.success({ title: response.data.message });
// //               }, 1000);
// //             } else {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               toaster.clear();
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };
// //       //Expiry certificate type End
// //       $scope.expandSubMenu = function () {
// //         if ($rootScope.subMenuActive == true) {
// //           $rootScope.subMenuActive = false;
// //         } else {
// //           $rootScope.subMenuActive = true;
// //           $rootScope.subConfigMenuActive = false;
// //         }
// //       };

// //       $scope.expandConfigSubMenu = function () {
// //         if ($rootScope.subConfigMenuActive == true) {
// //           $rootScope.subConfigMenuActive = false;
// //         } else {
// //           $rootScope.subConfigMenuActive = true;
// //           $rootScope.subMenuActive = false;
// //         }
// //       };

// //       $scope.placeHolderList = function () {
// //         $scope.loader = true;

// //         FunctionalityService.getPlaceHolderList($scope.userProfileId).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;

// //             if (response.status == 201 || response.status == 200) {
// //               $scope.allPlaceHolderList = JSON.stringify(
// //                 response.data.documentHolderList
// //               );
// //               $scope.allPlaceHolderList = response.data.documentHolderList;
// //               console.log(
// //                 '$scope.allPlaceHolderList',
// //                 JSON.stringify($scope.allPlaceHolderList)
// //               );
// //               if ($scope.allPlaceHolderList == undefined) {
// //                 toaster.clear();
// //                 toaster.info({ title: 'No records found' });
// //               }
// //             } else {
// //               toaster.clear();
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       $scope.addPlaceHolder = function () {
// //         $scope.loader = true;

// //         var addPlaceHolderData = {
// //           userId: $scope.userProfileId,
// //           documentHolderName: $scope.placeholdername,
// //           documentHolderDescription: $scope.placeholderdescription,
// //           documentFileNumber: $scope.placeholderfilename,
// //           organizationName: $scope.orgName,
// //           type: 'Standard',
// //         };
// //         //var addPlaceHolderData = { "userId": $scope.userProfileId, "documentHolderName": $scope.placeholdername, "documentHolderDescription": $scope.placeholderdescription ,"documentFileNumber": $scope.placeholderfilename};
// //         FunctionalityService.addPlaceHolder(addPlaceHolderData).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;

// //             if (response.status == 201 || response.status == 200) {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 toaster.clear();
// //                 toaster.success({ title: response.data.message });
// //               }, 1000);
// //             } else {
// //               $('#addplaceholder').modal('hide');
// //               $state.reload();
// //               toaster.clear();
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       $scope.clearAddPlaceholder = function () {
// //         $scope.placeholdername = '';
// //         $scope.placeholderdescription = '';
// //         $scope.placeholderfilename = '';
// //       };

// //       $scope.updatePlaceHolder;
// //       $scope.getEditPlaceHolder = function (update) {
// //         $scope.updatePlaceHolder = update;
// //       };
// //       $scope.editPlaceHolder = function () {
// //         $scope.loader = true;

// //         var updatePlaceHolderData = {
// //           userId: $scope.userProfileId,
// //           documentHolderId: $scope.updatePlaceHolder.documentHolderId,
// //           documentHolderName: $scope.updatePlaceHolder.documentHolderName,
// //           documentHolderDescription:
// //             $scope.updatePlaceHolder.documentHolderDescription,
// //           documentFileNumber: $scope.updatePlaceHolder.documentFileNumber,
// //           organizationName: $scope.updatePlaceHolder.organizationName,
// //         };
// //         FunctionalityService.editPlaceHolder(updatePlaceHolderData).then(
// //           function mySuccess(response) {
// //             $scope.loader = false;

// //             if (response.status == 201 || response.status == 200) {
// //               $('#updateplaceholder').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 toaster.clear();
// //                 toaster.success({ title: response.data.message });
// //               }, 1000);
// //             } else {
// //               $('#updateplaceholder').modal('hide');
// //               $state.reload();
// //               toaster.clear();
// //               toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       $scope.closeAddplaceholder = function () {
// //         $('#addplaceholder').modal('hide');
// //         $state.reload();
// //       };
// //       $scope.clsoedUpdateplaceHolder = function () {
// //         $('#updateplaceholder').modal('hide');
// //         $state.reload();
// //       };

// //       $scope.resetUpdateplaceHolder = function () {
// //         $scope.updatePlaceholdeForm.$setPristine();

// //         $scope.updatePlaceHolder.documentHolderName = '';

// //         $scope.updatePlaceHolder.documentHolderDescription = '';

// //         $scope.updatePlaceHolder.documentFileNumber = '';
// //         $scope.certificateType = '';
// //         $scope.expirydescription = '';
// //       };
// //       $scope.resetExpiryplaceHolder = function () {
// //         $scope.certificateType = '';
// //         $scope.expirydescription = '';
// //       };
// //     },
// //   ])
// //   .directive('customFocus', [
// //     function () {
// //       var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
// //       return {
// //         restrict: 'A', //Angular will only match the directive against attribute names
// //         require: 'ngModel',
// //         link: function (scope, element, attrs, ctrl) {
// //           ctrl.$focused = false;

// //           element
// //             .bind('focus', function (evt) {
// //               element.addClass(FOCUS_CLASS);
// //               scope.$apply(function () {
// //                 ctrl.$focused = true;
// //               });
// //             })
// //             .bind('blur', function (evt) {
// //               element.removeClass(FOCUS_CLASS);
// //               scope.$apply(function () {
// //                 ctrl.$focused = false;
// //               });
// //             });
// //         },
// //       };
// //     },
// //   ]);

// var saPlaceHolder = angular.module('dapp.SaConfigPlaceHolderController', [
//   'angularUtils.directives.dirPagination',
//   'toaster',
// ]);

// saPlaceHolder
//   .controller('SaConfigPlaceHolderController', [
//     '$scope',
//     '$window',
//     '$location',
//     '$state',
//     '$rootScope',
//     '$timeout',
//     'toaster',
//     'FunctionalityService',
//     'DeletePopup',
//     function (
//       $scope,
//       $window,
//       $location,
//       $state,
//       $rootScope,
//       $timeout,
//       toaster,
//       FunctionalityService,
//       DeletePopup
//     ) {
//       $scope.currentPage = 1;
//       $scope.viewby = 10;
//       $scope.itemsPerPage = $scope.viewby;
//       $scope.userProfileId = $window.localStorage.getItem('userId');
//       $scope.allPlaceHolderList;
//       $scope.loader = false;

//       // --- HELPER TO FETCH CERTIFICATES IN BACKGROUND ---
//       $scope.getCertificateList = function () {
//         FunctionalityService.getExpirycertificateList().then(
//           function (response) {
//             if (response.status == 200) {
//               $scope.expirycertificateList = response.data.expiryCertificateTypeDTOs;
//             }
//           },
//           function myError(err) {
//             console.log('Error response', err);
//           }
//         );
//       };

//       // Load Certificates on Init
//       $scope.$on('$viewContentLoaded', function () {
//         $scope.getCertificateList();
//       });

//       // Load Subscriptions on Init
//       $scope.$on('$viewContentLoaded', function () {
//         $scope.loader = true;
//         FunctionalityService.getSubscriptionList($scope.userProfileId).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//             if (response.status == 201 || response.status == 200) {
//               $scope.allSubscriptionList = response.data.subscriptionInfos;
//               if ($scope.allSubscriptionList == undefined) {
//                 toaster.pop('info', "Info", "No records found");
//               }
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       });

//       $scope.organizationChanges = function (organization) {
//         $scope.orgName = organization.organizationName;
//       };

//       // ---------------------------------------------------------
//       // ADD CERTIFICATE TYPE
//       // ---------------------------------------------------------
//       $scope.addCertificateType = function () {
//         $scope.loader = true;
//         var certificateDate = {
//           certificateType: $scope.certificateType,
//           expiryHolderDescription: $scope.expirydescription,
//         };
//         FunctionalityService.addCertificateType(certificateDate).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//             if (response.status == 201 || response.status == 200) {
//               $('#addplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               toaster.pop('success', "Success", response.data.message);

//               // Refresh table silently instead of reloading page
//               $scope.getCertificateList();
//               $scope.resetExpiryplaceHolder();
//             } else {
//               $('#addplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       // ---------------------------------------------------------
//       // DELETE PLACEHOLDER
//       // ---------------------------------------------------------
//       $scope.confirmDeletePlaceHolder = function (placeholder) {
//         DeletePopup.confirm(
//           'Delete Placeholder',
//           'Are you sure you want to delete this placeholder?',
//           function () {
//             $scope.deletePlaceHolder(placeholder);
//           }
//         );
//       };

//       $scope.deletePlaceHolder = function (placeholder) {
//         $scope.loader = true;
//         var deletePlaceHolderData = {
//           userId: $scope.userProfileId,
//           documentHolderId: placeholder.documentHolderId,
//         };
//         FunctionalityService.deletePlaceHolder(deletePlaceHolderData).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200 || response.status == 201) {
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               // Remove from list dynamically without reloading
//               $scope.allPlaceHolderList = $scope.allPlaceHolderList.filter(
//                 function (item) {
//                   return item.documentHolderId !== placeholder.documentHolderId;
//                 }
//               );
//               toaster.pop('success', "Success", response.data.message);
//             } else {
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function (err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       // ---------------------------------------------------------
//       // DELETE CERTIFICATE
//       // ---------------------------------------------------------
//       $scope.confirmDeleteCertificate = function (certificate) {
//         DeletePopup.confirm(
//           'Delete Certificate',
//           'Are you sure you want to delete this certificate?',
//           function () {
//             $scope.deleteCertificate(certificate);
//           }
//         );
//       };

//       $scope.deleteCertificate = function (certificate) {
//         $scope.loader = true;
//         var deleteData = {
//           certificateTypeId: certificate.certificateTypeId,
//         };
//         FunctionalityService.deleteCertificateType(deleteData).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200 || response.status == 201) {
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               // Remove from list dynamically without reloading
//               $scope.expirycertificateList = $scope.expirycertificateList.filter(function (item) {
//                   return item.certificateTypeId !== certificate.certificateTypeId;
//               });
//               toaster.pop('success', "Success", response.data.message);
//             } else {
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function (err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       // ---------------------------------------------------------
//       // EDIT CERTIFICATE
//       // ---------------------------------------------------------
//       $scope.getEditCertificate = function (data) {
//         $scope.editCertificate = angular.copy(data);
//       };

//       $scope.resetUpdateCertificate = function () {
//         $scope.loader = true;
//         var updateData = {
//           certificateType: $scope.editCertificate.certificateType,
//           expiryHolderDescription: $scope.editCertificate.expiryHolderDescription,
//           certificateTypeId: $scope.editCertificate.certificateTypeId,
//         };
//         FunctionalityService.updateCertificateType(updateData).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//             if (response.status == 201 || response.status == 200) {
//               $('#updateplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               toaster.pop('success', "Success", response.data.message);

//               // Refresh table silently
//               $scope.getCertificateList();
//             } else {
//               $('#updateplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.expandSubMenu = function () {
//         if ($rootScope.subMenuActive == true) {
//           $rootScope.subMenuActive = false;
//         } else {
//           $rootScope.subMenuActive = true;
//           $rootScope.subConfigMenuActive = false;
//         }
//       };

//       $scope.expandConfigSubMenu = function () {
//         if ($rootScope.subConfigMenuActive == true) {
//           $rootScope.subConfigMenuActive = false;
//         } else {
//           $rootScope.subConfigMenuActive = true;
//           $rootScope.subMenuActive = false;
//         }
//       };

//       // ---------------------------------------------------------
//       // LIST PLACEHOLDERS
//       // ---------------------------------------------------------
//       $scope.placeHolderList = function () {
//         $scope.loader = true;
//         FunctionalityService.getPlaceHolderList($scope.userProfileId).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//             if (response.status == 201 || response.status == 200) {
//               $scope.allPlaceHolderList = response.data.documentHolderList;
//             } else {
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       // ---------------------------------------------------------
//       // ADD PLACEHOLDER
//       // ---------------------------------------------------------
//       $scope.addPlaceHolder = function () {
//         $scope.loader = true;
//         var addPlaceHolderData = {
//           userId: $scope.userProfileId,
//           documentHolderName: $scope.placeholdername,
//           documentHolderDescription: $scope.placeholderdescription,
//           documentFileNumber: $scope.placeholderfilename,
//           organizationName: $scope.orgName,
//           type: 'Standard',
//         };
//         FunctionalityService.addPlaceHolder(addPlaceHolderData).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//             if (response.status == 201 || response.status == 200) {
//               $('#addplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               toaster.pop('success', "Success", response.data.message);

//               // Refresh table silently
//               $scope.placeHolderList();
//               $scope.clearAddPlaceholder();
//             } else {
//               $('#addplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.clearAddPlaceholder = function () {
//         $scope.placeholdername = '';
//         $scope.placeholderdescription = '';
//         $scope.placeholderfilename = '';
//       };

//       // ---------------------------------------------------------
//       // EDIT PLACEHOLDER
//       // ---------------------------------------------------------
//       $scope.getEditPlaceHolder = function (update) {
//         $scope.updatePlaceHolder = angular.copy(update);
//       };

//       $scope.editPlaceHolder = function () {
//         $scope.loader = true;
//         var updatePlaceHolderData = {
//           userId: $scope.userProfileId,
//           documentHolderId: $scope.updatePlaceHolder.documentHolderId,
//           documentHolderName: $scope.updatePlaceHolder.documentHolderName,
//           documentHolderDescription:
//             $scope.updatePlaceHolder.documentHolderDescription,
//           documentFileNumber: $scope.updatePlaceHolder.documentFileNumber,
//           organizationName: $scope.updatePlaceHolder.organizationName,
//         };
//         FunctionalityService.editPlaceHolder(updatePlaceHolderData).then(
//           function mySuccess(response) {
//             $scope.loader = false;

//             if (response.status == 201 || response.status == 200) {
//               $('#updateplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();

//               toaster.pop('success', "Success", response.data.message);

//               // Refresh table silently
//               $scope.placeHolderList();
//             } else {
//               $('#updateplaceholder').modal('hide');
//               $('body').removeClass('modal-open');
//               $('.modal-backdrop').remove();
//               toaster.pop('error', "Error", response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.closeAddplaceholder = function () {
//         $('#addplaceholder').modal('hide');
//         $('body').removeClass('modal-open');
//         $('.modal-backdrop').remove();
//       };

//       $scope.clsoedUpdateplaceHolder = function () {
//         $('#updateplaceholder').modal('hide');
//         $('body').removeClass('modal-open');
//         $('.modal-backdrop').remove();
//       };

//       $scope.resetUpdateplaceHolder = function () {
//         $scope.updatePlaceholdeForm.$setPristine();
//         $scope.updatePlaceHolder.documentHolderName = '';
//         $scope.updatePlaceHolder.documentHolderDescription = '';
//         $scope.updatePlaceHolder.documentFileNumber = '';
//         $scope.certificateType = '';
//         $scope.expirydescription = '';
//       };

//       $scope.resetExpiryplaceHolder = function () {
//         $scope.certificateType = '';
//         $scope.expirydescription = '';
//       };
//     },
//   ])
//   .directive('customFocus', [
//     function () {
//       var FOCUS_CLASS = 'custom-focused';
//       return {
//         restrict: 'A',
//         require: 'ngModel',
//         link: function (scope, element, attrs, ctrl) {
//           ctrl.$focused = false;

//           element
//             .bind('focus', function (evt) {
//               element.addClass(FOCUS_CLASS);
//               scope.$apply(function () {
//                 ctrl.$focused = true;
//               });
//             })
//             .bind('blur', function (evt) {
//               element.removeClass(FOCUS_CLASS);
//               scope.$apply(function () {
//                 ctrl.$focused = false;
//               });
//             });
//         },
//       };
//     },
//   ]);
var saPlaceHolder = angular.module('dapp.SaConfigPlaceHolderController', [
  'angularUtils.directives.dirPagination',
  'toaster',
]);

saPlaceHolder
  .controller('SaConfigPlaceHolderController', [
    '$scope',
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
      $scope.userProfileId = $window.localStorage.getItem('userId');
      $scope.allPlaceHolderList;
      $scope.loader = false;

      /**
       * FETCH DATA HELPERS
       * These update the table without refreshing the page.
       */
      $scope.fetchExpiryCertificates = function () {
        FunctionalityService.getExpirycertificateList().then(
          function (response) {
            if (response.status == 200) {
              $scope.expirycertificateList =
                response.data.expiryCertificateTypeDTOs;
            }
          },
          function (err) {
            console.log('Error fetching certificates', err);
          }
        );
      };

      $scope.placeHolderList = function () {
        $scope.loader = true;
        FunctionalityService.getPlaceHolderList($scope.userProfileId).then(
          function mySuccess(response) {
            $scope.loader = false;
            if (response.status == 201 || response.status == 200) {
              $scope.allPlaceHolderList = response.data.documentHolderList;
            } else {
              toaster.pop('error', null, response.data.message);
            }
          },
          function (err) {
            $scope.loader = false;
          }
        );
      };

      // Initial Load
      $scope.$on('$viewContentLoaded', function () {
        $scope.fetchExpiryCertificates();
        $scope.placeHolderList();
      });

      // Load Subscriptions for dropdowns
      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getSubscriptionList($scope.userProfileId).then(
          function mySuccess(response) {
            if (response.status == 201 || response.status == 200) {
              $scope.allSubscriptionList = response.data.subscriptionInfos;
            }
          }
        );
      });

      $scope.organizationChanges = function (organization) {
        $scope.orgName = organization.organizationName;
      };

      /**
       * MODAL CLEANUP HELPER
       * Prevents the screen from getting stuck (unscollable).
       */
      function cleanupModal() {
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
        $('body').css('padding-right', '0px');
      }

      // ---------------------------------------------------------
      // ADD CERTIFICATE TYPE
      // ---------------------------------------------------------
      $scope.addCertificateType = function () {
        $scope.loader = true;
        var certificateDate = {
          certificateType: $scope.certificateType,
          expiryHolderDescription: $scope.expirydescription,
        };
        FunctionalityService.addCertificateType(certificateDate).then(
          function mySuccess(response) {
            $scope.loader = false;
            if (response.status == 201 || response.status == 200) {
              $('#addplaceholder').modal('hide');
              cleanupModal();

              // Only message, no title
              toaster.pop('success', null, response.data.message);

              $scope.fetchExpiryCertificates();
              $scope.resetExpiryplaceHolder();
            } else {
              toaster.pop('error', null, response.data.message);
            }
          }
        );
      };

      // ---------------------------------------------------------
      // UPDATE CERTIFICATE TYPE
      // ---------------------------------------------------------
      $scope.getEditCertificate = function (data) {
        $scope.editCertificate = angular.copy(data);
      };

      $scope.resetUpdateCertificate = function () {
        $scope.loader = true;
        var updateData = {
          certificateType: $scope.editCertificate.certificateType,
          expiryHolderDescription:
            $scope.editCertificate.expiryHolderDescription,
          certificateTypeId: $scope.editCertificate.certificateTypeId,
        };
        FunctionalityService.updateCertificateType(updateData).then(
          function mySuccess(response) {
            $scope.loader = false;
            if (response.status == 201 || response.status == 200) {
              $('#updateplaceholder').modal('hide');
              cleanupModal();

              toaster.pop('success', null, response.data.message);
              $scope.fetchExpiryCertificates();
            } else {
              toaster.pop('error', null, response.data.message);
            }
          }
        );
      };

      // ---------------------------------------------------------
      // DELETE CERTIFICATE
      // ---------------------------------------------------------
      $scope.confirmDeleteCertificate = function (certificate) {
        DeletePopup.confirm('Delete Certificate', 'Are you sure?', function () {
          $scope.deleteCertificate(certificate);
        });
      };

      $scope.deleteCertificate = function (certificate) {
        $scope.loader = true;
        FunctionalityService.deleteCertificateType({
          certificateTypeId: certificate.certificateTypeId,
        }).then(function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.expirycertificateList = $scope.expirycertificateList.filter(
              function (item) {
                return item.certificateTypeId !== certificate.certificateTypeId;
              }
            );
            toaster.pop('success', null, response.data.message);
          }
        });
      };

      // ---------------------------------------------------------
      // ADD PLACEHOLDER
      // ---------------------------------------------------------
      $scope.addPlaceHolder = function () {
        $scope.loader = true;
        var addPlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderName: $scope.placeholdername,
          documentHolderDescription: $scope.placeholderdescription,
          organizationName: $scope.orgName,
          type: 'Standard',
        };
        FunctionalityService.addPlaceHolder(addPlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;
            if (response.status == 201 || response.status == 200) {
              $('#addplaceholder').modal('hide');
              cleanupModal();

              toaster.pop('success', null, response.data.message);
              $scope.placeHolderList();
              $scope.clearAddPlaceholder();
            }
          }
        );
      };

      // ---------------------------------------------------------
      // EDIT PLACEHOLDER
      // ---------------------------------------------------------
      $scope.getEditPlaceHolder = function (update) {
        $scope.updatePlaceHolder = angular.copy(update);
      };

      $scope.editPlaceHolder = function () {
        $scope.loader = true;
        var updatePlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderId: $scope.updatePlaceHolder.documentHolderId,
          documentHolderName: $scope.updatePlaceHolder.documentHolderName,
          documentHolderDescription:
            $scope.updatePlaceHolder.documentHolderDescription,
          organizationName: $scope.updatePlaceHolder.organizationName,
        };
        FunctionalityService.editPlaceHolder(updatePlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;
            if (response.status == 201 || response.status == 200) {
              $('#updateplaceholder').modal('hide');
              cleanupModal();

              toaster.pop('success', null, response.data.message);
              $scope.placeHolderList();
            }
          }
        );
      };

      // ---------------------------------------------------------
      // DELETE PLACEHOLDER
      // ---------------------------------------------------------
      $scope.confirmDeletePlaceHolder = function (placeholder) {
        DeletePopup.confirm('Delete Placeholder', 'Are you sure?', function () {
          $scope.deletePlaceHolder(placeholder);
        });
      };

      $scope.deletePlaceHolder = function (placeholder) {
        $scope.loader = true;
        var data = {
          userId: $scope.userProfileId,
          documentHolderId: placeholder.documentHolderId,
        };
        FunctionalityService.deletePlaceHolder(data).then(function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.allPlaceHolderList = $scope.allPlaceHolderList.filter(
              function (item) {
                return item.documentHolderId !== placeholder.documentHolderId;
              }
            );
            toaster.pop('success', null, response.data.message);
          }
        });
      };

      // STANDARD UI CONTROLS
      $scope.expandSubMenu = function () {
        $rootScope.subMenuActive = !$rootScope.subMenuActive;
        if ($rootScope.subMenuActive) $rootScope.subConfigMenuActive = false;
      };

      $scope.expandConfigSubMenu = function () {
        $rootScope.subConfigMenuActive = !$rootScope.subConfigMenuActive;
        if ($rootScope.subConfigMenuActive) $rootScope.subMenuActive = false;
      };

      $scope.closeAddplaceholder = function () {
        $('#addplaceholder').modal('hide');
        cleanupModal();
      };

      $scope.clsoedUpdateplaceHolder = function () {
        $('#updateplaceholder').modal('hide');
        cleanupModal();
      };

      $scope.clearAddPlaceholder = function () {
        $scope.placeholdername = '';
        $scope.placeholderdescription = '';
      };

      $scope.resetUpdateplaceHolder = function () {
        $scope.updatePlaceholdeForm.$setPristine();
        $scope.updatePlaceHolder.documentHolderName = '';
        $scope.updatePlaceHolder.documentHolderDescription = '';
      };

      $scope.resetExpiryplaceHolder = function () {
        $scope.certificateType = '';
        $scope.expirydescription = '';
      };
    },
  ])
  .directive('customFocus', [
    function () {
      return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
          ctrl.$focused = false;
          element
            .bind('focus', function () {
              element.addClass('custom-focused');
              scope.$apply(function () {
                ctrl.$focused = true;
              });
            })
            .bind('blur', function () {
              element.removeClass('custom-focused');
              scope.$apply(function () {
                ctrl.$focused = false;
              });
            });
        },
      };
    },
  ]);
