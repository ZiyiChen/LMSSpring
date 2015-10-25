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

	var getBooksCountByPublishers = function () {
		$http({
			method: 'GET',
			url: 'countBooksByPublishers'
		}).then(function successCallback(response) {
			$scope.booksCountByPublisher = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});
	}

	getBooksCountByPublishers();
	
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
		var createBookInstance = $uibModal.open({
			templateUrl: 'template/createBookTemplate.html',
			controller: 'createBookCtrl',
			size: 'lg'
		});
		createBookInstance.result.then(function (msg) {
			$scope.showBooks();
			getBooksCountByPublishers();
		}, function () {

		});
	}

	$scope.$watch('searchText', function(newValue, oldValue) {
		$scope.showBooks();
	});

	$scope.$watch('currentPage', function(newValue, oldValue) {
		$scope.showBooks();
	});

	$scope.showEditBookModal = function (book) {
		var editBookInstance = $uibModal.open({
			templateUrl: 'template/editBookTemplate.html',
			controller: 'editBookCtrl',
			size: 'lg',
			resolve: {
				book : function () {
					return book;
				}
			}
		});
		editBookInstance.result.then(function (msg) {
			getBooksCountByPublishers();
			$scope.showBooks();
		}, function () {

		});
	}
}]);

lmsModule.controller('editBookCtrl', ['$scope', '$modalInstance', '$http', 'book', function ($scope, $modalInstance, $http, book) {

	$scope.title = book.title;
	$http({
		method: 'GET',
		url: 'listPublishers'
	}).then(function successCallback(response) {
		var datas = response.data;
		var res = [];
		angular.forEach(datas, function(data, key) {
			if (book.publisher) {
				if (data.publisherId == book.publisher.publisherId) {
					data.ticked = true;
				}
			}
			this.push(data);
		}, res);
		$scope.publishers = res;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listAuthors'
	}).then (function successCallBack(response) {
		var datas = response.data;
		var res = [];
		angular.forEach(datas, function(data, key) {
			for (i = 0 ; i < book.authors.length; i ++) {
				if (book.authors[i].authorId == data.authorId) {
					data.ticked = true;
				}
			}
			this.push(data);
		}, res);
		$scope.authors = res;
		console.log('success');
	}, function errorCallback(response) {
		console.log(response.data);
	});

	$http({
		method: 'GET',
		url: 'listGenres'
	}).then (function successCallBack(response) {
		var datas = response.data;
		var res = [];
		angular.forEach(datas, function(data, key) {
			for (i = 0 ; i < book.genres.length; i ++) {
				if (book.genres[i].genreId == data.genreId) {
					data.ticked = true;
				}
			}
			this.push(data);
		}, res);
		$scope.genres = res;
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
				"bookId": book.bookId,
				"title": $scope.title,
				"publisher": pub,
				"genres": gens,
				"authors": auths
		};

		$http({
			method: 'POST',
			url: 'updateBook',
			data: bk
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('updated');
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
			$modalInstance.close('added');
		});
	}
}]);

//DIRECTIVE
lmsModule.directive ('countBooksChart', ['$window', function($window) {
	return {
		restrict: 'E',
		templateUrl: 'template/bookChartTemplate.html',
		link: function(scope, element, attrs) {
			console.log(attrs.data);
			console.log(scope.booksCountByPublisher);
			
			var d3 = $window.d3;
			var rawSvg=element.find('svg');
			var svg = d3.select(rawSvg[0]);

			var padding = parseInt(attrs.padding) || 20,
			barPadding = parseInt(attrs.barPadding) || 0.1;

			scope.$watch(function() {
				return scope.booksCountByPublisher;
			}, function() {
				redrawChart();
			});
			
			$window.onresize = function() {
				scope.$apply();
			};
			scope.$watch(function() {
				return angular.element($window)[0].innerWidth;
			}, function() {
				redrawChart();
			});

			var redrawChart = function () {
				svg.selectAll('*').remove();
				if (!scope.booksCountByPublisher) return;

				var es = d3.select(element[0]).node();
				var ek = d3.select(element[0]).node().offsetParent.offsetWidth;
				var width = d3.select(element[0]).node().offsetWidth,
				height = 200;

				var xScale = d3.scale.ordinal()
				.domain(scope.booksCountByPublisher.map(function(d) { return d.publisherName; }))
				.rangeBands([padding, width - 2 * padding], barPadding);

				var yScale = d3.scale.linear()
				.domain([0, d3.max(scope.booksCountByPublisher, function(d) {return d.count;})])
				.range([height - padding, padding]);

				var xAxis = d3.svg.axis()
				.scale(xScale)
				.orient("bottom");

				var yAxis = d3.svg.axis()
				.scale(yScale)
				.orient("left")
				.ticks(5);

				svg.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + (height - padding) + ")")
				.call(xAxis);

				svg.append("g")
				.attr("class", "y axis")
				.attr("transform", "translate(" + padding + ", 0)")
				.call(yAxis);

				svg.selectAll(".bar")
				.data(scope.booksCountByPublisher)
				.enter()
				.append("rect")
				.attr("class", "bar")
				.attr("x", function(d) {return xScale(d.publisherName);})
				.attr("width", xScale.rangeBand())
				.attr("y", function(d) {return yScale(d.count);})
				.attr("height", function(d) { return height - yScale(d.count) - padding;});
			}
			redrawChart();
		}
	};
}]);


//lmsModule.directive('createBookModal', ['$http', function($http) {
//return {
//restrict: 'E',
//scope: {

//},
//templateUrl: 'template/createBookTemplate.html',
//link: function ($scope, element, attrs) {
//$http({
//method: 'GET',
//url: 'listPublishers'
//}).then(function successCallback(response) {
//$scope.publishers = response.data;
//console.log('success');
//}, function errorCallback(response) {
//console.log(response.data);
//});

//$http({
//method: 'GET',
//url: 'listAuthors'
//}).then (function successCallBack(response) {
//$scope.authors = response.data;
//console.log('success');
//}, function errorCallback(response) {
//console.log(response.data);
//});

//$http({
//method: 'GET',
//url: 'listGenres'
//}).then (function successCallBack(response) {
//$scope.genres = response.data;
//console.log('success');
//}, function errorCallback(response) {
//console.log(response.data);
//});
//$scope.addBook = function () {
//angular.forEach($scope.selectedAuths, function(value, key) {
//this.push({"authorId": value.id,
//"authorName": null,
//"books": null});
//}, auths);
//angular.forEach($scope.selectedGens, function(value, key) {
//this.push({"genreId": value.id,
//"genreName": null,
//"books": null});
//}, gens);
//var bk = {
//"bookId": null,
//"title": $scope.title,
//"publisher": {
//"publisherId": $scope.selectedPublisher.id,
//"publisherName": null,
//"address": null,
//"phone": null,
//"books": null
//},
//"genres": gens,
//"authors": auths,
//"copies": null,
//"loans": null
//};

//$http({
//method: 'POST',
//url: 'addBook',
//data: {book : bk}
//}).then (function successCallBack(response) {
//console.log('success');
//}, function errorCallback(response) {
//console.log(response.data);
//});
//}

//}
//};
//}]);