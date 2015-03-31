wccApp.factory('Start', function($resource) {
	return $resource('wcc/rest/start', {}, {});
});
wccApp.factory('PostalCode', function($resource) {
	return $resource('wcc/rest/postalCodes/:id', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		},
		'get' : {
			method : 'GET',
			transformResponse : function(data) {
				data = angular.fromJson(data);
				return data;
			}
		},
		'update' : {
			method : 'PUT'
		}
	});
});

wccApp.factory('RequestProvider', [ '$http', function($http) {
	return {
		execute : function(method, url, params) {
			var promise = $http({
				url : url,
				method : method,
				params : params
			});
			return promise
		}
	}
} ]);

wccApp.factory('Session', function() {
	this.create = function(firstName, username, permissions) {
		this.firstName = firstName;
		this.username = username;
		this.permissions = permissions;
	};
	this.invalidate = function() {
		this.firstName = null;
		this.username = null;
		this.permissions = null;
	};
	return this;
});

wccApp
		.factory(
				'AuthenticationSharedService', //
				function($rootScope, $http, authService, Session,
						Base64Service, AccessToken, Start) {
					return {
						login : function(param) {
							var data = "username="
									+ param.username
									+ "&password="
									+ param.password
									+ "&grant_type=password&scope=read%20write&client_secret=wccAppOauthAuthenticationSecret&client_id=wccApp";
							$http
									.post(
											'oauth/token',
											data,
											{
												headers : {
													"Content-Type" : "application/x-www-form-urlencoded",
													"Accept" : "application/json",
													"Authorization" : "Basic "
															+ Base64Service
																	.encode("wccApp"
																			+ ':'
																			+ "wccAppOauthAuthenticationSecret")
												},
												ignoreAuthModule : 'ignoreAuthModule'
											})
									.success(
											function(data, status, headers,
													config) {
												httpHeaders.common['Authorization'] = 'Bearer '
														+ data.access_token;
												AccessToken.set(data);
												Start
														.get(function(data) {
															Session
																	.create(
																			data.name,
																			data.username,
																			data.permissions);
															$rootScope.session = Session;
															authService
																	.loginConfirmed(data);
														});
											})
									.error(
											function(data, status, headers,
													config) {
												console.log(data);
												$rootScope.authenticated = false;
												$rootScope.authenticationError = true;
												Session.invalidate();
												AccessToken.remove();
												delete httpHeaders.common['Authorization'];
												$rootScope
														.$broadcast(
																'event:auth-loginRequired',
																data);
											});
						},
						valid : function(authorizedRoles) {
							if (AccessToken.get() !== null) {
								httpHeaders.common['Authorization'] = 'Bearer '
										+ AccessToken.get();
							}

							$http
									.get(
											'protected/authentication_check.gif',
											{
												ignoreAuthModule : 'ignoreAuthModule'
											})
									.success(
											function(data, status, headers,
													config) {
												if (!Session.username
														|| AccessToken.get() != undefined) {
													if (AccessToken.get() == undefined
															|| AccessToken
																	.expired()) {
														$rootScope
																.$broadcast("event:auth-loginRequired");
														return;
													}
													Start
															.get(function(data) {
																Session
																		.create(
																				data.firstName,
																				data.username,
																				data.permissions);
																$rootScope.session = Session;
																if (!$rootScope
																		.isAuthorized(authorizedRoles)) {
																	// user is
																	// not
																	// allowed
																	$rootScope
																			.$broadcast("event:auth-notAuthorized");
																} else {
																	$rootScope
																			.$broadcast("event:auth-loginConfirmed");
																}
															});
												} else {
													if (!$rootScope
															.isAuthorized(authorizedRoles)) {
														// user is not allowed
														$rootScope
																.$broadcast("event:auth-notAuthorized");
													} else {
														$rootScope
																.$broadcast("event:auth-loginConfirmed");
													}
												}
											})
									.error(
											function(data, status, headers,
													config) {
												if (!$rootScope
														.isAuthorized(authorizedRoles)) {
													$rootScope
															.$broadcast(
																	'event:auth-loginRequired',
																	data);
												}
											});
						},
						isAuthorized : function(authorizedRoles) {
							if (!angular.isArray(authorizedRoles)) {
								if (authorizedRoles == '*') {
									return true;
								}
								authorizedRoles = [ authorizedRoles ];
							}

							var isAuthorized = false;
							angular
									.forEach(
											authorizedRoles,
											function(authorizedRole) {
												var authorized = (!!Session.username && Session.permissions
														.indexOf(authorizedRole) !== -1);

												if (authorized
														|| authorizedRole == '*') {
													isAuthorized = true;
												}
											});

							return isAuthorized;
						},
						logout : function() {
							$rootScope.authenticationError = false;
							$rootScope.authenticated = false;
							$rootScope.session = null;
							AccessToken.remove();

							$http.get('wcc/logout');
							Session.invalidate();
							delete httpHeaders.common['Authorization'];
							authService.loginCancelled();
						}
					};
				});

wccApp.service('ParseLinks', function() {
	this.parse = function(header) {
		if (header.length == 0) {
			throw new Error("input must not be of zero length");
		}

		// Split parts by comma
		var parts = header.split(',');
		var links = {};
		// Parse each part into a named link
		angular.forEach(parts, function(p) {
			var section = p.split(';');
			if (section.length != 2) {
				throw new Error("section could not be split on ';'");
			}
			var url = section[0].replace(/<(.*)>/, '$1').trim();
			var queryString = {};
			url.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0,
					$1, $2, $3) {
				queryString[$1] = $3;
			});
			var page = queryString['page'];
			if (angular.isString(page)) {
				page = parseInt(page);
			}
			var name = section[1].replace(/rel="(.*)"/, '$1').trim();
			links[name] = page;
		});

		return links;
	}
});
