var lmsModule = angular.module('lmsApp', [ 'ngRoute']);

var pageSize = 13;

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
lmsModule.controller('listBooksCtrl', ['$scope', '$http', function ($scope, $http) {
	$scope.pageNo = 1;
	$scope.searchText = '';
	$scope.showBooks = function () {
//		$http({
//			method: 'GET',
//			url: 'listBooksPage/'+$scope.pageNo+'/'+pageSize,
//			params:{
//				searchText: $scope.searchText
//			}
//		}).then(function successCallback(data) {
//			$scope.books = data;
//			console.log('success');
//		}, function errorCallback(data) {
//			console.log(data);
//		});
		console.log('listBooksPage/'+$scope.pageNo+'/'+pageSize);
		$http
		.get('/listBooks')
		.success(function (data,status) {
			$scope.books = data
		});
	}
}]);