var AdminReportIssue = angular.module('dapp.AdminReportIssueContrroller', []);

AdminReportIssue.controller('AdminReportIssueContrroller', ['$scope', '$timeout', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster', function ($scope, $timeout, $window, $location, $state, $rootScope, FunctionalityService, toaster) {


    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.organizationName = $window.localStorage.getItem('organizationName');
$scope.loader = false;
$scope.report = {
    organizationName: $scope.organizationName
}
    $scope.popupClear = function (report) {
        $scope.report.name = "";
        $scope.report.organizationName = $scope.organizationName;
        $scope.report.email = "";
        $scope.report.reason = "";
        $scope.report.phoneNumber = "";
    }
    $scope.reportIssue = function (report) {
        toaster.clear();
        $scope.loader = true;
        $scope.report = report;
        $scope.report.userId = $scope.userProfileId;
        FunctionalityService.reportanIssue($scope.report).then(function (response) {
            $scope.loader = false;

            if (response.status == 200 || response.status == 201) {
                toaster.pop("success", response.data.message);
                setTimeout(function () {
                    $state.go('dapp.admindashboard');
        
                  }, 3000);
            }
            else {
                toaster.pop("error", response.data.message);
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });

    }

}])



