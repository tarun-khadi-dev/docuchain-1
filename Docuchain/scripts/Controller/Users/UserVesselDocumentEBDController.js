// var userVesselDocEBD = angular.module('dapp.UserVesselDocumentEBDController', [
//   'ui.select',
//   'angularUtils.directives.dirPagination',
//   'toaster',
//   'moment-picker',
//   '720kb.tooltips',
//   'ngSanitize',
// ]);

// userVesselDocEBD.controller('UserVesselDocumentEBDController', [
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
//     FunctionalityService
//   ) {
//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );
//     $scope.libShipId;
//     $scope.libshipName;
//     $scope.loader = false;

//     // $scope.group = {};
//     // $scope.vesselSelectedList = [];

//     $scope.group = {
//       vesselSelectedList: [],
//     };

//     $scope.formattedIssueDate = '';
//     $scope.formattedExpiryDate = '';
//     $scope.formattedlastAnnual = '';
//     $scope.formattednextAnnual = '';
//     $scope.issuingAuthority = '';
//     $scope.notfound = true;
//     $scope.currentPage = 1;
//     $scope.viewby = 73;
//     $scope.roleIdForView = $scope.sessionObject.roleId;
//     $scope.itemsPerPageForHistory = $scope.viewby;
//     itemsPerPageHistory = $scope.viewby;
//     $scope.activeText = 'Active';
//     if ($scope.sessionObject.roleId != 3) {
//       $scope.libShipId = $window.localStorage.getItem('libShipId');
//       $scope.libshipName = $window.localStorage.getItem('libshipName');
//       $scope.libshipNameDoc = $scope.libshipName + ' ' + 'Documents';
//     } else if ($scope.sessionObject.shipProfileInfos.length > 0) {
//       $scope.libShipId = $scope.sessionObject.shipProfileInfos[0].id;
//       $scope.libshipName = $scope.sessionObject.shipProfileInfos[0].shipName;
//     }

//     //for creating group
//     $scope.oldAndNewExpDocWithoutDub = [];

//     $scope.selctedcheckboxlst = [];
//     $scope.expiryDocumentHolderList = [];
//     $scope.expiryDocumentHolderListLen = $scope.expiryDocumentHolderList.length;

//     $scope.viewDocumentObjectInfo;
//     $scope.viewDocumentUrl;
//     $scope.historyEBDObject;

//     $rootScope.convertedFile = '';
//     $rootScope.uploadDocFile;
//     $scope.loader = false;
//     $rootScope.dcoumentHolderName;
//     $rootScope.docId;
//     $scope.documentHolderHistory = [];
//     $scope.documentHolderHistoryLength = $scope.documentHolderHistory.length;
//     $scope.groupTagExpiryDoc;
//     $scope.groupListExpiry;
//     $scope.shareExpDoc = [];
//     $scope.records = false;

//     $scope.selectedDocumentHolderIds = [];
//     $scope.oldAndNewExpDocWithoutDub = [];
//     $scope.vesselList = [];
//     $scope.expiryDocumentHolderListFileter = [];
//     $scope.VesselListLenEBD = $scope.vesselList.length;

//     console.log(
//       "$window.localStorage.getItem('userId');",
//       $window.localStorage.getItem('userId')
//     );
//     $scope.userDetails = function () {
//       $state.go('dapp.userProfile');
//     };

//     $scope.getAllExpiryList = function () {
//       $scope.loader = true;
//       var archivedStatus = 0;
//       FunctionalityService.getAllExpiryDocumentList(
//         $scope.libShipId,
//         archivedStatus
//       ).then(
//         function (response) {
//           if (response.status == 200 || response.status == 201) {
//             $scope.expiryDocumentHolderList = response.data.expiryDocumentList;
//             $scope.expiryDocumentHolderListFileter =
//               $scope.expiryDocumentHolderList;
//             $scope.expiryDocumentHolderListLen =
//               $scope.expiryDocumentHolderList.length;
//             $scope.loader = false;

//             console.log(
//               'response.data.expiryDocumentList' +
//                 JSON.stringify($scope.expiryDocumentHolderList)
//             );

//             // --- REDIRECTION FILTER LOGIC START ---
//             // Check if we navigated here from the dashboard with a specific filter
//             var savedStatus = $window.localStorage.getItem(
//               'redirectFilterStatus'
//             );
//             if (savedStatus) {
//               // Apply the filter programmatically
//               $scope.documentTypeFilter(savedStatus);
//               // Set the dropdown model so the UI matches the filter
//               $scope.selectStatus = savedStatus;
//               // Clear the storage so it doesn't affect subsequent manual navigations
//               $window.localStorage.removeItem('redirectFilterStatus');
//             }
//             // --- REDIRECTION FILTER LOGIC END ---
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           if ($scope.expiryDocumentHolderList.length == 0) {
//             $scope.records = true;
//           } else {
//             $scope.records = false;
//           }
//           console.log('Error response', err);
//         }
//       );
//       var data1 = {
//         userId: $scope.sessionObject.userId,
//         roleId: $scope.sessionObject.roleId,
//       };

//       // to display vessels in dropdown while creating EBD
//       FunctionalityService.getShipProfileList($scope.sessionObject.userId).then(
//         function (response) {
//           if (response.status == 200) {
//             $scope.vesselList = response.data.shipProfileList;
//             console.log('All Org Vessels:', $scope.vesselList);
//           }
//         },
//         function myError(err) {
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.getAllGroupList = function () {
//       $scope.loader = true;

//       var groupdata = {
//         userProfileId: $scope.sessionObject.userId,
//         shipId: $scope.libShipId,
//       };

//       FunctionalityService.getGroupListShip(groupdata).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.message = JSON.stringify(response.data.groupList);
//             $scope.groupList = response.data.groupList;
//             $scope.groupListLength = $scope.groupList.length;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.faq = function () {
//       // $state.go('dapp.faq'); // REDIRECT DISABLED
//     };

//     $scope.saveAsDraft = function () {
//       $scope.loader = true;

//       // Prepare data (same as createDocument but logic handles status on backend)
//       var draftData = {
//         certificateNumber: $scope.libcreate.certificateNumber,
//         placeOfIssue: $scope.libcreate.placeOfIssue,
//         issueDate: $scope.dateConversion($scope.libcreate.dateOfIssue),
//         expiryDate: $scope.dateConversion($scope.libcreate.dateOfExpiry),
//         lastAnnual: $scope.dateConversion($scope.libcreate.lastAnnual),
//         nextAnnual: $scope.dateConversion($scope.libcreate.nextAnnual),
//         uploadedUserId: $scope.sessionObject.userId,
//         shipProfileId: $scope.libShipId,
//         documentHolderId: $rootScope.docId,
//         remarks: $scope.libcreate.remarks,
//         issuingAuthority: $scope.issuingAuthority,
//         // --- ADD THESE NEW FIELDS ---
//         draftId: $scope.currentDraftId || null,
//         documentDataId: $scope.currentDocumentDataId || null,
//         isDraft: true,
//       };

//       FunctionalityService.saveDraftDocument(
//         JSON.stringify(draftData),
//         $rootScope.uploadDocFile
//       ).then(
//         function (response) {
//           $scope.loader = false;
//           if (response.status == 200) {
//             $('#createEBD').modal('hide');
//             $scope.clearFields();
//             $scope.getAllExpiryList();
//             toaster.success({ title: 'Draft saved successfully' });
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           toaster.error({ title: 'Failed to save draft' });
//         }
//       );
//     };

//     $scope.uploadFile = function () {
//       if (!$scope.myFile) return;

//       // 1. Instant PDF Preview
//       $rootScope.convertedFile = $sce.trustAsResourceUrl(
//         URL.createObjectURL($scope.myFile)
//       );
//       $rootScope.uploadDocFile = $scope.myFile; // Crucial for final submit

//       // 2. Clear old fields and LOCK UI
//       $scope.isScanning = true;
//       $scope.libcreate = {};
//       $scope.issuingAuthority = '';

//       $('#fileupload').modal('hide');
//       $('#createEBD').modal('show');

//       // 3. Call OCR Scan
//       FunctionalityService.scanExpiryDocument($scope.myFile).then(
//         function (response) {
//           // Use $timeout to ensure data-binding occurs and UI unlocks
//           $timeout(function () {
//             $scope.isScanning = false;

//             if (response.status == 200 || response.status == 201) {
//               var scannedData = response.data.expiryDocumentDTOs;
//               if (scannedData != null) {
//                 $scope.issuingAuthority = scannedData.issuingAuthority || '';
//                 $scope.libcreate.certificateNumber =
//                   scannedData.certificateNumber || '';
//                 $scope.libcreate.placeOfIssue = scannedData.placeOfIssue || '';
//                 $scope.libcreate.remarks = scannedData.remarks || '';

//                 // Date Formatting
//                 if (scannedData.issueDateString) {
//                   $scope.libcreate.dateOfIssue = moment(
//                     scannedData.issueDateString,
//                     'DD-MM-YYYY'
//                   ).format('DD-MM-YYYY');
//                 }
//                 if (scannedData.expiryDateString) {
//                   $scope.libcreate.dateOfExpiry = moment(
//                     scannedData.expiryDateString,
//                     'DD-MM-YYYY'
//                   ).format('DD-MM-YYYY');
//                 }

//                 // Reset Annuals as they aren't usually scanned
//                 $scope.libcreate.lastAnnual = '';
//                 $scope.libcreate.nextAnnual = '';
//               }
//             }
//           }, 500);
//         },
//         function (error) {
//           $scope.isScanning = false;
//           toaster.error({ title: 'Extraction failed, please fill manually.' });
//         }
//       );
//     };
//     $scope.clearFileField = function () {
//       $('#filename').val('');
//       $scope.loader = false;
//       $scope.isDisabled = false;
//     };

//     $scope.storeDocumentholder = function (obj) {
//       $rootScope.dcoumentHolderName = obj.documentHolderName;
//       $rootScope.docId = obj.documentHolderId;
//       $scope.clearFileField();
//     };

//     $scope.createDocument = function () {
//       $scope.loader = true;

//       // Convert all 4 date fields using the conversion helper before saving
//       $scope.formattedIssueDate = $scope.dateConversion(
//         $scope.libcreate.dateOfIssue
//       );
//       $scope.formattedExpiryDate = $scope.dateConversion(
//         $scope.libcreate.dateOfExpiry
//       );
//       $scope.formattedlastAnnual = $scope.dateConversion(
//         $scope.libcreate.lastAnnual
//       );
//       $scope.formattednextAnnual = $scope.dateConversion(
//         $scope.libcreate.nextAnnual
//       );

//       $scope.saveData = {
//         certificateNumber: $scope.libcreate.certificateNumber,
//         placeOfIssue: $scope.libcreate.placeOfIssue,
//         issueDate: $scope.formattedIssueDate,
//         expiryDate: $scope.formattedExpiryDate,
//         lastAnnual: $scope.formattedlastAnnual,
//         nextAnnual: $scope.formattednextAnnual,
//         uploadedUserId: $scope.sessionObject.userId,
//         loginId: $scope.sessionObject.userId,
//         shipProfileId: $scope.libShipId,
//         documentHolderId: $rootScope.docId,
//         remarks: $scope.libcreate.remarks,
//         issuingAuthority: $scope.issuingAuthority,
//         // --- ADD THESE NEW FIELDS ---
//         draftId: $scope.currentDraftId || null,
//         documentDataId: $scope.currentDocumentDataId || null,
//         isDraft: false,
//       };

//       FunctionalityService.saveExpiryDocument(
//         JSON.stringify($scope.saveData),
//         $rootScope.uploadDocFile
//       ).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#createEBD').modal('hide');
//             $('#filename').val('');
//             $rootScope.uploadDocFile = '';
//             $scope.clearFields();
//             $scope.getAllExpiryList();
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

//     // Helper to convert DD-MM-YYYY string format to YYYY-MM-DD for the API
//     $scope.dateConversion = function (day) {
//       if (!day || day == '') {
//         return null;
//       }
//       var stringDate1 = day;
//       var splitDate1 = stringDate1.split('-');
//       var day1 = splitDate1[0];
//       var month1 = splitDate1[1];
//       var year1 = splitDate1[2];
//       return year1 + '-' + month1 + '-' + day1;
//     };

//     $scope.cancelExpiry = function () {
//       $('#viewExpiryDocument').modal('hide');
//     };

//     $scope.updateExpiryDoc = function () {
//       $scope.loader = true;

//       // Convert all 4 date fields using the conversion helper before updating
//       if (
//         $scope.issueDateStringEdit != '' &&
//         $scope.issueDateStringEdit != undefined
//       ) {
//         $scope.formattedIssueDate = $scope.dateConversion(
//           $scope.issueDateStringEdit
//         );
//       }
//       if (
//         $scope.expiryDateStringEdit != '' &&
//         $scope.expiryDateStringEdit != undefined
//       ) {
//         $scope.formattedExpiryDate = $scope.dateConversion(
//           $scope.expiryDateStringEdit
//         );
//       }
//       if (
//         $scope.lastAnnualStringEdit != '' &&
//         $scope.lastAnnualStringEdit != undefined
//       ) {
//         $scope.formattedlastAnnual = $scope.dateConversion(
//           $scope.lastAnnualStringEdit
//         );
//       }
//       if (
//         $scope.nextAnnualStringEdit != '' &&
//         $scope.nextAnnualStringEdit != undefined
//       ) {
//         $scope.formattednextAnnual = $scope.dateConversion(
//           $scope.nextAnnualStringEdit
//         );
//       }

//       var data = {
//         id: $scope.docId,
//         certificateNumber: $scope.certificateNumberEdit,
//         placeOfIssue: $scope.placeOfIssueEdit,
//         issueDate: $scope.formattedIssueDate,
//         expiryDate: $scope.formattedExpiryDate,
//         lastAnnual: $scope.formattedlastAnnual,
//         nextAnnual: $scope.formattednextAnnual,
//         loginId: $scope.sessionObject.userId,
//         uploadedUserId: $scope.sessionObject.userId,
//         shipProfileId: $scope.libShipId,
//         documentHolderId: $scope.docHolderId,
//         remarks: $scope.remarksEdit,
//         issuingAuthority: $scope.issuingAuthorityEdit,
//       };

//       FunctionalityService.updateExpiryDocument(data).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $('#viewExpiryDocument').modal('hide');
//             $scope.getAllExpiryList(); // Refresh list to reflect changes
//             $timeout(function () {
//               toaster.clear();
//               toaster.success({ title: response.data.message });
//             }, 1000);
//           } else {
//             toaster.clear();
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           toaster.error({ title: 'Error updating document' });
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.clearFields = function () {
//       $scope.libcreate.certificateNumber = '';
//       $scope.libcreate.placeOfIssue = '';
//       $scope.libcreate.dateOfIssue = '';
//       $scope.libcreate.dateOfExpiry = '';
//       $scope.libcreate.lastAnnual = '';
//       $scope.libcreate.nextAnnual = '';
//       $scope.libcreate.remarks = '';
//       $scope.issuingAuthority = '';
//       $('#filename').val('');
//       $scope.loader = false;
//       $scope.uploadDocFile = '';
//       // --- ADD THESE RESETS ---
//       $scope.currentDraftId = null;
//       $scope.currentDocumentDataId = null;
//     };

//     // $scope.handleDraftClick = function (doc) {
//     //   Swal.fire({
//     //     html: `<div class="logout-card">
//     //             <div class="logout-icon">
//     //                 <i class="fa fa-file-text-o"></i>
//     //             </div>
//     //             <h3>Draft Available</h3>
//     //             <p>Do you want to resume your saved draft or upload a completely new document?</p>
//     //         </div>`,
//     //     width: 480,
//     //     showCancelButton: true,
//     //     confirmButtonColor: '#4a7be6', // Match the primary blue
//     //     cancelButtonColor: '#6c757d', // Match the secondary grey
//     //     confirmButtonText: 'Resume Draft',
//     //     cancelButtonText: 'Upload New',
//     //   }).then(function (result) {
//     //     if (result.isConfirmed) {
//     //       // User chose to "Resume Draft" (Blue Button)
//     //       $scope.$apply(function () {
//     //         $scope.resumeDraft(doc);
//     //       });
//     //     } else if (result.dismiss === Swal.DismissReason.cancel) {
//     //       // User chose to "Upload New" (Grey Button)
//     //       $scope.$apply(function () {
//     //         // Clear any existing draft tracking so it acts like a brand new upload
//     //         $scope.currentDraftId = doc.draftId; // Keep draftId so backend overwrites/deletes it
//     //         $scope.currentDocumentDataId = null;

//     //         // Trigger the standard upload flow
//     //         $scope.storeDocumentholder(doc);
//     //         $('#fileupload').modal('show');
//     //       });
//     //     }
//     //   });
//     // };
//     $scope.handleDraftClick = function (doc) {
//       Swal.fire({
//         html: `<div class="logout-card">
//                 <div class="logout-icon">
//                     <i class="fa fa-file-text-o"></i>
//                 </div>
//                 <h3>Draft Available</h3>
//                 <p>Do you want to resume your saved draft or upload a completely new document?</p>
//             </div>`,
//         width: 480,
//         showCloseButton: true, // <--- THIS BRINGS THE "X" BACK
//         showCancelButton: true,
//         confirmButtonColor: '#4a7be6', // Blue color for resume
//         cancelButtonColor: '#6c757d', // Grey color for new upload
//         confirmButtonText: 'Resume Draft',
//         cancelButtonText: 'Upload New',
//       }).then(function (result) {
//         if (result.isConfirmed) {
//           // User chose to "Resume Draft"
//           $scope.$apply(function () {
//             $scope.resumeDraft(doc);
//           });
//         } else if (result.dismiss === Swal.DismissReason.cancel) {
//           // User chose to "Upload New"
//           $scope.$apply(function () {
//             // Clear any existing draft tracking so it acts like a brand new upload
//             $scope.currentDraftId = doc.draftId;
//             $scope.currentDocumentDataId = null;

//             // Trigger the standard upload flow
//             $scope.storeDocumentholder(doc);
//             $('#fileupload').modal('show');
//           });
//         }
//       });
//     };
//     $scope.closeViewPopup = function () {};

//     $scope.viewExpiryDocumentInformation = function (documentObj) {
//       $scope.viewDocumentObjectInfo = documentObj;
//       $scope.docId = documentObj.id;
//       $scope.docHolderId = documentObj.documentHolderId;

//       $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
//         documentObj.documentPreviewUrl
//       );

//       $scope.certificateNumberEdit = documentObj.certificateNumber;
//       $scope.issuingAuthorityEdit = documentObj.issuingAuthority;
//       $scope.placeOfIssueEdit = documentObj.placeOfIssue;
//       $scope.issueDateStringEdit = documentObj.issueDateString;
//       $scope.expiryDateStringEdit = documentObj.expiryDateString;
//       $scope.lastAnnualStringEdit = documentObj.lastAnnualString;
//       $scope.nextAnnualStringEdit = documentObj.nextAnnualString;
//       $scope.remarksEdit = documentObj.remarks;

//       $('#viewExpiryDocument').modal('show');
//     };

//     $scope.openHistoryPopup = function (documentObj) {
//       $scope.loader = true;

//       $('#historyEBD').modal('toggle');
//       $scope.historyEBDObject = documentObj;
//       var historyRequest = {
//         documentHolderId: documentObj.documentHolderId,
//         shipProfileId: $scope.libShipId,
//       };
//       FunctionalityService.getDocumentHolderHistory(historyRequest).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $scope.documentHolderHistory = response.data.expiryDocumentList;
//             $scope.documentHolderHistoryLength =
//               $scope.documentHolderHistory.length;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     // $scope.openGroupTagEBDPopup = function (expiryDataObj) {
//     //   $scope.loader = true;

//     //   $scope.groupTagExpiryDoc = expiryDataObj;

//     //   var groupdata = {
//     //     userProfileId: $scope.sessionObject.userId,
//     //     shipId: $scope.libShipId,
//     //   };

//     //   FunctionalityService.getGroupListShip(groupdata).then(
//     //     function (response) {
//     //       $scope.loader = false;

//     //       if (response.status == 200) {
//     //         $scope.message = JSON.stringify(response.data.groupList);
//     //         $scope.groupListExpiry = response.data.groupList;
//     //         $scope.groupListExpiryLength = $scope.groupListExpiry.length;
//     //       }
//     //     },
//     //     function myError(err) {
//     //       $scope.loader = false;
//     //       console.log('Error response', err);
//     //     }
//     //   );
//     // };

//     // group tag share when selected multiple placeholders
//     //     $scope.openGroupTagEBDPopup = function(expiryDataObj) {

//     //       if ($scope.selctedExpiredDocumentList.length === 0) {
//     //         toaster.clear();
//     //         toaster.info({
//     //             title: 'Please select the document before tagging',
//     //         });
//     //         return;
//     //     }
//     //     $scope.loader = true;
//     //     // Handle selection
//     //     if ($scope.selctedExpiredDocumentList.length > 0) {
//     //         $scope.groupTagExpiryDocList = angular.copy($scope.selctedExpiredDocumentList);
//     //     } else {
//     //         $scope.groupTagExpiryDocList = [expiryDataObj];
//     //     }
//     //     var groupdata = {
//     //         userProfileId: $scope.sessionObject.userId,
//     //         shipId: $scope.libShipId,
//     //     };
//     //     FunctionalityService.getGroupListShip(groupdata)
//     //     .then(function(response) {
//     //         $scope.loader = false;
//     //         if (response.status == 200) {
//     //             $scope.groupListExpiry = response.data.groupList;
//     //             $('#tagGroupPopup').modal('show');
//     //         }
//     //     }, function(err) {
//     //         $scope.loader = false;
//     //         console.log('Error response', err);
//     //     });
//     // };

//     $scope.openGroupTagEBDPopup = function (expiryDataObj) {
//       //  CASE 1: Bulk selection
//       if ($scope.selctedExpiredDocumentList.length > 0) {
//         $scope.groupTagExpiryDocList = angular.copy(
//           $scope.selctedExpiredDocumentList
//         );
//       }
//       //  CASE 2: Single row click
//       else if (expiryDataObj) {
//         $scope.groupTagExpiryDocList = [expiryDataObj];
//       }
//       //  CASE 3: Nothing selected
//       else {
//         toaster.clear();
//         toaster.info({
//           title: 'Please select the document before tagging',
//         });
//         return;
//       }
//       $scope.loader = true;

//       var groupdata = {
//         userProfileId: $scope.sessionObject.userId,
//         shipId: $scope.libShipId,
//       };
//       FunctionalityService.getGroupListShip(groupdata).then(
//         function (response) {
//           $scope.loader = false;
//           if (response.status == 200) {
//             $scope.groupListExpiry = response.data.groupList;
//             // Open modal
//             $('#tagGroupPopup').modal('show');
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     ///////////////////////////////////////////////////////////////

//     //   $scope.tagExpiryDocumentToGRoup = function () {
//     //     $scope.loader = true;

//     //     var documentHolderIds = [];

//     //     angular.forEach($scope.selctedExpiredDocumentList, function (doc) {
//     //         documentHolderIds.push(doc.documentHolderId);
//     //     });

//     //     var payload = {
//     //         groupId: $scope.gorupSelected,
//     //         userProfileId: $scope.sessionObject.userId,
//     //         documentHolderIds: documentHolderIds,
//     //         loginId: $scope.sessionObject.userId
//     //     };

//     //     FunctionalityService.addMultipleExpiryDocToGroup(payload)
//     //     .then(function (response) {
//     //         $scope.loader = false;

//     //         if (response.status == 200) {
//     //             $('#tagGroupPopup').modal('hide');

//     //             toaster.success({
//     //                 title: "Documents tagged successfully"
//     //             });

//     //             // reset selection
//     //             $scope.checkall = false;
//     //             $scope.selctedExpiredDocumentList = [];

//     //         } else {
//     //             toaster.error({
//     //                 title: "Error tagging documents"
//     //             });
//     //         }

//     //     }, function (err) {
//     //         $scope.loader = false;
//     //         console.log(err);
//     //     });
//     // };
//     $scope.resumeDraft = function (doc) {
//       // 1. Set global document tracking variables
//       $rootScope.dcoumentHolderName = doc.documentHolderName;
//       $rootScope.docId = doc.documentHolderId;

//       // 2. Track draft-specific IDs so backend knows we are resuming
//       $scope.currentDraftId = doc.draftId;
//       $scope.currentDocumentDataId = doc.documentDataId;

//       // 3. Pre-fill the form with draft data
//       $scope.libcreate = {
//         certificateNumber: doc.certificateNumber,
//         placeOfIssue: doc.placeOfIssue,
//         dateOfIssue: doc.issueDateString,
//         dateOfExpiry: doc.expiryDateString,
//         lastAnnual: doc.lastAnnualString,
//         nextAnnual: doc.nextAnnualString,
//         remarks: doc.remarks,
//       };
//       $scope.issuingAuthority = doc.issuingAuthority;

//       // 4. Set the PDF Preview URL and clear any pending file uploads
//       if (doc.documentPreviewUrl) {
//         $rootScope.convertedFile = $sce.trustAsResourceUrl(
//           doc.documentPreviewUrl
//         );
//       }
//       $rootScope.uploadDocFile = null;
//       $scope.isScanning = false;

//       // 5. Open the Creation Form Modal directly (skip the upload modal)
//       $('#createEBD').modal('show');
//     };

//     $scope.tagExpiryDocumentToGRoup = function () {
//       if (
//         !$scope.groupTagExpiryDocList ||
//         $scope.groupTagExpiryDocList.length === 0
//       ) {
//         toaster.error({ title: 'No document selected' });
//         return;
//       }

//       $scope.loader = true;

//       var documentHolderIds = [];

//       angular.forEach($scope.groupTagExpiryDocList, function (doc) {
//         documentHolderIds.push(doc.documentHolderId);
//       });

//       var payload = {
//         groupId: $scope.gorupSelected,
//         userProfileId: $scope.sessionObject.userId,
//         documentHolderIds: documentHolderIds,
//         loginId: $scope.sessionObject.userId,
//       };

//       FunctionalityService.addMultipleExpiryDocToGroup(payload).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $('#tagGroupPopup').modal('hide');

//             toaster.success({
//               title: 'Documents tagged successfully',
//             });

//             // Reset
//             $scope.checkall = false;
//             $scope.selctedExpiredDocumentList = [];
//             $scope.groupTagExpiryDocList = [];
//           } else {
//             toaster.error({
//               title: response.data.message,
//             });
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log(err);
//         }
//       );
//     };

//     $scope.clearTagDocumentFields = function () {
//       $scope.gorupSelected = '';
//     };

//     $scope.close = function () {};

//     $scope.selctedExpiredDocumentList = [];

//     $scope.checkUncheckAll = function () {
//       $scope.selctedExpiredDocumentList = [];
//       $scope.selectedDocumentHolderIds = [];
//       if ($scope.checkall) {
//         $scope.checkall = true;
//         angular.forEach($scope.expiryDocumentHolderList, function (value) {
//           if (value.id != undefined) {
//             $scope.selctedExpiredDocumentList.push(value);
//             $scope.selectedDocumentHolderIds.push(value.documentHolderId);
//           }
//         });
//       } else {
//         $scope.checkall = false;
//       }
//       angular.forEach($scope.expiryDocumentHolderList, function (user) {
//         user.checked = $scope.checkall;
//       });
//     };

//     $scope.updateCheckall = function ($index, user) {
//       var userTotal = $scope.expiryDocumentHolderList.length;
//       var count = 0;
//       $scope.selctedExpiredDocumentList = [];
//       $scope.selectedDocumentHolderIds = [];
//       angular.forEach($scope.expiryDocumentHolderList, function (item) {
//         if (item.checked) {
//           count++;
//           $scope.selctedExpiredDocumentList.push(item);
//           $scope.selectedDocumentHolderIds.push(item.documentHolderId);
//         }
//       });
//       if (userTotal == count) {
//         $scope.checkall = true;
//       } else {
//         $scope.checkall = false;
//       }
//     };

//     $scope.groupExpiryDoclist = [];

//     $scope.viewGroup = function (group) {
//       $scope.loader = true;

//       // ✅ Reset previous data
//       $scope.groupExpiryDoclist = [];

//       $scope.groupEmailForShare = group.emailId;
//       $scope.groupIdForShare = group.id;

//       FunctionalityService.viewGroup(group.id).then(
//         function (response) {
//           $scope.loader = false;

//           var groupInfo = response.data.groupInfo || {};

//           $scope.groupExpiryDoclist = groupInfo.expiryDocumentDtos || [];

//           console.log('Loaded group docs:', $scope.groupExpiryDoclist);

//           $scope.showUpdateIngroupCheck = true;
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.IsVisible = false;
//     $scope.ShowCreateGroup = function () {
//       $scope.IsVisible = true;
//       $scope.groupExpiryDoclist = [];
//       $scope.groupIdForShare = '';
//       $scope.showUpdateIngroupCheck = true;
//     };

//     // This watcher listens for changes in the selected vessels list. When a vessel is selected, it automatically loads the associated documents for that vessel and updates the UI accordingly.
//     $scope.$watch(
//       'group.vesselSelectedList',
//       function (newVal) {
//         if (newVal && newVal.length > 0) {
//           $scope.groupExpiryDoclist = [];
//           angular.forEach(newVal, function (vessel) {
//             FunctionalityService.getAllExpiryDocumentList(vessel.id, 0).then(
//               function (response) {
//                 if (response.status === 200) {
//                   angular.forEach(
//                     response.data.expiryDocumentList,
//                     function (doc) {
//                       if (doc.id != null && doc.documentStatus === 'Approved') {
//                         // Avoid duplicates
//                         var exists = $scope.groupExpiryDoclist.some(
//                           function (d) {
//                             return d.id === doc.id;
//                           }
//                         );
//                         if (!exists) {
//                           $scope.groupExpiryDoclist.push(doc);
//                         }
//                       }
//                     }
//                   );
//                   console.log(
//                     'Updated Existing Docs:',
//                     $scope.groupExpiryDoclist
//                   );
//                 }
//               },
//               function (err) {
//                 console.log('Error loading ship documents', err);
//               }
//             );
//           });
//         }
//       },
//       true
//     );

//     $scope.showTable = function () {
//       if ($scope.groupSearch != undefined) {
//         if ($scope.groupSearch != '') {
//           $scope.showUpdateIngroupCheck = false;
//           $scope.showgroup = true;
//         }
//       }
//     };

//     $scope.$watch('groupSearch', function (query) {
//       $scope.groupSearchlength = $filter('filter')($scope.groupList, query);
//       if ($scope.groupSearchlength <= 0) {
//         $scope.createbutton = true;
//         $scope.groupExpiryDoclist = [];
//         $scope.showUpdateIngroupCheck = false;
//       } else {
//         $scope.createbutton = false;
//         $scope.IsVisible = false;
//         $scope.showUpdateIngroupCheck = false;
//         $scope.showgroup = false;
//         $scope.groupExpiryDoclist = [];
//       }
//     });

//     $scope.expirydata = [];
//     $scope.getExpiryDocument = function (ship) {
//       $scope.loader = true;

//       var archivedStatus = 0;
//       FunctionalityService.getAllExpiryDocumentList(ship, archivedStatus).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             $scope.expirydata = [];
//             angular.forEach(response.data.expiryDocumentList, function (value) {
//               if (value.id != null && value.documentStatus == 'Approved')
//                 $scope.expirydata.push(value);
//             });
//             $scope.totalItems = $scope.expirydata.length;
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

//     $scope.sharePopup = function () {
//       if ($scope.selctedExpiredDocumentList.length > 0) {
//         $('#sharepopup').modal('toggle');
//         // $scope.selctedcheckboxlst = $scope.selctedcheckboxlst.concat(
//         //   $scope.selctedExpiredDocumentList
//         // );
//         $scope.selctedcheckboxlst = angular.copy(
//           $scope.selctedExpiredDocumentList
//         );

//         $scope.selecetedCheckBoxValue = function (
//           selctedExpiredDocumentList,
//           active
//         ) {
//           if (active) {
//             $scope.selctedcheckboxlst.push(selctedExpiredDocumentList);
//           } else {
//             $scope.selctedcheckboxlst.splice(
//               $scope.selctedcheckboxlst.indexOf(selctedExpiredDocumentList),
//               1
//             );
//           }
//         };
//       } else {
//         toaster.clear();
//         toaster.info({
//           title: 'Please select the document before click share',
//         });
//       }
//     };

//     $scope.shareexpirydocument = function (group) {
//       // --- NEW SORTING LOGIC ---
//       // This helper function extracts the number at the start of the name (e.g., "01", "10") and sorts numerically
//       var sortDocumentsByNumber = function (a, b) {
//         var numA = parseInt(a.documentHolderName.match(/\d+/)) || 0;
//         var numB = parseInt(b.documentHolderName.match(/\d+/)) || 0;
//         return numA - numB;
//       };
//       // -------------------------

//       if ($scope.groupIdForShare != '') {
//         // Existing Group Flow (already works correctly)
//         if ($scope.groupIdForShare != undefined) {
//           $scope.checkedUsers = [];
//           angular.forEach($scope.groupExpiryDoclist, function (user) {
//             if (user.Selected) {
//               $scope.shareExpDoc.push(user);
//             }
//           });

//           $scope.oldAndNewExpDocWithoutDub = [];
//           $scope.oldAndNewExpDoc = $scope.selctedcheckboxlst.concat(
//             $scope.shareExpDoc
//           );
//           angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
//             var exists = false;
//             angular.forEach(
//               $scope.oldAndNewExpDocWithoutDub,
//               function (val2, key) {
//                 if (angular.equals(value.id, val2.id)) {
//                   exists = true;
//                 }
//               }
//             );
//             if (exists == false && value.id != '') {
//               $scope.oldAndNewExpDocWithoutDub.push(value);
//             }
//           });

//           $scope.documentHolderIds = [];
//           angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
//             $scope.documentHolderIds.push(value.documentHolderId);
//           });

//           if ($scope.updateInGroup == true) {
//             var addexdoctogroup = {
//               groupId: $scope.groupIdForShare,
//               userProfileId: $scope.sessionObject.userId,
//               documentHolderIds: $scope.documentHolderIds,
//               loginId: $scope.sessionObject.userId,
//             };

//             FunctionalityService.updateshareExpiryDoc(
//               JSON.stringify(addexdoctogroup)
//             ).then(
//               function (response) {
//                 $scope.loader = false;

//                 if (response.status == 200) {
//                   $scope.expDocUrl = '';
//                   var hasExistingSelection = false;

//                   // SORT BEFORE BUILDING HTML
//                   $scope.groupExpiryDoclist.sort(sortDocumentsByNumber);
//                   $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

//                   angular.forEach($scope.groupExpiryDoclist, function (value) {
//                     if (value.Selected) {
//                       hasExistingSelection = true;
//                     }
//                   });

//                   if (hasExistingSelection) {
//                     angular.forEach(
//                       $scope.groupExpiryDoclist,
//                       function (value) {
//                         if (value.Selected) {
//                           // USE PROPER HTML TAGS
//                           $scope.expDocUrl +=
//                             '<strong>' +
//                             value.documentHolderName +
//                             ':</strong><br>' +
//                             '<a href="' +
//                             value.documentDownloadUrl +
//                             '">' +
//                             value.documentDownloadUrl +
//                             '</a><br><br>';
//                         }
//                       }
//                     );
//                   } else {
//                     angular.forEach(
//                       $scope.selctedcheckboxlst,
//                       function (value) {
//                         if (value) {
//                           // USE PROPER HTML TAGS
//                           $scope.expDocUrl +=
//                             '<strong>' +
//                             value.documentHolderName +
//                             ':</strong><br>' +
//                             '<a href="' +
//                             value.documentDownloadUrl +
//                             '">' +
//                             value.documentDownloadUrl +
//                             '</a><br><br>';
//                         }
//                       }
//                     );
//                   }

//                   $scope.expDocUrlshare =
//                     $scope.expDocUrl +
//                     '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.';
//                   $('#sharepopup').modal('hide');
//                   $('#shareMail').modal('show');

//                   $timeout(function () {
//                     if (CKEDITOR.instances.mailEditor) {
//                       CKEDITOR.instances.mailEditor.setData(
//                         $scope.expDocUrlshare || ''
//                       );
//                     }
//                   }, 300);
//                 } else {
//                   toaster.error('Problem in sharing expiry document');
//                 }
//               },
//               function myError(err) {
//                 $scope.loader = false;
//                 console.log('Error response', err);
//               }
//             );
//           } else {
//             $scope.expDocUrl = '';

//             // SORT BEFORE BUILDING HTML
//             $scope.oldAndNewExpDocWithoutDub.sort(sortDocumentsByNumber);

//             angular.forEach(
//               $scope.oldAndNewExpDocWithoutDub,
//               function (value, key) {
//                 // USE PROPER HTML TAGS
//                 $scope.expDocUrl +=
//                   '<strong>' +
//                   value.documentHolderName +
//                   ':</strong><br>' +
//                   '<a href="' +
//                   value.documentDownloadUrl +
//                   '">' +
//                   value.documentDownloadUrl +
//                   '</a><br><br>';
//               }
//             );
//             $scope.expDocUrlshare =
//               $scope.expDocUrl +
//               '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.';

//             $('#sharepopup').modal('hide');
//             $('#shareMail').modal('show');

//             $timeout(function () {
//               if (CKEDITOR.instances.mailEditor) {
//                 CKEDITOR.instances.mailEditor.setData(
//                   $scope.expDocUrlshare || ''
//                 );
//               }
//             }, 300);
//           }
//         } else {
//           $scope.expDocUrl = '';

//           // SORT BEFORE BUILDING HTML
//           $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

//           angular.forEach($scope.selctedcheckboxlst, function (value, key) {
//             // USE PROPER HTML TAGS
//             $scope.expDocUrl +=
//               '<strong>' +
//               value.documentHolderName +
//               ':</strong><br>' +
//               '<a href="' +
//               value.documentDownloadUrl +
//               '">' +
//               value.documentDownloadUrl +
//               '</a><br><br>';
//           });

//           $scope.expDocUrlshare =
//             $scope.expDocUrl +
//             '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments. Check your e-mail security settings to determine how attachments are handled.';

//           $('#sharepopup').modal('hide');
//           $('#shareMail').modal('show');

//           $timeout(function () {
//             if (CKEDITOR.instances.mailEditor) {
//               CKEDITOR.instances.mailEditor.setData(
//                 $scope.expDocUrlshare || ''
//               );
//             }
//           }, 300);
//         }
//       } else {
//         // CREATE GROUP FLOW - FIXED
//         if ($scope.IsVisible == true) {
//           $scope.updateInGroup = true;

//           // Initialize shipIds to prevent errors
//           $scope.shipIds = [];
//           $scope.shipIds.push($scope.libShipId);

//           if ($scope.sessionObject.roleId != 3) {
//             angular.forEach(group.vesselSelectedList, function (infos) {
//               if ($scope.libShipId != infos.id) {
//                 $scope.shipIds.push(infos.id);
//               }
//             });
//           }

//           // Collect ONLY checked existing documents
//           $scope.selectedDocumentHolderIds = [];
//           angular.forEach($scope.groupExpiryDoclist, function (value) {
//             if (value.Selected) {
//               $scope.selectedDocumentHolderIds.push(value.documentHolderId);
//             }
//           });

//           // Store group data for later use (when user clicks "Submit")
//           $scope.pendingGroupData = {
//             userProfileId: $scope.sessionObject.userId,
//             groupName: $scope.groupName,
//             shipIds: $scope.shipIds,
//             emailId: group.emailId,
//             mode: 'Email',
//             keyword: $scope.keyword,
//             documentHolderIds: $scope.selectedDocumentHolderIds,
//             loginId: $scope.sessionObject.userId,
//           };

//           // Prepare the email content FIRST
//           $scope.expDocUrl = '';

//           // SORT BEFORE BUILDING HTML
//           $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

//           angular.forEach($scope.selctedcheckboxlst, function (value, key) {
//             // USE PROPER HTML TAGS
//             $scope.expDocUrl +=
//               '<strong>' +
//               value.documentHolderName +
//               ':</strong><br>' +
//               '<a href="' +
//               value.documentDownloadUrl +
//               '">' +
//               value.documentDownloadUrl +
//               '</a><br><br>';
//           });

//           // Format the email content
//           $scope.expDocUrlshare =
//             $scope.expDocUrl +
//             '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments. Check your e-mail security settings to determine how attachments are handled.';
//           // Hide the share popup
//           $('#sharepopup').modal('hide');
//           $('#shareMail').modal('show');

//           // Show the email composition popup NOW
//           $timeout(function () {
//             if (CKEDITOR.instances.mailEditor) {
//               CKEDITOR.instances.mailEditor.setData(
//                 $scope.expDocUrlshare || ''
//               );
//             }
//           }, 300);
//         }
//       }
//       $scope.loader = false;
//       $scope.shareExpDoc = [];
//       $scope.oldAndNewExpDoc = [];
//       $scope.documentHolderIds = [];
//     };
//     $scope.setPage = function (pageNo) {
//       $scope.currentPage = pageNo;
//     };

//     $scope.dateConversionForExpiry = function (day) {
//       var stringDate1 = day;
//       var splitDate1 = stringDate1.split('-');
//       var day1 = splitDate1[0];
//       var month1 = splitDate1[1];
//       var year1 = splitDate1[2];
//       this.dStartDate = month1 + '-' + day1 + '-' + year1;
//       return this.dStartDate;
//     };

//     $rootScope.documentTypeFilter = function (status) {
//       $scope.fielterFailure = 1;
//       $scope.selectStatus = status;
//       $scope.expiryDocumentHolderListFileter = [];
//       var today = new Date();
//       var renewalDate = new Date(new Date().setDate(today.getDate() + 31));
//       var expiryDate;
//       if (status == 'Active') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           ) {
//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );
//           }
//           if (expiryDate > renewalDate || expiry.expiryDateString == '') {
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//         });
//       } else if (status == 'Renewal') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           ) {
//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );
//             if (today <= expiryDate && renewalDate >= expiryDate)
//               $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//         });
//       } else if (status == 'Expired') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (
//             expiry.expiryDateString != undefined ||
//             expiry.expiryDateString != ''
//           )
//             expiryDate = new Date(
//               $scope.dateConversionForExpiry(expiry.expiryDateString)
//             );
//           if (expiryDate < today)
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//         });
//       } else if (status == 'Missing') {
//         angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
//           if (expiry.statusColor == undefined) {
//             $scope.expiryDocumentHolderListFileter.push(expiry);
//           }
//         });
//       } else {
//         $scope.expiryDocumentHolderListFileter =
//           $scope.expiryDocumentHolderList;
//       }
//       if ($scope.expiryDocumentHolderListFileter.length == 0) {
//         $scope.fielterFailure = 0;
//       }
//     };

//     $scope.$on('$viewContentLoaded', function () {
//       FunctionalityService.getDashboardTopCountBasedOnVessel(
//         $scope.libShipId
//       ).then(function (response) {
//         if (response.status == 200) {
//           $scope.headerDetails = response.data.usershipCount;
//         }
//       });
//     });

//     $timeout(function () {
//       CKEDITOR.config.versionCheck = false;

//       if (CKEDITOR.instances.mailEditor) {
//         CKEDITOR.instances.mailEditor.destroy(true);
//       }

//       CKEDITOR.replace('mailEditor', {
//         height: 150,
//         removePlugins: 'elementspath',
//         resize_enabled: false,
//         allowedContent: true,
//         extraAllowedContent: '*(*);*{*}',
//       });
//     }, 500);

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
//       localStorage.removeItem('logout-event');
//       $window.localStorage.removeItem('logoPicture');

//       toaster.pop('success', 'Logout succesfully');
//       setTimeout(function () {
//         $location.path('/');
//       }, 2000);
//       $window.location.reload();
//     };

//     $scope.confirmLogout = function () {
//       Swal.fire({
//         html: `<div class="logout-card">
//                 <div class="logout-icon">
//                     <i class="fa fa-sign-out"></i>
//                 </div>
//                 <h3>Logout</h3>
//                 <p>Are you sure you want to logout?</p>
//             </div>`,
//         width: 480,
//         showCancelButton: true,
//         confirmButtonText: 'Logout',
//         cancelButtonText: 'Cancel',
//         confirmButtonColor: '#4a7be6',
//       }).then(function (result) {
//         if (result.isConfirmed) {
//           $scope.logout();
//         }
//       });
//     };

//     // $scope.submitEmailRequest = function () {
//     //   // Get latest CKEditor content before sending
//     //   if (CKEDITOR.instances.mailEditor) {
//     //     $scope.expDocUrlshare = CKEDITOR.instances.mailEditor.getData();
//     //   }
//     //   $scope.loader = true; // Start loader
//     //   // Validate email address exists
//     //   if (
//     //     (!$scope.groupEmailForShare ||
//     //       $scope.groupEmailForShare.trim() === '') &&
//     //     (!$scope.pendingGroupData ||
//     //       !$scope.pendingGroupData.emailId ||
//     //       $scope.pendingGroupData.emailId.trim() === '')
//     //   ) {
//     //     toaster.pop('error', 'Error', 'Recipient email address is missing');
//     //     $scope.loader = false;
//     //     return;
//     //   }

//     //   // Validate email content exists
//     //   if (!$scope.expDocUrlshare || $scope.expDocUrlshare.trim() === '') {
//     //     toaster.pop('error', 'Error', 'Email content is missing');
//     //     $scope.loader = false;
//     //     return;
//     //   }

//     //   // Prepare email data
//     //   var emailData = {
//     //     to: ($scope.groupEmailForShare || $scope.pendingGroupData.emailId)
//     //       .split(',')
//     //       .map((e) => e.trim())
//     //       .filter((e) => e.length > 0),
//     //     subject: $scope.libshipNameDoc,
//     //     body: $scope.expDocUrlshare,
//     //   };

//     //   // CREATE GROUP FLOW - Need to create group first, THEN send email
//     //   if ($scope.pendingGroupData) {
//     //     console.log('Creating new group and sending email...');

//     //     // First create the group
//     //     FunctionalityService.shareExpiryDoc(
//     //       JSON.stringify($scope.pendingGroupData)
//     //     )
//     //       .then(function (groupResponse) {
//     //         if (groupResponse.status === 200) {
//     //           console.log('Group created successfully, now sending email...');

//     //           // Now send the email
//     //           return FunctionalityService.sendEmail(emailData);
//     //         } else {
//     //           throw new Error(
//     //             'Failed to create group: ' +
//     //               (groupResponse.data?.message || 'Unknown error')
//     //           );
//     //         }
//     //       })
//     //       .then(function (emailResponse) {
//     //         if (emailResponse.status === 200) {
//     //           toaster.pop('success', 'Success', 'Email sent successfully');
//     //           $('#shareMail').modal('hide');
//     //           // --- NEW: Reload the page after 1.5 seconds to clear checkboxes ---
//     //           $timeout(function () {
//     //             $window.reload(); // Cleanly reloads the Angular state
//     //           }, 1500);
//     //           $scope.pendingGroupData = null; // Clear pending data
//     //         } else {
//     //           throw new Error(
//     //             'Email send failed: ' +
//     //               (emailResponse.data?.message || 'Unknown error')
//     //           );
//     //         }
//     //       })
//     //       .catch(function (error) {
//     //         console.error('Error in email flow:', error);
//     //         toaster.pop(
//     //           'error',
//     //           'Error',
//     //           error.message || 'Failed to send email'
//     //         );
//     //       })
//     //       .finally(function () {
//     //         $scope.loader = false;
//     //       });
//     //   }
//     //   // EXISTING GROUP FLOW - Just send the email
//     //   else {
//     //     console.log('Sending email to existing group...');

//     //     FunctionalityService.sendEmail(emailData)
//     //       .then(function (response) {
//     //         if (response.status === 200) {
//     //           toaster.pop('success', 'Success', 'Email sent successfully');
//     //           $('#shareMail').modal('hide');
//     //         } else {
//     //           throw new Error(
//     //             'Email send failed: ' +
//     //               (response.data?.message || 'Unknown error')
//     //           );
//     //         }
//     //       })
//     //       .catch(function (error) {
//     //         console.error('Email send error:', error);
//     //         toaster.pop(
//     //           'error',
//     //           'Error',
//     //           error.message || 'Failed to send email'
//     //         );
//     //       })
//     //       .finally(function () {
//     //         $scope.loader = false;
//     //       });
//     //   }
//     // };
//     $scope.submitEmailRequest = function () {
//       // Get latest CKEditor content before sending
//       if (CKEDITOR.instances.mailEditor) {
//         $scope.expDocUrlshare = CKEDITOR.instances.mailEditor.getData();
//       }
//       $scope.loader = true; // Start loader

//       // Validate email address exists
//       if (
//         (!$scope.groupEmailForShare ||
//           $scope.groupEmailForShare.trim() === '') &&
//         (!$scope.pendingGroupData ||
//           !$scope.pendingGroupData.emailId ||
//           $scope.pendingGroupData.emailId.trim() === '')
//       ) {
//         toaster.pop('error', 'Error', 'Recipient email address is missing');
//         $scope.loader = false;
//         return;
//       }

//       // Validate email content exists
//       if (!$scope.expDocUrlshare || $scope.expDocUrlshare.trim() === '') {
//         toaster.pop('error', 'Error', 'Email content is missing');
//         $scope.loader = false;
//         return;
//       }

//       // Prepare email data
//       var emailData = {
//         to: ($scope.groupEmailForShare || $scope.pendingGroupData.emailId)
//           .split(',')
//           .map((e) => e.trim())
//           .filter((e) => e.length > 0),
//         subject: $scope.libshipNameDoc,
//         body: $scope.expDocUrlshare,
//       };

//       // CREATE GROUP FLOW - Need to create group first, THEN send email
//       if ($scope.pendingGroupData) {
//         console.log('Creating new group and sending email...');

//         // First create the group
//         FunctionalityService.shareExpiryDoc(
//           JSON.stringify($scope.pendingGroupData)
//         )
//           .then(function (groupResponse) {
//             if (groupResponse.status === 200) {
//               console.log('Group created successfully, now sending email...');
//               // Now send the email
//               return FunctionalityService.sendEmail(emailData);
//             } else {
//               throw new Error(
//                 'Failed to create group: ' +
//                   (groupResponse.data?.message || 'Unknown error')
//               );
//             }
//           })
//           .then(function (emailResponse) {
//             if (emailResponse.status === 200) {
//               toaster.pop('success', 'Success', 'Email sent successfully');
//               $('#shareMail').modal('hide');
//               $scope.pendingGroupData = null; // Clear pending data

//               // --- NEW: Reload the page after 1.5 seconds to clear checkboxes ---
//               $timeout(function () {
//                 $state.reload(); // Cleanly reloads the Angular state
//               }, 1500);
//             } else {
//               throw new Error(
//                 'Email send failed: ' +
//                   (emailResponse.data?.message || 'Unknown error')
//               );
//             }
//           })
//           .catch(function (error) {
//             console.error('Error in email flow:', error);
//             toaster.pop(
//               'error',
//               'Error',
//               error.message || 'Failed to send email'
//             );
//           })
//           .finally(function () {
//             $scope.loader = false;
//           });
//       }
//       // EXISTING GROUP FLOW - Just send the email
//       else {
//         console.log('Sending email to existing group...');

//         FunctionalityService.sendEmail(emailData)
//           .then(function (response) {
//             if (response.status === 200) {
//               toaster.pop('success', 'Success', 'Email sent successfully');
//               $('#shareMail').modal('hide');

//               // --- NEW: Reload the page after 1.5 seconds to clear checkboxes ---
//               $timeout(function () {
//                 $state.reload(); // Cleanly reloads the Angular state
//               }, 1500);
//             } else {
//               throw new Error(
//                 'Email send failed: ' +
//                   (response.data?.message || 'Unknown error')
//               );
//             }
//           })
//           .catch(function (error) {
//             console.error('Email send error:', error);
//             toaster.pop(
//               'error',
//               'Error',
//               error.message || 'Failed to send email'
//             );
//           })
//           .finally(function () {
//             $scope.loader = false;
//           });
//       }
//     };
//   },
// ]);

// userVesselDocEBD.directive('fileModel', [
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

// userVesselDocEBD.directive('customFocus', [
//   function () {
//     var FOCUS_CLASS = 'custom-focused';
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

var userVesselDocEBD = angular.module('dapp.UserVesselDocumentEBDController', [
  'ui.select',
  'angularUtils.directives.dirPagination',
  'toaster',
  'moment-picker',
  '720kb.tooltips',
  'ngSanitize',
]);

userVesselDocEBD.controller('UserVesselDocumentEBDController', [
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
    FunctionalityService
  ) {
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );
    $scope.libShipId;
    $scope.libshipName;
    $scope.loader = false;

    $scope.group = {
      vesselSelectedList: [],
    };

    $scope.formattedIssueDate = '';
    $scope.formattedExpiryDate = '';
    $scope.formattedlastAnnual = '';
    $scope.formattednextAnnual = '';
    $scope.issuingAuthority = '';
    $scope.notfound = true;
    $scope.currentPage = 1;
    $scope.viewby = 73;
    $scope.roleIdForView = $scope.sessionObject.roleId;
    $scope.itemsPerPageForHistory = $scope.viewby;
    itemsPerPageHistory = $scope.viewby;
    $scope.activeText = 'Active';

    if ($scope.sessionObject.roleId != 3) {
      $scope.libShipId = $window.localStorage.getItem('libShipId');
      $scope.libshipName = $window.localStorage.getItem('libshipName');
      $scope.libshipNameDoc = $scope.libshipName + ' ' + 'Documents';
    } else if ($scope.sessionObject.shipProfileInfos.length > 0) {
      $scope.libShipId = $scope.sessionObject.shipProfileInfos[0].id;
      $scope.libshipName = $scope.sessionObject.shipProfileInfos[0].shipName;
    }

    $scope.oldAndNewExpDocWithoutDub = [];
    $scope.selctedcheckboxlst = [];
    $scope.expiryDocumentHolderList = [];
    $scope.expiryDocumentHolderListLen = $scope.expiryDocumentHolderList.length;

    $scope.viewDocumentObjectInfo;
    $scope.viewDocumentUrl;
    $scope.historyEBDObject;

    $rootScope.convertedFile = '';
    $rootScope.uploadDocFile;
    $scope.loader = false;
    $rootScope.dcoumentHolderName;
    $rootScope.docId;
    $scope.documentHolderHistory = [];
    $scope.documentHolderHistoryLength = $scope.documentHolderHistory.length;
    $scope.groupTagExpiryDoc;
    $scope.groupListExpiry;
    $scope.shareExpDoc = [];
    $scope.records = false;

    $scope.selectedDocumentHolderIds = [];
    $scope.oldAndNewExpDocWithoutDub = [];
    $scope.vesselList = [];
    $scope.expiryDocumentHolderListFileter = [];
    $scope.VesselListLenEBD = $scope.vesselList.length;

    $scope.userDetails = function () {
      $state.go('dapp.userProfile');
    };

    $scope.getAllExpiryList = function () {
      $scope.loader = true;
      var archivedStatus = 0;
      FunctionalityService.getAllExpiryDocumentList(
        $scope.libShipId,
        archivedStatus
      ).then(
        function (response) {
          if (response.status == 200 || response.status == 201) {
            $scope.expiryDocumentHolderList = response.data.expiryDocumentList;
            $scope.expiryDocumentHolderListFileter =
              $scope.expiryDocumentHolderList;
            $scope.expiryDocumentHolderListLen =
              $scope.expiryDocumentHolderList.length;
            $scope.loader = false;

            // --- REDIRECTION FILTER LOGIC START ---
            var savedStatus = $window.localStorage.getItem(
              'redirectFilterStatus'
            );
            if (savedStatus) {
              $scope.documentTypeFilter(savedStatus);
              $scope.selectStatus = savedStatus;
              $window.localStorage.removeItem('redirectFilterStatus');
            }
            // --- REDIRECTION FILTER LOGIC END ---
          }
        },
        function myError(err) {
          $scope.loader = false;
          if ($scope.expiryDocumentHolderList.length == 0) {
            $scope.records = true;
          } else {
            $scope.records = false;
          }
          console.log('Error response', err);
        }
      );
      var data1 = {
        userId: $scope.sessionObject.userId,
        roleId: $scope.sessionObject.roleId,
      };

      FunctionalityService.getShipProfileList($scope.sessionObject.userId).then(
        function (response) {
          if (response.status == 200) {
            $scope.vesselList = response.data.shipProfileList;
          }
        },
        function myError(err) {
          console.log('Error response', err);
        }
      );
    };

    $scope.getAllGroupList = function () {
      $scope.loader = true;

      var groupdata = {
        userProfileId: $scope.sessionObject.userId,
        shipId: $scope.libShipId,
      };

      FunctionalityService.getGroupListShip(groupdata).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $scope.message = JSON.stringify(response.data.groupList);
            $scope.groupList = response.data.groupList;
            $scope.groupListLength = $scope.groupList.length;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.faq = function () {};

    $scope.saveAsDraft = function () {
      $scope.loader = true;

      var draftData = {
        certificateNumber: $scope.libcreate.certificateNumber,
        placeOfIssue: $scope.libcreate.placeOfIssue,
        issueDate: $scope.dateConversion($scope.libcreate.dateOfIssue),
        expiryDate: $scope.dateConversion($scope.libcreate.dateOfExpiry),
        lastAnnual: $scope.dateConversion($scope.libcreate.lastAnnual),
        nextAnnual: $scope.dateConversion($scope.libcreate.nextAnnual),
        uploadedUserId: $scope.sessionObject.userId,
        shipProfileId: $scope.libShipId,
        documentHolderId: $rootScope.docId,
        remarks: $scope.libcreate.remarks,
        issuingAuthority: $scope.issuingAuthority,
        draftId: $scope.currentDraftId || null,
        documentDataId: $scope.currentDocumentDataId || null,
        isDraft: true,
      };

      FunctionalityService.saveDraftDocument(
        JSON.stringify(draftData),
        $rootScope.uploadDocFile
      ).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200) {
            $('#createEBD').modal('hide');
            $scope.clearFields();
            $scope.getAllExpiryList();
            toaster.success({ title: 'Draft saved successfully' });
          }
        },
        function (err) {
          $scope.loader = false;
          toaster.error({ title: 'Failed to save draft' });
        }
      );
    };

    $scope.uploadFile = function () {
      if (!$scope.myFile) return;

      $rootScope.convertedFile = $sce.trustAsResourceUrl(
        URL.createObjectURL($scope.myFile)
      );
      $rootScope.uploadDocFile = $scope.myFile;

      $scope.isScanning = true;
      $scope.libcreate = {};
      $scope.issuingAuthority = '';

      $('#fileupload').modal('hide');
      $('#createEBD').modal('show');

      FunctionalityService.scanExpiryDocument($scope.myFile).then(
        function (response) {
          $timeout(function () {
            $scope.isScanning = false;

            if (response.status == 200 || response.status == 201) {
              var scannedData = response.data.expiryDocumentDTOs;
              if (scannedData != null) {
                $scope.issuingAuthority = scannedData.issuingAuthority || '';
                $scope.libcreate.certificateNumber =
                  scannedData.certificateNumber || '';
                $scope.libcreate.placeOfIssue = scannedData.placeOfIssue || '';
                $scope.libcreate.remarks = scannedData.remarks || '';

                if (scannedData.issueDateString) {
                  $scope.libcreate.dateOfIssue = moment(
                    scannedData.issueDateString,
                    'DD-MM-YYYY'
                  ).format('DD-MM-YYYY');
                }
                if (scannedData.expiryDateString) {
                  $scope.libcreate.dateOfExpiry = moment(
                    scannedData.expiryDateString,
                    'DD-MM-YYYY'
                  ).format('DD-MM-YYYY');
                }

                $scope.libcreate.lastAnnual = '';
                $scope.libcreate.nextAnnual = '';
              }
            }
          }, 500);
        },
        function (error) {
          $scope.isScanning = false;
          toaster.error({ title: 'Extraction failed, please fill manually.' });
        }
      );
    };
    $scope.clearFileField = function () {
      $('#filename').val('');
      $scope.loader = false;
      $scope.isDisabled = false;
    };

    $scope.storeDocumentholder = function (obj) {
      $rootScope.dcoumentHolderName = obj.documentHolderName;
      $rootScope.docId = obj.documentHolderId;
      $scope.clearFileField();
    };

    $scope.createDocument = function () {
      $scope.loader = true;

      $scope.formattedIssueDate = $scope.dateConversion(
        $scope.libcreate.dateOfIssue
      );
      $scope.formattedExpiryDate = $scope.dateConversion(
        $scope.libcreate.dateOfExpiry
      );
      $scope.formattedlastAnnual = $scope.dateConversion(
        $scope.libcreate.lastAnnual
      );
      $scope.formattednextAnnual = $scope.dateConversion(
        $scope.libcreate.nextAnnual
      );

      $scope.saveData = {
        certificateNumber: $scope.libcreate.certificateNumber,
        placeOfIssue: $scope.libcreate.placeOfIssue,
        issueDate: $scope.formattedIssueDate,
        expiryDate: $scope.formattedExpiryDate,
        lastAnnual: $scope.formattedlastAnnual,
        nextAnnual: $scope.formattednextAnnual,
        uploadedUserId: $scope.sessionObject.userId,
        loginId: $scope.sessionObject.userId,
        shipProfileId: $scope.libShipId,
        documentHolderId: $rootScope.docId,
        remarks: $scope.libcreate.remarks,
        issuingAuthority: $scope.issuingAuthority,
        draftId: $scope.currentDraftId || null,
        documentDataId: $scope.currentDocumentDataId || null,
        isDraft: false,
      };

      FunctionalityService.saveExpiryDocument(
        JSON.stringify($scope.saveData),
        $rootScope.uploadDocFile
      ).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $('#createEBD').modal('hide');
            $('#filename').val('');
            $rootScope.uploadDocFile = '';
            $scope.clearFields();
            $scope.getAllExpiryList();
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

    $scope.dateConversion = function (day) {
      if (!day || day == '') {
        return null;
      }
      var stringDate1 = day;
      var splitDate1 = stringDate1.split('-');
      var day1 = splitDate1[0];
      var month1 = splitDate1[1];
      var year1 = splitDate1[2];
      return year1 + '-' + month1 + '-' + day1;
    };

    $scope.cancelExpiry = function () {
      $('#viewExpiryDocument').modal('hide');
    };

    $scope.updateExpiryDoc = function () {
      $scope.loader = true;

      if (
        $scope.issueDateStringEdit != '' &&
        $scope.issueDateStringEdit != undefined
      ) {
        $scope.formattedIssueDate = $scope.dateConversion(
          $scope.issueDateStringEdit
        );
      }
      if (
        $scope.expiryDateStringEdit != '' &&
        $scope.expiryDateStringEdit != undefined
      ) {
        $scope.formattedExpiryDate = $scope.dateConversion(
          $scope.expiryDateStringEdit
        );
      }
      if (
        $scope.lastAnnualStringEdit != '' &&
        $scope.lastAnnualStringEdit != undefined
      ) {
        $scope.formattedlastAnnual = $scope.dateConversion(
          $scope.lastAnnualStringEdit
        );
      }
      if (
        $scope.nextAnnualStringEdit != '' &&
        $scope.nextAnnualStringEdit != undefined
      ) {
        $scope.formattednextAnnual = $scope.dateConversion(
          $scope.nextAnnualStringEdit
        );
      }

      var data = {
        id: $scope.docId,
        certificateNumber: $scope.certificateNumberEdit,
        placeOfIssue: $scope.placeOfIssueEdit,
        issueDate: $scope.formattedIssueDate,
        expiryDate: $scope.formattedExpiryDate,
        lastAnnual: $scope.formattedlastAnnual,
        nextAnnual: $scope.formattednextAnnual,
        loginId: $scope.sessionObject.userId,
        uploadedUserId: $scope.sessionObject.userId,
        shipProfileId: $scope.libShipId,
        documentHolderId: $scope.docHolderId,
        remarks: $scope.remarksEdit,
        issuingAuthority: $scope.issuingAuthorityEdit,
      };

      FunctionalityService.updateExpiryDocument(data).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $('#viewExpiryDocument').modal('hide');
            $scope.getAllExpiryList();
            $timeout(function () {
              toaster.clear();
              toaster.success({ title: response.data.message });
            }, 1000);
          } else {
            toaster.clear();
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          toaster.error({ title: 'Error updating document' });
          console.log('Error response', err);
        }
      );
    };

    $scope.clearFields = function () {
      $scope.libcreate.certificateNumber = '';
      $scope.libcreate.placeOfIssue = '';
      $scope.libcreate.dateOfIssue = '';
      $scope.libcreate.dateOfExpiry = '';
      $scope.libcreate.lastAnnual = '';
      $scope.libcreate.nextAnnual = '';
      $scope.libcreate.remarks = '';
      $scope.issuingAuthority = '';
      $('#filename').val('');
      $scope.loader = false;
      $scope.uploadDocFile = '';
      $scope.currentDraftId = null;
      $scope.currentDocumentDataId = null;
    };

    $scope.handleDraftClick = function (doc) {
      Swal.fire({
        html: `<div class="logout-card">
                <div class="logout-icon">
                    <i class="fa fa-file-text-o"></i>
                </div>
                <h3>Draft Available</h3>
                <p>Do you want to resume your saved draft or upload a completely new document?</p>
            </div>`,
        width: 480,
        showCloseButton: true,
        showCancelButton: true,
        confirmButtonColor: '#4a7be6',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Resume Draft',
        cancelButtonText: 'Upload New',
      }).then(function (result) {
        if (result.isConfirmed) {
          $scope.$apply(function () {
            $scope.resumeDraft(doc);
          });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          $scope.$apply(function () {
            $scope.currentDraftId = doc.draftId;
            $scope.currentDocumentDataId = null;
            $scope.storeDocumentholder(doc);
            $('#fileupload').modal('show');
          });
        }
      });
    };
    $scope.closeViewPopup = function () {};

    $scope.viewExpiryDocumentInformation = function (documentObj) {
      $scope.viewDocumentObjectInfo = documentObj;
      $scope.docId = documentObj.id;
      $scope.docHolderId = documentObj.documentHolderId;

      $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
        documentObj.documentPreviewUrl
      );

      $scope.certificateNumberEdit = documentObj.certificateNumber;
      $scope.issuingAuthorityEdit = documentObj.issuingAuthority;
      $scope.placeOfIssueEdit = documentObj.placeOfIssue;
      $scope.issueDateStringEdit = documentObj.issueDateString;
      $scope.expiryDateStringEdit = documentObj.expiryDateString;
      $scope.lastAnnualStringEdit = documentObj.lastAnnualString;
      $scope.nextAnnualStringEdit = documentObj.nextAnnualString;
      $scope.remarksEdit = documentObj.remarks;

      $('#viewExpiryDocument').modal('show');
    };

    $scope.openHistoryPopup = function (documentObj) {
      $scope.loader = true;

      $('#historyEBD').modal('toggle');
      $scope.historyEBDObject = documentObj;
      var historyRequest = {
        documentHolderId: documentObj.documentHolderId,
        shipProfileId: $scope.libShipId,
      };
      FunctionalityService.getDocumentHolderHistory(historyRequest).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $scope.documentHolderHistory = response.data.expiryDocumentList;
            $scope.documentHolderHistoryLength =
              $scope.documentHolderHistory.length;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.openGroupTagEBDPopup = function (expiryDataObj) {
      if ($scope.selctedExpiredDocumentList.length > 0) {
        $scope.groupTagExpiryDocList = angular.copy(
          $scope.selctedExpiredDocumentList
        );
      } else if (expiryDataObj) {
        $scope.groupTagExpiryDocList = [expiryDataObj];
      } else {
        toaster.clear();
        toaster.info({
          title: 'Please select the document before tagging',
        });
        return;
      }
      $scope.loader = true;

      var groupdata = {
        userProfileId: $scope.sessionObject.userId,
        shipId: $scope.libShipId,
      };
      FunctionalityService.getGroupListShip(groupdata).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200) {
            $scope.groupListExpiry = response.data.groupList;
            $('#tagGroupPopup').modal('show');
          }
        },
        function (err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.resumeDraft = function (doc) {
      $rootScope.dcoumentHolderName = doc.documentHolderName;
      $rootScope.docId = doc.documentHolderId;

      $scope.currentDraftId = doc.draftId;
      $scope.currentDocumentDataId = doc.documentDataId;

      $scope.libcreate = {
        certificateNumber: doc.certificateNumber,
        placeOfIssue: doc.placeOfIssue,
        dateOfIssue: doc.issueDateString,
        dateOfExpiry: doc.expiryDateString,
        lastAnnual: doc.lastAnnualString,
        nextAnnual: doc.nextAnnualString,
        remarks: doc.remarks,
      };
      $scope.issuingAuthority = doc.issuingAuthority;

      if (doc.documentPreviewUrl) {
        $rootScope.convertedFile = $sce.trustAsResourceUrl(
          doc.documentPreviewUrl
        );
      }
      $rootScope.uploadDocFile = null;
      $scope.isScanning = false;

      $('#createEBD').modal('show');
    };

    $scope.tagExpiryDocumentToGRoup = function () {
      if (
        !$scope.groupTagExpiryDocList ||
        $scope.groupTagExpiryDocList.length === 0
      ) {
        toaster.error({ title: 'No document selected' });
        return;
      }

      $scope.loader = true;

      var documentHolderIds = [];

      angular.forEach($scope.groupTagExpiryDocList, function (doc) {
        documentHolderIds.push(doc.documentHolderId);
      });

      var payload = {
        groupId: $scope.gorupSelected,
        userProfileId: $scope.sessionObject.userId,
        documentHolderIds: documentHolderIds,
        loginId: $scope.sessionObject.userId,
      };

      FunctionalityService.addMultipleExpiryDocToGroup(payload).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            $('#tagGroupPopup').modal('hide');

            toaster.success({
              title: 'Documents tagged successfully',
            });

            $scope.checkall = false;
            $scope.selctedExpiredDocumentList = [];
            $scope.groupTagExpiryDocList = [];
          } else {
            toaster.error({
              title: response.data.message,
            });
          }
        },
        function (err) {
          $scope.loader = false;
          console.log(err);
        }
      );
    };

    $scope.clearTagDocumentFields = function () {
      $scope.gorupSelected = '';
    };

    $scope.close = function () {};

    $scope.selctedExpiredDocumentList = [];

    $scope.checkUncheckAll = function () {
      $scope.selctedExpiredDocumentList = [];
      $scope.selectedDocumentHolderIds = [];
      if ($scope.checkall) {
        $scope.checkall = true;
        angular.forEach($scope.expiryDocumentHolderList, function (value) {
          if (value.id != undefined) {
            $scope.selctedExpiredDocumentList.push(value);
            $scope.selectedDocumentHolderIds.push(value.documentHolderId);
          }
        });
      } else {
        $scope.checkall = false;
      }
      angular.forEach($scope.expiryDocumentHolderList, function (user) {
        user.checked = $scope.checkall;
      });
    };

    $scope.updateCheckall = function ($index, user) {
      var userTotal = $scope.expiryDocumentHolderList.length;
      var count = 0;
      $scope.selctedExpiredDocumentList = [];
      $scope.selectedDocumentHolderIds = [];
      angular.forEach($scope.expiryDocumentHolderList, function (item) {
        if (item.checked) {
          count++;
          $scope.selctedExpiredDocumentList.push(item);
          $scope.selectedDocumentHolderIds.push(item.documentHolderId);
        }
      });
      if (userTotal == count) {
        $scope.checkall = true;
      } else {
        $scope.checkall = false;
      }
    };

    $scope.groupExpiryDoclist = [];

    $scope.viewGroup = function (group) {
      $scope.loader = true;
      $scope.groupExpiryDoclist = [];

      $scope.groupEmailForShare = group.emailId;
      $scope.groupIdForShare = group.id;

      FunctionalityService.viewGroup(group.id).then(
        function (response) {
          $scope.loader = false;

          var groupInfo = response.data.groupInfo || {};

          $scope.groupExpiryDoclist = groupInfo.expiryDocumentDtos || [];

          console.log('Loaded group docs:', $scope.groupExpiryDoclist);

          $scope.showUpdateIngroupCheck = true;
        },
        function (err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.IsVisible = false;
    $scope.ShowCreateGroup = function () {
      $scope.IsVisible = true;
      $scope.groupExpiryDoclist = [];
      $scope.groupIdForShare = '';
      $scope.showUpdateIngroupCheck = true;
    };

    $scope.$watch(
      'group.vesselSelectedList',
      function (newVal) {
        if (newVal && newVal.length > 0) {
          $scope.groupExpiryDoclist = [];
          angular.forEach(newVal, function (vessel) {
            FunctionalityService.getAllExpiryDocumentList(vessel.id, 0).then(
              function (response) {
                if (response.status === 200) {
                  angular.forEach(
                    response.data.expiryDocumentList,
                    function (doc) {
                      if (doc.id != null && doc.documentStatus === 'Approved') {
                        var exists = $scope.groupExpiryDoclist.some(
                          function (d) {
                            return d.id === doc.id;
                          }
                        );
                        if (!exists) {
                          $scope.groupExpiryDoclist.push(doc);
                        }
                      }
                    }
                  );
                }
              },
              function (err) {
                console.log('Error loading ship documents', err);
              }
            );
          });
        }
      },
      true
    );

    $scope.showTable = function () {
      if ($scope.groupSearch != undefined) {
        if ($scope.groupSearch != '') {
          $scope.showUpdateIngroupCheck = false;
          $scope.showgroup = true;
        }
      }
    };

    $scope.$watch('groupSearch', function (query) {
      $scope.groupSearchlength = $filter('filter')($scope.groupList, query);
      if ($scope.groupSearchlength <= 0) {
        $scope.createbutton = true;
        $scope.groupExpiryDoclist = [];
        $scope.showUpdateIngroupCheck = false;
      } else {
        $scope.createbutton = false;
        $scope.IsVisible = false;
        $scope.showUpdateIngroupCheck = false;
        $scope.showgroup = false;
        $scope.groupExpiryDoclist = [];
      }
    });

    $scope.expirydata = [];
    $scope.getExpiryDocument = function (ship) {
      $scope.loader = true;

      var archivedStatus = 0;
      FunctionalityService.getAllExpiryDocumentList(ship, archivedStatus).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $scope.expirydata = [];
            angular.forEach(response.data.expiryDocumentList, function (value) {
              if (value.id != null && value.documentStatus == 'Approved')
                $scope.expirydata.push(value);
            });
            $scope.totalItems = $scope.expirydata.length;
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

    $scope.sharePopup = function () {
      if ($scope.selctedExpiredDocumentList.length > 0) {
        $('#sharepopup').modal('toggle');
        $scope.selctedcheckboxlst = angular.copy(
          $scope.selctedExpiredDocumentList
        );

        $scope.selecetedCheckBoxValue = function (
          selctedExpiredDocumentList,
          active
        ) {
          if (active) {
            $scope.selctedcheckboxlst.push(selctedExpiredDocumentList);
          } else {
            $scope.selctedcheckboxlst.splice(
              $scope.selctedcheckboxlst.indexOf(selctedExpiredDocumentList),
              1
            );
          }
        };
      } else {
        toaster.clear();
        toaster.info({
          title: 'Please select the document before click share',
        });
      }
    };

    $scope.shareexpirydocument = function (group) {
      var sortDocumentsByNumber = function (a, b) {
        var numA = parseInt(a.documentHolderName.match(/\d+/)) || 0;
        var numB = parseInt(b.documentHolderName.match(/\d+/)) || 0;
        return numA - numB;
      };

      if ($scope.groupIdForShare != '') {
        if ($scope.groupIdForShare != undefined) {
          $scope.checkedUsers = [];
          angular.forEach($scope.groupExpiryDoclist, function (user) {
            if (user.Selected) {
              $scope.shareExpDoc.push(user);
            }
          });

          $scope.oldAndNewExpDocWithoutDub = [];
          $scope.oldAndNewExpDoc = $scope.selctedcheckboxlst.concat(
            $scope.shareExpDoc
          );
          angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
            var exists = false;
            angular.forEach(
              $scope.oldAndNewExpDocWithoutDub,
              function (val2, key) {
                if (angular.equals(value.id, val2.id)) {
                  exists = true;
                }
              }
            );
            if (exists == false && value.id != '') {
              $scope.oldAndNewExpDocWithoutDub.push(value);
            }
          });

          $scope.documentHolderIds = [];
          angular.forEach($scope.oldAndNewExpDoc, function (value, key) {
            $scope.documentHolderIds.push(value.documentHolderId);
          });

          if ($scope.updateInGroup == true) {
            var addexdoctogroup = {
              groupId: $scope.groupIdForShare,
              userProfileId: $scope.sessionObject.userId,
              documentHolderIds: $scope.documentHolderIds,
              loginId: $scope.sessionObject.userId,
            };

            FunctionalityService.updateshareExpiryDoc(
              JSON.stringify(addexdoctogroup)
            ).then(
              function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                  $scope.expDocUrl = '';
                  var hasExistingSelection = false;

                  $scope.groupExpiryDoclist.sort(sortDocumentsByNumber);
                  $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

                  angular.forEach($scope.groupExpiryDoclist, function (value) {
                    if (value.Selected) {
                      hasExistingSelection = true;
                    }
                  });

                  if (hasExistingSelection) {
                    angular.forEach(
                      $scope.groupExpiryDoclist,
                      function (value) {
                        if (value.Selected) {
                          $scope.expDocUrl +=
                            '<strong>' +
                            value.documentHolderName +
                            ':</strong><br>' +
                            '<a href="' +
                            value.documentDownloadUrl +
                            '">' +
                            value.documentDownloadUrl +
                            '</a><br><br>';
                        }
                      }
                    );
                  } else {
                    angular.forEach(
                      $scope.selctedcheckboxlst,
                      function (value) {
                        if (value) {
                          $scope.expDocUrl +=
                            '<strong>' +
                            value.documentHolderName +
                            ':</strong><br>' +
                            '<a href="' +
                            value.documentDownloadUrl +
                            '">' +
                            value.documentDownloadUrl +
                            '</a><br><br>';
                        }
                      }
                    );
                  }

                  $scope.expDocUrlshare =
                    $scope.expDocUrl +
                    '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.';
                  $('#sharepopup').modal('hide');
                  $('#shareMail').modal('show');

                  $timeout(function () {
                    if (CKEDITOR.instances.mailEditor) {
                      CKEDITOR.instances.mailEditor.setData(
                        $scope.expDocUrlshare || ''
                      );
                    }
                  }, 300);
                } else {
                  toaster.error('Problem in sharing expiry document');
                }
              },
              function myError(err) {
                $scope.loader = false;
                console.log('Error response', err);
              }
            );
          } else {
            $scope.expDocUrl = '';
            $scope.oldAndNewExpDocWithoutDub.sort(sortDocumentsByNumber);

            angular.forEach(
              $scope.oldAndNewExpDocWithoutDub,
              function (value, key) {
                $scope.expDocUrl +=
                  '<strong>' +
                  value.documentHolderName +
                  ':</strong><br>' +
                  '<a href="' +
                  value.documentDownloadUrl +
                  '">' +
                  value.documentDownloadUrl +
                  '</a><br><br>';
              }
            );
            $scope.expDocUrlshare =
              $scope.expDocUrl +
              '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments.';

            $('#sharepopup').modal('hide');
            $('#shareMail').modal('show');

            $timeout(function () {
              if (CKEDITOR.instances.mailEditor) {
                CKEDITOR.instances.mailEditor.setData(
                  $scope.expDocUrlshare || ''
                );
              }
            }, 300);
          }
        } else {
          $scope.expDocUrl = '';
          $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

          angular.forEach($scope.selctedcheckboxlst, function (value, key) {
            $scope.expDocUrl +=
              '<strong>' +
              value.documentHolderName +
              ':</strong><br>' +
              '<a href="' +
              value.documentDownloadUrl +
              '">' +
              value.documentDownloadUrl +
              '</a><br><br>';
          });

          $scope.expDocUrlshare =
            $scope.expDocUrl +
            '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments. Check your e-mail security settings to determine how attachments are handled.';

          $('#sharepopup').modal('hide');
          $('#shareMail').modal('show');

          $timeout(function () {
            if (CKEDITOR.instances.mailEditor) {
              CKEDITOR.instances.mailEditor.setData(
                $scope.expDocUrlshare || ''
              );
            }
          }, 300);
        }
      } else {
        if ($scope.IsVisible == true) {
          $scope.updateInGroup = true;

          $scope.shipIds = [];
          $scope.shipIds.push($scope.libShipId);

          if ($scope.sessionObject.roleId != 3) {
            angular.forEach(group.vesselSelectedList, function (infos) {
              if ($scope.libShipId != infos.id) {
                $scope.shipIds.push(infos.id);
              }
            });
          }

          $scope.selectedDocumentHolderIds = [];
          angular.forEach($scope.groupExpiryDoclist, function (value) {
            if (value.Selected) {
              $scope.selectedDocumentHolderIds.push(value.documentHolderId);
            }
          });

          $scope.pendingGroupData = {
            userProfileId: $scope.sessionObject.userId,
            groupName: $scope.groupName,
            shipIds: $scope.shipIds,
            emailId: group.emailId,
            mode: 'Email',
            keyword: $scope.keyword,
            documentHolderIds: $scope.selectedDocumentHolderIds,
            loginId: $scope.sessionObject.userId,
          };

          $scope.expDocUrl = '';
          $scope.selctedcheckboxlst.sort(sortDocumentsByNumber);

          angular.forEach($scope.selctedcheckboxlst, function (value, key) {
            $scope.expDocUrl +=
              '<strong>' +
              value.documentHolderName +
              ':</strong><br>' +
              '<a href="' +
              value.documentDownloadUrl +
              '">' +
              value.documentDownloadUrl +
              '</a><br><br>';
          });

          $scope.expDocUrlshare =
            $scope.expDocUrl +
            '<br><br>Note: To protect against computer viruses, e-mail programs may prevent sending or receiving certain types of file attachments. Check your e-mail security settings to determine how attachments are handled.';
          $('#sharepopup').modal('hide');
          $('#shareMail').modal('show');

          $timeout(function () {
            if (CKEDITOR.instances.mailEditor) {
              CKEDITOR.instances.mailEditor.setData(
                $scope.expDocUrlshare || ''
              );
            }
          }, 300);
        }
      }
      $scope.loader = false;
      $scope.shareExpDoc = [];
      $scope.oldAndNewExpDoc = [];
      $scope.documentHolderIds = [];
    };
    $scope.setPage = function (pageNo) {
      $scope.currentPage = pageNo;
    };

    // ===============================================
    // FIX: Add safety checks to avoid "split" crashes
    // ===============================================
    $scope.dateConversionForExpiry = function (day) {
      if (!day || typeof day !== 'string') return null;
      var stringDate1 = day;
      var splitDate1 = stringDate1.split('-');
      var day1 = splitDate1[0];
      var month1 = splitDate1[1];
      var year1 = splitDate1[2];
      this.dStartDate = month1 + '-' + day1 + '-' + year1;
      return this.dStartDate;
    };

    // ===============================================
    // FIX: Change || to && to handle missing dates properly
    // ===============================================
    $rootScope.documentTypeFilter = function (status) {
      $scope.fielterFailure = 1;
      $scope.selectStatus = status;
      $scope.expiryDocumentHolderListFileter = [];
      var today = new Date();
      var renewalDate = new Date(new Date().setDate(today.getDate() + 31));
      var expiryDate;

      if (status == 'Active') {
        angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
          if (expiry.expiryDateString) {
            expiryDate = new Date(
              $scope.dateConversionForExpiry(expiry.expiryDateString)
            );
          } else {
            expiryDate = null;
          }
          if (!expiryDate || expiryDate > renewalDate) {
            $scope.expiryDocumentHolderListFileter.push(expiry);
          }
        });
      } else if (status == 'Renewal') {
        angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
          if (expiry.expiryDateString) {
            expiryDate = new Date(
              $scope.dateConversionForExpiry(expiry.expiryDateString)
            );
            if (today <= expiryDate && renewalDate >= expiryDate)
              $scope.expiryDocumentHolderListFileter.push(expiry);
          }
        });
      } else if (status == 'Expired') {
        angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
          if (expiry.expiryDateString) {
            expiryDate = new Date(
              $scope.dateConversionForExpiry(expiry.expiryDateString)
            );
            if (expiryDate && expiryDate < today)
              $scope.expiryDocumentHolderListFileter.push(expiry);
          }
        });
      } else if (status == 'Missing') {
        angular.forEach($scope.expiryDocumentHolderList, function (expiry) {
          if (expiry.statusColor == undefined) {
            $scope.expiryDocumentHolderListFileter.push(expiry);
          }
        });
      } else {
        $scope.expiryDocumentHolderListFileter =
          $scope.expiryDocumentHolderList;
      }
      if ($scope.expiryDocumentHolderListFileter.length == 0) {
        $scope.fielterFailure = 0;
      }
    };

    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getDashboardTopCountBasedOnVessel(
        $scope.libShipId
      ).then(function (response) {
        if (response.status == 200) {
          $scope.headerDetails = response.data.usershipCount;
        }
      });
    });

    $timeout(function () {
      CKEDITOR.config.versionCheck = false;

      if (CKEDITOR.instances.mailEditor) {
        CKEDITOR.instances.mailEditor.destroy(true);
      }

      CKEDITOR.replace('mailEditor', {
        height: 150,
        removePlugins: 'elementspath',
        resize_enabled: false,
        allowedContent: true,
        extraAllowedContent: '*(*);*{*}',
      });
    }, 500);

    $scope.logout = function () {
      $window.localStorage.removeItem('sessionObject');
      $window.localStorage.removeItem('userRole');
      $window.localStorage.removeItem('userName');
      $window.localStorage.removeItem('userEmail');
      $window.localStorage.removeItem('userId');
      $window.localStorage.removeItem('roleId');
      $window.localStorage.removeItem('role');
      $window.localStorage.removeItem('organizationId');
      $window.localStorage.removeItem('organizationName');
      $window.localStorage.removeItem('profilePicture');
      $window.localStorage.removeItem('maxShipCount');
      $window.localStorage.removeItem('maxUserCount');
      $window.localStorage.removeItem('shipProfileInfos');
      $window.localStorage.removeItem('groupShipId');
      $window.localStorage.removeItem('groupShipName');
      $window.localStorage.removeItem('editId');
      $window.localStorage.removeItem('countryName');
      $window.localStorage.removeItem('stateName');
      $window.localStorage.removeItem('shipId');
      $window.localStorage.removeItem('libShipId');
      $window.localStorage.removeItem('libshipName');
      localStorage.removeItem('logout-event');
      $window.localStorage.removeItem('logoPicture');

      toaster.pop('success', 'Logout succesfully');
      setTimeout(function () {
        $location.path('/');
      }, 2000);
      $window.location.reload();
    };

    $scope.confirmLogout = function () {
      Swal.fire({
        html: `<div class="logout-card">
                <div class="logout-icon">
                    <i class="fa fa-sign-out"></i>
                </div>
                <h3>Logout</h3>
                <p>Are you sure you want to logout?</p>
            </div>`,
        width: 480,
        showCancelButton: true,
        confirmButtonText: 'Logout',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#4a7be6',
      }).then(function (result) {
        if (result.isConfirmed) {
          $scope.logout();
        }
      });
    };

    $scope.submitEmailRequest = function () {
      if (CKEDITOR.instances.mailEditor) {
        $scope.expDocUrlshare = CKEDITOR.instances.mailEditor.getData();
      }
      $scope.loader = true;

      if (
        (!$scope.groupEmailForShare ||
          $scope.groupEmailForShare.trim() === '') &&
        (!$scope.pendingGroupData ||
          !$scope.pendingGroupData.emailId ||
          $scope.pendingGroupData.emailId.trim() === '')
      ) {
        toaster.pop('error', 'Error', 'Recipient email address is missing');
        $scope.loader = false;
        return;
      }

      if (!$scope.expDocUrlshare || $scope.expDocUrlshare.trim() === '') {
        toaster.pop('error', 'Error', 'Email content is missing');
        $scope.loader = false;
        return;
      }

      var emailData = {
        to: ($scope.groupEmailForShare || $scope.pendingGroupData.emailId)
          .split(',')
          .map((e) => e.trim())
          .filter((e) => e.length > 0),
        subject: $scope.libshipNameDoc,
        body: $scope.expDocUrlshare,
      };

      if ($scope.pendingGroupData) {
        console.log('Creating new group and sending email...');

        FunctionalityService.shareExpiryDoc(
          JSON.stringify($scope.pendingGroupData)
        )
          .then(function (groupResponse) {
            if (groupResponse.status === 200) {
              console.log('Group created successfully, now sending email...');
              return FunctionalityService.sendEmail(emailData);
            } else {
              throw new Error(
                'Failed to create group: ' +
                  (groupResponse.data?.message || 'Unknown error')
              );
            }
          })
          .then(function (emailResponse) {
            if (emailResponse.status === 200) {
              toaster.pop('success', 'Success', 'Email sent successfully');
              $('#shareMail').modal('hide');
              $scope.pendingGroupData = null;

              $timeout(function () {
                $state.reload();
              }, 1500);
            } else {
              throw new Error(
                'Email send failed: ' +
                  (emailResponse.data?.message || 'Unknown error')
              );
            }
          })
          .catch(function (error) {
            console.error('Error in email flow:', error);
            toaster.pop(
              'error',
              'Error',
              error.message || 'Failed to send email'
            );
          })
          .finally(function () {
            $scope.loader = false;
          });
      } else {
        console.log('Sending email to existing group...');

        FunctionalityService.sendEmail(emailData)
          .then(function (response) {
            if (response.status === 200) {
              toaster.pop('success', 'Success', 'Email sent successfully');
              $('#shareMail').modal('hide');

              $timeout(function () {
                $state.reload();
              }, 1500);
            } else {
              throw new Error(
                'Email send failed: ' +
                  (response.data?.message || 'Unknown error')
              );
            }
          })
          .catch(function (error) {
            console.error('Email send error:', error);
            toaster.pop(
              'error',
              'Error',
              error.message || 'Failed to send email'
            );
          })
          .finally(function () {
            $scope.loader = false;
          });
      }
    };
  },
]);

userVesselDocEBD.directive('fileModel', [
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

userVesselDocEBD.directive('customFocus', [
  function () {
    var FOCUS_CLASS = 'custom-focused';
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
