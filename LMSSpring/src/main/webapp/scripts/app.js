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
	}).when("/listAuthors", {
		templateUrl : "listAuthorsTemplate.html",
		controller: "listAuthorsCtrl"
	}).when("/listPublishers", {
		templateUrl : "listPublishersTemplate.html",
		controller: "listPublishersCtrl"
	});
} ]);

//CONTROLLER
//book controllers
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

	$scope.showDeleteBookModal = function (book) {
		var deleBookInstance = $uibModal.open({
			templateUrl: 'template/deleteBookTemplate.html',
			controller: 'deleBookCtrl',
			resolve: {
				book : function () {
					return book;
				}
			}
		});
		deleBookInstance.result.then(function (msg) {
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

lmsModule.controller('deleBookCtrl', ['$scope', '$modalInstance', '$http', 'book', function ($scope, $modalInstance, $http, book) {

	$scope.title = book.title;

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.destory = function () {
		$http({
			method: 'POST',
			url: 'deleteBook',
			data: book
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('deleted');
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

//author controllers
lmsModule.controller('listAuthorsCtrl', ['$scope', '$http', '$uibModal', function ($scope, $http, $uibModal) {
	$scope.currentPage = 1;
	$scope.maxSize = 10;
	$scope.pageSize = 13;
	$scope.totalAuthors = 13;
	$scope.searchText = '';

	$scope.showAuthors = function () {
		$http({
			method: 'GET',
			url: 'listAuthorsPage/'+$scope.currentPage+'/'+$scope.pageSize,
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.authors = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});

		$http({
			method: 'GET',
			url: 'countAuthor/',
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.totalAuthors = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});
	}
	$scope.showAuthors();

	$scope.showCreateAuthorModal = function () {
		var createAuthorInstance = $uibModal.open({
			templateUrl: 'template/createAuthorTemplate.html',
			controller: 'createAuthorCtrl'
		});
		createAuthorInstance.result.then(function (msg) {
			$scope.showAuthors();
		}, function () {

		});
	}

	$scope.$watch('searchText', function(newValue, oldValue) {
		$scope.showAuthors();
	});

	$scope.$watch('currentPage', function(newValue, oldValue) {
		$scope.showAuthors();
	});

	$scope.showEditAuthorModal = function (author) {
		var editAuthorInstance = $uibModal.open({
			templateUrl: 'template/editAuthorTemplate.html',
			controller: 'editAuthorCtrl',
			resolve: {
				author : function () {
					return author;
				}
			}
		});
		editAuthorInstance.result.then(function (msg) {
			$scope.showAuthors();
		}, function () {

		});
	}

	$scope.showDeleteAuthorModal = function (author) {
		var deleAuthorInstance = $uibModal.open({
			templateUrl: 'template/deleteAuthorTemplate.html',
			controller: 'deleAuthorCtrl',
			resolve: {
				author : function () {
					return author;
				}
			}
		});
		deleAuthorInstance.result.then(function (msg) {
			$scope.showAuthors();
		}, function () {

		});
	}
}]);

lmsModule.controller('editAuthorCtrl', ['$scope', '$modalInstance', '$http', 'author', function ($scope, $modalInstance, $http, author) {

	$scope.name = author.authorName;

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.update = function () {
		author.authorName = $scope.name;

		$http({
			method: 'POST',
			url: 'updateAuthor',
			data: author
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('updated');
		});
	}
}]);

lmsModule.controller('deleAuthorCtrl', ['$scope', '$modalInstance', '$http', 'author', function ($scope, $modalInstance, $http, author) {

	$scope.name = author.authorName;

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.destory = function () {
		$http({
			method: 'POST',
			url: 'deleteAuthor',
			data: author
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('deleted');
		});
	}
}]);


lmsModule.controller('createAuthorCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.add = function () {
		var author = {
				"authorName": $scope.name
		};
		$http({
			method: 'POST',
			url: 'addAuthor',
			data: author
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('added');
		});
	}
}]);

//publisher controllers
lmsModule.controller('listPublishersCtrl', ['$scope', '$http', '$uibModal', function ($scope, $http, $uibModal) {
	$scope.currentPage = 1;
	$scope.maxSize = 10;
	$scope.pageSize = 13;
	$scope.totalPublishers = 13;
	$scope.searchText = '';

	$scope.showPublishers = function () {
		$http({
			method: 'GET',
			url: 'listPublishersPage/'+$scope.currentPage+'/'+$scope.pageSize,
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.publishers = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});

		$http({
			method: 'GET',
			url: 'countPublisher/',
			params:{
				searchText: $scope.searchText
			}
		}).then(function successCallback(response) {
			$scope.totalPublishers = response.data;
			console.log('success');
		}, function errorCallback(response) {
			console.log(response.data);
		});
	}
	$scope.showPublishers();

	$scope.showCreatePublisherModal = function () {
		var createPublisherInstance = $uibModal.open({
			templateUrl: 'template/createPublisherTemplate.html',
			controller: 'createPublisherCtrl'
		});
		createPublisherInstance.result.then(function (msg) {
			$scope.showPublishers();
		}, function () {

		});
	}

	$scope.$watch('searchText', function(newValue, oldValue) {
		$scope.showPublishers();
	});

	$scope.$watch('currentPage', function(newValue, oldValue) {
		$scope.showPublishers();
	});

	$scope.showEditPublisherModal = function (publisher) {
		var editPublisherInstance = $uibModal.open({
			templateUrl: 'template/editPublisherTemplate.html',
			controller: 'editPublisherCtrl',
			resolve: {
				publisher : function () {
					return publisher;
				}
			}
		});
		editPublisherInstance.result.then(function (msg) {
			$scope.showPublishers();
		}, function () {

		});
	}

	$scope.showDeletePublisherModal = function (publisher) {
		var delePublisherInstance = $uibModal.open({
			templateUrl: 'template/deletePublisherTemplate.html',
			controller: 'delePublisherCtrl',
			resolve: {
				publisher : function () {
					return publisher;
				}
			}
		});
		delePublisherInstance.result.then(function (msg) {
			$scope.showPublishers();
		}, function () {

		});
	}
}]);

lmsModule.controller('editPublisherCtrl', ['$scope', '$modalInstance', '$http', 'publisher', function ($scope, $modalInstance, $http, publisher) {

	$scope.name = publisher.publisherName;
	$scope.address = publisher.address;
	$scope.phone = publisher.phone;

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.update = function () {
		publisher.publisherName = $scope.name;
		publisher.address = $scope.address;
		publisher.phone = $scope.phone;
		$http({
			method: 'POST',
			url: 'updatePublisher',
			data: publisher
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('updated');
		});
	}
}]);

lmsModule.controller('delePublisherCtrl', ['$scope', '$modalInstance', '$http', 'publisher', function ($scope, $modalInstance, $http, publisher) {

	$scope.name = publisher.publisherName;

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.destory = function () {
		$http({
			method: 'POST',
			url: 'deletePublisher',
			data: publisher
		}).success(function(data) {
			console.log('success');
			$modalInstance.close('deleted');
		});
	}
}]);


lmsModule.controller('createPublisherCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.add = function () {
		var publisher = {
				"publisherName": $scope.name,
				"address": $scope.address,
				"phone": $scope.phone
		};
		$http({
			method: 'POST',
			url: 'addPublisher',
			data: publisher
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
		scope:{data: '='},
		templateUrl: 'template/bookChartTemplate.html',
		link: function(scope, element, attrs) {

			scope.$watch('data', function(data) {
				console.log(scope.data);
				redrawChart();
			})

			var d3 = $window.d3;
			var rawSvg=element.find('svg');
			var svg = d3.select(rawSvg[0]);

			var padding = parseInt(attrs.padding) || 20,
			barPadding = parseInt(attrs.barPadding) || 0.1;

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
				svg.attr("class", null);
				if (!scope.data) return;
				
				svg.attr("class", "chart");
				var width = d3.select(element[0]).node().offsetWidth,
				height = 200;
				
				var xScale = d3.scale.ordinal()
				.domain(scope.data.map(function(d) { return d.publisherName; }))
				.rangeBands([padding, width - 2 * padding], barPadding);

				var yScale = d3.scale.linear()
				.domain([0, d3.max(scope.data, function(d) {return d.count;})])
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
				.data(scope.data)
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