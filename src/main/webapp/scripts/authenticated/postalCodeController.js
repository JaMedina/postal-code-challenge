wccApp.controller('PostalCodeController', ['$scope', 'PostalCode', 'ParseLinks',//
function($scope, PostalCode, ParseLinks){
  $scope.postalCodes = [];
  $scope.page = 1;
  $scope.loadAll = function() {
      PostalCode.query({page: $scope.page, per_page: 15}, function(result, headers) {
          $scope.links = ParseLinks.parse(headers('link'));
          $scope.postalCodes = result;
      });
  };
  $scope.loadPage = function(page) {
      $scope.page = page;
      $scope.loadAll();
  };
  $scope.loadAll();

  $scope.create = function () {
      PostalCode.update($scope.postalCode,
          function () {
              $scope.loadAll();
              $('#savePostalCodeModal').modal('hide');
              $scope.clear();
          });
  };

  $scope.update = function (id) {
      PostalCode.get({id: id}, function(result) {
          $scope.postalCode = result;
          $('#savePostalCodeModal').modal('show');
      });
  };

  $scope.delete = function (id) {
      PostalCode.get({id: id}, function(result) {
          $scope.postalCode = result;
          $('#deletePostalCodeConfirmation').modal('show');
      });
  };

  $scope.confirmDelete = function (id) {
      PostalCode.delete({id: id},
          function () {
              $scope.loadAll();
              $('#deletePostalCodeConfirmation').modal('hide');
              $scope.clear();
          });
  };

  $scope.clear = function () {
      $scope.postalCode = {postalCode: null, latitude: null, longitude: null, id: null};
      $scope.editForm.$setPristine();
      $scope.editForm.$setUntouched();
  };
}]);