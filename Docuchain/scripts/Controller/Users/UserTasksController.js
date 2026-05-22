// // var userTasks = angular.module('dapp.UserTasksController', [
// //   'angularUtils.directives.dirPagination',
// //   'moment-picker',
// // ]);

// // userTasks
// //   .controller('UserTasksController', [
// //     '$scope',
// //     '$window',
// //     '$location',
// //     '$state',
// //     '$rootScope',
// //     'FunctionalityService',
// //     'toaster',
// //     '$timeout',
// //     'DeletePopup',
// //     function (
// //       $scope,
// //       $window,
// //       $location,
// //       $state,
// //       $rootScope,
// //       FunctionalityService,
// //       toaster,
// //       $timeout,
// //       DeletePopup
// //     ) {
// //       $scope.sessionObject =
// //         JSON.parse($window.localStorage.getItem('sessionObject')) || {};
// //       // $scope.userId = $scope.sessionObject.userId  || $window.localStorage.getItem('userId');

// //       var session = JSON.parse($window.localStorage.getItem('sessionObject'));

// //       if (session && session.userId) {
// //         $scope.userId = session.userId;
// //       } else {
// //         $scope.userId = $window.localStorage.getItem('userId');
// //       }

// //       $scope.currentPage = 1;
// //       $scope.viewby = 10;
// //       $scope.userProfileType = [];
// //       $rootScope.userInfoId = [];
// //       $scope.userList = [];
// //       $scope.taskUsers = [];
// //       $scope.updatetaskUsers = [];
// //       $scope.checkboxSelection = 1;
// //       $scope.updatetask = {};
// //       $scope.loader = false;
// //       $scope.mindate = moment().add(0, 'day');
// //       $scope.itemsPerPageForHistory = $scope.viewby;
// //       $scope.myVar = 'tabOne';
// //       $rootScope.selected = 5;
// //       $scope.change = function (value) {
// //         $scope.myVar = value;
// //       };

// //       $scope.userTaskListAssignedByUser = function () {
// //         $scope.loader = true;
// //         $scope.tab = 1;
// //         FunctionalityService.getTaskAssignedByUser($scope.userId).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.status == 200 || response.status == 201) {
// //               $scope.message = JSON.stringify(response.data.taskAssignedByUser);
// //               $scope.taskAssignedByUser = response.data.taskAssignedByUser;
// //             } else {
// //               toaster.pop('error', response.data.message);

// //               // toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };

// //       // ==========================================
// //       // Select/Clear All for UPDATE Task Users (Fixed for UserTasks)
// //       // ==========================================
// //       $scope.selectAllUpdateUsers = function () {
// //         if ($scope.userLists && $scope.userLists.length > 0) {
// //           if (!$scope.updatetask) $scope.updatetask = {};
// //           // CHANGED: UserTasks uses updatetaskUsers, not userProfileInfos
// //           $scope.updatetask.updatetaskUsers = angular.copy($scope.userLists);
// //         }
// //       };

// //       $scope.clearAllUpdateUsers = function () {
// //         if ($scope.updatetask) {
// //           // CHANGED: UserTasks uses updatetaskUsers, not userProfileInfos
// //           $scope.updatetask.updatetaskUsers = [];
// //         }
// //       };

// //       // ==========================================
// //       // Fix the Clear Inputs Function
// //       // ==========================================
// //       $scope.clearCreatetaskInputs = function (createTask) {
// //         console.log('empty');
// //         createTask.shipId = '';
// //         createTask.endDate = '';
// //         createTask.taskDetails = '';
// //         createTask.taskName = '';
// //         createTask.startDate = '';

// //         // Call this to properly clear the array
// //         $scope.clearAllCreateUsers();
// //       };

// //       // ==========================================
// //       // Select/Clear All for CREATE Task Users
// //       // ==========================================
// //       $scope.selectAllCreateUsers = function () {
// //         if ($scope.userLists && $scope.userLists.length > 0) {
// //           if (!$scope.createTask) $scope.createTask = {};

// //           // FIX: Use .slice() instead of angular.copy() to maintain exact object references
// //           $scope.createTask.selectedUsers = $scope.userLists.slice();
// //         }
// //       };

// //       $scope.clearAllCreateUsers = function () {
// //         if ($scope.createTask) {
// //           $scope.createTask.selectedUsers = [];
// //         }
// //       };

// //       // ==========================================
// //       // Select/Clear All for UPDATE Task Users
// //       // ==========================================
// //       $scope.selectAllUpdateUsers = function () {
// //         if ($scope.userLists && $scope.userLists.length > 0) {
// //           if (!$scope.updatetask) $scope.updatetask = {};

// //           // FIX: Use .slice() instead of angular.copy() here as well
// //           $scope.updatetask.updatetaskUsers = $scope.userLists.slice();
// //         }
// //       };

// //       $scope.clearAllUpdateUsers = function () {
// //         if ($scope.updatetask) {
// //           $scope.updatetask.updatetaskUsers = [];
// //         }
// //       };
// //       //User TASK ASSIGNED TO YOU
// //       $scope.userTaskListAssignedToYou = function () {
// //         $scope.loader = true;
// //         $scope.tab = 2;
// //         FunctionalityService.getTaskAssignedToUser($scope.userId).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.status == 200 || response.status == 201) {
// //               $scope.message = JSON.stringify(response.data.taskAssignedToUser);
// //               $scope.taskAssignedToUser = response.data.taskAssignedToUser;
// //             } else {
// //               toaster.pop('error', response.data.message);

// //               //toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };
// //       // $scope.taskListInfoAssignedToUser();

// //       //createTask start

// //       $scope.createTaskForUser = function (createTask) {
// //         $scope.loader = true;

// //         $scope.userIdSelected = [];
// //         angular.forEach(createTask.selectedUsers, function (value) {
// //           $scope.userIdSelected.push(value.userId);
// //         });
// //         if ($scope.createTask.checkboxSelection == 1) {
// //           var postData = {
// //             shipId: createTask.shipId,
// //             checkboxSelectionId: createTask.checkboxSelection,
// //             createdBy: $scope.userId,
// //             taskName: createTask.taskName,
// //             taskDetails: createTask.taskDetails,
// //             startDate: createTask.startDate,
// //             endDate: createTask.endDate,
// //             userProfileIds: $scope.userIdSelected,
// //           };
// //         } else {
// //           var postData = {
// //             checkboxSelectionId: createTask.checkboxSelection,
// //             createdBy: $scope.userId,
// //             taskName: createTask.taskName,
// //             taskDetails: createTask.taskDetails,
// //             startDate: createTask.startDate,
// //             endDate: createTask.endDate,
// //             userProfileIds: $scope.userIdSelected,
// //           };
// //         }
// //         FunctionalityService.createShipRelatedTask(postData).then(
// //           function (response) {
// //             $scope.loader = false;

// //             if (response.status == 200) {
// //               $('#createtsk').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 //toaster.clear();
// //                 toaster.success({ title: response.data.message });
// //                 //toaster.pop('success', response.data.message);
// //               }, 2000);
// //             } else {
// //               //toaster.clear();
// //               toaster.pop('error', response.data.message);
// //               //  toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function myError(err) {
// //             $scope.loader = false;
// //             console.log('Error response', err);
// //           }
// //         );
// //       };
// //       //End  createShipTask

// //       // $scope.getUpdateTaskId = function (updateId) {
// //       //   $scope.updatetask = angular.copy(updateId); // Use copy to avoid mutating the original list row

// //       //   // Ensure the picker recognizes these as moment objects
// //       //   if ($scope.updatetask.startDate) {
// //       //       $scope.updatetask.startDate = moment($scope.updatetask.startDate);
// //       //   }
// //       //   if ($scope.updatetask.endDate) {
// //       //       $scope.updatetask.endDate = moment($scope.updatetask.endDate);
// //       //   }

// //       //   // $scope.updatetask = updateId;
// //       //   $scope.updatetask.shipName = $scope.updatetask.shipName;
// //       //   $scope.userProfileId = [];
// //       //   angular.forEach(updateId.userProfileInfos, function (value) {
// //       //     $scope.userProfileId.push(value);
// //       //   });
// //       //   $scope.updatetask.updatetaskUsers = $scope.userProfileId;
// //       //   updatetask.updatetaskUsers = $scope.taskUsers;
// //       //   $scope.userProfileIdList = [];
// //       //   angular.forEach($scope.updatetask.userProfileInfos, function (value, key) {
// //       //     var userinfo = value.userId;
// //       //     $scope.userProfileIdList.push(userinfo);
// //       //   });
// //       // }

// //       $scope.getUpdateTaskId = function (updateId) {
// //         $scope.updatetask = angular.copy(updateId);

// //         // ✅ Use correct fields (same as admin)
// //         if (updateId.updateDStartDate) {
// //           $scope.updatetask.startDate = moment(
// //             updateId.updateDStartDate,
// //             'DD-MM-YYYY HH:mm'
// //           );
// //         }

// //         if (updateId.updateEndDate) {
// //           $scope.updatetask.endDate = moment(
// //             updateId.updateEndDate,
// //             'DD-MM-YYYY HH:mm'
// //           );
// //         }
// //         // Users mapping
// //         $scope.userProfileId = [];
// //         angular.forEach(updateId.userProfileInfos, function (value) {
// //           $scope.userProfileId.push(value);
// //         });

// //         $scope.updatetask.updatetaskUsers = $scope.userProfileId;

// //         $scope.userProfileIdList = [];
// //         angular.forEach($scope.updatetask.userProfileInfos, function (value) {
// //           $scope.userProfileIdList.push(value.userId);
// //         });
// //       };

// //       $scope.updateShipTask = function (data) {
// //         $scope.loader = true;

// //         var sdate = data.startDate;
// //         var edate = data.endDate;
// //         if (data.shipId == undefined) {
// //           $scope.checkboxSelection = 2;
// //         }
// //         // if ($scope.updateSDate != undefined) {
// //         //   sdate = $scope.updateSDate;
// //         // }
// //         // if ($scope.updateEDate != undefined) {
// //         //   edate = $scope.updateEDate;
// //         // }
// //         $scope.userProfileIdList = [];
// //         angular.forEach(data.updatetaskUsers, function (value) {
// //           $scope.userProfileIdList.push(value.userId);
// //         });

// //         var updateTaskData = {
// //           id: $scope.updatetask.taskId,
// //           checkboxSelectionId: $scope.checkboxSelection,
// //           createdBy: data.createdBy,
// //           shipId: data.shipId,
// //           taskName: data.taskName,
// //           taskDetails: data.taskDetails,
// //           startDate: data.startDate,
// //           endDate: data.endDate,
// //           userProfileIds: $scope.userProfileIdList,
// //           //"createdBy": "1",
// //           taskStatusId: data.taskStatusId,
// //           loginId: $scope.userId,
// //         };
// //         FunctionalityService.updateShipRelatedTask(
// //           JSON.stringify(updateTaskData)
// //         ).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.data.status == 'Success') {
// //               $('#updatetask').modal('hide');
// //               $timeout(function () {
// //                 toaster.pop('success', response.data.message);
// //               }, 2000);
// //               $scope.userTaskListAssignedByUser();
// //               // toaster.success({ title: response.data.message });
// //               //}, 2000);
// //             } else {
// //               // toaster.clear();
// //               toaster.pop('error', response.data.message);
// //             }
// //           },
// //           function (error) {
// //             $location.path('/');
// //             console.log('message :: ' + error.data.message);
// //           }
// //         );
// //       };

// //       // get ship profile list starting
// //       $scope.getShipProfileList = function () {
// //         $scope.loader = true;
// //         FunctionalityService.getShipProfileList($scope.userId).then(
// //           function (response) {
// //             $scope.loader = false;

// //             if (response.status == 200) {
// //               $scope.shipList = response.data.shipProfileList;
// //             }
// //           },
// //           function (error) {
// //             console.log('message :: ' + error);
// //           }
// //         );
// //       };

// //       //end get ship profile

// //       //view task Start by user

// //       $scope.getViewTaskById = function (taskAssignedByUser) {
// //         $rootScope.viewTaskByUsers = taskAssignedByUser;
// //         $scope.userprofile = taskAssignedByUser.userProfileInfos;
// //       };

// //       //view task End

// //       // //view task Start to user

// //       // $scope.getViewTaskByIdToUser = function (taskAssignedByUser) {
// //       //   $rootScope.viewTaskByToUsers = taskAssignedByUser;
// //       //   console.log("inside Task Id :: " + $rootScope.viewTaskByToUsers.id);
// //       // }

// //       // //view task End

// //       // //Delete Task Based on Id

// //       // $rootScope.deleteTaskId;
// //       // $scope.getDeleteTaskId = function (id) {
// //       //   $rootScope.deleteTaskId = id;
// //       //   $scope.deleteTaskId = id;

// //       // }
// //       $scope.cancelInput = function (cancelData) {
// //         $('#viewtaskaty').modal('hide');
// //         $state.reload();
// //         $scope.getAssignedTask();
// //       };
// //       // $scope.deleteTaskById = function () {
// //       //   $scope.loader = true;
// //       //   data = {
// //       //     "taskId": $rootScope.deleteTaskId
// //       //   }

// //       $scope.deleteTaskById = function (taskId) {
// //         console.log('UserId:', $scope.userId);
// //         console.log('TaskId:', taskId);
// //         $scope.loader = true;

// //         var data = {
// //           taskId: taskId,
// //           createdBy: $scope.userId,
// //         };

// //         FunctionalityService.deleteTask(data).then(function (response) {
// //           $scope.loader = false;

// //           if (response.data.status == 'Success') {
// //             toaster.pop('success', response.data.message);

// //             setTimeout(function () {
// //               $state.reload();
// //             }, 1000);
// //           } else {
// //             toaster.pop('error', response.data.message);
// //           }
// //         });
// //       };

// //       //   FunctionalityService.deleteTask(data).then(function (response) {
// //       //     $scope.loader = false;
// //       //     if (response.data.status == "Success") {
// //       //       // $('#delete').modal('hide');
// //       //       $state.reload();
// //       //       $timeout(function () {
// //       //         toaster.pop('success', response.data.message);
// //       //       }, 1000);
// //       //     }
// //       //     else {
// //       //       // $('#delete').modal('hide');

// //       //       toaster.pop('error', response.data.message);

// //       //     }

// //       //   }, function (error) {
// //       //     console.log("message :: " + error.message);
// //       //   });
// //       // }

// //       //End Delete Task Based On Id

// //       $scope.confirmDeleteTask = function (taskId) {
// //         DeletePopup.confirm(
// //           'Delete Task',
// //           'Are you sure you want to delete this task?',
// //           function () {
// //             $scope.deleteTaskById(taskId);
// //           }
// //         );
// //       };
// //       // get user profile list based on Id start
// //       $scope.getUserProfileList = function () {
// //         $scope.loader = true;

// //         FunctionalityService.getUserProfileList($scope.userId).then(
// //           function (response) {
// //             $scope.loader = false;

// //             if (response.status == 200) {
// //               $scope.userLists = response.data.userList;

// //               // $scope.commercialManagerUsers = response.data.userInfos.commercialManagerInfos;
// //               // $scope.shipmaster = response.data.userInfos.shipMasterInfos;
// //               // $scope.techManager = response.data.userInfos.technicalManagerInfos;

// //               // //iterating 3 arrays
// //               // angular.forEach($scope.techManager, function (data) {
// //               //   var name = { "id": data.userId, "userName": data.userName };
// //               //   $scope.userProfileType.push(name);
// //               //   console.log("technicalManagerInfos:", $scope.userProfileType);
// //               // });
// //               // angular.forEach($scope.commercialManagerUsers, function (data) {
// //               //   var name = { "id": data.userId, "userName": data.userName };
// //               //   $scope.userProfileType.push(name);
// //               //   console.log(" $scope.commercialManagerInfos:::", $scope.userProfileType);
// //               // });
// //               // angular.forEach($scope.shipmaster, function (data) {
// //               //   var name = { "id": data.userId, "userName": data.userName };
// //               //   $scope.userProfileType.push(name);
// //               // });
// //             }
// //           },
// //           function (error) {
// //             console.log('message :: ' + error);
// //           }
// //         );
// //       };
// //       //$scope.getUserProfileList();

// //       //end user profile list

// //       //get status list

// //       FunctionalityService.taskStatusList().then(
// //         function (response) {
// //           $scope.loader = false;

// //           $scope.statuslist = response.data.statusAll;
// //           $scope.message = JSON.stringify(response.data.statusAll);
// //           $scope.statusAll = response.data.statusAll;
// //           $scope.totalItems = $scope.statusAll.length;
// //           $scope.prograssing = false;
// //         },
// //         function (error) {
// //           $state.go('dapp.taskManagement');
// //           $scope.status = 'Unable to Create Room: ';
// //           console.log('message :: ' + error.message);
// //         }
// //       );
// //       //End get status list

// //       //start status update
// //       $scope.updateStatusWithRemarkToUsers = function (assignedTasks) {
// //         $scope.loader = true;

// //         var taskData = {
// //           createdBy: $scope.userId,
// //           taskStatusId: assignedTasks.taskStatus,
// //           taskId: assignedTasks.taskId,
// //           userRemarks: assignedTasks.userRemarks,
// //           loginId: $scope.userId,
// //         };
// //         FunctionalityService.updateStatusWithRemarks(taskData).then(
// //           function (response) {
// //             $scope.loader = false;
// //             if (response.data.status == 'Success') {
// //               // $scope.taskListInfoAssignedByUser();
// //               $('#viewtaskaty').modal('hide');
// //               $state.reload();
// //               $timeout(function () {
// //                 //toaster.clear();
// //                 $scope.tab = 2;
// //                 toaster.pop('success', response.data.message);

// //                 // toaster.success({ title: response.data.message });
// //               }, 1000);
// //             } else {
// //               //  toaster.clear();
// //               toaster.pop('error', response.data.message);

// //               //toaster.error({ title: response.data.message });
// //             }
// //           },
// //           function (error) {
// //             $scope.status = 'Unable to Create Room: ';
// //             console.log('message :: ' + error.message);
// //           }
// //         );
// //       };

// //       $scope.setPage = function (pageNo) {
// //         $scope.currentPage = pageNo;
// //       };

// //       $scope.pageChanged = function () {
// //         console.log('Page changed to: ' + $scope.currentPage);
// //       };

// //       $scope.setItemsPerPage = function (num) {
// //         $scope.itemsPerPage = num;
// //         $scope.currentPage = 1; //reset to first page
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

// var userTasks = angular.module('dapp.UserTasksController', [
//   'angularUtils.directives.dirPagination',
//   'moment-picker',
// ]);

// userTasks
//   .controller('UserTasksController', [
//     '$scope',
//     '$window',
//     '$location',
//     '$state',
//     '$stateParams', // <-- INJECTED HERE
//     '$rootScope',
//     'FunctionalityService',
//     'toaster',
//     '$timeout',
//     'DeletePopup',
//     function (
//       $scope,
//       $window,
//       $location,
//       $state,
//       $stateParams, // <-- INJECTED HERE
//       $rootScope,
//       FunctionalityService,
//       toaster,
//       $timeout,
//       DeletePopup
//     ) {
//       $scope.sessionObject =
//         JSON.parse($window.localStorage.getItem('sessionObject')) || {};

//       var session = JSON.parse($window.localStorage.getItem('sessionObject'));

//       if (session && session.userId) {
//         $scope.userId = session.userId;
//       } else {
//         $scope.userId = $window.localStorage.getItem('userId');
//       }

//       $scope.currentPage = 1;
//       $scope.viewby = 10;
//       $scope.userProfileType = [];
//       $rootScope.userInfoId = [];
//       $scope.userList = [];
//       $scope.taskUsers = [];
//       $scope.updatetaskUsers = [];
//       $scope.checkboxSelection = 1;
//       $scope.updatetask = {};
//       $scope.loader = false;
//       $scope.mindate = moment().add(0, 'day');
//       $scope.itemsPerPageForHistory = $scope.viewby;

//       $rootScope.selected = 5;

//       // ==========================================
//       // NEW URL LOGIC: Instantly pick the correct tab!
//       // ==========================================
//       // if ($stateParams.tabName) {
//       //   $scope.myVar = $stateParams.tabName;
//       // } else {
//       //   $scope.myVar = 'tabOne';
//       // }
//       // ==========================================
//       // NEW URL LOGIC: Instantly pick the correct tab!
//       // ==========================================
//       var activeTab = $window.localStorage.getItem('activeTaskTab');

//       if (activeTab) {
//         // Set the active tab based on what was clicked in the dashboard
//         $scope.myVar = activeTab;

//         // IMPORTANT: We wait 500ms before deleting the storage!
//         // This stops the "double-execution" bug from resetting the tab.
//         $timeout(function () {
//           $window.localStorage.removeItem('activeTaskTab');
//         }, 500);
//       } else if ($stateParams && $stateParams.tabName) {
//         // Fallback for router params if used elsewhere
//         $scope.myVar = $stateParams.tabName;
//       } else {
//         // Default behavior for normal navigation
//         $scope.myVar = 'tabOne';
//       }

//       $scope.change = function (value) {
//         $scope.myVar = value;
//       };
//       // ==========================================

//       $scope.change = function (value) {
//         $scope.myVar = value;
//       };
//       // ==========================================

//       $scope.userTaskListAssignedByUser = function () {
//         $scope.loader = true;
//         $scope.tab = 1;
//         FunctionalityService.getTaskAssignedByUser($scope.userId).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200 || response.status == 201) {
//               $scope.message = JSON.stringify(response.data.taskAssignedByUser);
//               $scope.taskAssignedByUser = response.data.taskAssignedByUser;
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

//       $scope.selectAllUpdateUsers = function () {
//         if ($scope.userLists && $scope.userLists.length > 0) {
//           if (!$scope.updatetask) $scope.updatetask = {};
//           $scope.updatetask.updatetaskUsers = angular.copy($scope.userLists);
//         }
//       };

//       $scope.clearAllUpdateUsers = function () {
//         if ($scope.updatetask) {
//           $scope.updatetask.updatetaskUsers = [];
//         }
//       };

//       $scope.clearCreatetaskInputs = function (createTask) {
//         createTask.shipId = '';
//         createTask.endDate = '';
//         createTask.taskDetails = '';
//         createTask.taskName = '';
//         createTask.startDate = '';
//         $scope.clearAllCreateUsers();
//       };

//       $scope.selectAllCreateUsers = function () {
//         if ($scope.userLists && $scope.userLists.length > 0) {
//           if (!$scope.createTask) $scope.createTask = {};
//           $scope.createTask.selectedUsers = $scope.userLists.slice();
//         }
//       };

//       $scope.clearAllCreateUsers = function () {
//         if ($scope.createTask) {
//           $scope.createTask.selectedUsers = [];
//         }
//       };

//       $scope.userTaskListAssignedToYou = function () {
//         $scope.loader = true;
//         $scope.tab = 2;
//         FunctionalityService.getTaskAssignedToUser($scope.userId).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.status == 200 || response.status == 201) {
//               $scope.message = JSON.stringify(response.data.taskAssignedToUser);
//               $scope.taskAssignedToUser = response.data.taskAssignedToUser;
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

//       $scope.createTaskForUser = function (createTask) {
//         $scope.loader = true;

//         $scope.userIdSelected = [];
//         angular.forEach(createTask.selectedUsers, function (value) {
//           $scope.userIdSelected.push(value.userId);
//         });
//         if ($scope.createTask.checkboxSelection == 1) {
//           var postData = {
//             shipId: createTask.shipId,
//             checkboxSelectionId: createTask.checkboxSelection,
//             createdBy: $scope.userId,
//             taskName: createTask.taskName,
//             taskDetails: createTask.taskDetails,
//             startDate: createTask.startDate,
//             endDate: createTask.endDate,
//             userProfileIds: $scope.userIdSelected,
//           };
//         } else {
//           var postData = {
//             checkboxSelectionId: createTask.checkboxSelection,
//             createdBy: $scope.userId,
//             taskName: createTask.taskName,
//             taskDetails: createTask.taskDetails,
//             startDate: createTask.startDate,
//             endDate: createTask.endDate,
//             userProfileIds: $scope.userIdSelected,
//           };
//         }
//         FunctionalityService.createShipRelatedTask(postData).then(
//           function (response) {
//             $scope.loader = false;

//             if (response.status == 200) {
//               $('#createtsk').modal('hide');
//               $state.reload();
//               $timeout(function () {
//                 toaster.success({ title: response.data.message });
//               }, 2000);
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

//       $scope.getUpdateTaskId = function (updateId) {
//         $scope.updatetask = angular.copy(updateId);

//         if (updateId.updateDStartDate) {
//           $scope.updatetask.startDate = moment(
//             updateId.updateDStartDate,
//             'DD-MM-YYYY HH:mm'
//           );
//         }

//         if (updateId.updateEndDate) {
//           $scope.updatetask.endDate = moment(
//             updateId.updateEndDate,
//             'DD-MM-YYYY HH:mm'
//           );
//         }

//         $scope.userProfileId = [];
//         angular.forEach(updateId.userProfileInfos, function (value) {
//           $scope.userProfileId.push(value);
//         });

//         $scope.updatetask.updatetaskUsers = $scope.userProfileId;

//         $scope.userProfileIdList = [];
//         angular.forEach($scope.updatetask.userProfileInfos, function (value) {
//           $scope.userProfileIdList.push(value.userId);
//         });
//       };

//       $scope.updateShipTask = function (data) {
//         $scope.loader = true;

//         var sdate = data.startDate;
//         var edate = data.endDate;
//         if (data.shipId == undefined) {
//           $scope.checkboxSelection = 2;
//         }

//         $scope.userProfileIdList = [];
//         angular.forEach(data.updatetaskUsers, function (value) {
//           $scope.userProfileIdList.push(value.userId);
//         });

//         var updateTaskData = {
//           id: $scope.updatetask.taskId,
//           checkboxSelectionId: $scope.checkboxSelection,
//           createdBy: data.createdBy,
//           shipId: data.shipId,
//           taskName: data.taskName,
//           taskDetails: data.taskDetails,
//           startDate: data.startDate,
//           endDate: data.endDate,
//           userProfileIds: $scope.userProfileIdList,
//           taskStatusId: data.taskStatusId,
//           loginId: $scope.userId,
//         };
//         FunctionalityService.updateShipRelatedTask(
//           JSON.stringify(updateTaskData)
//         ).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.data.status == 'Success') {
//               $('#updatetask').modal('hide');
//               $timeout(function () {
//                 toaster.pop('success', response.data.message);
//               }, 2000);
//               $scope.userTaskListAssignedByUser();
//             } else {
//               toaster.pop('error', response.data.message);
//             }
//           },
//           function (error) {
//             $location.path('/');
//             console.log('message :: ' + error.data.message);
//           }
//         );
//       };

//       $scope.getShipProfileList = function () {
//         $scope.loader = true;
//         FunctionalityService.getShipProfileList($scope.userId).then(
//           function (response) {
//             $scope.loader = false;

//             if (response.status == 200) {
//               $scope.shipList = response.data.shipProfileList;
//             }
//           },
//           function (error) {
//             console.log('message :: ' + error);
//           }
//         );
//       };

//       $scope.getViewTaskById = function (taskAssignedByUser) {
//         $rootScope.viewTaskByUsers = taskAssignedByUser;
//         $scope.userprofile = taskAssignedByUser.userProfileInfos;
//       };

//       $scope.cancelInput = function (cancelData) {
//         $('#viewtaskaty').modal('hide');
//         $state.reload();
//         $scope.getAssignedTask();
//       };

//       $scope.deleteTaskById = function (taskId) {
//         $scope.loader = true;

//         var data = {
//           taskId: taskId,
//           createdBy: $scope.userId,
//         };

//         FunctionalityService.deleteTask(data).then(function (response) {
//           $scope.loader = false;

//           if (response.data.status == 'Success') {
//             toaster.pop('success', response.data.message);

//             setTimeout(function () {
//               $state.reload();
//             }, 1000);
//           } else {
//             toaster.pop('error', response.data.message);
//           }
//         });
//       };

//       $scope.confirmDeleteTask = function (taskId) {
//         DeletePopup.confirm(
//           'Delete Task',
//           'Are you sure you want to delete this task?',
//           function () {
//             $scope.deleteTaskById(taskId);
//           }
//         );
//       };

//       $scope.getUserProfileList = function () {
//         $scope.loader = true;

//         FunctionalityService.getUserProfileList($scope.userId).then(
//           function (response) {
//             $scope.loader = false;

//             if (response.status == 200) {
//               $scope.userLists = response.data.userList;
//             }
//           },
//           function (error) {
//             console.log('message :: ' + error);
//           }
//         );
//       };

//       FunctionalityService.taskStatusList().then(
//         function (response) {
//           $scope.loader = false;

//           $scope.statuslist = response.data.statusAll;
//           $scope.message = JSON.stringify(response.data.statusAll);
//           $scope.statusAll = response.data.statusAll;
//           $scope.totalItems = $scope.statusAll.length;
//           $scope.prograssing = false;
//         },
//         function (error) {
//           $state.go('dapp.taskManagement');
//           $scope.status = 'Unable to Create Room: ';
//           console.log('message :: ' + error.message);
//         }
//       );

//       $scope.updateStatusWithRemarkToUsers = function (assignedTasks) {
//         $scope.loader = true;

//         var taskData = {
//           createdBy: $scope.userId,
//           taskStatusId: assignedTasks.taskStatus,
//           taskId: assignedTasks.taskId,
//           userRemarks: assignedTasks.userRemarks,
//           loginId: $scope.userId,
//         };
//         FunctionalityService.updateStatusWithRemarks(taskData).then(
//           function (response) {
//             $scope.loader = false;
//             if (response.data.status == 'Success') {
//               $('#viewtaskaty').modal('hide');
//               $state.reload();
//               $timeout(function () {
//                 $scope.tab = 2;
//                 toaster.pop('success', response.data.message);
//               }, 1000);
//             } else {
//               toaster.pop('error', response.data.message);
//             }
//           },
//           function (error) {
//             $scope.status = 'Unable to Create Room: ';
//             console.log('message :: ' + error.message);
//           }
//         );
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

var userTasks = angular.module('dapp.UserTasksController', [
  'angularUtils.directives.dirPagination',
  'moment-picker',
]);

userTasks
  .controller('UserTasksController', [
    '$scope',
    '$window',
    '$location',
    '$state',
    '$stateParams',
    '$rootScope',
    'FunctionalityService',
    'toaster',
    '$timeout',
    'DeletePopup',
    function (
      $scope,
      $window,
      $location,
      $state,
      $stateParams,
      $rootScope,
      FunctionalityService,
      toaster,
      $timeout,
      DeletePopup
    ) {
      $scope.sessionObject =
        JSON.parse($window.localStorage.getItem('sessionObject')) || {};

      var session = JSON.parse($window.localStorage.getItem('sessionObject'));

      if (session && session.userId) {
        $scope.userId = session.userId;
      } else {
        $scope.userId = $window.localStorage.getItem('userId');
      }

      $scope.currentPage = 1;
      $scope.viewby = 10;
      $scope.userProfileType = [];
      $rootScope.userInfoId = [];
      $scope.userList = [];
      $scope.taskUsers = [];
      $scope.updatetaskUsers = [];

      // FIX: Initialize createTask object with default radio button value (1 = Ship Related task)
      $scope.createTask = {
        checkboxSelection: 1,
      };

      $scope.updatetask = {};
      $scope.loader = false;
      $scope.mindate = moment().add(0, 'day');
      $scope.itemsPerPageForHistory = $scope.viewby;

      $rootScope.selected = 5;

      // ==========================================
      // NEW URL LOGIC: Instantly pick the correct tab!
      // ==========================================
      var activeTab = $window.localStorage.getItem('activeTaskTab');

      if (activeTab) {
        // Set the active tab based on what was clicked in the dashboard
        $scope.myVar = activeTab;

        // IMPORTANT: We wait 500ms before deleting the storage!
        // This stops the "double-execution" bug from resetting the tab.
        $timeout(function () {
          $window.localStorage.removeItem('activeTaskTab');
        }, 500);
      } else if ($stateParams && $stateParams.tabName) {
        // Fallback for router params if used elsewhere
        $scope.myVar = $stateParams.tabName;
      } else {
        // Default behavior for normal navigation
        $scope.myVar = 'tabOne';
      }

      $scope.change = function (value) {
        $scope.myVar = value;
      };

      $scope.userTaskListAssignedByUser = function () {
        $scope.loader = true;
        $scope.tab = 1;
        FunctionalityService.getTaskAssignedByUser($scope.userId).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200 || response.status == 201) {
              $scope.message = JSON.stringify(response.data.taskAssignedByUser);
              $scope.taskAssignedByUser = response.data.taskAssignedByUser;
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

      $scope.selectAllUpdateUsers = function () {
        if ($scope.userLists && $scope.userLists.length > 0) {
          if (!$scope.updatetask) $scope.updatetask = {};
          $scope.updatetask.updatetaskUsers = angular.copy($scope.userLists);
        }
      };

      $scope.clearAllUpdateUsers = function () {
        if ($scope.updatetask) {
          $scope.updatetask.updatetaskUsers = [];
        }
      };

      $scope.clearCreatetaskInputs = function (createTask) {
        if (!createTask) return;
        createTask.shipId = '';
        createTask.endDate = '';
        createTask.taskDetails = '';
        createTask.taskName = '';
        createTask.startDate = '';
        $scope.clearAllCreateUsers();
      };

      $scope.selectAllCreateUsers = function () {
        if ($scope.userLists && $scope.userLists.length > 0) {
          if (!$scope.createTask) $scope.createTask = { checkboxSelection: 1 };
          $scope.createTask.selectedUsers = $scope.userLists.slice();
        }
      };

      $scope.clearAllCreateUsers = function () {
        if ($scope.createTask) {
          $scope.createTask.selectedUsers = [];
        }
      };

      $scope.userTaskListAssignedToYou = function () {
        $scope.loader = true;
        $scope.tab = 2;
        FunctionalityService.getTaskAssignedToUser($scope.userId).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200 || response.status == 201) {
              $scope.message = JSON.stringify(response.data.taskAssignedToUser);
              $scope.taskAssignedToUser = response.data.taskAssignedToUser;
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

      $scope.createTaskForUser = function (createTask) {
        $scope.loader = true;

        $scope.userIdSelected = [];
        angular.forEach(createTask.selectedUsers, function (value) {
          $scope.userIdSelected.push(value.userId);
        });

        var postData = {
          checkboxSelectionId: createTask.checkboxSelection,
          createdBy: $scope.userId,
          taskName: createTask.taskName,
          taskDetails: createTask.taskDetails,
          startDate: createTask.startDate,
          endDate: createTask.endDate,
          userProfileIds: $scope.userIdSelected,
        };

        if ($scope.createTask.checkboxSelection == 1) {
          postData.shipId = createTask.shipId;
        }

        FunctionalityService.createShipRelatedTask(postData).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $('#createtsk').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.success({ title: response.data.message });
              }, 2000);
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

      $scope.getUpdateTaskId = function (updateId) {
        $scope.updatetask = angular.copy(updateId);

        if (updateId.updateDStartDate) {
          $scope.updatetask.startDate = moment(
            updateId.updateDStartDate,
            'DD-MM-YYYY HH:mm'
          );
        }

        if (updateId.updateEndDate) {
          $scope.updatetask.endDate = moment(
            updateId.updateEndDate,
            'DD-MM-YYYY HH:mm'
          );
        }

        $scope.userProfileId = [];
        angular.forEach(updateId.userProfileInfos, function (value) {
          $scope.userProfileId.push(value);
        });

        $scope.updatetask.updatetaskUsers = $scope.userProfileId;

        $scope.userProfileIdList = [];
        angular.forEach($scope.updatetask.userProfileInfos, function (value) {
          $scope.userProfileIdList.push(value.userId);
        });
      };

      $scope.updateShipTask = function (data) {
        $scope.loader = true;

        var sdate = data.startDate;
        var edate = data.endDate;
        if (data.shipId == undefined) {
          $scope.checkboxSelection = 2;
        }

        $scope.userProfileIdList = [];
        angular.forEach(data.updatetaskUsers, function (value) {
          $scope.userProfileIdList.push(value.userId);
        });

        var updateTaskData = {
          id: $scope.updatetask.taskId,
          checkboxSelectionId: $scope.checkboxSelection,
          createdBy: data.createdBy,
          shipId: data.shipId,
          taskName: data.taskName,
          taskDetails: data.taskDetails,
          startDate: data.startDate,
          endDate: data.endDate,
          userProfileIds: $scope.userProfileIdList,
          taskStatusId: data.taskStatusId,
          loginId: $scope.userId,
        };
        FunctionalityService.updateShipRelatedTask(
          JSON.stringify(updateTaskData)
        ).then(
          function (response) {
            $scope.loader = false;
            if (response.data.status == 'Success') {
              $('#updatetask').modal('hide');
              $timeout(function () {
                toaster.pop('success', response.data.message);
              }, 2000);
              $scope.userTaskListAssignedByUser();
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function (error) {
            $location.path('/');
            console.log('message :: ' + error.data.message);
          }
        );
      };

      $scope.getShipProfileList = function () {
        $scope.loader = true;
        FunctionalityService.getShipProfileList($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $scope.shipList = response.data.shipProfileList;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      };

      $scope.getViewTaskById = function (taskAssignedByUser) {
        $rootScope.viewTaskByUsers = taskAssignedByUser;
        $scope.userprofile = taskAssignedByUser.userProfileInfos;
      };

      $scope.cancelInput = function (cancelData) {
        $('#viewtaskaty').modal('hide');
        $state.reload();
        // FIX: Changed from $scope.getAssignedTask() which does not exist
        $scope.userTaskListAssignedToYou();
      };

      $scope.deleteTaskById = function (taskId) {
        $scope.loader = true;

        var data = {
          taskId: taskId,
          createdBy: $scope.userId,
        };

        FunctionalityService.deleteTask(data).then(function (response) {
          $scope.loader = false;

          if (response.data.status == 'Success') {
            toaster.pop('success', response.data.message);

            setTimeout(function () {
              $state.reload();
            }, 1000);
          } else {
            toaster.pop('error', response.data.message);
          }
        });
      };

      $scope.confirmDeleteTask = function (taskId) {
        DeletePopup.confirm(
          'Delete Task',
          'Are you sure you want to delete this task?',
          function () {
            $scope.deleteTaskById(taskId);
          }
        );
      };

      $scope.getUserProfileList = function () {
        $scope.loader = true;

        FunctionalityService.getUserProfileList($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $scope.userLists = response.data.userList;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      };

      FunctionalityService.taskStatusList().then(
        function (response) {
          $scope.loader = false;

          $scope.statuslist = response.data.statusAll;
          $scope.message = JSON.stringify(response.data.statusAll);
          $scope.statusAll = response.data.statusAll;
          $scope.totalItems = $scope.statusAll.length;
          $scope.prograssing = false;
        },
        function (error) {
          $state.go('dapp.taskManagement');
          $scope.status = 'Unable to Create Room: ';
          console.log('message :: ' + error.message);
        }
      );

      $scope.updateStatusWithRemarkToUsers = function (assignedTasks) {
        $scope.loader = true;

        var taskData = {
          createdBy: $scope.userId,
          taskStatusId: assignedTasks.taskStatus,
          taskId: assignedTasks.taskId,
          userRemarks: assignedTasks.userRemarks,
          loginId: $scope.userId,
        };
        FunctionalityService.updateStatusWithRemarks(taskData).then(
          function (response) {
            $scope.loader = false;
            if (response.data.status == 'Success') {
              $('#viewtaskaty').modal('hide');
              $state.reload();
              $timeout(function () {
                $scope.tab = 2;
                toaster.pop('success', response.data.message);
              }, 1000);
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function (error) {
            $scope.status = 'Unable to Create Room: ';
            console.log('message :: ' + error.message);
          }
        );
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
