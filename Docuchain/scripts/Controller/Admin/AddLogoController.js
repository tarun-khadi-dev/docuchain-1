adminDashboard.controller('AddLogoController', [
  '$scope',
  '$window',
  '$rootScope',
  'FunctionalityService',
  'toaster',
  function ($scope, $window, $rootScope, FunctionalityService, toaster) {
    $scope.thumbnail = {};

    // 1. Handle File Selection and Preview
    $scope.uploadFile = function (files) {
      var reader = new FileReader();
      reader.onload = function (e) {
        $scope.$apply(function () {
          $scope.thumbnail.dataUrl = e.target.result;
        });
      };
      reader.readAsDataURL(files[0]);
      $scope.selectedFile = files[0];
    };

    // 2. Update Function
    $scope.addLogoFunction = function () {
      if (!$scope.selectedFile) {
        toaster.error('Please select a file first');
        return;
      }

      $scope.loader = true;

      // Prepare FormData for API
      var fd = new FormData();
      fd.append('file', $scope.selectedFile);
      fd.append('userId', $window.localStorage.getItem('userId'));

      // Replace 'uploadLogoApi' with your actual service method name
      FunctionalityService.uploadLogoApi(fd).then(
        function (response) {
          $scope.loader = false;

          // Assume API returns the new path in response.data.path
          var newLogoPath = response.data.path;

          // Update LocalStorage
          $window.localStorage.setItem('logoPicture', newLogoPath);

          // 🔥 CRITICAL: Tell the header to update
          $rootScope.$broadcast('logoUpdated');

          toaster.success('Logo updated successfully');
        },
        function (err) {
          $scope.loader = false;
          toaster.error('Upload failed');
          console.error(err);
        }
      );
    };
  },
]);
