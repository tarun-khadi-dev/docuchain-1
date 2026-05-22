var userMyWorkspaceEBDList = angular.module('dapp.UserMyWorkSpaceExpiryDocumentController', []);

userMyWorkspaceEBDList.controller('UserMyWorkSpaceExpiryDocumentController', ['$scope', '$window', '$location', '$state', '$stateParams', '$rootScope', '$timeout', 'toaster', '$filter', '$sce', 'FunctionalityService', function ($scope, $window, $location, $state, $stateParams, $rootScope, $timeout, toaster, $filter, $sce, FunctionalityService) {


    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));
    $scope.groupShipId = $window.localStorage.getItem('groupShipId');
    $scope.groupShipName = $window.localStorage.getItem('groupShipName');
    $scope.ebdGroupId = $stateParams.groupId;
    $scope.ebdGroupName = $stateParams.groupName;
    $scope.ebdgroupShipName = $stateParams.groupShipName;
    $scope.loader = false;
    $scope.groupEBDList = [];
    $scope.groupEBDListLength = $scope.groupEBDList.length;
    $scope.itemsPerPageGroupEbd = 10;
    $scope.currentPageGroupEbd = 1;
    if ($scope.ebdGroupId != 'null') {
        $scope.getGroupExpiryDcoumentList = function () {
            $scope.loader = true;

            FunctionalityService.getAllGroupExpiryDocumentList($scope.ebdGroupId)
                .then(function (response) {
                    $scope.loader = false;

                    $scope.groupEBDList = response.data.expiryDocumentList;
                    // $scope.groupEBDListLength = $scope.groupEBDList.length
                  }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                  });
        }

        $scope.groupId;
        $scope.expiryDocId;
        $scope.getExpriyDocId = function (deleteGroupId) {
            $scope.groupId = deleteGroupId.groupId;
            $scope.expiryDocId = deleteGroupId.documentHolderId;

        }
        $scope.deleteGroupExpiryDocument = function () {
            $scope.loader = true;

            var groupExpiryDocumentData = { "documentHolderId": $scope.expiryDocId, "groupId": $scope.groupId,"loginId":$scope.sessionObject.userId };
            FunctionalityService.deleteGroupExpiryDocument(JSON.stringify(groupExpiryDocumentData))
                .then(function (response) {
                    $scope.loader = false;

                    if (response.status == 200) {
                        $('#deleteGroupExpiry').modal('hide');
                        toaster.clear();
                        toaster.success({ title: response.data.message});
                        $scope.getGroupExpiryDcoumentList();
                    }
                    else {
                        toaster.clear();
                        toaster.error({ title: response.data.message});
                    }
                }, function myError(err) {
                    $scope.loader = false;
                    console.log("Error response", err);
                  });
        }


    }


}]);