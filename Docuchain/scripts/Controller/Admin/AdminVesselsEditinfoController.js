var adminVesselsadd = angular.module('dapp.AdminVesselsEditinfoController', [
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
  .controller('AdminVesselsEditinfoController', [
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
      $scope.userProfileId = $window.localStorage.getItem('userId');
      $scope.userId = $window.localStorage.getItem('userId');
      $scope.organizationId = $window.localStorage.getItem('organizationId');

      $scope.vesselsList = [];
      $scope.commercialManagerInfoList = [];
      $scope.techManagerInfoList = [];
      $rootScope.countryName;
      $rootScope.stateName;

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
      $scope.loader = false;
      $scope.vesselModel = true;
      $scope.vesselUser = false;
      $scope.vesselTick = false;
      $scope.allPlaceHolderListCustom = [];
      $scope.allPlaceHolderListEdited = [];
      $scope.allPlaceHolderListStandard = [];
      $scope.selectedCustom = [];
      $scope.selectedStandard = [];
      $scope.selectecustom = [];
      $scope.selecteStandard = [];
      $scope.roleList = {};
      $scope.changeCustomplace = [];
      $scope.editvesselId = $window.localStorage.getItem('editId');

      if (!$scope.editvesselId) {
        toaster.error({ title: 'Vessel ID missing. Please reopen vessel.' });
        $state.go('dapp.adminVessels');
        return;
      }

      $scope.editcountryNameJson = $window.localStorage.getItem('countryName');
      $scope.editstateNameJson = $window.localStorage.getItem('stateName');
      $scope.editcountryName = JSON.parse($scope.editcountryNameJson);
      $scope.editstateName = JSON.parse($scope.editstateNameJson);

      $scope.vesselTick = false;

      $scope.userData = {};
      $scope.countryInfos = [];
      $scope.countryId = [];
      $scope.portInfos = [];
      $scope.countryName;
      $scope.vesselsTypeInfos = [];
      $scope.shipPic;
      $scope.shipMasterInfos = [];
      $rootScope.editcountryId;
      $rootScope.countryIdstate = '';
      $scope.shipUserId;
      $scope.selectedTechdetails;
      $scope.selectedComdetails;
      $scope.vesselsId;
      $scope.shipMasterName = 'Ship Master';
      $scope.technicalmasterName = 'Technical';
      $scope.commercialmasterName = 'Commercial';
      $scope.roleAliasList;
      $scope.roleId;
      $scope.selectedUsers = [{}];
      $scope.allPlaceHolderListEdit = $rootScope.allPlaceHolderList;

      callCommercial();
      callTech();
      callShipmaster();
      init();

      function init() {
        $scope.loader = true;
        loadCustomPlaceholders()
          .then(loadEditedPlaceholders)
          .then(loadStandardPlaceholders)
          .then(loadRoles)
          .then(loadVessels)
          .then(loadRoleList)
          .finally(function () {
            $scope.loader = false;
          });
      }

      function loadCustomPlaceholders() {
        var dataid = {
          userId: $scope.userProfileId,
          type: 'Custom',
          vesselid: $scope.editvesselId,
        };

        return FunctionalityService.getPlaceHolderListByorganizatinId(
          dataid
        ).then(
          function (response) {
            if (response.status == 201 || response.status == 200) {
              $scope.allPlaceHolderListCustom =
                response.data.documentHolderList || [];
              $scope.selectedCustom = [];
              angular.forEach($scope.allPlaceHolderListCustom, function (val) {
                $scope.selectedCustom.push(val);
              });
              $scope.selectecustom = $scope.selectedCustom;
            } else {
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function (err) {
            console.log('Error response', err);
          }
        );
      }

      function loadEditedPlaceholders() {
        var dataid = { userId: $scope.userProfileId };

        return FunctionalityService.getPlaceHolderListByorganizatinId(
          dataid
        ).then(
          function (response) {
            if (response.status == 201 || response.status == 200) {
              $scope.allPlaceHolderListEdited =
                response.data.documentHolderList || [];
              if ($scope.allPlaceHolderListEdited.length === 0) {
                toaster.clear();
                toaster.info({ title: 'No records found' });
              }
            } else {
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function (err) {
            console.log('Error response', err);
          }
        );
      }

      function loadStandardPlaceholders() {
        var dataid = {
          userId: $scope.userProfileId,
          type: 'Standard',
          vesselid: $scope.editvesselId,
        };

        return FunctionalityService.getPlaceHolderListByorganizatinId(
          dataid
        ).then(
          function (response) {
            if (response.status == 201 || response.status == 200) {
              $scope.allPlaceHolderListStandard =
                response.data.documentHolderList || [];
              $scope.selectedStandard = [];

              angular.forEach(
                $scope.allPlaceHolderListStandard,
                function (val) {
                  angular.forEach(
                    $scope.allPlaceHolderListEdited,
                    function (edit) {
                      if (val.documentHolderName === edit.documentHolderName) {
                        $scope.selectedStandard.push(edit);
                      }
                    }
                  );
                }
              );
              $scope.selecteStandard = $scope.selectedStandard;
            } else {
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function (err) {
            console.log('Error response', err);
          }
        );
      }

      function loadRoles() {
        return FunctionalityService.getListRolesName($scope.userProfileId).then(
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
      }

      function loadVessels() {
        return FunctionalityService.getVesselsNameList(
          $scope.userProfileId
        ).then(
          function (response) {
            if (response.status == 200) {
              $scope.users = response.data.shipProfileList;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      }

      function loadRoleList() {
        return FunctionalityService.getRoleList($scope.userProfileId).then(
          function (response) {
            if (response.status == 200) {
              $scope.roleList = response.data.roleAliasInfos;
            }
          },
          function (error) {
            console.log('message :: ' + error);
          }
        );
      }

      $scope.auth = {};
      $scope.addNewUser = function (businessCategory) {
        $scope.businessCategory = businessCategory;
      };

      $scope.addUser = function (user) {
        $scope.loader = true;

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
          roleId: $scope.roleId,
          businessCategory: $scope.businessCategory,
          mail: user.mail,
          organizationId: $scope.organizationId,
          loginId: $scope.userId,
          shipProfileIds: $scope.userInfoId,
        };
        FunctionalityService.addUser(userInfo, $scope.file).then(
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
        $scope.thumbnail.dataUrll = 'image/avatarimg.jpg';
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
                if (
                  value.id == $scope.editvesselId &&
                  value.countryName == $scope.editcountryName &&
                  value.stateName == $scope.editstateName
                ) {
                  $scope.userData = value;

                  if (value.keelLaidString) {
                    $scope.userData.keelLaidString = moment(
                      value.keelLaidString,
                      'YYYY-MM-DD'
                    );
                  }
                  // if (value.delivered) {
                  //   $scope.userData.delivered = moment(
                  //     value.delivered,
                  //     'DD-MM-YYYY'
                  //   );
                  // }
                  if (value.delivered) {
                    $scope.userData.delivered = moment(value.delivered);
                  }

                  $scope.vesselsId = $scope.userData.id;
                  $scope.thumbnail.dataUrl = $scope.userData.shipProfilePicPath;
                  $scope.commercialManagerInfoList.push(
                    value.commercialManagerInfoList
                  );
                  $scope.techManagerInfoList.push(value.techManagerInfoList);
                }
              });
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
              port($rootScope.editcountryId);
            }
            $scope.countryId.push(id.countryId);
          });
        } else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      });

      function port(editcountryId) {
        FunctionalityService.getPort(editcountryId).then(function (response) {
          if (response.status == 201 || response.status == 200) {
            $scope.portInfos = response.data.portInfos;
            console.log($scope.portInfos);
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
          console.log('if');
        } else {
          $scope.vesselUser = true;
          $scope.vesselTick = true;
          console.log('else');
        }
      };

      $scope.prevTovesselInfo = function () {
        $scope.vesselUser = false;
        $scope.vesselTick = false;
      };

      function callCommercial() {
        FunctionalityService.getCommercial($scope.userId).then(
          function (response) {
            $scope.commercialManagerInfos =
              response.data.commercialManagerInfos;
          }
        );
      }

      function callTech() {
        FunctionalityService.getTech($scope.userId).then(function (response) {
          $scope.technicalManagerInfos = response.data.technicalManagerInfos;
        });
      }

      function callShipmaster() {
        FunctionalityService.getShipmaster($scope.userId).then(
          function (response) {
            $scope.shipMasterInfos = response.data.shipMasterInfos;
          }
        );
      }

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

      $scope.thumbnail = {};
      $scope.fileReaderSupported = window.FileReader != null;

      $scope.uploadFileUser = function (files) {
        if (files != null) {
          var file = files[0];
          $scope.shipPic = file;
          if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
            $timeout(function () {
              var fileReader = new FileReader();
              fileReader.readAsDataURL(file);
              fileReader.onload = function (e) {
                $timeout(function () {
                  $scope.thumbnail.dataUrll = e.target.result;
                });
              };
            });
          }
        }
      };

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

      $scope.changeselected = function (data) {
        $scope.selecteStandardChanges = data;
      };

      $scope.changeCustom = function (data) {
        $scope.selecteCustomChanges = data;
        $scope.selectecustom = $scope.selecteCustomChanges;
      };

      $scope.editShipSave = function (shipDetails) {
        $scope.loader = true;
        $scope.docIdsEdit = [];
        if ($rootScope.errorFile === 'Picture should be below 1MB') {
          toaster.pop('error', $rootScope.errorFile);
        } else {
          $rootScope.errorFile = '';
          var string = shipDetails.countryName,
            substring = '{';
          var countryJSON = string.includes(substring);
          if (countryJSON == true) {
            $scope.jsonCountry = JSON.parse(shipDetails.countryName);
            $scope.countryName = $scope.jsonCountry.countryName;
          } else {
            $scope.countryName = shipDetails.countryName;
          }

          $scope.techManagerIds = [];
          $scope.commercialMasterIds = [];

          if (shipDetails.techManagerInfoList != null) {
            angular.forEach(shipDetails.techManagerInfoList, function (value) {
              $scope.techManagerIds.push(value.userId);
            });
          }

          if (shipDetails.commercialManagerInfoList != null) {
            angular.forEach(
              shipDetails.commercialManagerInfoList,
              function (value) {
                $scope.commercialMasterIds.push(value.userId);
              }
            );
          }

          if (shipDetails.shiMasterInfo != null) {
            $scope.shipUserId = shipDetails.shiMasterInfo.userId;
          }
          angular.forEach($scope.allPlaceHolderListCustom, function (oldobj) {
            var condition = 0;
            angular.forEach($scope.changeCustomplace, function (item) {
              if (item.documentHolderId == oldobj.documentHolderId) {
                condition = 1;
              }
            });
            if (condition == 0) {
              $scope.docIdsEdit.push(oldobj.documentHolderId);
            }
          });

          $scope.selectecustomArr = [];
          $scope.selecteStandardArr = [];
          if ($scope.selecteCustomChanges == undefined)
            $scope.selecteCustomChanges = $scope.selectecustom;

          angular.forEach($scope.selecteCustomChanges, function (oldobj) {
            var obj = {
              documentHolderName: oldobj.documentHolderName,
              documentHolderDescription: oldobj.documentHolderDescription,
              documentHolderId: oldobj.documentHolderId,
              documentHolderType: oldobj.documentHolderType,
              organizationName: oldobj.organizationName,
              type: oldobj.type,
            };
            $scope.selectecustomArr.push(obj);
          });

          if ($scope.selecteStandardChanges == undefined)
            $scope.selecteStandardChanges = $scope.selecteStandard;

          angular.forEach($scope.selecteStandardChanges, function (oldobj) {
            var obj = {
              documentHolderName: oldobj.documentHolderName,
              documentHolderDescription: oldobj.documentHolderDescription,
              documentHolderId: oldobj.documentHolderId,
              documentHolderType: oldobj.documentHolderType,
              organizationName: oldobj.organizationName,
              type: oldobj.type,
            };
            $scope.selecteStandardArr.push(obj);
          });

          var data = {
            id: $scope.vesselsId,
            bhp: shipDetails.bhp,
            builder: shipDetails.builder,
            callSign: shipDetails.callSign,
            countryName: $scope.countryName,
            // delivered: shipDetails.delivered,
            engineType: shipDetails.engineType,
            imo: shipDetails.imo,
            internationalGRT: shipDetails.internationalGRT,
            internationalNRT: shipDetails.internationalNRT,
            // // keelLaid: shipDetails.keelLaidString,
            // keelLaid: shipDetails.keelLaidString
            //   ? moment(shipDetails.keelLaidString).format('DD-MM-YYYY')
            //   : null,
            // delivered: shipDetails.delivered
            //   ? moment(shipDetails.delivered).format('DD-MM-YYYY')
            //   : null,
            keelLaid: shipDetails.keelLaidString
              ? moment(shipDetails.keelLaidString).toISOString()
              : null,
            delivered: shipDetails.delivered
              ? moment(shipDetails.delivered).toISOString()
              : null,
            registeredOwner: shipDetails.registeredOwner,

            shipName: shipDetails.shipName,
            shipTypes: shipDetails.shipTypes,
            stateName: shipDetails.stateName,
            dwt: shipDetails.dwt,
            weight: shipDetails.weight,
            length: shipDetails.length,
            breadth: shipDetails.breadth,
            loginId: $scope.userId,
            commercialMasterIds: $scope.commercialMasterIds,
            techManagerIds: $scope.techManagerIds,
            shipMasterId: $scope.shipUserId,
            status: 1,
            organizationId: $scope.organizationId,
            docIds: $scope.docIdsEdit,
            customDocumentHolders: $scope.selectecustomArr,
            standardDocumentHolders: $scope.selecteStandardArr,
          };

          FunctionalityService.editShip(data, $scope.shipPic).then(
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
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        }
      };

      $scope.editShipSaveOne = function (shipDetails) {
        $scope.loader = true;

        if ($rootScope.errorFile === 'Picture should be below 1MB') {
          toaster.pop('error', $rootScope.errorFile);
        } else {
          $rootScope.errorFile = '';
          var data = {
            id: $scope.vesselsId,
            bhp: shipDetails.bhp,
            builder: shipDetails.builder,
            callSign: shipDetails.callSign,
            countryName: shipDetails.countryName,
            delivered: shipDetails.delivered,
            engineType: shipDetails.engineType,
            imo: shipDetails.imo,
            internationalGRT: shipDetails.internationalGRT,
            internationalNRT: shipDetails.internationalNRT,
            keelLaid: shipDetails.keelLaidString,
            registeredOwner: shipDetails.registeredOwner,
            shipName: shipDetails.shipName,
            shipTypes: shipDetails.shipTypes,
            stateName: shipDetails.stateName,
            dwt: shipDetails.dwt,
            weight: shipDetails.weight,
            length: shipDetails.length,
            breadth: shipDetails.breadth,
            loginId: $scope.userId,
            status: 1,
            organizationId: $scope.organizationId,
          };

          FunctionalityService.editShip(data, $scope.shipPic).then(
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
              }
            },
            function myError(err) {
              $scope.loader = false;
              console.log('Error response', err);
            }
          );
        }
      };

      $scope.vesselUserShow = function (tickValid) {
        $('#addplaceholder').modal('hide');
        $scope.vesselplaceholderDiv = false;
        if (Object.keys(tickValid).length === 0) {
          $scope.vesselUser = true;
          $scope.vesselTick = false;
        } else {
          $scope.vesselUser = true;
          $scope.vesselTick = true;
        }
      };

      $scope.vesselplaceholder = function (tickValid) {
        if (Object.keys(tickValid).length === 0) {
          $scope.vesselModel = false;
          $scope.vesselplaceholderDiv = true;
          $scope.vesselTick = false;
        } else {
          $scope.vesselModel = false;
          $scope.vesselplaceholderDiv = true;
          $scope.vesselTick = true;
        }
      };

      $scope.prevTovesselInfo = function () {
        $scope.vesselUser = false;
        $scope.vesselTick = false;
        $scope.vesselModel = true;
        $('#addplaceholder').modal('hide');
        $scope.vesselplaceholderDiv = false;
      };

      $scope.prevTovesselInfoLast = function () {
        $scope.vesselUser = false;
        $scope.vesselTick = false;
        $scope.vesselplaceholderDiv = true;
        $('#addplaceholder').modal('hide');
      };

      $scope.delete;
      $scope.getDeletePlaceHolder = function (deleteData) {
        $scope.delete = deleteData;
      };

      $scope.deletePlaceHolder = function () {
        $scope.loader = true;

        var deletePlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderId: $scope.delete.documentHolderId,
        };
        FunctionalityService.deletePlaceHolder(deletePlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $('#delete').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 1000);
            } else {
              $('#delete').modal('hide');
              $state.reload();
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.updatePlaceHolder;
      $scope.getEditPlaceHolder = function (update) {
        $scope.updatePlaceHolder = update;
      };

      $scope.editPlaceHolder = function () {
        $scope.loader = true;

        var updatePlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderId: $scope.updatePlaceHolder.documentHolderId,
          documentHolderName: $scope.updatePlaceHolder.documentHolderName,
          documentHolderDescription:
            $scope.updatePlaceHolder.documentHolderDescription,
          documentFileNumber: $scope.updatePlaceHolder.documentFileNumber,
          organizationName: $scope.updatePlaceHolder.organizationName,
        };
        FunctionalityService.editPlaceHolder(updatePlaceHolderData).then(
          function mySuccess(response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              $('#updateplaceholder').modal('hide');
              $state.reload();
              $timeout(function () {
                toaster.clear();
                toaster.success({ title: response.data.message });
              }, 1000);
            } else {
              $('#updateplaceholder').modal('hide');
              $state.reload();
              toaster.clear();
              toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      // --- Select/Clear All for Standard Placeholders (EDIT PAGE) ---
      $scope.selectAllStandardEdit = function () {
        // Copies all items from the master list into the selected model
        $scope.selecteStandard = angular.copy($scope.allPlaceHolderListEdited);

        // Trigger the change function so the array updates
        if ($scope.changeselected) {
          $scope.changeselected($scope.selecteStandard);
        }
      };

      $scope.clearAllStandardEdit = function () {
        // Empties the selection
        $scope.selecteStandard = [];

        // Trigger the change function
        if ($scope.changeselected) {
          $scope.changeselected($scope.selecteStandard);
        }
      };

      // --- Select/Clear All for Custom Placeholders (EDIT PAGE) ---
      $scope.selectAllCustom = function () {
        // Copy the full list into the selected model
        $scope.selectecustom = angular.copy($scope.allPlaceHolderListCustom);

        // Trigger the change function
        if ($scope.changeCustom) {
          $scope.changeCustom($scope.selectecustom);
        }
      };

      $scope.clearAllCustom = function () {
        // Empty the selected model
        $scope.selectecustom = [];

        // Trigger the change function
        if ($scope.changeCustom) {
          $scope.changeCustom($scope.selectecustom);
        }
      };

      $scope.allPlaceHolderListArr = [];
      $scope.addplaceholderEdit = function () {
        var addPlaceHolderData = {
          userId: $scope.userProfileId,
          documentHolderName: $scope.placeholdername,
          documentHolderDescription: $scope.placeholderdescription,
          organizationName: $scope.organizationName,
          type: 'Custom',
        };

        $scope.selectecustom.push(addPlaceHolderData);
        $('#addplaceholderEdit').modal('hide');
        $scope.placeholdername = '';
        $scope.placeholderdescription = '';
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
