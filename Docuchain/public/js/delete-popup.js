// angular.module('dapp').factory('DeletePopup', function () {
//     return {
//         deleteConfirm: function (title, message, callback) {
//             Swal.fire({
//                 html: `<div class="logout-card">
//                         <div class="logout-icon delete-icon">
//                             <i class="fa fa-trash"></i>
//                       </div>
//                         <h3>${title}</h3>
//                         <p>${message}</p>
//                       </div>`,
//                 width: 480,
//                 showCancelButton: true,
//                 confirmButtonText: 'Delete',
//                 cancelButtonText: 'Cancel',
//                 confirmButtonColor: '#e55353'
//             }).then((result) => {
//                 if (result.isConfirmed) {
//                     callback();
//                }
//             });
//         }
//     };
// });

angular.module('dapp').factory('DeletePopup', function () {

    return {
        confirm: function (title, message, callback) {

            Swal.fire({
                html: `<div class="logout-card">
                        <div class="logout-icon delete-icon">
                            <i class="fa fa-trash"></i>
                        </div>
                        <h3>${title}</h3>
                        <p>${message}</p>
                      </div>`,
                width: 480,
                showCancelButton: true,
                confirmButtonText: 'Delete',
                cancelButtonText: 'Cancel',
                confirmButtonColor: '#e55353'
            }).then((result) => {

                if (result.isConfirmed) {
                    callback();
                }

            });

        }
    };

});