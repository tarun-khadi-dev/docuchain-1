var adminVesselsadd = angular.module('dapp.AdminVesselsAddinfoController', [
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

adminVesselsadd
  .controller('AdminVesselsAddinfoController', [
    '$scope',
    '$timeout',
    '$window',
    '$location',
    '$state',
    '$rootScope',
    'FunctionalityService',
    '$http',
    'toaster',
    function (
      $scope,
      $timeout,
      $window,
      $location,
      $state,
      $rootScope,
      FunctionalityService,
      $http,
      toaster
    ) {
      $scope.userProfileId = $window.localStorage.getItem('userId');
      $scope.userId = $window.localStorage.getItem('userId');
      $scope.organizationId = $window.localStorage.getItem('organizationId');
      $scope.organizationName =
        $window.localStorage.getItem('organizationName');
      $scope.adminUserList;
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
      $scope.viewby = 5;
      $scope.uiselectshow = true;
      $scope.signleVesselName = false;
      $scope.itemsPerPage = $scope.viewby;
      $scope.addPopupsubmit = true;
      $scope.editPopupsubmit = false;
      $scope.roleList = {};
      $scope.userData = {};
      $scope.countryInfos = [];
      $scope.countryId = [];
      $scope.portInfos = [];
      $scope.countryName;
      $scope.vesselUser = false;
      $scope.vesselTick = false;
      $scope.vesselsTypeInfos = [];
      $scope.shipPic;
      $scope.commercialManagerInfos = [];
      $scope.technicalManagerInfos = [];
      $scope.shipMasterInfos = [];
      $scope.commercialManagerInfoList = [];
      $scope.techManagerInfoList = [];
      $scope.shipUserId;
      $scope.techManagerIds = [];
      $scope.commercialMasterIds = [];
      $scope.allPlaceHolderListCustom = [];
      $scope.selectedTechdetails;
      $scope.selectedComdetails;
      $scope.shipMasterName = 'Ship Master';
      $scope.technicalmasterName = 'Technical';
      $scope.commercialmasterName = 'Commercial';
      $scope.roleAliasList;
      $scope.roleId;
      $scope.loader = false;
      $scope.vesselModel = true;
      callCommercial();
      callTech();
      callShipmaster();

      // Load cloned vessel data if present

      var cloneDataStr = localStorage.getItem('cloneVesselData');
      if (cloneDataStr) {
        var parsed = JSON.parse(cloneDataStr);

        var unbindWatch = $scope.$watchGroup(
          [
            'countryInfos.length',
            'vesselsTypeInfos.length',
            'technicalManagerInfos.length',
            'commercialManagerInfos.length',
          ],
          function (newValues) {
            // Condition: Country and Vessel Type lists MUST be loaded
            // Managers are optional but we check if lists exist
            if (newValues[0] > 0 && newValues[1] > 0) {
              console.log('Clone logic firing: All core lists loaded.');

              // 1. Map basic text fields
              $scope.userData.shipName = parsed.shipName;
              $scope.userData.imo = parsed.imo;
              $scope.userData.callSign = parsed.callSign;
              $scope.userData.registeredOwner = parsed.registeredOwner;
              if (parsed.keelLaid) {
                $scope.userData.keelLaid = moment(parsed.keelLaid);
              }
              console.log('CLONE DATA:', parsed);
              console.log('KEEL LAID:', parsed.keelLaid);
              if (parsed.delivered) {
                $scope.userData.delivered = moment(parsed.delivered);
              }

              // 2. Map Country (Nationality)
              if (parsed.countryNameStr) {
                var countryObj = $scope.countryInfos.find(
                  (c) =>
                    c.countryName.toLowerCase() ===
                    parsed.countryNameStr.toLowerCase()
                );
                if (countryObj) {
                  $scope.userData.countryName = JSON.stringify(
                    angular.copy(countryObj)
                  );
                  $scope.countryChange($scope.userData.countryName); // Load ports
                }
              }

              // 3. Map Vessel Type
              if (parsed.vesselTypeNameStr) {
                var typeObj = $scope.vesselsTypeInfos.find(
                  (t) =>
                    t.vesselsTypeName.toLowerCase() ===
                    parsed.vesselTypeNameStr.toLowerCase()
                );
                if (typeObj) {
                  $scope.userData.shipTypes = JSON.stringify(
                    angular.copy(typeObj)
                  );
                }
              }

              // 4. Map Port (Registry) - Special Watch
              var unbindPort = $scope.$watch(
                'portInfos.length',
                function (len) {
                  if (len > 0 && parsed.portNameStr) {
                    var portObj = $scope.portInfos.find(
                      (p) =>
                        p.portName.toLowerCase() ===
                        parsed.portNameStr.toLowerCase()
                    );
                    if (portObj) {
                      $scope.userData.stateName = JSON.stringify(
                        angular.copy(portObj)
                      );
                    }
                    unbindPort();
                  }
                }
              );
              if (newValues[2] > 0 && parsed.techManagerIds) {
                $scope.userData.techManagerIds =
                  $scope.technicalManagerInfos.filter(function (t) {
                    return parsed.techManagerIds.some((id) => id == t.userId);
                  });

                $scope.selectedTechdetails = $scope.userData.techManagerIds;
                console.log('UI Match Tech:', $scope.userData.techManagerIds);
              }

              if (newValues[3] > 0 && parsed.commercialMasterIds) {
                $scope.userData.commercialMasterIds =
                  $scope.commercialManagerInfos.filter(function (c) {
                    return parsed.commercialMasterIds.some(
                      (id) => id == c.userId
                    );
                  });
                $scope.selectedComdetails = $scope.userData.commercialMasterIds;
                console.log(
                  'UI Match Commercial:',
                  $scope.userData.commercialMasterIds
                );
              }

              // Cleanup
              localStorage.removeItem('cloneVesselData');
              unbindWatch();
            }
          }
        );
      }
      $scope.$on('$viewContentLoaded', function () {
        FunctionalityService.getListRolesName($scope.userProfileId).then(
          function (response) {
            if (response.status == 200) {
              $scope.roleAliasList = response.data.roleAliasInfos;
              angular.forEach($scope.roleAliasList, function (obj) {
                if (obj.roleId == 3) {
                  $scope.shipMasterName = obj.roleAliasName;
                } else if (obj.roleId == 4) {
                  $scope.technicalmasterName = obj.roleAliasName;
                } else if (obj.roleId == 5) {
                  $scope.commercialmasterName = obj.roleAliasName;
                }
              });
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      });
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

      $scope.selectedUsers = [{}];

      $scope.auth = {};

      $scope.thumbnail = {
        // dataUrl: 'adsfas'
      };
      $scope.fileReaderSupported = window.FileReader != null;
      $scope.uploadFile = function (files) {
        if (files != null) {
          var file = files[0];

          if (files[0].size > 2048000) {
            $rootScope.errorFile = document.getElementById(
              'sizeOffile'
            ).innerHTML = 'Picture should be below 1MB';
          } else {
            document.getElementById('sizeOffile').innerHTML = '';
            $rootScope.errorFile = '';
          }

          $scope.shipPic = file;

          //     if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
          //       $timeout(function () {
          //         var fileReader = new FileReader();
          //         fileReader.readAsDataURL(file);
          //         fileReader.onload = function (e) {
          //           $timeout(function () {
          //             $scope.thumbnail.dataUrl = e.target.result;
          //             $scope.userData.shipProfilePicPath = $scope.thumbnail.dataUrl;
          //           });
          //         };
          //       });
          //     }
          //   }
          // };
          // $scope.addNewUser = function (businessCategory) {
          //   $scope.businessCategory = businessCategory;
          // };
          if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
            $timeout(function () {
              var fileReader = new FileReader();
              fileReader.readAsDataURL(file);
              fileReader.onload = function (e) {
                $timeout(function () {
                  $scope.thumbnail.dataUrl = e.target.result;
                  $scope.userData.shipProfilePicPath = $scope.thumbnail.dataUrl;
                });
              };
            });
          }
        }
      };
      // --- Select/Clear All for Standard Placeholders ---
      $scope.selectAllStandard = function () {
        // Copy the full list into the selected model
        $scope.userData.selectedocu = angular.copy($scope.allPlaceHolderList);
      };

      $scope.clearAllStandard = function () {
        // Empty the selected model
        $scope.userData.selectedocu = [];
      };

      // --- Select/Clear All for Custom Placeholders ---
      $scope.selectAllCustom = function () {
        // Copy the full list into the selected model
        $scope.userData.selectecustom = angular.copy(
          $scope.allPlaceHolderListCustom
        );
      };

      $scope.clearAllCustom = function () {
        // Empty the selected model
        $scope.userData.selectecustom = [];
      };

      // // --- AUTOFILL FROM PDF LOGIC ---
      // $scope.uploadVesselPdfForAutofill = function (files) {
      //   if (files && files.length > 0) {
      //     var file = files[0];

      //     if (file.type !== 'application/pdf') {
      //       toaster.pop(
      //         'error',
      //         'Invalid File',
      //         'Please upload a valid PDF document.'
      //       );
      //       return;
      //     }

      //     $scope.loader = true;

      //     FunctionalityService.scanVesselDocument(file).then(
      //       function (response) {
      //         $scope.loader = false;

      //         if (response.status == 200 || response.status == 201) {
      //           var data = response.data;
      //           toaster.pop(
      //             'success',
      //             'Document Scanned',
      //             'Vessel form automatically populated.'
      //           );

      //           console.log('PDF Autofill Data:', data);

      //           // Wrap in $timeout to force AngularJS to update the UI and custom directives
      //           $timeout(function () {
      //             // 1. Map Text fields
      //             $scope.userData.shipName =
      //               data.vesselName ||
      //               data.shipName ||
      //               $scope.userData.shipName;
      //             $scope.userData.callSign =
      //               data.callSign || $scope.userData.callSign;

      //             // IMO Number: strip any non-digit characters so it passes your custom "number" directive
      //             var rawImo = data.imo || data.imoNumber || '';
      //             if (rawImo) {
      //               $scope.userData.imo = rawImo
      //                 .toString()
      //                 .replace(/[^0-9]/g, '');
      //             }

      //             // 2. Map Numeric Fields (parseFloat ensures they bind to <input type="number"> correctly)
      //             // Removing any accidental commas/text before parsing just to be safe
      //             if (data.grossTonnage)
      //               $scope.userData.dwt = parseFloat(
      //                 (data.grossTonnage + '').replace(/[^0-9.]/g, '')
      //               );
      //             if (data.dwtSummer)
      //               $scope.userData.weight = parseFloat(
      //                 (data.dwtSummer + '').replace(/[^0-9.]/g, '')
      //               );
      //             if (data.lengthOA)
      //               $scope.userData.length = parseFloat(
      //                 (data.lengthOA + '').replace(/[^0-9.]/g, '')
      //               );

      //             if (data.breadth)
      //               $scope.userData.breadth = parseFloat(
      //                 (data.breadth + '').replace(/[^0-9.]/g, '')
      //               );
      //             $scope.userData.internationalGRT =
      //               data.internationalGRT || data.grt;
      //             $scope.userData.internationalNRT =
      //               data.internationalNRT || data.nrt;

      //             // 3. Map Dates (Passing the epoch timestamp directly, customdatepicker uses moment() which reads epochs)
      //             if (data.keelLaidDate) {
      //               $scope.userData.keelLaid = data.keelLaidDate;
      //             }
      //             if (data.deliveryDate) {
      //               $scope.userData.delivered = data.deliveryDate;
      //             }

      //             // 4. Map Country (Nationality) dropdown
      //             var extractedNationality = data.nationality || data.flag;
      //             if (
      //               extractedNationality &&
      //               $scope.countryInfos &&
      //               $scope.countryInfos.length > 0
      //             ) {
      //               var countryObj = $scope.countryInfos.find(
      //                 (c) =>
      //                   c.countryName.toLowerCase() ===
      //                   extractedNationality.toLowerCase()
      //               );
      //               if (countryObj) {
      //                 $scope.userData.countryName = JSON.stringify(
      //                   angular.copy(countryObj)
      //                 );
      //                 $scope.countryChange($scope.userData.countryName); // Trigger port fetch
      //               }
      //             }

      //             // 5. Map Port dropdown (Wait slightly for the country API to fetch ports)
      //             var extractedPort = data.port || data.portOfRegistry;
      //             if (extractedPort) {
      //               $timeout(function () {
      //                 if ($scope.portInfos && $scope.portInfos.length > 0) {
      //                   var portObj = $scope.portInfos.find(
      //                     (p) =>
      //                       p.portName.toLowerCase() ===
      //                       extractedPort.toLowerCase()
      //                   );
      //                   if (portObj) {
      //                     $scope.userData.stateName = JSON.stringify(
      //                       angular.copy(portObj)
      //                     );
      //                   }
      //                 }
      //               }, 800);
      //             }
      //           });
      //         } else {
      //           toaster.pop(
      //             'error',
      //             'Extraction Failed',
      //             response.data.message || 'Could not parse document.'
      //           );
      //         }
      //       },
      //       function myError(err) {
      //         $scope.loader = false;
      //         toaster.pop(
      //           'error',
      //           'Server Error',
      //           'Failed to communicate with the scanning server.'
      //         );
      //         console.log('Scan Error response', err);
      //       }
      //     );
      //   }
      // };
      // // --- END AUTOFILL LOGIC ---

      // --- AUTOFILL FROM PDF LOGIC ---
      // $scope.uploadVesselPdfForAutofill = function (files) {
      //   if (files && files.length > 0) {
      //     var file = files[0];

      //     if (file.type !== 'application/pdf') {
      //       toaster.pop(
      //         'error',
      //         'Invalid File',
      //         'Please upload a valid PDF document.'
      //       );
      //       return;
      //     }

      //     $scope.loader = true;

      //     FunctionalityService.scanVesselDocument(file).then(
      //       function (response) {
      //         $scope.loader = false;

      //         if (response.status == 200 || response.status == 201) {
      //           var data = response.data;
      //           toaster.pop(
      //             'success',
      //             'Document Scanned',
      //             'Vessel form automatically populated.'
      //           );

      //           console.log('PDF Autofill Data:', data);

      //           // Wrap in $timeout to force AngularJS to update the UI and custom directives
      //           $timeout(function () {
      //             // 1. Map Text fields
      //             $scope.userData.shipName =
      //               data.vesselName ||
      //               data.shipName ||
      //               $scope.userData.shipName;
      //             $scope.userData.callSign =
      //               data.callSign || $scope.userData.callSign;

      //             // ---> NEW FIX: Map Builder / Yard <---
      //             $scope.userData.builder =
      //               data.builderYard || data.builder || $scope.userData.builder;

      //             // IMO Number: strip any non-digit characters
      //             var rawImo = data.imo || data.imoNumber || '';
      //             if (rawImo) {
      //               $scope.userData.imo = rawImo
      //                 .toString()
      //                 .replace(/[^0-9]/g, '');
      //             }

      //             // 2. Map Numeric Fields (Strict Mapping)

      //             // DWT (Deadweight Tonnage) -> mapped to grossTonnageSuez
      //             var extractedDwt = data.grossTonnageSuez || data.grossTonnage;
      //             if (extractedDwt !== undefined && extractedDwt !== null) {
      //               $scope.userData.dwt = parseFloat(
      //                 (extractedDwt + '').replace(/[^0-9.]/g, '')
      //               );
      //             }

      //             // Weight (MT) -> mapped to dwtSummer
      //             var extractedWeight = data.dwtSummer;
      //             if (
      //               extractedWeight !== undefined &&
      //               extractedWeight !== null
      //             ) {
      //               $scope.userData.weight = parseFloat(
      //                 (extractedWeight + '').replace(/[^0-9.]/g, '')
      //               );
      //             }

      //             // Length (Meters) -> mapped to lengthOA
      //             var extractedLength =
      //               data.lengthOA || data.lengthOverall || data.length;
      //             if (
      //               extractedLength !== undefined &&
      //               extractedLength !== null
      //             ) {
      //               $scope.userData.length = parseFloat(
      //                 (extractedLength + '').replace(/[^0-9.]/g, '')
      //               );
      //             }

      //             // Map Breadth
      //             if (data.breadth !== undefined && data.breadth !== null) {
      //               $scope.userData.breadth = parseFloat(
      //                 (data.breadth + '').replace(/[^0-9.]/g, '')
      //               );
      //             }

      //             $scope.userData.internationalGRT =
      //               data.internationalGRT || data.grt;
      //             $scope.userData.internationalNRT =
      //               data.internationalNRT || data.nrt;

      //             // 3. Map Dates
      //             if (data.keelLaidDate) {
      //               $scope.userData.keelLaid = data.keelLaidDate;
      //             }
      //             if (data.deliveryDate) {
      //               $scope.userData.delivered = data.deliveryDate;
      //             }

      //             // 4. Map Country (Nationality) dropdown
      //             var extractedNationality = data.nationality || data.flag;
      //             if (
      //               extractedNationality &&
      //               $scope.countryInfos &&
      //               $scope.countryInfos.length > 0
      //             ) {
      //               var countryObj = $scope.countryInfos.find(
      //                 (c) =>
      //                   c.countryName.toLowerCase() ===
      //                   extractedNationality.toLowerCase()
      //               );
      //               if (countryObj) {
      //                 $scope.userData.countryName = JSON.stringify(
      //                   angular.copy(countryObj)
      //                 );
      //                 $scope.countryChange($scope.userData.countryName); // Trigger port fetch
      //               }
      //             }

      //             // 5. Map Port dropdown
      //             var extractedPort = data.port || data.portOfRegistry;
      //             if (extractedPort) {
      //               $timeout(function () {
      //                 if ($scope.portInfos && $scope.portInfos.length > 0) {
      //                   var portObj = $scope.portInfos.find(
      //                     (p) =>
      //                       p.portName.toLowerCase() ===
      //                       extractedPort.toLowerCase()
      //                   );
      //                   if (portObj) {
      //                     $scope.userData.stateName = JSON.stringify(
      //                       angular.copy(portObj)
      //                     );
      //                   }
      //                 }
      //               }, 800);
      //             }
      //           });
      //         } else {
      //           toaster.pop(
      //             'error',
      //             'Extraction Failed',
      //             response.data.message || 'Could not parse document.'
      //           );
      //         }
      //       },
      //       function myError(err) {
      //         $scope.loader = false;
      //         toaster.pop(
      //           'error',
      //           'Server Error',
      //           'Failed to communicate with the scanning server.'
      //         );
      //         console.log('Scan Error response', err);
      //       }
      //     );
      //   }
      // };
      // --- END AUTOFILL LOGIC ---
      // --- AUTOFILL FROM PDF LOGIC ---
      $scope.uploadVesselPdfForAutofill = function (files) {
        if (files && files.length > 0) {
          var file = files[0];

          if (file.type !== 'application/pdf') {
            toaster.pop(
              'error',
              'Invalid File',
              'Please upload a valid PDF document.'
            );
            return;
          }

          $scope.loader = true;

          FunctionalityService.scanVesselDocument(file).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 200 || response.status == 201) {
                var data = response.data;
                toaster.pop(
                  'success',
                  'Document Scanned',
                  'Vessel form automatically populated.'
                );

                console.log('PDF Autofill Data:', data);

                // Wrap in $timeout to force AngularJS to update the UI and custom directives
                $timeout(function () {
                  // 1. Map Text fields
                  $scope.userData.shipName =
                    data.vesselName ||
                    data.shipName ||
                    $scope.userData.shipName;
                  $scope.userData.callSign =
                    data.callSign || $scope.userData.callSign;
                  $scope.userData.builder =
                    data.builderYard || data.builder || $scope.userData.builder;

                  // IMO Number: strip any non-digit characters
                  var rawImo = data.imo || data.imoNumber || '';
                  if (rawImo) {
                    $scope.userData.imo = rawImo
                      .toString()
                      .replace(/[^0-9]/g, '');
                  }

                  // 2. Map Numeric Fields (Strict Mapping)
                  // DWT (Deadweight Tonnage) -> mapped to grossTonnageSuez
                  var extractedDwt = data.grossTonnageSuez || data.grossTonnage;
                  if (extractedDwt !== undefined && extractedDwt !== null) {
                    $scope.userData.dwt = parseFloat(
                      (extractedDwt + '').replace(/[^0-9.]/g, '')
                    );
                  }

                  // Weight (MT) -> mapped to dwtSummer
                  var extractedWeight = data.dwtSummer;
                  if (
                    extractedWeight !== undefined &&
                    extractedWeight !== null
                  ) {
                    $scope.userData.weight = parseFloat(
                      (extractedWeight + '').replace(/[^0-9.]/g, '')
                    );
                  }

                  // Length (Meters) -> mapped to lengthOA
                  var extractedLength =
                    data.lengthOA || data.lengthOverall || data.length;
                  if (
                    extractedLength !== undefined &&
                    extractedLength !== null
                  ) {
                    $scope.userData.length = parseFloat(
                      (extractedLength + '').replace(/[^0-9.]/g, '')
                    );
                  }

                  // Map Breadth
                  if (data.breadth !== undefined && data.breadth !== null) {
                    $scope.userData.breadth = parseFloat(
                      (data.breadth + '').replace(/[^0-9.]/g, '')
                    );
                  }

                  $scope.userData.internationalGRT =
                    data.internationalGRT || data.grt;
                  $scope.userData.internationalNRT =
                    data.internationalNRT || data.nrt;

                  // 3. Map Dates (FIXED: Converting directly to Moment objects so moment-picker recognizes them instantly)
                  if (data.keelLaidDate) {
                    $scope.userData.keelLaid = moment(data.keelLaidDate);
                  } else if (data.keelLaidDateString) {
                    $scope.userData.keelLaid = moment(
                      data.keelLaidDateString,
                      'DD-MM-YYYY'
                    );
                  }

                  if (data.deliveryDate) {
                    $scope.userData.delivered = moment(data.deliveryDate);
                  } else if (data.deliveryDateString) {
                    $scope.userData.delivered = moment(
                      data.deliveryDateString,
                      'DD-MM-YYYY'
                    );
                  }

                  // 4. Map Country (Nationality) dropdown
                  var extractedNationality = data.nationality || data.flag;
                  if (
                    extractedNationality &&
                    $scope.countryInfos &&
                    $scope.countryInfos.length > 0
                  ) {
                    var countryObj = $scope.countryInfos.find(
                      (c) =>
                        c.countryName.toLowerCase() ===
                        extractedNationality.toLowerCase()
                    );
                    if (countryObj) {
                      $scope.userData.countryName = JSON.stringify(
                        angular.copy(countryObj)
                      );
                      $scope.countryChange($scope.userData.countryName); // Trigger port fetch
                    }
                  }

                  // 5. Map Port dropdown
                  var extractedPort = data.port || data.portOfRegistry;
                  if (extractedPort) {
                    $timeout(function () {
                      if ($scope.portInfos && $scope.portInfos.length > 0) {
                        var portObj = $scope.portInfos.find(
                          (p) =>
                            p.portName.toLowerCase() ===
                            extractedPort.toLowerCase()
                        );
                        if (portObj) {
                          $scope.userData.stateName = JSON.stringify(
                            angular.copy(portObj)
                          );
                        }
                      }
                    }, 800);
                  }
                });
              } else {
                toaster.pop(
                  'error',
                  'Extraction Failed',
                  response.data.message || 'Could not parse document.'
                );
              }
            },
            function myError(err) {
              $scope.loader = false;
              toaster.pop(
                'error',
                'Server Error',
                'Failed to communicate with the scanning server.'
              );
              console.log('Scan Error response', err);
            }
          );
        }
      };
      // --- END AUTOFILL LOGIC ---
      $scope.addNewUser = function (businessCategory) {
        $scope.businessCategory = businessCategory;
      };
      $scope.addUser = function (user) {
        $scope.loader = true;
        if (user.password !== user.confirmPassword) {
          $scope.loader = false;

          toaster.error('error', 'Password does not match');
        } else {
          $scope.userInfoId = [];
          $scope.selectedUserslist = user.selectedUsers;
          angular.forEach($scope.selectedUserslist, function (infos) {
            $scope.userInfoId.push(infos.id);
          });

          if ($scope.roleAliasList != null) {
            angular.forEach($scope.roleAliasList, function (obj) {
              if (obj.roleAliasName == $scope.businessCategory) {
                $scope.roleId = obj.roleId;
              }
            });
          }
          var userInfo = {
            firstName: user.firstName,
            lastName: user.lastName,
            userName: user.userName,
            password: user.password,
            businessCategory: $scope.businessCategory,
            roleId: $scope.roleId,
            mail: user.mail,
            organizationId: $scope.organizationId,
            loginId: $scope.userId,
            shipProfileIds: $scope.userInfoId,
          };
          FunctionalityService.addUser(userInfo, $scope.shipPic).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 200 || response.status == 201) {
                toaster.pop('success', response.data.message);
                callCommercial();
                callTech();
                callShipmaster();
                $scope.popupClear();
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
      $scope.popupClear = function () {
        $scope.user.firstName = '';
        $scope.user.lastName = '';
        $scope.user.mail = '';
        $scope.user.userName = '';
        $scope.user.password = '';
        $scope.user.confirmPassword = '';
        $scope.user.selectedCategory = '';
        $scope.user.selectedUsers = '';
        $scope.user = angular.copy($scope.master);
        if ($scope.addNewUserSelectForm)
          $scope.addNewUserSelectForm.$setPristine();
        $scope.thumbnail.dataUrls = 'image/avatarimg.jpg';
        document.getElementById('control').value = '';
        $scope.data.image = undefined;
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

      getList();
      function getList() {
        FunctionalityService.getVessellist($scope.userId).then(
          function (response) {
            if (response.status == 201 || response.status == 200) {
              $scope.vesselsList = response.data.shipProfileList;
              angular.forEach($scope.vesselsList, function (value) {
                $scope.commercialManagerInfoList.push(
                  value.commercialManagerInfoList
                );
                $scope.techManagerInfoList.push(value.techManagerInfoList);
              });
            } else if (response.status == 206) {
              toaster.pop('error', response.data.message);
            }
          }
        );
      }

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

      // Function to go back to the FIRST step (Vessel Info)
      $scope.prevTovesselInfo = function () {
        $scope.vesselModel = true;
        $scope.vesselplaceholderDiv = false;
        $scope.vesselUser = false;

        $('#addplaceholder').modal('hide');

        // Smooth scroll to top so the user sees the start of the form
        $window.scrollTo(0, 0);
      };

      // Function to go to the SECOND step (Placeholder)
      $scope.vesselplaceholder = function (tickValid) {
        // Logic to check if step 1 is valid
        if (Object.keys(tickValid).length === 0) {
          $scope.vesselModel = false;
          $scope.vesselplaceholderDiv = true;
          $scope.vesselUser = false;

          $scope.vesselTick = false;
        } else {
          $scope.vesselModel = false;
          $scope.vesselplaceholderDiv = true;
          $scope.vesselUser = false;

          $scope.vesselTick = true;
        }
        $window.scrollTo(0, 0);
      };

      // Function to go to the THIRD step (Vessel Users)
      $scope.vesselUserShow = function (tickValid) {
        $scope.vesselModel = false;
        $scope.vesselplaceholderDiv = false;
        $scope.vesselUser = true;

        $('#addplaceholder').modal('hide');
        $window.scrollTo(0, 0);
      };

      //   function cloneShipmaster()
      function callCommercial() {
        FunctionalityService.getCommercial($scope.userId).then(
          function (response) {
            $scope.commercialManagerInfos =
              response.data.commercialManagerInfos;

            // --- ADD CLONE MATCHING HERE ---
            var cloneDataStr = localStorage.getItem('cloneVesselData');
            if (cloneDataStr) {
              var parsed = JSON.parse(cloneDataStr);
              if (parsed.commercialMasterIds && $scope.commercialManagerInfos) {
                $scope.selectedComdetails =
                  $scope.commercialManagerInfos.filter(function (c) {
                    return parsed.commercialMasterIds.some(
                      (id) => id == c.userId
                    );
                  });
                console.log(
                  'Matched Commercial Managers from API:',
                  $scope.selectedComdetails
                );
              }
            }
          }
        );
      }

      function callTech() {
        FunctionalityService.getTech($scope.userId).then(function (response) {
          $scope.technicalManagerInfos = response.data.technicalManagerInfos;

          // --- ADD CLONE MATCHING HERE ---
          var cloneDataStr = localStorage.getItem('cloneVesselData');
          if (cloneDataStr) {
            var parsed = JSON.parse(cloneDataStr);
            if (parsed.techManagerIds && $scope.technicalManagerInfos) {
              $scope.selectedTechdetails = $scope.technicalManagerInfos.filter(
                function (t) {
                  return parsed.techManagerIds.some((id) => id == t.userId);
                }
              );
              console.log(
                'Matched Tech Managers from API:',
                $scope.selectedTechdetails
              );
            }
          }
        });
      }

      function callShipmaster() {
        FunctionalityService.getShipmaster($scope.userId).then(
          function (response) {
            $scope.shipMasterInfos = response.data.shipMasterInfos;
          }
        );
      }

      FunctionalityService.getCountry().then(function (response) {
        if (response.status == 201 || response.status == 200) {
          $scope.countryInfos = response.data.countryInfos;
          angular.forEach($scope.countryInfos, function (id) {
            $scope.countryId.push(id.countryId);
          });
        } else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
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
      $scope.uploadFileuser = function (files) {
        if (files != null) {
          var file = files[0];

          $scope.shipPic = file;
          if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
            $timeout(function () {
              var fileReader = new FileReader();
              fileReader.readAsDataURL(file);
              fileReader.onload = function (e) {
                $timeout(function () {
                  $scope.thumbnail.dataUrls = e.target.result;
                });
              };
            });
          }
        }
      };

      $scope.addShipChange = function (details) {
        $scope.shipUserId = details.userId;
      };

      $scope.addTechChange = function (details) {
        $scope.selectedTechdetails = details;
      };

      $scope.addComChange = function (details) {
        $scope.selectedComdetails = details;
      };

      $scope.addShipSave = function (shipDetails) {
        $scope.loader = true;
        $scope.docIds = [];
        $scope.customDocumentHolders = [];
        console.log('shipDetails.selectedocu::', shipDetails.selectedocu);
        angular.forEach(shipDetails.selectedocu, function (item) {
          $scope.docIds.push(item.documentHolderId);
        });
        angular.forEach(shipDetails.selectecustom, function (item) {
          var dName = item.documentHolderName;
          var distripution = item.documentHolderDescription;
          var obj = {
            documentHolderName: dName,
            documentHolderDescription: distripution,
          };
          $scope.customDocumentHolders.push(obj);
        });

        if ($rootScope.errorFile === 'Picture should be below 1MB') {
          toaster.pop('error', $rootScope.errorFile);
        } else {
          $rootScope.errorFile = '';

          if ($scope.selectedTechdetails != null) {
            angular.forEach($scope.selectedTechdetails, function (value) {
              $scope.techManagerIds.push(value.userId);
            });
          }
          if ($scope.selectedComdetails != null) {
            angular.forEach($scope.selectedComdetails, function (value) {
              $scope.commercialMasterIds.push(value.userId);
            });
          }

          $scope.jsonCountry = JSON.parse(shipDetails.countryName);
          $scope.jsonState = JSON.parse(shipDetails.stateName);
          $scope.jsonShipType = JSON.parse(shipDetails.shipTypes);

          var data = {
            bhp: shipDetails.bhp,
            builder: shipDetails.builder,
            callSign: shipDetails.callSign,
            countryName: $scope.jsonCountry.countryName,
            delivered: shipDetails.delivered,
            engineType: shipDetails.engineType,
            imo: shipDetails.imo,
            internationalGRT: shipDetails.internationalGRT,
            internationalNRT: shipDetails.internationalNRT,
            keelLaid: shipDetails.keelLaid,
            registeredOwner: shipDetails.registeredOwner,
            shipName: shipDetails.shipName,
            shipTypes: $scope.jsonShipType.vesselsTypeName,
            stateName: $scope.jsonState.portName,
            dwt: shipDetails.dwt,
            breadth: shipDetails.breadth,
            weight: shipDetails.weight,
            length: shipDetails.length,
            loginId: $scope.userId,
            commercialMasterIds: $scope.commercialMasterIds,
            techManagerIds: $scope.techManagerIds,
            shipMasterId: $scope.shipUserId,
            status: 1,
            organizationId: $scope.organizationId,
            documentHolderName: shipDetails.placeholdername,
            documentHolderDescription: shipDetails.placeholderdescription,
            type: 'Custom',
            docIds: $scope.docIds,
            customDocumentHolders: $scope.customDocumentHolders,
          };

          FunctionalityService.addShip(data, $scope.shipPic).then(
            function (response) {
              $scope.loader = false;

              if (response.status == 201 || response.status == 200) {
                toaster.pop('success', response.data.message);
                setTimeout(function () {
                  $state.go('dapp.adminVessels');
                }, 500);

                getList();
              } else if (response.status == 206) {
                toaster.pop('error', response.data.message);
                $scope.commercialMasterIds = [];
                $scope.techManagerIds = [];
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        }
      };

      $scope.addPlaceHolder = function () {
        var addPlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderName: $scope.placeholdername,
          documentHolderDescription: $scope.placeholderdescription,
          organizationName: $scope.organizationName,
          type: 'Custom',
        };
        $scope.allPlaceHolderListCustom.push(addPlaceHolderData);
        $scope.userData.selectecustom = $scope.allPlaceHolderListCustom;
        $('#addplaceholder').modal('hide');
        $scope.placeholdername = '';
        $scope.placeholderdescription = '';
      };

      $scope.clearAddPlaceholder = function () {
        $scope.placeholdername = '';
        $scope.placeholderdescription = '';
        $scope.placeholderfilename = '';
      };

      $scope.$on('$viewContentLoaded', function () {
        $scope.loader = true;
        var dataid = { userId: $scope.userProfileId };

        // --- 1. FIRST CALL: Load the standard dropdown list (Full List) ---
        FunctionalityService.getPlaceHolderListByorganizatinId(dataid).then(
          function mySuccess(response) {
            if (response.status == 200 || response.status == 201) {
              $scope.allPlaceHolderList = response.data.documentHolderList;
              console.log(
                'Dropdown Master List Loaded:',
                $scope.allPlaceHolderList.length
              );
            }
          }
        );

        // --- 2. SECOND CALL: If Cloning, get the specific assigned placeholders ---
        var cloneDataStr = localStorage.getItem('cloneVesselData');
        if (cloneDataStr) {
          var parsed = JSON.parse(cloneDataStr);
          if (parsed.originalVesselId) {
            // Prepare the specific request for assigned placeholders
            var cloneRequest = {
              userId: $scope.userProfileId,
              type: 'Standard',
              vesselid: parsed.originalVesselId,
            };

            console.log(
              'Calling API second time for Clone assignments:',
              cloneRequest
            );
            FunctionalityService.getPlaceHolderListByorganizatinId(
              cloneRequest
            ).then(function (response) {
              if (response.status == 200 || response.status == 201) {
                var assignedList = response.data.documentHolderList || [];

                console.log('Assigned from API:', assignedList);

                // 🔥 Reset first
                $scope.userData.selectedocu = [];

                // 🔥 MATCH BY NAME (NOT ID)
                angular.forEach($scope.allPlaceHolderList, function (master) {
                  angular.forEach(assignedList, function (assigned) {
                    if (
                      master.documentHolderName === assigned.documentHolderName
                    ) {
                      $scope.userData.selectedocu.push(master);
                    }
                  });
                });

                console.log('Final Selected:', $scope.userData.selectedocu);
              }
            });
          }
        } else {
          $scope.loader = false;
        }
        console.log('Master List:', $scope.allPlaceHolderList);

        console.log('Final Selected:', $scope.userData.selectedocu);
        // Call custom placeholders as usual
        placeholderByCustom();
      });

      function placeholderByCustom() {
        var dataid = { userId: $scope.userProfileId, type: 'Custom' };
        console.log('data is ::', dataid);
        FunctionalityService.getPlaceHolderListByorganizatinId(dataid).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              // $scope.allPlaceHolderListCustom = JSON.stringify(response.data.documentHolderList);
              // $scope.allPlaceHolderListCustom = response.data.documentHolderList;
              console.log(
                '$scope.allPlaceHolderListCustom',
                JSON.stringify($scope.allPlaceHolderListCustom)
              );
              if ($scope.allPlaceHolderListCustom == undefined) {
                toaster.clear();
                toaster.info({ title: 'No records found' });
              }
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
      }
    },
  ])

  .directive('customdatepicker', [
    '$window',
    '$templateCache',
    function ($window, $templateCache) {
      if (!$window.moment) {
        console.log(
          'moment.js is required for this datepicker, http://momentjs.com/'
        );
        return {
          link: function () {},
        };
      }

      var fileName = 'datepicker.directive.html';
      var template = $templateCache.get(fileName);

      if (!template) {
        template = [
          '<form name="datepicker">',
          '<div class="btn-group btn-group-justified" role="group">',
          '<div ng-repeat="i in localeOrder track by $index" class="btn-group" role="group">',
          '<button type="button" class="btn btn-default dropdown-toggle drop-focus" data-toggle="dropdown">',
          '<span ng-bind="date[options[i].name] || options[i].name"> </span>',
          '<span class="caret"></span>',
          '</button>',

          '<ul class="dropdown-menu" role="menu">',
          '<li ng-repeat="(j, option) in options[i].options track by $index" ng-class="{\'selectedval\': option.selected === true, \'disabled\': option.disabled === true}">',
          '<a ng-click="select(options[i].name, option)" ng-bind="options[i].labels[j] || option.value"></a>',
          '</li>',
          '</ul>',

          '</div>',
          '</div>',
          '</form>',
        ].join('');
        $templateCache.put(fileName, template);
      }

      return {
        restrict: 'A',
        //replace: true,
        require: 'ngModel',
        templateUrl: fileName,
        scope: {
          model: '=ngModel',
          minDate: '=minDate',
          maxDate: '=maxDate',
          locale: '=?locale',
        },
        link: function (scope, elem, attrs, ngModelCtrl) {
          var day = [
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
          ];
          var month = [
            'January',
            'February',
            'March',
            'April',
            'May',
            'June',
            'July',
            'August',
            'September',
            'October',
            'November',
            'December',
          ];
          scope.localeOrder = ['day', 'month', 'year'];

          // params object is the main object to be modified. It is iterated by ng-repeat in the dom.
          var params = {
            day: {
              name: 'day',
              initLabel: 'Day',
              options: [],
            },
            month: {
              name: 'month',
              initLabel: 'Month',
              options: [],
            },
            year: {
              name: 'year',
              initLabel: 'Year',
              options: [],
            },
          };

          // setting up exceptions because of minDate, maxDate;
          var minDate;
          var maxDate;

          // setting minDate
          if (scope.minDate && scope.minDate !== 'now') {
            var tempMinDate;

            if (typeof scope.minDate === 'number') {
              // assume this is something like -100 or +100;
              if (scope.minDate > 0) {
                tempMinDate = moment().add(scope.minDate, 'year');
              } else if (scope.minDate < 0) {
                tempMinDate = moment().subtract(
                  Math.abs(scope.minDate),
                  'year'
                );
              } else {
                tempMinDate = moment();
              }
            } else {
              tempMinDate = moment(scope.minDate);
            }

            // setting it to minDate
            minDate = tempMinDate;
          } else {
            minDate = moment();
          }

          // setting maxDate. THIS IS NOT DRY, BUT A PASTE FROM SETTING minDate !
          if (scope.maxDate && scope.maxDate !== 'now') {
            var tempMaxDate;

            if (typeof scope.maxDate === 'number') {
              // assume this is something like -100 or +100;
              if (scope.maxDate > 0) {
                tempMaxDate = moment().add(scope.maxDate, 'year');
              } else if (scope.maxDate < 0) {
                tempMaxDate = moment().subtract(
                  Math.abs(scope.maxDate),
                  'year'
                );
              } else {
                tempMaxDate = moment();
              }
            } else {
              tempMaxDate = moment(scope.maxDate);
            }

            // setting it to maxDate
            maxDate = tempMaxDate;
          } else {
            maxDate = moment();
          }

          // copying scope.options, which is used in view.
          scope.options = angular.copy(params);

          // holder for date object, modified by scope.select(). Every change the method setupModel() is called, a new Date is created from this.
          scope.date = {};

          // Select the value;
          scope.select = function (typeString, option) {
            if (option.disabled === undefined || option.disabled !== true) {
              scope.date[typeString] = option.value;
              calcAvailableDates(scope.date);
              setupModel();
            }
          };

          // recreate scope.options based on params and date input;
          function calcAvailableDates(dateObj) {
            var maxYear = maxDate.get('year');
            var minYear = minDate.get('year');
            var maxMonth = maxDate.get('month');
            var minMonth = minDate.get('month');
            var maxDay = maxDate.get('date');
            var minDay = minDate.get('date');

            // CALCULATE YEARS
            if (minDate !== maxDate) {
              scope.options.year.options = [];
              for (var i = minYear; i <= maxYear; i++) {
                var yearObj = {
                  value: i,
                  available: true,
                };

                if (dateObj && dateObj.year === i) {
                  yearObj.selected = true;
                }

                scope.options.year.options.push(yearObj);
              }
            } else {
              // do something like assumptions, e.g. datepicker?
            }

            // CALCULATE MONTHS
            if (minDate !== maxDate) {
              scope.options.month.options = [];

              for (var i = 0; i < month.length; i++) {
                var monthObj = {
                  value: month[i],
                };

                if (dateObj && dateObj.month === month[i]) {
                  monthObj.selected = true;
                }

                if (dateObj && dateObj.year) {
                  if (dateObj.year === minYear) {
                    if (minMonth > i) {
                      monthObj.disabled = true;
                    }
                  } else if (dateObj.year === maxYear) {
                    if (maxMonth < i) {
                      monthObj.disabled = true;
                    }
                  }
                }

                scope.options.month.options.push(monthObj);
              }
            } else {
              // do something like assumptions, e.g. datepicker?
            }

            // CALCULATE DAYS
            if (minDate !== maxDate) {
              scope.options.day.options = [];
              if (dateObj) {
                // if ((dateObj && dateObj.years && dateObj.months) || dateObj.months) {

                var useYear;
                if (dateObj.year) {
                  useYear = dateObj.year;
                } else {
                  useYear = moment().get('year');
                }

                var totalDaysMonth = moment({
                  year: useYear,
                  month: month.indexOf(dateObj.month),
                })
                  .endOf('month')
                  .get('date');

                for (var i = 0; i < totalDaysMonth; i++) {
                  var dayObj = {};
                  dayObj.value = i + 1;

                  if (dateObj && dateObj.day && dateObj.day === i + 1) {
                    dayObj.selected = true;
                  }

                  // setting values disabled when they're mindate < or > maxDate

                  if (dateObj.year && dateObj.month) {
                    if (
                      dateObj.year === minYear &&
                      month.indexOf(dateObj.month) === minMonth
                    ) {
                      if (minDay > i) {
                        dayObj.disabled = true;
                      }
                    } else if (
                      dateObj.year === maxYear &&
                      month.indexOf(dateObj.month) === maxMonth
                    ) {
                      if (maxDay < i) {
                        dayObj.disabled = true;
                      }
                    }
                  }

                  scope.options.day.options.push(dayObj);
                }
              } else {
                // only days available, just use 31 days.
                scope.options.day.options.push(dayObj);
                for (var i = 0; i < day.length; i++) {
                  var dayObj = {};
                  dayObj.value = i + 1;

                  if (dateObj && dateObj.day && dateObj.day === i + 1) {
                    dayObj.selected = true;
                  }

                  scope.options.day.options.push(dayObj);
                }
              }
            }
          }

          function setupModel() {
            // Not setting before all values are available.
            if (
              scope.date.year !== undefined &&
              scope.date.month !== undefined &&
              scope.date.day !== undefined
            ) {
              var createDate = {
                year: scope.date.year,
                month: month.indexOf(scope.date.month),
                date: scope.date.day,
              };

              createDate = moment.utc(createDate);
              ngModelCtrl.$setViewValue(createDate);
            }
          }

          // toView - what is transformed from model to view.
          var toView = function (value) {
            if (value !== undefined) {
              var tempDate = moment(value);
              calcAvailableDates({
                year: tempDate.get('year'),
                month: month[tempDate.get('month')],
                day: tempDate.get('date'),
              });
            } else {
              calcAvailableDates();
            }

            if (value !== undefined) {
              var createDate = moment(value);
              if (createDate.isValid()) {
                var obj = {};
                obj.day = createDate.date();
                obj.month = month[createDate.month()];
                obj.year = createDate.year();

                scope.date = obj;
              }
            } else {
              scope.date = {};
            }

            setupModel();

            return value;
          };

          // toModel - what is transformed from view to model (called by ngModelCtrl.$setViewValue(createDate))
          // in setupModel()
          var toModel = function (value) {
            console.log('toModel ', value);

            if (value && value.isValid()) {
              if (
                (value.isAfter(minDate) || minDate.isSame(value)) &&
                (maxDate.isAfter(value) || maxDate.isSame(value))
              ) {
                ngModelCtrl.$setValidity('not-allowed', true);
                return value.format();
              } else {
                ngModelCtrl.$setValidity('not-allowed', false);
                return undefined;
              }
            } else {
              return undefined;
            }
          };

          // Pushing functions to $parsers and $formatters.
          ngModelCtrl.$parsers.push(toModel);
          ngModelCtrl.$formatters.push(toView);
        },
      };
    },
  ])
  .directive('customFocus', [
    function () {
      var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
      return {
        restrict: 'A', //Angular will only match the directive against attribute names
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
  ])
  .directive('number', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$parsers.push(function (input) {
          if (input == undefined) return '';
          var inputNumber = input.toString().replace(/[^0-9]/g, '');
          if (inputNumber != input) {
            ctrl.$setViewValue(inputNumber);
            ctrl.$render();
          }
          return inputNumber;
        });
      },
    };
  });
