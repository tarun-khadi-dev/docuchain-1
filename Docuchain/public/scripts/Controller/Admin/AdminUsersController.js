// var adminUsers = angular.module('dapp.AdminUsersController', [
//   'ui.select',
//   'ngSanitize',
//   'angularUtils.directives.dirPagination',
// ]);

// adminUsers
//   .controller('AdminUsersController', [
//     '$scope',
//     '$window',
//     '$location',
//     '$state',
//     '$rootScope',
//     'toaster',
//     '$http',
//     '$timeout',
//     'FunctionalityService',
//      'DeletePopup',
//     function (
//       $scope,
//       $window,
//       $location,
//       $state,
//       $rootScope,
//       toaster,
//       $http,
//       $timeout,
//       FunctionalityService,
//       // Upload,
//       DeletePopup
//     ) {
//       $scope.userProfileId = $window.localStorage.getItem('userId');
//       $scope.organizationId = $window.localStorage.getItem('organizationId');
//       $scope.maxUserCount = $window.localStorage.getItem('maxUserCount');

//       $scope.adminUserList = [];
//       $scope.selctedUserList = [];
//       $scope.example1model = [];
//       $scope.example2settings = { displayProp: 'id' };
//       $scope.popuptitle = 'Add New User';
//       $scope.myFile = '';
//       $scope.file;
//       $scope.user = {};
//       $scope.password = true;
//       $scope.confirmPassword = true;
//       $scope.roleCheckmultiselece = true;
//       $scope.roleCheck = false;
//       $scope.updateUNameEnable = false;
//       $scope.updateUNameDisable = true;
//       $scope.currentPage = 1;
//       $scope.viewby = 10;
//       $scope.itemsPerPage = $scope.viewby;
//       $scope.addPopupsubmit = true;
//       $scope.editPopupsubmit = false;
//       $scope.isReadOnly = true;
//       $scope.loader = false;
//       var buttonVessel = document.getElementById('addUserlimits');

//       $scope.$on('$viewContentLoaded', function () {
//         FunctionalityService.getVesselsNameList($scope.userProfileId).then(
//           function (response) {
//             if (response.status == 200) {
//               $scope.users = response.data.shipProfileList;
//             }
//           },
//           function (error) {
//             console.log('message :: ' + error);
//           }
//         );
//       });

//       $scope.$on('$viewContentLoaded', function () {
//         FunctionalityService.getOrganizationUserList($scope.userProfileId).then(
//           function mySuccess(response) {
//             if (response.status == 201 || response.status == 200) {
//               $scope.adminUserList = JSON.stringify(response.data.getUserList);
//               $scope.adminUserList = response.data.getUserList;
//             }
//           },
//           function (error) {
//             $scope.status = 'Unable to Create Room: ' + error;
//           }
//         );
//       });

//       $scope.$on('$viewContentLoaded', function () {
//         FunctionalityService.getRoleList($scope.userProfileId).then(
//           function (response) {
//             if (response.status == 200) {
//               $scope.roleList = response.data.roleAliasInfos;
//             }
//           },
//           function (error) {
//             console.log('message :: ' + error);
//           }
//         );
//       });

//       $scope.checkUserlimit = function () {
//         if ($scope.adminUserList.length >= $scope.maxUserCount) {
//           toaster.error('User limit exceedeed');
//           buttonVessel.disabled = true;
//         } else {
//           buttonVessel.disabled = false;
//           $scope.addUserModel();
//         }
//       };

//       $scope.selectedUsers = [];
//       $scope.thumbnail = {};

//       $scope.fileReaderSupported = window.FileReader != null;
//       $scope.uploadFile = function (files) {
//         if (files != null) {
//           $scope.file = files[0];
//           if (
//             $scope.fileReaderSupported &&
//             $scope.file.type.indexOf('image') > -1
//           ) {
//             $timeout(function () {
//               var fileReader = new FileReader();
//               fileReader.readAsDataURL($scope.file);
//               fileReader.onload = function (e) {
//                 $timeout(function () {
//                   $scope.thumbnail.dataUrl = e.target.result;
//                 });
//               };
//             });
//           }
//         }
//       };
//       $scope.uploadFileedit = function (files) {
//         if (files != null) {
//           $scope.file = files[0];
//           if (
//             $scope.fileReaderSupported &&
//             $scope.file.type.indexOf('image') > -1
//           ) {
//             $timeout(function () {
//               var fileReader = new FileReader();
//               fileReader.readAsDataURL($scope.file);
//               fileReader.onload = function (e) {
//                 $timeout(function () {
//                   $scope.thumbnail.dataUrledit = e.target.result;
//                 });
//               };
//             });
//           }
//         }
//       };

//       $scope.addUser = function (user) {
//         $scope.loader = true;

//         if (user.password !== user.confirmPassword) {
//           $scope.loader = false;
//           toaster.error('error', 'Password does not match');
//         } else {
//           if (user.id == undefined) {
//             var userInfoId = [];

//             // Single select for ALL roles now
//             if (
//               user.selectedUsers != null &&
//               user.selectedUsers.id != undefined
//             ) {
//               userInfoId.push(user.selectedUsers.id);
//             }

//             var userInfo = {
//               firstName: user.firstName,
//               lastName: user.lastName,
//               userName: user.userName,
//               password: user.password,
//               roleId: user.selectedCategory.roleId,
//               businessCategory: user.selectedCategory.roleAliasName,
//               mail: user.mail,
//               organizationId: $scope.organizationId,
//               loginId: $scope.userProfileId,
//               shipProfileIds: userInfoId,
//             };
//             FunctionalityService.addUser(userInfo, $scope.file).then(
//               function (response) {
//                 $scope.loader = false;

//                 if (response.status == 200 || response.status == 201) {
//                   $('#addNewUser').modal('hide');
//                   $timeout(function () {
//                     toaster.success({ title: response.data.message });
//                   }, 300);
//                   $state.reload();
//                 } else {
//                   toaster.clear();
//                   toaster.error({ title: response.data.message });
//                 }
//               },
//               function myError(err) {
//                 $scope.loader = false;
//                 console.log('Error response', err);
//               }
//             );
//           } else if (user.id != undefined) {
//             $scope.userInfoIdupdateEdit = [];

//             // Single select for ALL roles now
//             if (
//               user.selectedUsersEdit != null &&
//               user.selectedUsersEdit.id != undefined
//             ) {
//               $scope.userInfoIdupdateEdit.push(user.selectedUsersEdit.id);
//             }

//             var userInfo = {
//               firstName: user.firstNameEdit,
//               lastName: user.lastNameEdit,
//               mail: user.mailEdit,
//               userId: user.id,
//               shipProfileIds: $scope.userInfoIdupdateEdit,
//             };
//             FunctionalityService.editUser(userInfo, $scope.file).then(
//               function mySuccess(response) {
//                 $scope.loader = false;

//                 if (response.status == 200 || response.status == 201) {
//                   $('#ediUserinfo').modal('hide');
//                   toaster.pop('success', response.data.message);
//                   setTimeout(function () {
//                     $state.reload();
//                   }, 1000);
//                 } else {
//                   toaster.pop('error', response.data.message);
//                 }
//               },
//               function myError(err) {
//                 $scope.loader = false;
//                 console.log('Error response', err);
//               }
//             );
//           }
//         }
//       };

// // delete
//       $scope.confirmDeleteUser = function(id){
//           $scope.deleteuserId = [];
//           $scope.deleteuserId.push(id);
//           DeletePopup.confirm(
//               "Delete User",
//               "Are you sure you want to delete this user?",
//               function(){
//                   $scope.deleteUser();
//               }
//           );
//       };

//       $scope.deleteuserId = [];
//       $scope.getDeleteUserId = function (id) {
//         $scope.deleteuserId.push(id);
//       };

//       $scope.deleteUser = function () {
//         $scope.loader = true;
//         var deleteUser = {
//           userIds: $scope.deleteuserId,
//           loginId: $scope.userProfileId,
//         };

//         FunctionalityService.deleteAdminUser(deleteUser).then(
//         function (response) {
//           $scope.loader = false;
//           if (response.status === 200) {
//             $('#delete').modal('hide');
//             $scope.getUserList();
//             toaster.success({ title: response.data.message });
//           } else {
//             toaster.error({ title: response.data.message });
//           }
//         },
//         function (err) {
//           $scope.loader = false;
//           console.log("Error response", err);
//         }
//       );
//     };

//       $scope.changeStatusAdmin = function (status) {
//         $scope.loader = true;
//         var data = status.userId;
//         FunctionalityService.getchangeStatusOfUser(data).then(
//           function mySuccess(response) {
//             $scope.loader = false;
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.checkUncheckAll = function () {
//         $scope.selctedUserList = [];
//         if ($scope.checkall) {
//           $scope.checkall = true;
//           angular.forEach($scope.adminUserList, function (value) {
//             $scope.selctedUserList.push(value.userId);
//           });
//         } else {
//           $scope.checkall = false;
//         }
//         angular.forEach($scope.adminUserList, function (user) {
//           user.checked = $scope.checkall;
//         });
//       };

//       $scope.updateCheckall = function ($index, user) {
//         var userTotal = $scope.adminUserList.length;
//         var count = 0;
//         $scope.selctedUserList = [];
//         angular.forEach($scope.adminUserList, function (item) {
//           if (item.checked) {
//             count++;
//             $scope.selctedUserList.push(item.userId);
//           }
//         });
//         if (userTotal == count) {
//           $scope.checkall = true;
//         } else {
//           $scope.checkall = false;
//         }
//       };

//       $scope.singleToggle = function (object) {
//         $scope.loader = true;
//         toaster.clear();
//         $scope.selctedUserList = [];
//         $scope.selctedUserList.push(object.userId);

//         if (object.status == 1) {
//           var data = {
//             userIds: $scope.selctedUserList,
//             loginId: $scope.userProfileId,
//             status: 0,
//           };
//           FunctionalityService.deActivateAllUser(JSON.stringify(data)).then(
//             function (response) {
//               $scope.loader = false;
//               if (response.status == 201 || response.status == 200) {
//                 toaster.pop('success', response.data.message);
//                 setTimeout(function () {
//                   $state.reload();
//                 }, 1000);
//                 $scope.selctedUserList = [];
//                 $scope.checkall = false;
//               } else if (response.status == 206) {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         } else {
//           var data = {
//             userIds: $scope.selctedUserList,
//             loginId: $scope.userProfileId,
//             status: 1,
//           };
//           FunctionalityService.activateAllUser(JSON.stringify(data)).then(
//             function (response) {
//               $scope.loader = false;

//               if (response.status == 201 || response.status == 200) {
//                 toaster.pop('success', response.data.message);
//                 setTimeout(function () {
//                   $state.reload();
//                 }, 1000);
//                 $scope.selctedUserList = [];
//                 $scope.checkall = false;
//               } else if (response.status == 206) {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         }
//       };

//       $scope.toggleChange = function (user) {
//         $scope.loader = true;
//         $scope.selctedUserList.push(user.userId);
//         if (user.status == 1) {
//           var userData = {
//             userIds: $scope.selctedUserList,
//             status: 0,
//             loginId: $scope.userProfileId,
//           };
//           FunctionalityService.activateAllUser(JSON.stringify(userData)).then(
//             function (response) {
//               $scope.loader = false;

//               if (response.status == 200) {
//                 toaster.pop('success', 'User deactivated successfully');
//                 setTimeout(function () {
//                   $state.reload();
//                 }, 1000);
//               } else {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         } else {
//           var userData = {
//             userIds: $scope.selctedUserList,
//             status: 1,
//             loginId: $scope.userProfileId,
//           };
//           FunctionalityService.deActivateAllUser(JSON.stringify(userData)).then(
//             function (response) {
//               $scope.loader = false;

//               if (response.status == 200) {
//                 toaster.pop('success', 'User activated successfully');
//                 setTimeout(function () {
//                   $state.reload();
//                 }, 1000);
//               } else {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         }
//       };

//       $scope.activateAllUser = function () {
//         $scope.loader = true;
//         var userData = {
//           userIds: $scope.selctedUserList,
//           status: 1,
//           loginId: $scope.userProfileId,
//         };
//         FunctionalityService.activateAllUser(JSON.stringify(userData)).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200) {
//               toaster.pop('success', response.data.message);
//               setTimeout(function () {
//                 $state.reload();
//               }, 1000);
//             } else {
//               toaster.pop('error', response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.deActivateAllUser = function () {
//         $scope.loader = true;
//         var userData = {
//           userIds: $scope.selctedUserList,
//           status: 0,
//           loginId: $scope.userProfileId,
//         };
//         FunctionalityService.deActivateAllUser(JSON.stringify(userData)).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200) {
//               toaster.pop('success', response.data.message);
//               setTimeout(function () {
//                 $state.reload();
//               }, 1000);
//             } else {
//               toaster.pop('error', response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       $scope.deleteAllUser = function () {
//         $scope.loader = true;
//         var userData = {
//           userIds: $scope.selctedUserList,
//           loginId: $scope.userProfileId,
//         };
//         FunctionalityService.deleteAdminUser(JSON.stringify(userData)).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200) {
//               toaster.pop('success', response.data.message);
//               setTimeout(function () {
//                 $state.reload();
//               }, 1000);
//             } else {
//               toaster.pop('error', response.data.message);
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       };

//       adminUsers.directive('fileInput', [
//         '$parse',
//         function ($parse) {
//           return {
//             restrict: 'A',
//             link: function (scope, ele, attrs) {
//               ele.bind('change', function () {
//                 $parse(attrs.fileInput).assign(scope, ele[0].files);
//                 scope.$apply();
//               });
//             },
//           };
//         },
//       ]);

//       $scope.editUser = function (userId) {
//         $scope.popuptitle = 'Edit User';
//         $scope.selectedUsers = [];
//         $scope.roleCheckmultiselece = false;
//         $scope.roleCheck = true;
//         $scope.password = false;
//         $scope.confirmPassword = false;
//         $scope.updateUNameDisable = false;
//         $scope.updateUNameEnable = true;
//         $scope.addPopupsubmit = false;
//         $scope.editPopupsubmit = true;

//         angular.forEach($scope.adminUserList, function (value) {
//           if (value.userId == userId) {
//             $scope.user.id = value.userId;
//             $scope.user.firstNameEdit = value.firstName;
//             $scope.user.lastNameEdit = value.lastName;
//             $scope.user.userNameEdit = value.userName;
//             $scope.user.businessCategoryEdit = value.businessCategory;
//             $scope.user.mailEdit = value.mail;
//             $scope.user.selectedCategoryEdit = value.businessCategory;
//             $scope.user.roleId = value.roleId;
//             $scope.thumbnail.dataUrledit = value.profilePicture;

//             // Single select for ALL roles now
//             if (value.shipProfileInfos && value.shipProfileInfos.length > 0) {
//               $scope.user.selectedUsersEdit = value.shipProfileInfos[0];
//             } else {
//               $scope.user.selectedUsersEdit = undefined;
//             }
//           }
//         });
//       };

//       $scope.viewUser = function (userId) {
//         $scope.selectedUsers = [];
//         angular.forEach($scope.adminUserList, function (value) {
//           if (value.userId == userId) {
//             $scope.user.id = value.userId;
//             $scope.user.firstNameView = value.firstName;
//             $scope.user.lastNameView = value.lastName;
//             $scope.user.userNameView = value.userName;
//             $scope.user.businessCategoryView = value.businessCategory;
//             $scope.user.mailView = value.mail;
//             $scope.user.selectedCategoryView = value.businessCategory;
//             $scope.thumbnail.dataUrl = value.profilePicture;

//             // Single select for ALL roles now
//             if (value.shipProfileInfos && value.shipProfileInfos.length > 0) {
//               $scope.user.selectedUsersView = value.shipProfileInfos[0];
//             } else {
//               $scope.user.selectedUsersView = undefined;
//             }
//           }
//         });
//       };

//       $scope.cloneUser = function (userToClone) {
//         $scope.addUserModel();
//         $scope.popuptitle = 'Clone User';
//         $scope.showClonePasswordWarning = true;
//         $scope.roleLocked = true;
//         $scope.disabled = false;

//         $scope.user.id = undefined;

//         $scope.user.firstName = userToClone.firstName + ' - copy';
//         $scope.user.lastName = userToClone.lastName + ' - copy';
//         $scope.user.userName = userToClone.userName + '-copy';

//         if (userToClone.mail && userToClone.mail.indexOf('@') > -1) {
//           var emailParts = userToClone.mail.split('@');
//           $scope.user.mail = emailParts[0] + '-copy@' + emailParts[1];
//         } else {
//           $scope.user.mail = userToClone.mail + '-copy';
//         }

//         $scope.user.password = 'Temp@123';
//         $scope.user.confirmPassword = 'Temp@123';

//         if ($scope.roleList) {
//           var matchedRole = $scope.roleList.find(function (role) {
//             return (
//               role.roleAliasName === userToClone.role ||
//               role.roleAliasName === userToClone.businessCategory
//             );
//           });

//           if (matchedRole) {
//             $scope.user.selectedCategory = matchedRole;
//           }
//         }

//         $scope.user.selectedUsers = undefined;
//       };

//       $scope.hideCloneWarning = function () {
//         $scope.showClonePasswordWarning = false;
//       };

//       $scope.setPage = function (pageNo) {
//         $scope.currentPage = pageNo;
//       };

//       $scope.pageChanged = function () {
//         console.log('Page changed to: ' + $scope.currentPage);
//       };

//       $scope.setItemsPerPage = function (num) {
//         $scope.itemsPerPage = num;
//         $scope.currentPage = 1;
//       };

//       $scope.checkPassword = function (password, confirmPassword) {
//         if (password != confirmPassword) {
//           $scope.IsMatch = true;
//           $scope.isDisabled = true;
//         } else if (password == confirmPassword) {
//           $scope.IsMatch = false;
//           $scope.newpassword = password;
//           $scope.isDisabled = false;
//         }
//       };

//       // Not strictly needed anymore since everyone is single-select, but kept for bindings
//       $scope.categoryChanges = function (selectedCategory) {
//         console.log('Role Selected: ' + JSON.stringify(selectedCategory));
//       };

//       $scope.close = function () {
//         $state.reload();
//       };

//       $scope.addUserModel = function () {
//         $scope.popuptitle = 'Add New User';
//         $scope.selectedUsers = [{}];
//         $scope.roleCheckmultiselece = true;
//         $scope.password = true;
//         $scope.confirmPassword = true;
//         $scope.roleCheck = false;
//         $scope.addPopupsubmit = true;
//         $scope.editPopupsubmit = false;
//         $scope.roleLocked = false;
//         $scope.showClonePasswordWarning = false;
//         $scope.popupClear();
//       };

//       $scope.popupClear = function () {
//         $scope.user.firstName = '';
//         $scope.user.lastName = '';
//         $scope.user.mail = '';
//         $scope.user.userName = '';
//         $scope.user.password = '';
//         $scope.user.confirmPassword = '';
//         $scope.user.selectedCategory = '';
//         $scope.user.selectedUsers = undefined;
//       };

//       $scope.popupClearView = function () {
//         $scope.user.firstNameView = '';
//         $scope.user.lastNameView = '';
//         $scope.user.mailView = '';
//         $scope.user.userNameView = '';
//         $scope.user.selectedCategoryView = '';
//         if (angular.isDefined($scope.user.selectedUsersView)) {
//           delete $scope.user.selectedUsersView;
//         }
//       };

//       $scope.moreShipClick = function (role, removeUserId) {
//         console.log(
//           'UserDetails :: ' + role + ' removeUserId :: ' + removeUserId
//         );
//         $scope.roleNameForUser = role;
//         $scope.userIdForRemove = removeUserId;
//       };

//       $scope.delShip = function (role, removeUserId, shipId) {
//         $scope.loader = true;

//         if (role == 'ShipMaster') {
//           var data = {
//             id: shipId,
//             userId: $scope.userProfileId,
//             shipMasterId: removeUserId,
//           };
//           FunctionalityService.delShipMaster(data).then(
//             function (response) {
//               $scope.loader = false;
//               if (response.status == 201 || response.status == 200) {
//                 $timeout(function () {
//                   toaster.pop('success', response.data.message);
//                 }, 300);
//                 $state.reload();
//               } else if (response.status == 206) {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         }
//         if (role == 'TechManager') {
//           var data = {
//             id: shipId,
//             userId: $scope.userProfileId,
//             techManagerIds: [removeUserId],
//           };
//           FunctionalityService.delTech(data).then(
//             function (response) {
//               $scope.loader = false;
//               if (response.status == 201 || response.status == 200) {
//                 $timeout(function () {
//                   toaster.pop('success', response.data.message);
//                 }, 300);
//                 $state.reload();
//               } else if (response.status == 206) {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         }
//         if (role == 'CommercialManager') {
//           var data = {
//             id: shipId,
//             userId: $scope.userProfileId,
//             commercialMasterIds: [removeUserId],
//           };
//           FunctionalityService.delCom(data).then(
//             function (response) {
//               $scope.loader = false;
//               if (response.status == 201 || response.status == 200) {
//                 $timeout(function () {
//                   toaster.pop('success', response.data.message);
//                 }, 300);
//                 $state.reload();
//               } else if (response.status == 206) {
//                 toaster.pop('error', response.data.message);
//               }
//             },
//             function myError(err) {
//               $scope.loader = false;
//               console.log('Error response', err);
//             }
//           );
//         }
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

var adminUsers = angular.module('dapp.AdminUsersController', [
  'ui.select',
  'ngSanitize',
  'angularUtils.directives.dirPagination',
]);

adminUsers
  .controller('AdminUsersController', [
    '$scope',
    '$window',
    '$location',
    '$state',
    '$rootScope',
    'toaster',
    '$http',
    '$timeout',
    'FunctionalityService',
    'DeletePopup',
    function (
      $scope,
      $window,
      $location,
      $state,
      $rootScope,
      toaster,
      $http,
      $timeout,
      FunctionalityService,
      // Upload,
      DeletePopup
    ) {
      $scope.userProfileId = $window.localStorage.getItem('userId');
      $scope.organizationId = $window.localStorage.getItem('organizationId');
      $scope.maxUserCount = $window.localStorage.getItem('maxUserCount');

      $scope.adminUserList = [];
      $scope.selctedUserList = [];
      $scope.example1model = [];
      $scope.example2settings = { displayProp: 'id' };
      $scope.popuptitle = 'Add New User';
      $scope.myFile = '';
      $scope.file;
      $scope.user = {};
      $scope.password = true;
      $scope.confirmPassword = true;
      $scope.roleCheckmultiselece = true;
      $scope.roleCheck = false;
      $scope.updateUNameEnable = false;
      $scope.updateUNameDisable = true;
      $scope.currentPage = 1;
      $scope.viewby = 10;
      $scope.itemsPerPage = $scope.viewby;
      $scope.addPopupsubmit = true;
      $scope.editPopupsubmit = false;
      $scope.isReadOnly = true;
      $scope.loader = false;
      var buttonVessel = document.getElementById('addUserlimits');

      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getVesselsNameList($scope.userProfileId).then(
          function (response) {
            if (response.status == 200) {
              $scope.users = response.data.shipProfileList;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      });

      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getOrganizationUserList($scope.userProfileId).then(
          function mySuccess(response) {
            if (response.status == 201 || response.status == 200) {
              $scope.adminUserList = JSON.stringify(response.data.getUserList);
              $scope.adminUserList = response.data.getUserList;
            }
          },
          function (error) {
            $scope.status = 'Unable to Create Room: ' + error;
          }
        );
      });

      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getRoleList($scope.userProfileId).then(
          function (response) {
            if (response.status == 200) {
              $scope.roleList = response.data.roleAliasInfos;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      });

      $scope.checkUserlimit = function () {
        if ($scope.adminUserList.length >= $scope.maxUserCount) {
          toaster.error('User limit exceedeed');
          buttonVessel.disabled = true;
        } else {
          buttonVessel.disabled = false;
          $scope.addUserModel();
        }
      };

      $scope.selectedUsers = [];
      $scope.thumbnail = {};

      $scope.fileReaderSupported = window.FileReader != null;
      $scope.uploadFile = function (files) {
        if (files != null) {
          $scope.file = files[0];
          if (
            $scope.fileReaderSupported &&
            $scope.file.type.indexOf('image') > -1
          ) {
            $timeout(function () {
              var fileReader = new FileReader();
              fileReader.readAsDataURL($scope.file);
              fileReader.onload = function (e) {
                $timeout(function () {
                  $scope.thumbnail.dataUrl = e.target.result;
                });
              };
            });
          }
        }
      };
      $scope.uploadFileedit = function (files) {
        if (files != null) {
          $scope.file = files[0];
          if (
            $scope.fileReaderSupported &&
            $scope.file.type.indexOf('image') > -1
          ) {
            $timeout(function () {
              var fileReader = new FileReader();
              fileReader.readAsDataURL($scope.file);
              fileReader.onload = function (e) {
                $timeout(function () {
                  $scope.thumbnail.dataUrledit = e.target.result;
                });
              };
            });
          }
        }
      };

      $scope.addUser = function (user) {
        $scope.loader = true;

        // if (user.password !== user.confirmPassword) {
        //   $scope.loader = false;
        //   toaster.error('error', 'Password does not match');
        // } else {
        if (user.id == undefined && user.password !== user.confirmPassword) {
          $scope.loader = false;
          toaster.error('error', 'Password does not match');
          return;
        } else {
          if (user.id == undefined) {
            var userInfoId = [];

            // Single select for ALL roles now
            if (
              user.selectedUsers != null &&
              user.selectedUsers.id != undefined
            ) {
              userInfoId.push(user.selectedUsers.id);
            }

            var userInfo = {
              firstName: user.firstName,
              lastName: user.lastName,
              userName: user.userName,
              password: user.password,
              roleId: user.selectedCategory.roleId,
              businessCategory: user.selectedCategory.roleAliasName,
              mail: user.mail,
              organizationId: $scope.organizationId,
              loginId: $scope.userProfileId,
              shipProfileIds: userInfoId,
            };
            FunctionalityService.addUser(userInfo, $scope.file).then(
              function (response) {
                $scope.loader = false;

                if (response.status == 200 || response.status == 201) {
                  $('#addNewUser').modal('hide');
                  $timeout(function () {
                    toaster.success({ title: response.data.message });
                  }, 300);
                  $state.reload();
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
          } else if (user.id != undefined) {
            $scope.userInfoIdupdateEdit = [];

            // Single select for ALL roles now
            if (
              user.selectedUsersEdit != null &&
              user.selectedUsersEdit.id != undefined
            ) {
              $scope.userInfoIdupdateEdit.push(user.selectedUsersEdit.id);
            }

            var userInfo = {
              firstName: user.firstNameEdit,
              lastName: user.lastNameEdit,
              mail: user.mailEdit,
              userId: user.id,
              shipProfileIds: $scope.userInfoIdupdateEdit,
            };
            FunctionalityService.editUser(userInfo, $scope.file).then(
              function mySuccess(response) {
                $scope.loader = false;

                if (response.status == 200 || response.status == 201) {
                  $('#ediUserinfo').modal('hide');
                  toaster.pop('success', response.data.message);
                  setTimeout(function () {
                    $state.reload();
                  }, 1000);
                } else {
                  toaster.pop('error', response.data.message);
                }
              },
              function myError(err) {
                $scope.loader = false;
                console.log('Error response', err);
              }
            );
          }
        }
      };

      // delete
      $scope.confirmDeleteUser = function (id) {
        $scope.deleteuserId = [];
        $scope.deleteuserId.push(id);
        DeletePopup.confirm(
          'Delete User',
          'Are you sure you want to delete this user?',
          function () {
            $scope.deleteUser();
          }
        );
      };

      $scope.deleteuserId = [];
      $scope.getDeleteUserId = function (id) {
        $scope.deleteuserId.push(id);
      };

      //   $scope.deleteUser = function () {
      //     $scope.loader = true;
      //     var deleteUser = {
      //       userIds: $scope.deleteuserId,
      //       loginId: $scope.userProfileId,
      //     };

      //     FunctionalityService.deleteAdminUser(deleteUser).then(
      //     function (response) {
      //       $scope.loader = false;
      //       if (response.status === 200) {
      //         $('#delete').modal('hide');
      //         $scope.getUserList();
      //         toaster.success({ title: response.data.message });
      //       } else {
      //         toaster.error({ title: response.data.message });
      //       }
      //     },
      //     function (err) {
      //       $scope.loader = false;
      //       console.log("Error response", err);
      //     }
      //   );
      // };

      $scope.deleteUser = function () {
        $scope.loader = true;

        var deleteUser = {
          userIds: $scope.deleteuserId,
          loginId: $scope.userProfileId,
        };

        FunctionalityService.deleteAdminUser(deleteUser).then(
          function (response) {
            $scope.loader = false;

            if (response.status === 200) {
              toaster.success({ title: response.data.message });

              // remove deleted user from UI
              $scope.adminUserList = $scope.adminUserList.filter(function (u) {
                return !$scope.deleteuserId.includes(u.userId);
              });

              $scope.deleteuserId = []; // IMPORTANT
            }
          },
          function (err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.changeStatusAdmin = function (status) {
        $scope.loader = true;
        var data = status.userId;
        FunctionalityService.getchangeStatusOfUser(data).then(
          function mySuccess(response) {
            $scope.loader = false;
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.checkUncheckAll = function () {
        $scope.selctedUserList = [];
        if ($scope.checkall) {
          $scope.checkall = true;
          angular.forEach($scope.adminUserList, function (value) {
            $scope.selctedUserList.push(value.userId);
          });
        } else {
          $scope.checkall = false;
        }
        angular.forEach($scope.adminUserList, function (user) {
          user.checked = $scope.checkall;
        });
      };

      $scope.updateCheckall = function ($index, user) {
        var userTotal = $scope.adminUserList.length;
        var count = 0;
        $scope.selctedUserList = [];
        angular.forEach($scope.adminUserList, function (item) {
          if (item.checked) {
            count++;
            $scope.selctedUserList.push(item.userId);
          }
        });
        if (userTotal == count) {
          $scope.checkall = true;
        } else {
          $scope.checkall = false;
        }
      };

      $scope.singleToggle = function (object) {
        $scope.loader = true;
        toaster.clear();
        $scope.selctedUserList = [];
        $scope.selctedUserList.push(object.userId);

        if (object.status == 1) {
          var data = {
            userIds: $scope.selctedUserList,
            loginId: $scope.userProfileId,
            status: 0,
          };
          FunctionalityService.deActivateAllUser(JSON.stringify(data)).then(
            function (response) {
              $scope.loader = false;
              if (response.status == 201 || response.status == 200) {
                toaster.pop('success', response.data.message);
                setTimeout(function () {
                  $state.reload();
                }, 1000);
                $scope.selctedUserList = [];
                $scope.checkall = false;
              } else if (response.status == 206) {
                toaster.pop('error', response.data.message);
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        } else {
          var data = {
            userIds: $scope.selctedUserList,
            loginId: $scope.userProfileId,
            status: 1,
          };
          FunctionalityService.activateAllUser(JSON.stringify(data)).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 201 || response.status == 200) {
                toaster.pop('success', response.data.message);
                setTimeout(function () {
                  $state.reload();
                }, 1000);
                $scope.selctedUserList = [];
                $scope.checkall = false;
              } else if (response.status == 206) {
                toaster.pop('error', response.data.message);
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        }
      };

      $scope.toggleChange = function (user) {
        $scope.loader = true;
        $scope.selctedUserList.push(user.userId);
        if (user.status == 1) {
          var userData = {
            userIds: $scope.selctedUserList,
            status: 0,
            loginId: $scope.userProfileId,
          };
          FunctionalityService.activateAllUser(JSON.stringify(userData)).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 200) {
                toaster.pop('success', 'User deactivated successfully');
                setTimeout(function () {
                  $state.reload();
                }, 1000);
              } else {
                toaster.pop('error', response.data.message);
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        } else {
          var userData = {
            userIds: $scope.selctedUserList,
            status: 1,
            loginId: $scope.userProfileId,
          };
          FunctionalityService.deActivateAllUser(JSON.stringify(userData)).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 200) {
                toaster.pop('success', 'User activated successfully');
                setTimeout(function () {
                  $state.reload();
                }, 1000);
              } else {
                toaster.pop('error', response.data.message);
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        }
      };

      $scope.activateAllUser = function () {
        $scope.loader = true;
        var userData = {
          userIds: $scope.selctedUserList,
          status: 1,
          loginId: $scope.userProfileId,
        };
        FunctionalityService.activateAllUser(JSON.stringify(userData)).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200) {
              toaster.pop('success', response.data.message);
              setTimeout(function () {
                $state.reload();
              }, 1000);
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

      $scope.deActivateAllUser = function () {
        $scope.loader = true;
        var userData = {
          userIds: $scope.selctedUserList,
          status: 0,
          loginId: $scope.userProfileId,
        };
        FunctionalityService.deActivateAllUser(JSON.stringify(userData)).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200) {
              toaster.pop('success', response.data.message);
              setTimeout(function () {
                $state.reload();
              }, 1000);
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

      $scope.deleteAllUser = function () {
        $scope.loader = true;
        var userData = {
          userIds: $scope.selctedUserList,
          loginId: $scope.userProfileId,
        };
        FunctionalityService.deleteAdminUser(JSON.stringify(userData)).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200) {
              toaster.pop('success', response.data.message);
              setTimeout(function () {
                $state.reload();
              }, 1000);
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

      adminUsers.directive('fileInput', [
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

      $scope.editUser = function (userId) {
        $scope.popuptitle = 'Edit User';
        $scope.selectedUsers = [];
        $scope.roleCheckmultiselece = false;
        $scope.roleCheck = true;
        $scope.password = false;
        $scope.confirmPassword = false;
        $scope.updateUNameDisable = false;
        $scope.updateUNameEnable = true;
        $scope.addPopupsubmit = false;
        $scope.editPopupsubmit = true;

        angular.forEach($scope.adminUserList, function (value) {
          if (value.userId == userId) {
            $scope.user.id = value.userId;
            $scope.user.firstNameEdit = value.firstName;
            $scope.user.lastNameEdit = value.lastName;
            $scope.user.userNameEdit = value.userName;
            $scope.user.businessCategoryEdit = value.businessCategory;
            $scope.user.mailEdit = value.mail;
            $scope.user.selectedCategoryEdit = value.businessCategory;
            $scope.user.roleId = value.roleId;
            $scope.thumbnail.dataUrledit = value.profilePicture;

            // Single select for ALL roles now
            if (value.shipProfileInfos && value.shipProfileInfos.length > 0) {
              $scope.user.selectedUsersEdit = value.shipProfileInfos[0];
            } else {
              $scope.user.selectedUsersEdit = undefined;
            }
          }
        });
      };

      $scope.viewUser = function (userId) {
        $scope.selectedUsers = [];
        angular.forEach($scope.adminUserList, function (value) {
          if (value.userId == userId) {
            $scope.user.id = value.userId;
            $scope.user.firstNameView = value.firstName;
            $scope.user.lastNameView = value.lastName;
            $scope.user.userNameView = value.userName;
            $scope.user.businessCategoryView = value.businessCategory;
            $scope.user.mailView = value.mail;
            $scope.user.selectedCategoryView = value.businessCategory;
            $scope.thumbnail.dataUrl = value.profilePicture;

            // Single select for ALL roles now
            if (value.shipProfileInfos && value.shipProfileInfos.length > 0) {
              $scope.user.selectedUsersView = value.shipProfileInfos[0];
            } else {
              $scope.user.selectedUsersView = undefined;
            }
          }
        });
      };

      $scope.cloneUser = function (userToClone) {
        $scope.addUserModel();
        $scope.popuptitle = 'Clone User';
        $scope.showClonePasswordWarning = true;
        $scope.roleLocked = true;
        $scope.disabled = false;

        $scope.user.id = undefined;

        $scope.user.firstName = userToClone.firstName + ' - copy';
        $scope.user.lastName = userToClone.lastName + ' - copy';
        $scope.user.userName = userToClone.userName + '-copy';

        if (userToClone.mail && userToClone.mail.indexOf('@') > -1) {
          var emailParts = userToClone.mail.split('@');
          $scope.user.mail = emailParts[0] + '-copy@' + emailParts[1];
        } else {
          $scope.user.mail = userToClone.mail + '-copy';
        }

        $scope.user.password = 'Temp@123';
        $scope.user.confirmPassword = 'Temp@123';

        if ($scope.roleList) {
          var matchedRole = $scope.roleList.find(function (role) {
            return (
              role.roleAliasName === userToClone.role ||
              role.roleAliasName === userToClone.businessCategory
            );
          });

          if (matchedRole) {
            $scope.user.selectedCategory = matchedRole;
          }
        }

        $scope.user.selectedUsers = undefined;
      };

      $scope.hideCloneWarning = function () {
        $scope.showClonePasswordWarning = false;
      };

      $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
      };

      $scope.pageChanged = function () {
        console.log('Page changed to: ' + $scope.currentPage);
      };

      $scope.setItemsPerPage = function (num) {
        $scope.itemsPerPage = num;
        $scope.currentPage = 1;
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

      // Not strictly needed anymore since everyone is single-select, but kept for bindings
      $scope.categoryChanges = function (selectedCategory) {
        console.log('Role Selected: ' + JSON.stringify(selectedCategory));
      };

      $scope.close = function () {
        $state.reload();
      };

      $scope.addUserModel = function () {
        $scope.popuptitle = 'Add New User';
        $scope.selectedUsers = [{}];
        $scope.roleCheckmultiselece = true;
        $scope.password = true;
        $scope.confirmPassword = true;
        $scope.roleCheck = false;
        $scope.addPopupsubmit = true;
        $scope.editPopupsubmit = false;
        $scope.roleLocked = false;
        $scope.showClonePasswordWarning = false;
        $scope.popupClear();
      };

      $scope.popupClear = function () {
        $scope.user.firstName = '';
        $scope.user.lastName = '';
        $scope.user.mail = '';
        $scope.user.userName = '';
        $scope.user.password = '';
        $scope.user.confirmPassword = '';
        $scope.user.selectedCategory = '';
        $scope.user.selectedUsers = undefined;
      };

      $scope.popupClearView = function () {
        $scope.user.firstNameView = '';
        $scope.user.lastNameView = '';
        $scope.user.mailView = '';
        $scope.user.userNameView = '';
        $scope.user.selectedCategoryView = '';
        if (angular.isDefined($scope.user.selectedUsersView)) {
          delete $scope.user.selectedUsersView;
        }
      };

      $scope.moreShipClick = function (role, removeUserId) {
        console.log(
          'UserDetails :: ' + role + ' removeUserId :: ' + removeUserId
        );
        $scope.roleNameForUser = role;
        $scope.userIdForRemove = removeUserId;
      };

      // $scope.delShip = function (role, removeUserId, shipId) {
      //   $scope.loader = true;

      //   if (role == 'ShipMaster') {
      //     var data = {
      //       id: shipId,
      //       userId: $scope.userProfileId,
      //       shipMasterId: removeUserId,
      //     };
      //     FunctionalityService.delShipMaster(data).then(
      //       function (response) {
      //         $scope.loader = false;
      //         if (response.status == 201 || response.status == 200) {
      //           $timeout(function () {
      //             toaster.pop('success', response.data.message);
      //           }, 300);
      //           $state.reload();
      //         } else if (response.status == 206) {
      //           toaster.pop('error', response.data.message);
      //         }
      //       },
      //       function myError(err) {
      //         $scope.loader = false;
      //         console.log('Error response', err);
      //       }
      //     );
      //   }
      //   if (role == 'TechManager') {
      //     var data = {
      //       id: shipId,
      //       userId: $scope.userProfileId,
      //       techManagerIds: [removeUserId],
      //     };
      //     FunctionalityService.delTech(data).then(
      //       function (response) {
      //         $scope.loader = false;
      //         if (response.status == 201 || response.status == 200) {
      //           $timeout(function () {
      //             toaster.pop('success', response.data.message);
      //           }, 300);
      //           $state.reload();
      //         } else if (response.status == 206) {
      //           toaster.pop('error', response.data.message);
      //         }
      //       },
      //       function myError(err) {
      //         $scope.loader = false;
      //         console.log('Error response', err);
      //       }
      //     );
      //   }
      //   if (role == 'CommercialManager') {
      //     var data = {
      //       id: shipId,
      //       userId: $scope.userProfileId,
      //       commercialMasterIds: [removeUserId],
      //     };
      //     FunctionalityService.delCom(data).then(
      //       function (response) {
      //         $scope.loader = false;
      //         if (response.status == 201 || response.status == 200) {
      //           $timeout(function () {
      //             toaster.pop('success', response.data.message);
      //           }, 300);
      //           $state.reload();
      //         } else if (response.status == 206) {
      //           toaster.pop('error', response.data.message);
      //         }
      //       },
      //       function myError(err) {
      //         $scope.loader = false;
      //         console.log('Error response', err);
      //       }
      //     );
      //   }
      // };

      $scope.confirmRemoveUser = function (
        type,
        role,
        userId,
        shipId,
        isFromModal = false
      ) {
        let title = 'Remove Vessel';
        let message = 'Are you sure you want to remove this vessel from user?';

        DeletePopup.confirm(
          title,
          message,
          function () {
            $scope.loader = true;

            let apiCall;
            let data;

            // 🔹 Decide API based on role
            if (role === 'ShipMaster') {
              data = {
                id: shipId,
                userId: $scope.userProfileId,
                shipMasterId: userId,
              };
              apiCall = FunctionalityService.delShipMaster(data);
            } else if (role === 'TechManager') {
              data = {
                id: shipId,
                userId: $scope.userProfileId,
                techManagerIds: [userId],
              };
              apiCall = FunctionalityService.delTech(data);
            } else if (role === 'CommercialManager') {
              data = {
                id: shipId,
                userId: $scope.userProfileId,
                commercialMasterIds: [userId],
              };
              apiCall = FunctionalityService.delCom(data);
            }

            // 🔹 Execute API
            apiCall.then(
              function (response) {
                $scope.loader = false;

                if (response.status === 200 || response.status === 201) {
                  toaster.success({ title: response.data.message });

                  // ✅ Update UI without reload
                  $scope.adminUserList.forEach((user) => {
                    if (user.userId === userId) {
                      user.shipProfileInfos = user.shipProfileInfos.filter(
                        (s) => s.id !== shipId
                      );
                    }
                  });

                  // 🔥 Close modal if needed
                  if (isFromModal) {
                    $('#shipList').modal('hide');
                  }
                } else if (response.status === 206) {
                  toaster.error({ title: response.data.message });
                }
              },
              function (err) {
                $scope.loader = false;
                console.log('Error response', err);
              }
            );
          },
          'Remove'
        );
      };
    },
  ])
  .directive('customFocus', [
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
