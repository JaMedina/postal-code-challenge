'use strict';
var httpHeaders;
var wccApp = angular.module('wccApp', [ 'ngRoute', 'ngResource',
		'http-auth-interceptor', 'wccAppUtils', 'infinite-scroll' ]);
wccApp.config(function($routeProvider, $httpProvider, ROLES) {
	// Routing Configuration
	httpHeaders = $httpProvider.defaults.headers;
	$routeProvider //
	.when('/login', {
		templateUrl : 'views/public/login.html',
		controller : 'LoginController',
		access : {
			authorizedRoles : [ ROLES.all ]
		}
	}).when('/register', {
		templateUrl : 'views/public/register.html',
		controller : 'RegisterController',
		access : {
			authorizedRoles : [ ROLES.all ]
		}
	}).when('/postalCode', {
		templateUrl : 'views/authenticated/postalcode.html',
		controller : 'PostalCodeController',
		access : {
			authorizedRoles : [ ROLES.user ]
		},
	}).when('/calculateDistance', {
		templateUrl : 'views/authenticated/calculateDistance.html',
		controller : 'CalculateDistanceController',
		access : {
			authorizedRoles : [ ROLES.user ]
		},
	}).when('/reports', {
		templateUrl : 'views/authenticated/reports.html',
		controller : 'ReportsController',
		access : {
			authorizedRoles : [ ROLES.user ]
		},
	}).otherwise({
		templateUrl : 'views/public/main.html',
		access : {
			authorizedRoles : [ ROLES.all ]
		}
	});
});

wccApp.run(function($rootScope, $location, $http, AuthenticationSharedService,
		Session, ROLES) {
	$rootScope.authenticated = false;
	$rootScope.$on('$routeChangeStart', function(event, next) {
		$rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
		$rootScope.userRoles = ROLES;
		AuthenticationSharedService.valid(next.access.authorizedRoles);
	});

	// Call when the the client is confirmed
	$rootScope.$on('event:auth-loginConfirmed', function(data) {
		$rootScope.authenticated = true;
		if ($location.path() === "/login") {
			var search = $location.search();
			if (search.redirect !== undefined) {
				$location.path(search.redirect).search('redirect', null)
						.replace();
			} else {
				$location.path('/').replace();
			}
		}
	});

	// Call when the 401 response is returned by the server
	$rootScope.$on('event:auth-loginRequired', function(rejection) {
		Session.invalidate();
		$rootScope.authenticated = false;
		if ($location.path() !== "/" && $location.path() !== ""
				&& $location.path() !== "/register"
				&& $location.path() !== "/login") {
			var redirect = $location.path();
			$location.path('/login').search('redirect', redirect).replace();
		}
	});

	// Call when the 403 response is returned by the server
	$rootScope.$on('event:auth-notAuthorized', function(rejection) {
		console.log(rejection);
		$rootScope.errorMessage = 'errors.403';
		$location.path('/error').replace();
	});

	// Call when the user logs out
	$rootScope.$on('event:auth-loginCancelled', function() {
		$location.path('');
	});

});