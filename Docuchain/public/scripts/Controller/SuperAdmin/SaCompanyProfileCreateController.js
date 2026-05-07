var saCompanyProfileCreate = angular.module('dapp.SaCompanyProfileCreateController', ['angularUtils.directives.dirPagination', 'toaster', 'moment-picker', '720kb.tooltips']);

saCompanyProfileCreate.controller('SaCompanyProfileCreateController', ['$scope', '$stateParams', '$window', '$location', '$state', '$rootScope', '$timeout', 'toaster', 'FunctionalityService', function ($scope, $stateParams, $window, $location, $state, $rootScope, $timeout, toaster, FunctionalityService) {
    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject')) || {};

    // //$scope.userProfileId = $window.sessionStorage.getItem('userId');
    // $scope.organizationInfo;
    // $scope.subscriptionInfo;
    $scope.tab = 1;
    $scope.tabName = 'tab1';
    $scope.editOrganization;
    $scope.organization = {};
    $scope.organizationInfo = {};
    $scope.subscriptionInfo = {};
    $scope.userInfo = {};
    $scope.loader = false;

    $scope.mindate = moment().add(0, 'day');
    $scope.previous1 = function () {
        $scope.tab = 1;
    }
    $scope.next3 = function (subscription) {
        $scope.tab = 3;
        $scope.subscriptionInfo = subscription;
    }
    // $scope.next2 = function (organization) {
    //     $scope.tab = 2;
    //     $scope.organizationInfo = organization;
    // }

$scope.next2 = function(organization) {
    // console.log("--- DEBUG: TAB 1 DATA RECEIVED ---");
    // console.log("Address 1:", organization.addressLine1);
    // console.log("Address 2:", organization.addressLine2);
    $scope.tab = 2;
    $scope.organizationInfo = organization;
}


    $scope.organizationId = $stateParams.organizationId
    $scope.checkPassword = function (password, confirmPassword) {
        if (password != confirmPassword) {
            $scope.IsMatch = true;
            $scope.isDisabled = true;
        }
        else if (password == confirmPassword) {
            $scope.IsMatch = false;
            $scope.newpassword = password;
            $scope.isDisabled = false;
        }

    }

    $scope.createOrganization = function () {
        $scope.loader = true;
        var createOrganizationData = {
            "organizationName": $scope.organization.organizationName,
            "registrationNumber": $scope.organization.registrationNumber,
            // "address": $scope.organization.address,
            // two lines of adress
            "addressLine1": $scope.organization.addressLine1,
            "addressLine2": $scope.organization.addressLine2,
            "phoneNumber": $scope.organization.phoneNumber,
            "emailId": $scope.organization.emailId,
            "alternatePhoneNumber": $scope.organization.alternatePhoneNumber,
            "bankAccountNumber": $scope.organization.bankAccountNumber,
            "contactPersonName": $scope.organization.contactPersonName,
            "contactPersonEmail": $scope.organization.contactPersonEmail,
            "contactPersonPhoneNo": $scope.organization.contactPersonPhoneNo,
            "contactPersonAlternatePhoneNo": $scope.organization.contactPersonAlternatePhoneNo,
            "userId": $scope.sessionObject.userId
        };
        FunctionalityService.createOrganization(createOrganizationData)
            .then(function mySuccess(response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);
                    $state.go('dapp.saCompanyProfile');

                } else {
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }

    $scope.createOrganizationwithSubcription = function (userInfo) {
        $scope.loader = true;
        if (userInfo != undefined) {
            if (userInfo.password != userInfo.confirmPassword) {
                $scope.loader = false;
                toaster.pop("error", "Password does not match")
            }
            var createOrganizationData = {
                "organizationName": $scope.organizationInfo.organizationName,
                "registrationNumber": $scope.organizationInfo.registrationNumber,
                // "address": $scope.organizationInfo.address,
                // two lins of adress
                "addressLine1": $scope.organizationInfo.addressLine1,
                "addressLine2": $scope.organizationInfo.addressLine2,
                "phoneNumber": $scope.organizationInfo.phoneNumber,
                "emailId": $scope.organizationInfo.emailId,
                "alternatePhoneNumber": $scope.organizationInfo.alternatePhoneNumber,
                "bankAccountNumber": $scope.organizationInfo.bankAccountNumber,
                "contactPersonName": $scope.organizationInfo.contactPersonName,
                "contactPersonEmail": $scope.organizationInfo.contactPersonEmail,
                "contactPersonPhoneNo": $scope.organizationInfo.contactPersonPhoneNo,
                "contactPersonAlternatePhoneNo": $scope.organizationInfo.contactPersonAlternatePhoneNo,
                "userId": $scope.sessionObject.userId,
                "subscriptionInfo": $scope.subscriptionInfo,
                "userInfo": $scope.userInfo
            };
            FunctionalityService.createOrganizationwithSubcription(createOrganizationData).then(function mySuccess(response) {
                if (!createOrganizationData.addressLine1) {
                        console.error("CRITICAL ERROR: addressLine1 is missing from the payload!");
                    }
                    $scope.loader = false;

                    if (response.status == 201 || response.status == 200) {
                        $state.reload();
                        $timeout(function () {
                            toaster.clear();
                            toaster.success({ title: response.data.message });
                        }, 1000);
                        $state.go('dapp.saCompanyProfile');

                    } else {
                        toaster.clear();
                        toaster.error({ title: response.data.message });
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                });
        } else {
            var createOrganizationData = {
                "organizationName": $scope.organizationInfo.organizationName,
                "registrationNumber": $scope.organizationInfo.registrationNumber,
                // "address": $scope.organizationInfo.address,
                // two lines of adress
                "addressLine1": $scope.organizationInfo.addressLine1,
                "addressLine2": $scope.organizationInfo.addressLine2,
                "phoneNumber": $scope.organizationInfo.phoneNumber,
                "emailId": $scope.organizationInfo.emailId,
                "alternatePhoneNumber": $scope.organizationInfo.alternatePhoneNumber,
                "bankAccountNumber": $scope.organizationInfo.bankAccountNumber,
                "contactPersonName": $scope.organizationInfo.contactPersonName,
                "contactPersonEmail": $scope.organizationInfo.contactPersonEmail,
                "contactPersonPhoneNo": $scope.organizationInfo.contactPersonPhoneNo,
                "contactPersonAlternatePhoneNo": $scope.organizationInfo.contactPersonAlternatePhoneNo,
                "userId": $scope.sessionObject.userId,
                "subscriptionInfo": $scope.subscriptionInfo,
                "userInfo": $scope.userInfo
            };
            FunctionalityService.createOrganizationwithSubcription(createOrganizationData)
                .then(function mySuccess(response) {
                    $scope.loader = false;

                    if (response.status == 201 || response.status == 200) {
                        $state.reload();
                        $timeout(function () {
                            toaster.clear();
                            toaster.success({ title: response.data.message });
                        }, 1000);
                        $state.go('dapp.saCompanyProfile');

                    } else {
                        toaster.clear();
                        toaster.error({ title: response.data.message });
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                });
        }
    }
    $scope.organizationView = function () {
        $scope.loader = true;

        FunctionalityService.getorganizationView($scope.organizationId)
            .then(function mySuccess(response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    $scope.editOrganization = JSON.stringify(response.data.organizationInfo);
                    $scope.editOrganization = response.data.organizationInfo;
                    if ($scope.editOrganization == undefined) {
                        toaster.pop('info', "No records found");
                    }
                } else {
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }

    $scope.updatOrganization = function () {
        $scope.loader = true;
        var updatOrganizationData = {
            "organizationId": $scope.organizationId,
            "organizationName": $scope.editOrganization.organizationName,
            "registrationNumber": $scope.editOrganization.registrationNumber,
            // "address": $scope.editOrganization.address,
            // two lines of adress
            "addressLine1": $scope.editOrganization.addressLine1,
            "addressLine2": $scope.editOrganization.addressLine2,
            "phoneNumber": $scope.editOrganization.phoneNumber,
            "emailId": $scope.editOrganization.emailId,
            "alternatePhoneNumber": $scope.editOrganization.alternatePhoneNumber,
            "bankAccountNumber": $scope.editOrganization.bankAccountNumber,
            "contactPersonName": $scope.editOrganization.contactPersonName,
            "contactPersonEmail": $scope.editOrganization.contactPersonEmail,
            "contactPersonPhoneNo": $scope.editOrganization.contactPersonPhoneNo,
            "contactPersonAlternatePhoneNo": $scope.editOrganization.contactPersonAlternatePhoneNo,
            "userId": $scope.sessionObject.userId
            //"subscriptionInfo":$scope.editOrganization.subscriptionInfo
        };

        FunctionalityService.updatOrganization(updatOrganizationData)
            .then(function mySuccess(response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);
                    $state.go('dapp.saCompanyProfile');

                } else {
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }
    $scope.updateOrganizationwithSubcription = function () {
        $scope.loader = true;

        var editOrganizationData = {
            "organizationId": $scope.organizationId,
            "organizationName": $scope.editOrganization.organizationName,
            "registrationNumber": $scope.editOrganization.registrationNumber,
            // "address": $scope.editOrganization.address,
            // two lines of adress
            "addressLine1": $scope.editOrganization.addressLine1,
            "addressLine2": $scope.editOrganization.addressLine2,  
            // "addressLine1": $scope.organizationInfo.addressLine1,
            // "addressLine2": $scope.organizationInfo.addressLine2, 
            "phoneNumber": $scope.editOrganization.phoneNumber,
            "emailId": $scope.editOrganization.emailId,
            "alternatePhoneNumber": $scope.editOrganization.alternatePhoneNumber,
            "bankAccountNumber": $scope.editOrganization.bankAccountNumber,
            "contactPersonName": $scope.editOrganization.contactPersonName,
            "contactPersonEmail": $scope.editOrganization.contactPersonEmail,
            "contactPersonPhoneNo": $scope.editOrganization.contactPersonPhoneNo,
            "contactPersonAlternatePhoneNo": $scope.editOrganization.contactPersonAlternatePhoneNo,
            "userId": $scope.sessionObject.userId,
            "subscriptionInfo": $scope.editOrganization.subscriptionInfo
        };
        FunctionalityService.updatOrganization(editOrganizationData)
            .then(function mySuccess(response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);
                    $state.go('dapp.saCompanyProfile');

                } else {
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }
    $scope.closeView = function () {
        $state.go('dapp.saCompanyProfile');
    }


    

}]);
