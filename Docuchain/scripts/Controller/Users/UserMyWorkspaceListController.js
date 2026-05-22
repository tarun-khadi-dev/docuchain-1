// var userMyWorkspaceList = angular.module('dapp.UserMyWorkspaceListController', [
//   'ui.select',
//   'angularUtils.directives.dirPagination',
//   'toaster',
//   'moment-picker',
//   '720kb.tooltips',
//   'ngSanitize',
// ]);

// userMyWorkspaceList.controller('UserMyWorkspaceListController', [
//   '$scope',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   '$timeout',
//   'toaster',
//   '$filter',
//   '$sce',
//   'FunctionalityService',
//   'DeletePopup',
//   function (
//     $scope,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     $timeout,
//     toaster,
//     $filter,
//     $sce,
//     FunctionalityService,
//     DeletePopup
//   ) {
//     $scope.loader = false;
//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );

//     $scope.groupShipId;
//     $scope.groupShipName;

//     if ($scope.sessionObject.roleId != 3) {
//       $scope.groupShipId = $window.localStorage.getItem('groupShipId');
//       $scope.groupShipName = $window.localStorage.getItem('groupShipName');
//       $scope.libshipNameDoc = $scope.groupShipName + ' ' + 'Documents';
//     } else if ($scope.sessionObject.shipProfileInfos.length > 0) {
//       $scope.groupShipId = $scope.sessionObject.shipProfileInfos[0].id;
//       $scope.groupShipName = $scope.sessionObject.shipProfileInfos[0].shipName;
//       $scope.libshipNameDoc = $scope.groupShipName + ' ' + 'Documents';
//     }

//     $scope.groupList = [];

//     $scope.groupListLength = $scope.groupList.length;
//     $scope.itemsPerPageGroup = 10;
//     $scope.currentPageGroup = 1;
//     $scope.loader = false;

//     $scope.deleteGroupObj;

//     //This method is used to get All Group List
//     $scope.getAllGroupList = function () {
//       $scope.loader = true;

//       var groupdata = {
//         userProfileId: $scope.sessionObject.userId,
//         shipId: $scope.groupShipId,
//       };

//       FunctionalityService.getGroupListShip(groupdata).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.message = JSON.stringify(response.data.groupList);
//             $scope.groupList = response.data.groupList;
//             $scope.groupListLength = $scope.groupList.length;
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

//       var data1 = {
//         userId: $scope.sessionObject.userId,
//         roleId: $scope.sessionObject.roleId,
//       };
//       FunctionalityService.getVesselProfileList(data1).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.vesselList = response.data.shipProfileList;
//             // $scope.vesselListLength = $scope.vesselList.length;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };
//     $scope.closeClick = function () {
//       $state.reload();
//     };
//     //This method is used to create Group
//     $scope.createGroupInMyWorkspace = function (group) {
//       $scope.loader = true;
//       $scope.shipIds = [];
//       $scope.shipIds.push($scope.groupShipId);

//       var groupData = {
//         groupName: $scope.group.groupName,
//         loginId: $scope.sessionObject.userId,
//         userProfileId: $scope.sessionObject.userId,
//         shipIds: $scope.shipIds,
//         emailId: $scope.group.emailId,
//         keyword: $scope.group.Keyword,
//       };
//       FunctionalityService.addGroup(JSON.stringify(groupData)).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $('#createGroup').modal('hide');
//             $state.reload();
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//               //toaster.pop('success', response.data.message);
//             }, 1000);
//           } else {
//             $scope.loader = false;
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//             //toaster.pop('error', response.data.message);
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     //This method is used to clear the create popup fields
//     $scope.clearGroupClearFileds = function () {
//       $scope.group.groupName = '';
//       $scope.group.emailId = '';
//       $scope.group.Keyword = '';
//     };

//     //This method is used to store the delete group object
//     $scope.storeGroupObjDelete = function (groupData) {
//       $scope.deleteGroupObj = groupData;
//     };

//     $scope.deleteGroup = function (group) {
//       $scope.loader = true;
//       var groupData = {
//         loginId: $scope.sessionObject.userId,
//         groupId: group.id,
//       };
//       FunctionalityService.deleteGroup(groupData).then(
//         function (response) {
//           $scope.loader = false;
//           if (response.status == 200) {
//             toaster.success({ title: response.data.message });
//             $scope.getAllGroupList();
//           } else {
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.confirmDeleteExpiry = function (groupEBD) {
//       DeletePopup.confirm(
//         'Delete Certificate',
//         'Are you sure you want to delete this certificate?',
//         function () {
//           $scope.loader = true;
//           let data = {
//             documentHolderId: groupEBD.documentHolderId,
//             groupId: groupEBD.groupId,
//             loginId: $scope.sessionObject.userId,
//           };

//           FunctionalityService.deleteGroupExpiryDocument(
//             JSON.stringify(data)
//           ).then(
//             function (response) {
//               $scope.loader = false;
//               if (response.status === 200) {
//                 toaster.success({ title: response.data.message });
//                 // Update UI WITHOUT reload
//                 $scope.groupEBDList = $scope.groupEBDList.filter(
//                   (item) => item.documentHolderId !== groupEBD.documentHolderId
//                 );
//               } else {
//                 toaster.error({ title: response.data.message });
//               }
//             },
//             function (err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         },
//         'Delete'
//       );
//     };

//     $scope.confirmDelete = function (group) {
//       DeletePopup.confirm(
//         'Delete Group',
//         'Are you sure you want to delete this group?',
//         function () {
//           $scope.deleteGroup(group);
//         }
//       );
//     };

//     //This method is used to share expiry document with windows mail
//     $scope.shareExpiryDocumentWithMail = function (group) {
//       $scope.loader = true;
//       $scope.expDocUrlGroup = '';
//       FunctionalityService.getAllGroupExpiryDocumentList(group.id).then(
//         function (response) {
//           $scope.loader = false;
//           $scope.groupExpiryList = response.data.expiryDocumentList;

//           if ($scope.groupExpiryList && $scope.groupExpiryList.length > 0) {
//             angular.forEach($scope.groupExpiryList, function (value) {
//               $scope.expDocUrlGroup +=
//                 '\n' +
//                 value.documentHolderName +
//                 ':\n' +
//                 value.documentDownloadUrl +
//                 '\n';
//             });

//             $scope.expDocUrlshare = $scope.expDocUrlGroup;
//             $scope.groupEmailForShare = group.emailId;
//             $scope.libshipNameDoc = $scope.groupShipName + ' Documents';
//             if (CKEDITOR.instances.mailEditor) {
//               CKEDITOR.instances.mailEditor.setData($scope.expDocUrlshare);
//             }
//             $('#shareMail').modal('show');
//           } else {
//             toaster.clear();
//             toaster.info({ title: 'No documents found' });
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     // --- New Variables for the View Modal ---
//     $scope.groupEBDList = [];
//     $scope.itemsPerPageGroupEbd = 10;
//     $scope.currentPageGroupEbd = 1;
//     $scope.selectedGroupName = '';
//     $scope.selectedGroupId = null;

//     // --- Opens the Modal and fetches the list ---
//     $scope.viewGroupDetails = function (group) {
//       $scope.loader = true;
//       $scope.selectedGroupName = group.groupName;
//       $scope.selectedGroupId = group.id;

//       FunctionalityService.getAllGroupExpiryDocumentList(group.id).then(
//         function (response) {
//           $scope.loader = false;
//           $scope.groupEBDList = response.data.expiryDocumentList;

//           // Open the modal
//           $('#viewGroupModal').modal('show');
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//           toaster.error({ title: 'Failed to load certificates.' });
//         }
//       );
//     };

//     // --- VIEW DOCUMENT LOGIC ADDED HERE ---
//     $scope.previewDocUrl = '';
//     $scope.viewDocName = '';

//     // $scope.viewDocument = function (documentObj) {
//     //   if (documentObj && documentObj.documentPreviewUrl) {
//     //     $scope.previewDocUrl = $sce.trustAsResourceUrl(
//     //       documentObj.documentPreviewUrl
//     //     );
//     //     $scope.viewDocName = documentObj.documentHolderName;
//     //     $('#documentPreviewModal').modal('show');
//     //   } else {
//     //     toaster.clear();
//     //     toaster.info({ title: 'No preview available for this document.' });
//     //   }
//     // };
//     // --- VIEW DOCUMENT IN NEW TAB LOGIC ---
//     $scope.viewDocument = function (documentObj) {
//       if (documentObj && documentObj.documentPreviewUrl) {
//         // Opens the document URL in a new browser tab/window
//         $window.open(documentObj.documentPreviewUrl, '_blank');
//       } else {
//         toaster.clear();
//         toaster.info({ title: 'No preview available for this document.' });
//       }
//     };
//     // ---------------------------------------

//     // Clear the URL to stop background playback (if any) when modal closes
//     $('#documentPreviewModal').on('hidden.bs.modal', function () {
//       $timeout(function () {
//         $scope.previewDocUrl = '';
//         $scope.viewDocName = '';
//       });
//     });
//     // ---------------------------------------

//     $scope.submitEmailRequest = function () {
//       if (CKEDITOR.instances.mailEditor) {
//         $scope.expDocUrlshare = CKEDITOR.instances.mailEditor.getData();
//       }
//       $scope.loader = true;
//       var emailData = {
//         to: $scope.groupEmailForShare
//           .split(',')
//           .map(function (e) {
//             return e.trim();
//           })
//           .filter(function (e) {
//             return e.length > 0;
//           }),
//         subject: $scope.libshipNameDoc,
//         body: $scope.expDocUrlshare,
//       };
//       FunctionalityService.sendEmail(emailData)
//         .then(function (response) {
//           toaster.pop('success', 'Success', 'Email sent successfully');
//           $('#shareMail').modal('hide');
//         })
//         .catch(function (error) {
//           toaster.pop('error', 'Error', 'Failed to send email');
//           console.error(error);
//         })
//         .finally(function () {
//           $scope.loader = false;
//         });
//     };
//     //editor intialize
//     $timeout(function () {
//       CKEDITOR.config.versionCheck = false;
//       if (!CKEDITOR.instances.mailEditor) {
//         CKEDITOR.replace('mailEditor', {
//           height: 150,
//           removePlugins: 'elementspath',
//           resize_enabled: false,
//           allowedContent: true,
//           extraAllowedContent: '*(*);*{*}',
//         });
//       }
//     }, 500);
//   },
// ]);

// userMyWorkspaceList.directive('customFocus', [
//   function () {
//     var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
//     return {
//       restrict: 'A',
//       require: 'ngModel',
//       link: function (scope, element, attrs, ctrl) {
//         ctrl.$focused = false;

//         element
//           .bind('focus', function (evt) {
//             element.addClass(FOCUS_CLASS);
//             scope.$apply(function () {
//               ctrl.$focused = true;
//             });
//           })
//           .bind('blur', function (evt) {
//             element.removeClass(FOCUS_CLASS);
//             scope.$apply(function () {
//               ctrl.$focused = false;
//             });
//           });
//       },
//     };
//   },
// ]);
// userMyWorkspaceList.directive('validateEmail', function () {
//   var EMAIL_REGEXP =
//     /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
//   return {
//     link: function (scope, elm) {
//       elm.on('keyup', function () {
//         var isMatchRegex = EMAIL_REGEXP.test(elm.val());
//         if (
//           (isMatchRegex && elm.hasClass('warning-email')) ||
//           elm.val() == ''
//         ) {
//           elm.removeClass('warning-email');
//         } else if (isMatchRegex == false && !elm.hasClass('warning-email')) {
//           elm.addClass('warning-email');
//         }
//       });
//     },
//   };
// });
// //Directive for File Upload
// userMyWorkspaceList.directive('fileModel', [
//   '$parse',
//   function ($parse) {
//     return {
//       restrict: 'A',
//       link: function (scope, element, attrs) {
//         var model = $parse(attrs.fileModel);
//         var modelSetter = model.assign;

//         element.bind('change', function () {
//           scope.$apply(function () {
//             modelSetter(scope, element[0].files[0]);
//           });
//         });
//       },
//     };
//   },
// ]);

var userMyWorkspaceList = angular.module('dapp.UserMyWorkspaceListController', [
  'ui.select',
  'angularUtils.directives.dirPagination',
  'toaster',
  'moment-picker',
  '720kb.tooltips',
  'ngSanitize',
]);

userMyWorkspaceList.controller('UserMyWorkspaceListController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  '$timeout',
  'toaster',
  '$filter',
  '$sce',
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
    $filter,
    $sce,
    FunctionalityService,
    DeletePopup
  ) {
    $scope.loader = false;
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.groupShipId;
    $scope.groupShipName;

    if ($scope.sessionObject.roleId != 3) {
      $scope.groupShipId = $window.localStorage.getItem('groupShipId');
      $scope.groupShipName = $window.localStorage.getItem('groupShipName');
      $scope.libshipNameDoc = $scope.groupShipName + ' ' + 'Documents';
    } else if ($scope.sessionObject.shipProfileInfos.length > 0) {
      $scope.groupShipId = $scope.sessionObject.shipProfileInfos[0].id;
      $scope.groupShipName = $scope.sessionObject.shipProfileInfos[0].shipName;
      $scope.libshipNameDoc = $scope.groupShipName + ' ' + 'Documents';
    }

    $scope.groupList = [];

    $scope.groupListLength = $scope.groupList.length;
    $scope.itemsPerPageGroup = 10;
    $scope.currentPageGroup = 1;
    $scope.loader = false;

    $scope.deleteGroupObj;

    //This method is used to get All Group List
    $scope.getAllGroupList = function () {
      $scope.loader = true;

      var groupdata = {
        userProfileId: $scope.sessionObject.userId,
        shipId: $scope.groupShipId,
      };

      FunctionalityService.getGroupListShip(groupdata).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $scope.message = JSON.stringify(response.data.groupList);
            $scope.groupList = response.data.groupList;
            $scope.groupListLength = $scope.groupList.length;
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

      var data1 = {
        userId: $scope.sessionObject.userId,
        roleId: $scope.sessionObject.roleId,
      };
      FunctionalityService.getVesselProfileList(data1).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $scope.vesselList = response.data.shipProfileList;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.closeClick = function () {
      $state.reload();
    };

    //This method is used to create Group
    $scope.createGroupInMyWorkspace = function (group) {
      $scope.loader = true;
      $scope.shipIds = [];
      $scope.shipIds.push($scope.groupShipId);

      var groupData = {
        groupName: $scope.group.groupName,
        loginId: $scope.sessionObject.userId,
        userProfileId: $scope.sessionObject.userId,
        shipIds: $scope.shipIds,
        emailId: $scope.group.emailId,
        keyword: $scope.group.Keyword,
      };
      FunctionalityService.addGroup(JSON.stringify(groupData)).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#createGroup').modal('hide');
            $state.reload();
            $timeout(function () {
              toaster.clear();
              toaster.success({ title: response.data.message });
            }, 1000);
          } else {
            $scope.loader = false;
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

    //This method is used to clear the create popup fields
    $scope.clearGroupClearFileds = function () {
      $scope.group.groupName = '';
      $scope.group.emailId = '';
      $scope.group.Keyword = '';
    };

    //This method is used to store the delete group object
    $scope.storeGroupObjDelete = function (groupData) {
      $scope.deleteGroupObj = groupData;
    };

    $scope.deleteGroup = function (group) {
      $scope.loader = true;
      var groupData = {
        loginId: $scope.sessionObject.userId,
        groupId: group.id,
      };
      FunctionalityService.deleteGroup(groupData).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200) {
            toaster.success({ title: response.data.message });
            $scope.getAllGroupList();
          } else {
            toaster.error({ title: response.data.message });
          }
        },
        function (err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.confirmDeleteExpiry = function (groupEBD) {
      DeletePopup.confirm(
        'Delete Certificate',
        'Are you sure you want to delete this certificate?',
        function () {
          $scope.loader = true;
          let data = {
            documentHolderId: groupEBD.documentHolderId,
            groupId: groupEBD.groupId,
            loginId: $scope.sessionObject.userId,
          };

          FunctionalityService.deleteGroupExpiryDocument(
            JSON.stringify(data)
          ).then(
            function (response) {
              $scope.loader = false;
              if (response.status === 200) {
                toaster.success({ title: response.data.message });
                // Update UI WITHOUT reload
                $scope.groupEBDList = $scope.groupEBDList.filter(
                  (item) => item.documentHolderId !== groupEBD.documentHolderId
                );
              } else {
                toaster.error({ title: response.data.message });
              }
            },
            function (err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        },
        'Delete'
      );
    };

    $scope.confirmDelete = function (group) {
      DeletePopup.confirm(
        'Delete Group',
        'Are you sure you want to delete this group?',
        function () {
          $scope.deleteGroup(group);
        }
      );
    };

    //This method is used to share expiry document with windows mail
    // $scope.shareExpiryDocumentWithMail = function (group) {
    //   $scope.loader = true;
    //   $scope.expDocUrlGroup = '';
    //   FunctionalityService.getAllGroupExpiryDocumentList(group.id).then(
    //     function (response) {
    //       $scope.loader = false;
    //       $scope.groupExpiryList = response.data.expiryDocumentList;

    //       if ($scope.groupExpiryList && $scope.groupExpiryList.length > 0) {
    //         angular.forEach($scope.groupExpiryList, function (value) {
    //           $scope.expDocUrlGroup +=
    //             '\n' +
    //             value.documentHolderName +
    //             ':\n' +
    //             value.documentDownloadUrl +
    //             '\n';
    //         });

    //         $scope.expDocUrlshare = $scope.expDocUrlGroup;
    //         $scope.groupEmailForShare = group.emailId;
    //         $scope.libshipNameDoc = $scope.groupShipName + ' Documents';
    //         if (CKEDITOR.instances.mailEditor) {
    //           CKEDITOR.instances.mailEditor.setData($scope.expDocUrlshare);
    //         }
    //         $('#shareMail').modal('show');
    //       } else {
    //         toaster.clear();
    //         toaster.info({ title: 'No documents found' });
    //       }
    //     },
    //     function (err) {
    //       $scope.loader = false;
    //       console.log('Error response', err);
    //     }
    //   );
    // };
    //This method is used to share expiry document with windows mail
    $scope.shareExpiryDocumentWithMail = function (group) {
      $scope.loader = true; // Start the loader
      $scope.expDocUrlGroup = '';

      FunctionalityService.getAllGroupExpiryDocumentList(group.id).then(
        function (response) {
          $scope.groupExpiryList = response.data.expiryDocumentList;

          if ($scope.groupExpiryList && $scope.groupExpiryList.length > 0) {
            // --- ADD THIS NEW SORTING LOGIC ---
            // This will sort the documents by the number at the beginning of their name
            $scope.groupExpiryList.sort(function (a, b) {
              var numA = parseInt(a.documentHolderName.match(/\d+/)) || 0;
              var numB = parseInt(b.documentHolderName.match(/\d+/)) || 0;
              return numA - numB;
            });
            // ----------------------------------
            // FIX 1: Use HTML (<br> and <a> tags) instead of plain text (\n)
            angular.forEach($scope.groupExpiryList, function (value) {
              $scope.expDocUrlGroup +=
                '<strong>' +
                value.documentHolderName +
                ':</strong><br>' +
                '<a href="' +
                value.documentDownloadUrl +
                '">' +
                value.documentDownloadUrl +
                '</a><br><br>';
            });

            $scope.expDocUrlshare = $scope.expDocUrlGroup;
            $scope.groupEmailForShare = group.emailId;
            $scope.libshipNameDoc = $scope.groupShipName + ' Documents';

            // Open the modal immediately
            $('#shareMail').modal('show');

            // FIX 2: Wait 300ms for Bootstrap's sliding animation to fully finish before setting data
            $timeout(function () {
              CKEDITOR.config.versionCheck = false;

              if (!CKEDITOR.instances.mailEditor) {
                // First time opening: initialize editor
                var editor = CKEDITOR.replace('mailEditor', {
                  height: 150,
                  removePlugins: 'elementspath',
                  resize_enabled: false,
                  allowedContent: true,
                  extraAllowedContent: '*(*);*{*}',
                });

                // Wait for CKEditor to finish building its UI
                editor.on('instanceReady', function () {
                  $timeout(function () {
                    // Wrap in $timeout to stay in Angular's digest cycle
                    CKEDITOR.instances.mailEditor.setData(
                      $scope.expDocUrlshare
                    );
                    $scope.loader = false; // Turn off loader safely
                  });
                });
              } else {
                // Second time onwards: editor is already built
                CKEDITOR.instances.mailEditor.setData($scope.expDocUrlshare);
                $scope.loader = false; // Turn off loader instantly
              }
            }, 300);
          } else {
            $scope.loader = false; // Hide loader if no data
            toaster.clear();
            toaster.info({ title: 'No documents found' });
          }
        },
        function (err) {
          $scope.loader = false; // Hide loader on error
          console.log('Error response', err);
        }
      );
    };
    // --- Variables for the View Modal ---
    $scope.groupEBDList = [];
    $scope.itemsPerPageGroupEbd = 10;
    $scope.currentPageGroupEbd = 1;
    $scope.selectedGroupName = '';
    $scope.selectedGroupId = null;

    // --- Opens the Modal and fetches the list ---
    $scope.viewGroupDetails = function (group) {
      $scope.loader = true;
      $scope.selectedGroupName = group.groupName;
      $scope.selectedGroupId = group.id;

      FunctionalityService.getAllGroupExpiryDocumentList(group.id).then(
        function (response) {
          $scope.loader = false;
          $scope.groupEBDList = response.data.expiryDocumentList;
          $('#viewGroupModal').modal('show');
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
          toaster.error({ title: 'Failed to load certificates.' });
        }
      );
    };

    // ==========================================
    // CUSTOM TOP SLIDE-DOWN DOCUMENT VIEWER LOGIC
    // ==========================================
    $scope.isTopViewerOpen = false;
    $scope.previewDocUrl = '';
    $scope.viewDocName = '';

    $scope.viewDocument = function (documentObj) {
      if (documentObj && documentObj.documentPreviewUrl) {
        $scope.previewDocUrl = $sce.trustAsResourceUrl(
          documentObj.documentPreviewUrl
        );
        $scope.viewDocName = documentObj.documentHolderName;
        // Trigger the slide-down animation
        $scope.isTopViewerOpen = true;
      } else {
        toaster.clear();
        toaster.info({ title: 'No preview available for this document.' });
      }
    };

    $scope.closeTopViewer = function () {
      $scope.isTopViewerOpen = false;
      // Delay clearing the URL to allow the CSS slide-up animation to finish smoothly (400ms)
      $timeout(function () {
        $scope.previewDocUrl = '';
        $scope.viewDocName = '';
      }, 400);
    };
    // ==========================================

    $scope.submitEmailRequest = function () {
      if (CKEDITOR.instances.mailEditor) {
        $scope.expDocUrlshare = CKEDITOR.instances.mailEditor.getData();
      }
      $scope.loader = true;
      var emailData = {
        to: $scope.groupEmailForShare
          .split(',')
          .map(function (e) {
            return e.trim();
          })
          .filter(function (e) {
            return e.length > 0;
          }),
        subject: $scope.libshipNameDoc,
        body: $scope.expDocUrlshare,
      };
      FunctionalityService.sendEmail(emailData)
        .then(function (response) {
          toaster.pop('success', 'Success', 'Email sent successfully');
          $('#shareMail').modal('hide');
        })
        .catch(function (error) {
          toaster.pop('error', 'Error', 'Failed to send email');
          console.error(error);
        })
        .finally(function () {
          $scope.loader = false;
        });
    };
    // //editor intialize
    // $timeout(function () {
    //   CKEDITOR.config.versionCheck = false;
    //   if (!CKEDITOR.instances.mailEditor) {
    //     CKEDITOR.replace('mailEditor', {
    //       height: 150,
    //       removePlugins: 'elementspath',
    //       resize_enabled: false,
    //       allowedContent: true,
    //       extraAllowedContent: '*(*);*{*}',
    //     });
    //   }
    // }, 500);
  },
]);

userMyWorkspaceList.directive('customFocus', [
  function () {
    var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$focused = false;

        element
          .bind('focus', function (evt) {
            element.addClass(FOCUS_CLASS);
            scope.$apply(function () {
              ctrl.$focused = true;
            });
          })
          .bind('blur', function (evt) {
            element.removeClass(FOCUS_CLASS);
            scope.$apply(function () {
              ctrl.$focused = false;
            });
          });
      },
    };
  },
]);

userMyWorkspaceList.directive('validateEmail', function () {
  var EMAIL_REGEXP =
    /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
  return {
    link: function (scope, elm) {
      elm.on('keyup', function () {
        var isMatchRegex = EMAIL_REGEXP.test(elm.val());
        if (
          (isMatchRegex && elm.hasClass('warning-email')) ||
          elm.val() == ''
        ) {
          elm.removeClass('warning-email');
        } else if (isMatchRegex == false && !elm.hasClass('warning-email')) {
          elm.addClass('warning-email');
        }
      });
    },
  };
});

//Directive for File Upload
userMyWorkspaceList.directive('fileModel', [
  '$parse',
  function ($parse) {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var model = $parse(attrs.fileModel);
        var modelSetter = model.assign;

        element.bind('change', function () {
          scope.$apply(function () {
            modelSetter(scope, element[0].files[0]);
          });
        });
      },
    };
  },
]);
