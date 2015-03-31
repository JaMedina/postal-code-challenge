wccApp.controller('MainController', [ '$scope', '$rootScope', 'AuthenticationSharedService',//
function($scope, $rootScope, AuthenticationSharedService) {	
	$scope.logout = function() {
		console.log('Clossing Session');
		AuthenticationSharedService.logout();
	}
} ]);