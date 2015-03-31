wccApp.controller('LoginController', [ '$scope', 'AuthenticationSharedService', //
function($scope, AuthenticationSharedService) {
	$scope.login = function() {
		AuthenticationSharedService.login({
		    username : $scope.username,
		    password : $scope.password
		});
	}
} ]);