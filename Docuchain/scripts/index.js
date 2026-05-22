'use strict';

angular
  .module('dapp', [
    'ui.router',
    'toaster',
    'moment-picker',

    '$idle',
    'dapp.FunctionalityService',
    'dapp.LoginController',
    'dapp.AdminHeaderController',
    'dapp.AdminDashboardController',
    'dapp.AdminVesselsController',
    'dapp.AdminUsersController',
    'dapp.AdminVesselsAddinfoController',
    'dapp.AdminTasksController',
    'dapp.AdminUserExtensionController',
    'dapp.AdminReportIssueContrroller',
    'dapp.UserHeaderController',
    'dapp.UserDashboardController',
    'dapp.UserVesselDcoumentController',
    'dapp.UserVesselDocumentEBDController',
    'dapp.UserDocumentApprovalController',
    'dapp.UserUserExtensionController',
    'dapp.UserMyWorkSpceController',
    'dapp.UserMyWorkspaceListController',
    'dapp.UserMyWorkSpaceExpiryDocumentController',
    'dapp.UserTasksController',
    'dapp.HomeController',
    'dapp.DashBoardController',
    'dapp.SaHeaderController',
    'dapp.AdminVesselsViewInfoController',
    'dapp.SaCompanyProfileCreateController',
    'dapp.AdminSuperAauditLogController',
    'dapp.AdminsuperAconfCountryController',
    'dapp.SaManageSubscriptionController',
    'dapp.SaCompanyProfileController',
    'dapp.SaConfigCountryController',
    'dapp.SaConfigCountryController',
    'dapp.SaConfigStateController',
    'dapp.SaConfigPlaceHolderController',
    'dapp.AdminVesselsEditinfoController',
    'dapp.AdminsuperAconfStateController',
    'dapp.saSubscrptionManageAdminController',
    'dapp.SessionExpiredController',
    'dapp.SaAauditLogController',
    'dapp.superAdminDashboardController',
    'dapp.SaDashboardController',
    'dapp.AdminAddPlaceHolderController',
  ])

  .config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
      // route for the login page
      // .state('session', {
      //   url: '/session',
      //   views: {
      //     /*'header': {
      //                  templateUrl : 'views/Header.html',
      //                  controller  : 'HomeController'
      //              },
      //              'sidebar@': {
      //                  templateUrl : 'views/Sidebar.html',
      //                  controller  : 'HomeController'
      //              },*/
      //     content: {
      //       templateUrl: 'views/Common/SessionExpired.html',
      //       controller: 'SessionExpiredController',
      //     },
      //     // /*'footer@': {
      //     //               templateUrl : 'views/loginFooter.html'
      //     //           }*/
      //   },
      // })
      .state('session', {
        url: '/session',
        onEnter: [
          '$state',
          function ($state) {
            /// 1. Wipe everything from Local Storage (roles, emails, images, etc.)
            localStorage.clear();

            // Redirect straight to the login state
            $state.go('dapp.login');
          },
        ],
      })
      .state('dapp', {
        url: '/',
        views: {
          /*'header': {
                       templateUrl : 'views/Header.html',
                       controller  : 'HomeController'
                   },
                   'sidebar@': {
                       templateUrl : 'views/Sidebar.html',
                       controller  : 'HomeController'
                   },*/
          content: {
            templateUrl: 'views/Common/Login.html',
            controller: 'LoginController',
          },
          /*'footer@': {
                        templateUrl : 'views/loginFooter.html'
                    }*/
        },
        data: {
          displayName: 'Login',
        },
      })
      .state('dapp.login', {
        url: 'login',
        views: {
          /*'header@': {
                       templateUrl : 'views/Header.html',
                       controller  : 'SidebarController'
                   },
                   'sidebar@': {
                       templateUrl : 'views/Sidebar.html',
                       controller  : 'SidebarController'
                   },*/
          'content@': {
            templateUrl: 'views/Common/Login.html',
            controller: 'LoginController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.forgotpassword', {
        url: 'forgotpassword',
        views: {
          /*'header@': {
                       templateUrl : 'views/Header.html',
                       controller  : 'SidebarController'
                   },
                   'sidebar@': {
                       templateUrl : 'views/Sidebar.html',
                       controller  : 'SidebarController'
                   },*/
          'content@': {
            templateUrl: 'views/Common/forgotpassword.html',
            controller: 'LoginController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.home', {
        url: 'home',
        views: {
          'header@': {
            templateUrl: 'views/Header.html',
            controller: 'HomeController',
          },
          'sidebar@': {
            templateUrl: 'views/Sidebar.html',
            controller: 'HomeController',
          },
          'content@': {
            templateUrl: 'views/home.html',
            controller: 'HomeController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.admindashboard', {
        url: 'admindashboard',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminDashboard.html',
            // controller  : 'AdminDashboardController'
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminProfile', {
        url: 'admindashboard/profile',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminProfile.html',
            controller: 'AdminDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.superAconfCountry', {
        url: 'adminsuperAconfCountry',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/superAconfCountry.html',
            controller: 'AdminsuperAconfCountryController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.superAconfState', {
        url: 'adminsuperAconfState',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/superAconfState.html',
            controller: 'AdminsuperAconfStateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.addLogo', {
        url: 'adminaddLogo',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/AddLogo.html',
            controller: 'AdminsuperAconfStateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminPlaceholder', {
        url: 'admin/adminPlaceholder',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/AddPlaceHolder.html',
            controller: 'AdminAddPlaceHolderController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminVessels', {
        url: 'admin/vessels',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminVessels.html',
            controller: 'AdminVesselsController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saAauditLog', {
        url: 'company/aauditLog',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'SaCompanyProfileController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaAauditLog.html',
            controller: 'SaAauditLogController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminsuperAauditLog', {
        url: 'admin/superAauditLog',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/SuperAauditLog.html',
            controller: 'AdminSuperAauditLogController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminvesselsAdd', {
        url: 'admin/vesselsAdd',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminVesselsAddinfo.html',
            controller: 'AdminVesselsAddinfoController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminvesselsEdit', {
        url: 'admin/vesselsEdit',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminVesselsEditinfo.html',
            controller: 'AdminVesselsEditinfoController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminvesselsView', {
        url: 'admin/vesselsView',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminVesselsViewInfo.html',
            controller: 'AdminVesselsViewInfoController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminUsers', {
        url: 'admin/users',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminVesselsController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminUsers.html',
            controller: 'AdminUsersController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminTasks', {
        url: 'admin/tasks',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminTasksController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminTasks.html',
            controller: 'AdminTasksController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.taskManagementByNotifyAdmin', {
        url: 'dapp.taskManagementByNotify/:id',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminTasksController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminTasks.html',
            controller: 'AdminTasksController',
          },
          /*'footer@': {
                       templateUrl : 'views/footer.html'
                   }*/
        },
      })
      .state('dapp.adminUserExtension', {
        url: 'admin/user/extension',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminUserExtensionController',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminUserExtension.html',
            controller: 'AdminUserExtensionController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.adminReportIssue', {
        url: 'admin/reportissue',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminReportIssueContrroller',
          },
          'content@': {
            templateUrl: 'views/Admin/AdminReportIssue.html',
            controller: 'AdminReportIssueContrroller',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.notificationadmin', {
        url: 'admin/notification',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Common/notification.html',
            controller: 'UserDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userDashboard', {
        url: 'users/dashboard',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserDashboard.html',
            controller: 'UserDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userProfile', {
        url: 'users/profile',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserProfile.html',
            controller: 'UserDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userNotification', {
        url: 'users/userNotification',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Common/notification.html',
            controller: 'UserDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.faq', {
        url: 'users/faq',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/Faq.html',
            controller: 'UserDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userVesselDocument', {
        url: 'users/vessel/document',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserVesselDocument.html',
            controller: 'UserVesselDcoumentController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userVesselDocumentEBD', {
        url: 'users/vessel/document/expirybased',
        views: {
       'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserVesselDocumentEBD.html',
            controller: 'UserVesselDocumentEBDController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userDoumentApproval', {
        url: 'users/document/approval',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserDocumentApproval.html',
            controller: 'UserDocumentApprovalController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userUserExtension', {
        url: 'users/userextension',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserUserExtension.html',
            controller: 'UserUserExtensionController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userMyWorkspace', {
        url: 'users/myworkspace',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserMyWorkSpace.html',
            controller: 'UserMyWorkSpceController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userMyWorkspaceList', {
        url: 'users/myworkspace/list',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserMyWorkspaceList.html',
            controller: 'UserMyWorkspaceListController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userMyWorkspaceExpiryDocument', {
        url: 'user/myworkspace/expiryBasedDocumentList/:groupId/:groupName/:groupShipName',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserMyWorkSpaceExpiryDocument.html',
            controller: 'UserMyWorkSpaceExpiryDocumentController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.userTasks', {
        url: 'users/tasks',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserTasks.html',
            controller: 'UserTasksController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.taskManagementByNotifyUser', {
        url: 'dapp.taskManagementByNotify/:id',
        views: {
          'header@': {
            templateUrl: 'views/Users/UserHeader.html',
            controller: 'UserHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Users/UserSidebar.html',
            controller: 'UserDashboardController',
          },
          'content@': {
            templateUrl: 'views/Users/UserTasks.html',
            controller: 'UserTasksController',
          },
          /*'footer@': {
                       templateUrl : 'views/footer.html'
                   }*/
        },
      })

      .state('dapp.saManageSubscription', {
        url: 'manage/subscription',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaManageSubscription.html',
            controller: 'SaManageSubscriptionController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saDashboard', {
        url: 'sa/dashboard',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaDashboard.html',
            controller: 'SaDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.saManageSubscriptionAdmin', {
        url: 'manage/subscription/admin/:userId/:organizationId/:subscriptionId',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/saSubscrptionManageAdmin.html',
            controller: 'saSubscrptionManageAdminController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saCompanyProfile', {
        url: 'company/profile',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaCompanyProfile.html',
            controller: 'SaCompanyProfileController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.saCompanyProfileupdate', {
        url: 'company/profile/update/:userId/:organizationId',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaCompanyProfileUpdate.html',
            controller: 'SaCompanyProfileCreateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.saCompanyProfileView', {
        url: 'company/profile/view/:organizationId',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaCompanyProfileView.html',
            controller: 'SaCompanyProfileCreateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saCompanyProfilecreate', {
        url: 'company/profile/create',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/superAcreateNew.html',
            controller: 'SaCompanyProfileCreateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saConfigCountry', {
        url: 'config/country',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaConfigCountry.html',
            controller: 'SaConfigCountryController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saConfigState', {
        url: 'config/state',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaConfigState.html',
            controller: 'SaConfigStateController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.saPlaceHolder', {
        url: 'config/placeholder',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaConfigPlaceHolder.html',
            controller: 'SaConfigPlaceHolderController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })

      .state('dapp.certificateType', {
        url: 'config/certificateType',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaConfigCertificate.html',
            controller: 'SaConfigPlaceHolderController',
          },
          /*'footer@': {
                                     templateUrl : 'views/footer.html'
                                 }*/
        },
      })

      .state('dapp.saProfile', {
        url: 'config/profile',
        views: {
          'header@': {
            templateUrl: 'views/SuperAdmin/SaHeader.html',
            controller: 'SaHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/SuperAdmin/SaSidebar.html',
            controller: 'superAdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/SuperAdmin/SaProfileEdit.html',
            controller: 'superAdminDashboardController',
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      })
      .state('dapp.expiredSubscription', {
        url: 'Expired',
        views: {
          'header@': {
            templateUrl: 'views/Admin/AdminHeader.html',
            controller: 'AdminHeaderController',
          },
          'sidebar@': {
            templateUrl: 'views/Admin/AdminSidebar.html',
            controller: 'AdminDashboardController',
          },
          'content@': {
            templateUrl: 'views/Admin/expiredPage.html',
            // controller  : 'AdminDashboardController'
          },
          /*'footer@': {
                        templateUrl : 'views/footer.html'
                    }*/
        },
      });
    // Default routing
    $urlRouterProvider.otherwise('/');
  })

  // Test Purpose
 .config(function ($idleProvider, $keepaliveProvider) {
    $keepaliveProvider.setInterval(1);

    // Change to 5 seconds for testing
    $idleProvider.setIdleTime(5);

    // Change to 10 MIN for testing
    $idleProvider.setTimeoutTime(600);

    $idleProvider.keepalive(true);
  })
  .run(function ($idle) {
    $idle.watch();
  })

  // .run(function ($rootScope, $state) {
  //   $rootScope.$on('$stateChangeStart', function (event, toState) {
  //     var session = localStorage.getItem('sessionObject');

  //     if (
  //       !session &&
  //       toState.name !== 'dapp' &&
  //       toState.name !== 'dapp.login' &&
  //       toState.name !== 'dapp.forgotpassword'
  //     ) {
  //       event.preventDefault();
  //       $state.go('dapp.login');
  //     }
  //   });
  // });
  .run(function ($rootScope, $state) {
    $rootScope.$on('$stateChangeStart', function (event, toState) {
      // --- NEW CODE START ---
      // Intercept the request if it's trying to go to the Session Expired page
      if (toState.name === 'session') {
        event.preventDefault(); // Stop it from going to the expired page
        localStorage.clear(); // Wipe everything in local storage
        return $state.go('dapp.login'); // Bounce straight to login
      }
      // --- NEW CODE END ---

      var session = localStorage.getItem('sessionObject');

      if (
        !session &&
        toState.name !== 'dapp' &&
        toState.name !== 'dapp.login' &&
        toState.name !== 'dapp.forgotpassword'
      ) {
        event.preventDefault();
        $state.go('dapp.login');
      }
    });
  });
// .config(function (IdleProvider, KeepaliveProvider, $urlRouterProvider) {
//     // configure Idle settings
//     IdleProvider.idle(5); // in seconds
//     IdleProvider.timeout(900); // in seconds
//    //keepaliveProvider.http('http://localhost:7002/#/login');
//     KeepaliveProvider.interval(1); // in seconds
//     $urlRouterProvider.otherwise('/');
//   })
// .run(function ($rootScope,Idle,$state) {
//     // start watching when the app runs. also starts the Keepalive service by default.
//     Idle.watch();
//     $rootScope.$on('IdleStart', function () {
//         //    alert("IdleStart");
//     });
//     $rootScope.$on('IdleTimeout', function () {
//         // alert("Your session expired");
//         $state.go("session");

//     });
//   });
