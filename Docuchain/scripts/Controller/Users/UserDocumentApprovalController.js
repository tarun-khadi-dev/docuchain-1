var userDocumentApproval = angular.module(
  'dapp.UserDocumentApprovalController',
  ['toaster']
);

userDocumentApproval.controller('UserDocumentApprovalController', [
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

    $scope.loader = false;
    $scope.documentList = [];
    $scope.documentListLen = $scope.documentList.length;
    $scope.itemsPerPage = 10;
    $scope.currentPage = 1;
    $scope.loader = false;
    $scope.viewDocumentObjectInfo;
    $scope.approveDocumentInfo;
    $scope.viewDocumentUrl;
    $rootScope.selected = 2;

    //This Method is uesd to get the all aproval document list
    $scope.getDcoumentApprovelList = function () {
      $scope.loader = true;
      var data = { loginId: $scope.sessionObject.userId };
      FunctionalityService.getDoucmentApprovalListService(
        JSON.stringify(data)
      ).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.documentList = response.data.expiryDocumentList;
            $scope.documentListLen = $scope.documentList.length;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    //This method is used open approve Reject  popup
    $scope.approveAndRejectPopup = function (documentObj) {
      $('#approveDocument').modal('toggle');
      $scope.approveDocumentInfo = documentObj;
      $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
        $scope.approveDocumentInfo.documentPreviewUrl
      );
    };

    //This method is used view Document information
    $scope.viewDocumentInformation = function (documentObj) {
      $('#viewDocument').modal('toggle');
      $scope.viewDocumentObjectInfo = documentObj;
      $scope.viewDocumentUrl = $sce.trustAsResourceUrl(
        $scope.viewDocumentObjectInfo.documentPreviewUrl
      );
    };

    //This method is used for Approve the document
    $scope.approvedExpiryDocument = function () {
      toaster.clear();

      $scope.loader = true;
      var postData = {
        id: $scope.approveDocumentInfo.id,
        status: 'Approved',
        approverId: $scope.sessionObject.userId,
        loginId: $scope.sessionObject.userId,
        remarks: $scope.approveDocumentInfo.remarks,
      };
      FunctionalityService.chageExpiryDocumentStatus(postData).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $('#approveDocument').modal('hide');
            toaster.success('Document approved successfully');
            setTimeout(function () {
              $state.reload();
            }, 500);
          } else {
            toaster.error('Document approved failed');
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    //This method is used for reject the document
    $scope.rejectExpiryDocument = function () {
      toaster.clear();

      $scope.loader = true;
      var postData = {
        id: $scope.approveDocumentInfo.id,
        status: 'Rejected',
        approverId: $scope.sessionObject.userId,
        loginId: $scope.sessionObject.userId,
        remarks: $scope.approveDocumentInfo.remarks,
      };
      FunctionalityService.chageExpiryDocumentStatus(postData).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $('#approveDocument').modal('hide');
            toaster.success('Document rejected successfully');
            setTimeout(function () {
              $state.reload();
            }, 500);
          } else {
            toaster.error('Document rejected failed');
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
  },
]);
