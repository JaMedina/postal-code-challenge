wccApp.controller('RegisterController', [ '$scope', 'RequestProvider',//
function($scope, RequestProvider) {
	$scope.user = {};

	$scope.register = function() {
		RequestProvider.execute('POST', 'public/rest/register', {
			name : $scope.user.name,
			username : $scope.user.username,
			password: $scope.user.password
		}).success(function(data, status, headers, config) {
			alert('Your user was created. Now you can login.');
			$scope.clear();
		}).error(function(data, status, headers, config) {
			alert(data.message);
		});
	}

	$scope.clear = function() {
		$scope.user = {
			name : null,
			username : null,
			password : null,
			confirmPassword : null
		};
	};
} ]);
