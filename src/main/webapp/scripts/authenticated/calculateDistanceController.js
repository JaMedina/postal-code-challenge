wccApp.controller('CalculateDistanceController', [ '$scope', 'PostalCode',
		'RequestProvider', //
		function($scope, PostalCode, RequestProvider) {
			$scope.resulOK = false;
			$scope.calculateDistance = function() {
				RequestProvider.execute('POST', 'wcc/rest/calculate', {
					origin:$scope.origin,
					destination: $scope.destination
				}).success(function(data, status, headers, config) {
					$scope.resulOK = true;
					$scope.result = data;
					$scope.distanceResult = JSON.stringify(data,null,"   ");
				}).error(function(data, status) {
					console.log(data);
					alert(data.message);
					if (status == 401) {
						$location.path('/').replace();
					}
				});
			}
		} ]);