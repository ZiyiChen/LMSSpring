var lmsModule = angular.module('lmsApp', [ 'ngRoute', 'ui.bootstrap', "isteven-multi-select"]);

//CONFIG
lmsModule.config([ "$routeProvider", function($routeProvider) {
	return $routeProvider.when("/", {
		redirectTo : "/index"
	}).when("/index", {
		templateUrl : "home.html"
	}).when("/listBooks", {
		templateUrl : "listBooksTemplate.html",
		controller: "listBooksCtrl"
	});
} ]);

//CONTROLLER
lmsModule.controller('listBooksCtrl', ['$scope', '$http', '$uibModal', function ($scope, $http, $uibModal) {
	$scope.currentPage = 1;
	$scope.maxSize = 10;
	$scope.pageSize = 13;
	$scope.totalBooks = 13;
	$scope.searchText = '';

	$scope.showBooks = function () {
		$http({
			method: 'GET',
			url: 'listBooksPage/'+$scope.currentPage+'/'+$scope.pageSize,
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.books = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});

		$http({
			method: 'GET',
			url: 'countBook/',
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.totalBooks = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});
	}
	$scope.showBooks();

	$scope.showCreateBookModal = function () {
		$uibModal.open({
			templateUrl: 'template/createBookTemplate.html',
			controller: 'createBookCtrl',
			size: 'lg'
		});
	}

	$scope.createBook = function () {
		$scope.createBookModal = true;
	};

	$scope.$watch('searchText', function(newValue, oldValue) {
		$scope.showBooks();
	});

	$scope.$watch('currentPage', function(newValue, oldValue) {
		$scope.showBooks();
	});

	$scope.showEditBookModal = function (book) {
		$uibModal.open({
			templateUrl: 'template/editBookTemplate.html',
			controller: 'editBookCtrl',
			size: 'lg',
			resolve: {
				book : function () {
					return book;
				}
			}
		});
	}
}]);

lmsModule.controller('createBookCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
	$http({
		method: 'GET',
		url: 'listPublishers'
	}).then(function successCallback(response) {
		$scope.publishers = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listAuthors'
	}).then (function successCallBack(response) {
		$scope.authors = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listGenres'
	}).then (function successCallBack(response) {
		$scope.genres = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});


	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.add = function () {
		var auths = [];
		var gens = [];
		var pub = !$scope.selectedPublisher[0] ? null : {
			"publisherId": $scope.selectedPublisher[0].publisherId
		};
		angular.forEach($scope.selectedAuthros, function(value, key) {
			this.push({"authorId": value.authorId});
		}, auths);
		angular.forEach($scope.selectedGenres, function(value, key) {
			this.push({"genreId": value.genreId});
		}, gens);
		var bk = {
				"title": $scope.title,
				"publisher": pub,
				"genres": gens,
				"authors": auths
		};

		console.log(bk);
		$http({
			method: 'POST',
			url: 'addBook',
			data: bk
		}).success(function(data) {
			console.log('success');
		});
		$modalInstance.dismiss('cancel');
	}
}]);

lmsModule.controller('editBookCtrl', ['$scope', '$modalInstance', '$http', 'book', function ($scope, $modalInstance, $http, book) {
	$http({
		method: 'GET',
		url: 'listPublishers'
	}).then(function successCallback(response) {
		$scope.publishers = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listAuthors'
	}).then (function successCallBack(response) {
		$scope.authors = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listGenres'
	}).then (function successCallBack(response) {
		$scope.genres = response.data;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.update = function () {
		var auths = [];
		var gens = [];
		var pub = !$scope.selectedPublisher[0] ? null : {
			"publisherId": $scope.selectedPublisher[0].publisherId
		};
		angular.forEach($scope.selectedAuthros, function(value, key) {
			this.push({"authorId": value.authorId});
		}, auths);
		angular.forEach($scope.selectedGenres, function(value, key) {
			this.push({"genreId": value.genreId});
		}, gens);
		var bk = {
				"title": $scope.title,
				"publisher": pub,
				"genres": gens,
				"authors": auths
		};

		console.log(bk);
		$http({
			method: 'POST',
			url: 'addBook',
			data: bk
		}).then (function successCallBack(response) {
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});
		$modalInstance.dismiss('cancel');
	}
}]);

//DIRECTIVE
lmsModule.directive('createBookModal', ['$http', function($http) {
	return {
		restrict: 'E',
		scope: {

		},
		trmplateUrl: 'template/createBookTemplate.html',
//		link: function ($scope, element, attrs) {
//		$http({
//		method: 'GET',
//		url: 'listPublishers'
//		}).then(function successCallback(response) {
//		$scope.publishers = response.data;
//		console.log('success');
//		}, function errorCallback(response) {
//		console.log(response.data);
//		});

//		$http({
//		method: 'GET',
//		url: 'listAuthors'
//		}).then (function successCallBack(response) {
//		$scope.authors = response.data;
//		console.log('success');
//		}, function errorCallback(response) {
//		console.log(response.data);
//		});

//		$http({
//		method: 'GET',
//		url: 'listGenres'
//		}).then (function successCallBack(response) {
//		$scope.genres = response.data;
//		console.log('success');
//		}, function errorCallback(response) {
//		console.log(response.data);
//		});
//		$scope.addBook = function () {
//		angular.forEach($scope.selectedAuths, function(value, key) {
//		this.push({"authorId": value.id,
//		"authorName": null,
//		"books": null});
//		}, auths);
//		angular.forEach($scope.selectedGens, function(value, key) {
//		this.push({"genreId": value.id,
//		"genreName": null,
//		"books": null});
//		}, gens);
//		var bk = {
//		"bookId": null,
//		"title": $scope.title,
//		"publisher": {
//		"publisherId": $scope.selectedPublisher.id,
//		"publisherName": null,
//		"address": null,
//		"phone": null,
//		"books": null
//		},
//		"genres": gens,
//		"authors": auths,
//		"copies": null,
//		"loans": null
//		};

//		$http({
//		method: 'POST',
//		url: 'addBook',
//		data: {book : bk}
//		}).then (function successCallBack(response) {
//		console.log('success');
//		}, function errorCallback(response) {
//		console.log(response.data);
//		});
//		}

//		}
	};
}]);