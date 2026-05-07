var adminVessels = angular.module('dapp.AdminVesselsController', [
  'angularUtils.directives.dirPagination',
]);
adminVessels.controller('AdminVesselsController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  'toaster',
  'DeletePopup',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    toaster,
    DeletePopup
  ) {
    if (localStorage.length == 0) {
      $location.path('/');
    }
    $scope.vesselsList = [];
    $scope.commercialManagerInfoList = [];
    $scope.techManagerInfoList = [];
    $scope.shipAllList = [];
    $scope.listTech = [];
    $scope.listCom = [];
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.totalTechLength;
    $scope.shipIds = [];

    $scope.userId = $window.localStorage.getItem('userId');
    $scope.maxShipCount = $window.localStorage.getItem('maxShipCount');
    $scope.getId;
    var selecTech;
    var selecShip;
    $scope.loader = false;
    var buttonVessel = document.getElementById('addVesselid');
    var selecCom;

    getList();

    function getList() {
      FunctionalityService.getVessellist($scope.userId).then(
        function (response) {
          if (response.status == 201 || response.status == 200) {
            $scope.vesselsList = response.data.shipProfileList;
            console.log('vessel response', response);
            angular.forEach($scope.vesselsList, function (value) {
              $scope.commercialManagerInfoList.push(
                value.commercialManagerInfoList
              );
              $scope.techManagerInfoList.push(value.techManagerInfoList);
            });

            // $scope.vesselsList.map((value, key) => {
            //     $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
            //     $scope.techManagerInfoList.push(value.techManagerInfoList);
            // })
          } else if (response.status == 206) {
            toaster.pop('error', response.data.message);
          }
        }
      );
    }

    // Clone vessel

    $scope.cloneVessel = function (vessels) {
      console.log('--- CLONE START ---', vessels);
      var foundCountry = $scope.vesselsList.find((v) => v.id === vessels.id);
      var cloneData = {
        originalVesselId: vessels.id, // Store original vessel ID for reference
        shipName: vessels.shipName + ' - COPY',
        imo: vessels.imo,
        callSign: vessels.callSign,
        registeredOwner: vessels.registeredOwner,
        keelLaid: vessels.keelLaidString,
        delivered: vessels.delivered,
        status: true,
        isClone: true,
        countryNameStr: vessels.countryName,
        portNameStr: vessels.stateName,
        vesselTypeNameStr: vessels.shipTypes,
        techManagerIds: vessels.techManagerInfoList
          ? vessels.techManagerInfoList.map((t) => t.userId)
          : [],
        commercialMasterIds: vessels.commercialManagerInfoList
          ? vessels.commercialManagerInfoList.map((c) => c.userId)
          : [],
      };

      console.log('--- CLONE DATA PREPARED ---', cloneData);
      localStorage.setItem('cloneVesselData', JSON.stringify(cloneData));
      $state.go('dapp.adminvesselsAdd');
    };

    $scope.confirmDelete = function (id) {
      DeletePopup.confirm(
        'Delete Vessel',
        'Are you sure you want to delete this vessel?',
        function () {
          $scope.deleteVessel(id);
        }
      );
    };

    $scope.deleteVessel = function (id) {
      $scope.loader = true;
      var vesselPlaceholderMap = JSON.parse(
        localStorage.getItem('vesselPlaceholderMap') || '{}'
      );
      delete vesselPlaceholderMap[id];
      localStorage.setItem(
        'vesselPlaceholderMap',
        JSON.stringify(vesselPlaceholderMap)
      );
      var data = {
        id: id,
        userId: $scope.userId,
      };
      FunctionalityService.deleteOne(data).then(
        function (response) {
          $scope.loader = false;
          if (response.status === 200 || response.status === 201) {
            getList();
            toaster.success({ title: 'Vessel profile deleted successfully' });
          } else if (response.status === 206) {
            toaster.error({ title: response.data.message });
          }
        },
        function (err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.checkVessellimit = function () {
      toaster.clear();
      if ($scope.vesselsList.length >= $scope.maxShipCount) {
        toaster.error('Vessel limit exceeded');
        buttonVessel.disabled = true;
      } else {
        buttonVessel.disabled = false;
        $state.go('dapp.adminvesselsAdd');
      }
    };
    $scope.adminvesselsEdit = function (
      editId,
      countryName,
      stateName,
      customDocumentHolders
    ) {
      $state.go('dapp.adminvesselsEdit');
      $rootScope.editId = editId;
      $rootScope.countryName = countryName;
      $rootScope.allPlaceHolderList = customDocumentHolders;
      $rootScope.stateName = stateName;
      $window.localStorage.setItem('editId', JSON.stringify($rootScope.editId));
      $window.localStorage.setItem(
        'countryName',
        JSON.stringify($rootScope.countryName)
      );
      $window.localStorage.setItem(
        'stateName',
        JSON.stringify($rootScope.stateName)
      );
    };

    $scope.list = {
      vesselsList: [],
    };

    // $scope.checkAll = function (allList) {
    //   if ($scope.checkall) {
    //     $scope.checkall = true;
    //     $scope.shipAllList = allList;
    //     $scope.shipIds = [];
    //   } else {
    //     $scope.checkall = false;
    //     $scope.shipIds = [];
    //   }

    //   angular.forEach($scope.vesselsList, function (each) {
    //     each.checked = $scope.checkall;
    //     $scope.shipIds.push(each.id);
    //   });
    // };
    $scope.checkAll = function (allList) {
      // 1. ALWAYS clear the array completely first
      $scope.shipIds = [];

      if ($scope.checkall === true) {
        // 2. If we are SELECTING ALL
        $scope.shipAllList = allList;
        angular.forEach($scope.vesselsList, function (each) {
          each.checked = true; // Tick the box
          $scope.shipIds.push(each.id); // Add the ID to the array
        });
      } else {
        // 3. If we are DESELECTING ALL
        angular.forEach($scope.vesselsList, function (each) {
          each.checked = false; // Untick the box
        });
        // We do NOT push any IDs here. The array stays completely empty.
      }
    };

    $scope.updateCheckall = function () {
      var userTotal = $scope.vesselsList.length;
      var count = 0;
      $scope.shipIds = [];
      angular.forEach($scope.vesselsList, function (item) {
        if (item.checked) {
          count++;
          $scope.shipIds.push(item.id);
        }
      });

      if (userTotal == count) {
        $scope.checkall = true;
      } else {
        $scope.checkall = false;
      }
    };
    $scope.singleToggle = function (object) {
      $scope.loader = true;

      toaster.clear();
      $scope.shipIds = [];
      $scope.shipIds.push(object.id);
      var data = {
        shipIds: $scope.shipIds,
        userId: $scope.userId,
      };

      if (object.status == 1) {
        FunctionalityService.deactive(data).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              getList();
              toaster.pop('success', 'Vessel profile deactivated successfully');
              $scope.shipIds = [];
              $scope.checkall = false;
            } else if (response.status == 206) {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      } else {
        FunctionalityService.active(data).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
              getList();
              toaster.pop('success', 'Vessel profile activated successfully');
              $scope.shipIds = [];
              $scope.checkall = false;
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
    $scope.openDelete = function (id) {
      $scope.shipIds = id;
    };
    $scope.yesDelete = function () {
      $scope.loader = true;

      // Remove placeholder mapping for this vessel from localStorage
      var vesselPlaceholderMap = JSON.parse(
        localStorage.getItem('vesselPlaceholderMap') || '{}'
      );
      delete vesselPlaceholderMap[$scope.shipIds];
      localStorage.setItem(
        'vesselPlaceholderMap',
        JSON.stringify(vesselPlaceholderMap)
      );

      var data = {
        id: $scope.shipIds,
        userId: $scope.userId,
      };

      FunctionalityService.deleteOne(data).then(function (response) {
        $scope.loader = false;

        if (response.status == 201 || response.status == 200) {
          getList();
          toaster.success({ title: 'Vessel profile deleted successfully' });
          setTimeout(function () {
            $state.reload();
          }, 300);
        } else if (response.status == 206) {
          toaster.error({ title: response.data.message });
        }
      });
    };

    $scope.activeAll = function () {
      $scope.loader = true;

      toaster.clear();
      var data = {
        shipIds: $scope.shipIds,
        userId: $scope.userId,
      };

      FunctionalityService.active(data).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            getList();
            toaster.success({ title: response.data.message });
            $scope.shipIds = [];
            $scope.checkall = false;
          } else if (response.status == 206) {
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.deactiveAll = function () {
      $scope.loader = true;

      toaster.clear();
      var data = {
        shipIds: $scope.shipIds,
        userId: $scope.userId,
      };
      FunctionalityService.deactive(data).then(
        function (response) {
          console.log('deactive response', response);
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            getList();
            toaster.success({ title: response.data.message });
            $scope.shipIds = [];
            $scope.checkall = false;
          } else if (response.status == 206) {
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.deleteAll = function () {
      $scope.loader = true;

      toaster.clear();
      var data = {
        shipIds: $scope.shipIds,
        userId: $scope.userId,
      };
      FunctionalityService.delete(data).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 201 || response.status == 200) {
            getList();
            toaster.success({ title: response.data.message });
            $scope.shipIds = [];
            $scope.checkall = false;
          } else if (response.status == 206) {
            toaster.error({ title: response.data.message });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.confirmRemove = function (
      type,
      userId,
      vesselId,
      isFromModal = false
    ) {
      let title = 'Delete';
      let message = 'Are you sure?';
      let apiCall;

      if (type === 'shipMaster') {
        title = 'Remove Ship Master';
        message = 'Are you sure you want to remove this ship master?';

        apiCall = function () {
          let data = {
            id: vesselId,
            userId: $scope.userId,
            shipMasterId: userId,
          };
          return FunctionalityService.delShipMaster(data);
        };
      } else if (type === 'tech') {
        title = 'Remove Technical Manager';
        message = 'Are you sure you want to remove this technical manager?';

        apiCall = function () {
          let data = {
            id: vesselId,
            userId: $scope.userId,
            techManagerIds: [userId],
          };
          return FunctionalityService.delTech(data);
        };
      } else if (type === 'commercial') {
        title = 'Remove Commercial Manager';
        message = 'Are you sure you want to remove this commercial manager?';

        apiCall = function () {
          let data = {
            id: vesselId,
            userId: $scope.userId,
            commercialMasterIds: [userId],
          };
          return FunctionalityService.delCom(data);
        };
      }

      // 🔹 Swal Confirmation
      DeletePopup.confirm(title, message, function () {
        $scope.loader = true;
        apiCall().then(
          function (response) {
            $scope.loader = false;
            if (response.status === 200 || response.status === 201) {
              getList();
              toaster.success({ title: response.data.message });
              if (isFromModal) {
                $('#techList').modal('hide');
                $('#commercialList').modal('hide');
              }
            } else if (response.status === 206) {
              toaster.error({ title: response.data.message });
            }
          },
          function (err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      });
    };

    $scope.techClick = function (listarray, id) {
      $scope.listTech = listarray;
      $scope.getId = id;
    };

    $scope.comClick = function (listarray, id) {
      $scope.listCom = listarray;
      $scope.getId = id;
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
]);
