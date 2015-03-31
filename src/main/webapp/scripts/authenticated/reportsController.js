wccApp.controller('ReportsController', [ '$scope', 'RequestProvider',//
function($scope, RequestProvider) {
	$scope.report = {};
	RequestProvider.execute('POST', 'wcc/rest/reports', {
		top : 5
	}).success(function(data, status, headers, config) {
		console.log(data);
		$scope.report = data;
	}).error(function(data, status) {
		console.log(data);
		alert(data.message);
		if (status == 401) {
			$location.path('/').replace();
		}
	});

} ]);