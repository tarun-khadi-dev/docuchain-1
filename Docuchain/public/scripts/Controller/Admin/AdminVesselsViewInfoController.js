var adminVesselsadd = angular.module('dapp.AdminVesselsViewInfoController', [
  'ui.select',
  'ngSanitize',
]);
adminVesselsadd.directive('fileInput', [
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
adminVesselsadd.controller('AdminVesselsViewInfoController', [
  '$scope',
  '$timeout',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  'toaster',
  function (
    $scope,
    $timeout,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    toaster
  ) {
    $scope.vesselsList = [];
    $scope.commercialManagerInfoList = [];
    $scope.techManagerInfoList = [];
    $rootScope.editId;
    $rootScope.countryName;
    $rootScope.stateName;
    $scope.vesselTick = false;
    $scope.isReadOnly = true;

    $scope.editvesselId = $window.localStorage.getItem('editId');
    $scope.editcountryNameJson = $window.localStorage.getItem('countryName');
    $scope.editstateNameJson = $window.localStorage.getItem('stateName');
    $scope.editcountryName = JSON.parse($scope.editcountryNameJson);
    $scope.editstateName = JSON.parse($scope.editstateNameJson);

    $scope.userId = $window.localStorage.getItem('userId');
    $scope.organizationId = $window.localStorage.getItem('organizationId');
    $scope.userData = {};
    $scope.countryInfos = [];
    $scope.countryId = [];
    $scope.portInfos = [];
    $scope.countryName;
    $scope.vesselUser = false;
    $scope.vesselsTypeInfos = [];
    $scope.shipPic;
    $scope.shipMasterInfos = [];
    $rootScope.editcountryId;
    $rootScope.countryIdstate = '';
    $scope.shipUserId;
    $scope.selectedTechdetails;
    $scope.selectedComdetails;
    $scope.vesselsId;

    getList();
    function getList() {
      FunctionalityService.getVessellist($scope.userId).then(
        function (response) {
          if (response.status == 201 || response.status == 200) {
            $scope.vesselsList = response.data.shipProfileList;
            angular.forEach($scope.vesselsList, function (value) {
              if (
                value.id == $scope.editvesselId &&
                value.countryName == $scope.editcountryName &&
                value.stateName == $scope.editstateName
              ) {
                // $scope.userData = value;
                $scope.userData = value;
                if ($scope.userData.delivered) {
                  $scope.userData.delivered = moment(
                    $scope.userData.delivered
                  ).format('DD-MM-YYYY');
                }
                if ($scope.userData.keelLaidString) {
                  $scope.userData.keelLaidString = moment(
                    $scope.userData.keelLaidString
                  ).format('DD-MM-YYYY');
                }

                $scope.vesselsId = $scope.userData.id;
                $scope.thumbnail.dataUrl = $scope.userData.shipProfilePicPath;
              }
              $scope.commercialManagerInfoList.push(
                value.commercialManagerInfoList
              );
              $scope.techManagerInfoList.push(value.techManagerInfoList);
            });
            // $scope.vesselsList.map((value, key) => {

            //   if ((value.id == $scope.editvesselId) && (value.countryName == $scope.editcountryName) && (value.stateName == $scope.editstateName)) {
            //     $scope.userData = value;
            //     $scope.vesselsId = $scope.userData.id;
            //     $scope.thumbnail.dataUrl = $scope.userData.shipProfilePicPath;
            //   }
            //   $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
            //   $scope.techManagerInfoList.push(value.techManagerInfoList);

            // })
          } else if (response.status == 206) {
            toaster.pop('error', response.data.message);
          }
        }
      );
    }

    FunctionalityService.getCountry().then(function (response) {
      if (response.status == 201 || response.status == 200) {
        $scope.countryInfos = response.data.countryInfos;

        angular.forEach($scope.countryInfos, function (id) {
          if ($scope.editcountryName == id.countryName) {
            $rootScope.editcountryId = '';
            $rootScope.editcountryId = id.countryId;
            // $window.sessionStorage.setItem('countryId', JSON.stringify($rootScope.editcountryId));
            port($rootScope.editcountryId);
          }
          $scope.countryId.push(id.countryId);
        });
        // $scope.countryInfos.map((id, i) => {
        //   if ($scope.editcountryName == id.countryName) {
        //     $rootScope.editcountryId = '';
        //     $rootScope.editcountryId = id.countryId;

        // $window.sessionStorage.setItem('countryId', JSON.stringify($rootScope.editcountryId));
        //     port($rootScope.editcountryId);

        //   }
        //   $scope.countryId.push(id.countryId);

        // })
      } else if (response.status == 206) {
        toaster.pop('error', response.data.message);
      }
    });
    // $rootScope.countryIdstate = $window.sessionStorage.getItem('countryId');

    // console.log("countryIdstate", $rootScope.countryIdstate)
    function port(editcountryId) {
      FunctionalityService.getPort(editcountryId).then(function (response) {
        if (response.status == 201 || response.status == 200) {
          $scope.portInfos = response.data.portInfos;
        } else if (response.status == 206) {
          toaster.pop('error', response.data.message);
          $scope.portInfos = [];
        }
      });
    }

    $scope.goOutlist = function () {
      $state.go('dapp.adminVessels');
    };

    var today = new Date();
    $scope.maxDate = new Date(
      today.getFullYear(),
      today.getMonth(),
      today.getDate() + 1
    );
    $scope.minDate = new Date(
      today.getFullYear() - 50,
      today.getMonth(),
      today.getDate() - 1
    );
    $scope.maxDate1 = new Date(
      today.getFullYear() + 50,
      today.getMonth(),
      today.getDate() + 1
    );
    $scope.minDate1 = new Date(
      today.getFullYear() - 50,
      today.getMonth(),
      today.getDate() - 1
    );

    $scope.vesselUserShow = function (tickValid) {
      if (Object.keys(tickValid).length === 0) {
        $scope.vesselUser = true;
        $scope.vesselTick = false;
      } else {
        $scope.vesselTick = true;
        $scope.vesselUser = true;
      }
    };
    $scope.prevTovesselInfo = function () {
      $scope.vesselUser = false;
      $scope.vesselTick = false;
    };
    FunctionalityService.getCommercial($scope.userId).then(function (response) {
      $scope.commercialManagerInfos = response.data.commercialManagerInfos;
    });
    FunctionalityService.getTech($scope.userId).then(function (response) {
      $scope.technicalManagerInfos = response.data.technicalManagerInfos;
    });
    FunctionalityService.getShipmaster($scope.userId).then(function (response) {
      $scope.shipMasterInfos = response.data.shipMasterInfos;
    });

    FunctionalityService.getShiptype($scope.userId).then(function (response) {
      if (response.status == 201 || response.status == 200) {
        $scope.vesselsTypeInfos = response.data.vesselsTypeInfos;
      } else if (response.status == 206) {
        toaster.pop('error', response.data.message);
      }
    });

    $scope.countryChange = function (country) {
      $scope.jsonC = JSON.parse(country);
      FunctionalityService.getPort($scope.jsonC.countryId).then(
        function (response) {
          if (response.status == 201 || response.status == 200) {
            $scope.portInfos = [];
            $scope.portInfos = response.data.portInfos;
          } else if (response.status == 206) {
            toaster.pop('error', response.data.message);
            $scope.portInfos = [];
          }
        }
      );
    };

    $scope.thumbnail = {
      // dataUrl: 'adsfas'
    };
    $scope.fileReaderSupported = window.FileReader != null;
    $scope.uploadFile = function (files) {
      if (files != null) {
        var file = files[0];
        $scope.shipPic = file;
        if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
          $timeout(function () {
            var fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function (e) {
              $timeout(function () {
                $scope.thumbnail.dataUrl = e.target.result;
              });
            };
          });
        }
      }
    };
  },
]);
