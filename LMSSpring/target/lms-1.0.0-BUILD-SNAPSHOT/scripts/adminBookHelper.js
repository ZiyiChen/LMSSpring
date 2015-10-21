/**
 * TODO add current page check;
 */
var pageSize = 13;
var loadCreateOnce = false;
var loadEditOnce = false;
var currentPage = 1;
var entries = 0;
/**
 * functions for Book
 * 
 */

function listBooks(searchText, pageNo, pageSize) {
	$.ajax({
		method:"POST",
		url:"listBooksPage",
		data:{
			searchText: searchText,
			pageNo: pageNo,
			pageSize : pageSize
		}
	}).done(function (data) {
		var dataString = "";
		var source = $("#bookEntryTemplate").html();
		var template = Handlebars.compile(source);
		data = $.parseJSON(data);
		entries = 0;
		currentPage = pageNo;
		$.each(data, function(id, item){
			var html = template (item);
			dataString += html;
			entries++;
		});
		$("#bookTbody").html(dataString);
	});
	$.ajax({
		method:"POST",
		url:"countBook",
		data:{searchText: searchText}
	}).done(function (data) {
		var noPage = Math.ceil(data/pageSize);
		var dataString = "";
		var source = $("#pageEntryTemplate").html();
		var template = Handlebars.compile(source);
		for(i = 1; i <= noPage; i++){
			var cass = "";
			if (i == pageNo)
				cass = "active";
			var pg = {pageNo : i, pageSize : pageSize, cass : cass, method : 'listBooks'};
			var html = template(pg);
			dataString += html;
		}
		$("#pages").html(dataString);
	});
}

function addBook () {
	$.ajax({
		  method: "POST",
		  url: "addBook",
		  data: { bookTitle: $("#crtBkTitle").val(),
			  bookPublisher: $("#crtBkPubSel").val(),
			  addedAuthors: $("#crtBkAuthSel").val(),
			  addedGenres: $("#crtBkGenSel").val()}
	}).done(function(msg) {
		if (!msg) 
			showSuccess("Add Book Success");
		else
			showWarning(msg);
		listBooks($('#searchText').val(), currentPage, pageSize);
	});
}

function updateBook () {
	$.ajax({
		  method: "POST",
		  url: "updateBook",
		  data: {bookId: $("#edBkId").val(),
			  bookTitle: $("#edBkTitle").val(),
			  bookPublisher: $("#edBkPubSel").val(),
			  updatedAuthors: $("#edBkAuthSel").val(),
			  updatedGenres: $("#edBkGenSel").val()}
	}).done(function( msg ) {
		if (!msg) 
			showSuccess("Update Book Success");
		else
			showWarning(msg);
		listBooks($('#searchText').val(), currentPage, pageSize);
	});
}

function createBookModal() {
	if(!loadCreateOnce) {
		getValidPublisher($('#crtBkPubSel'));
		getValidAuthor($('#crtBkAuthSel'));
		getValidGenre($('#crtBkGenSel'));
		loadCreateOnce = true;
	}
	$('#createBookModal').modal();
}

function getValidPublisher (selectionElement) {
	$.ajax({
		method:"POST",
		url:"listPublishers"
	}).done(function (data) {
		var pubArr = $.parseJSON(data);
		$.each(pubArr, function(index, item) {
			selectionElement.append($('<option>', { 
		        value: item.publisherId,
		        text : item.publisherName
		    }));
			selectionElement.selectpicker('refresh');
		});
	});
}

function getValidAuthor (selectionElement) {
	$.ajax({
		method:"POST",
		url:"listAuthors"
	}).done(function (data) {
		var authArr = $.parseJSON(data);
		$.each(authArr, function(index, item) {
			selectionElement.append($('<option>', { 
		        value: item.authorId,
		        text : item.authorName
		    }));
			selectionElement.selectpicker('refresh');
		});
	});
}

function getValidGenre (selectionElement) {
	$.ajax({
		method:"POST",
		url:"listGenres"
	}).done(function (data) {
		var genArr = $.parseJSON(data);
		$.each(genArr, function(index, item) {
			selectionElement.append($('<option>', { 
		        value: item.genreId,
		        text : item.genreName
		    }));
			selectionElement.selectpicker('refresh');
		});
	});
}

function deleteBookModal (id, title) {
	$('#delBkMsg').html("Are you sure you want to delete: \"<em>"+title+"</em>\" ?");
	$('#deBkId').val(id);
	$('#deleteBookModal').modal();
}

function destoryBook() {
	$.ajax({
		method:"POST",
		url:"deleteBook",
		data: { bookId: $('#deBkId').val()}
	}).done(function (msg) {
		if (!msg) 
			showSuccess("Delete Book Success");
		else
			showWarning(msg);
		if(--entries < 1)
			currentPage = currentPage > 1 ? currentPage - 1 : 1;
		
		listBooks($('#searchText').val(), currentPage, pageSize);
	});
}

function editBook(id) {
	if (!loadEditOnce){
		getValidPublisher($('#edBkPubSel'));
		getValidAuthor($('#edBkAuthSel'));
		getValidGenre($('#edBkGenSel'));
		loadEditOnce = true;
	}
	selectBookProperty(id);
	$('#editBookModal').modal();
}

function selectBookProperty (bkId) {
	var bk;
	$.ajax({
		method:"POST",
		url:"getBookById",
		data: { bookId: bkId}
	}).done(function (data) {
		bk = $.parseJSON(data);
		$("#edBkId").val(bkId);
		$("#edBkTitle").val(bk.title);
		if (bk.genres != null) {
			var addedGens = [];
			$.each(bk.genres, function(index, item) {
				addedGens.push(item.genreId);
			});
			$("#edBkGenSel option").each(function(){
				if($.inArray(parseInt($(this).val(), 10), addedGens) > -1){
					$(this).attr('selected', 'selected');
					$("#edBkGenSel").selectpicker('refresh');
				}
			});
			$("#edBkGenSel").selectpicker('refresh');
		}
		if (bk.authors) {
			var addedBks = [];
			$.each(bk.authors, function(index, item) {
				addedBks.push(item.authorId);
			});
			$("#edBkAuthSel option").each(function(){
				if($.inArray(parseInt($(this).val(),10), addedBks) > -1){
					$(this).attr('selected', 'selected');
				}
			});
			$("#edBkAuthSel").selectpicker('refresh');
		}
		if (bk.publisher) {
			var addedPub = bk.publisher;
			$("#edBkPubSel option").each(function(){
				if($(this).val() == addedPub.publisherId){
					$(this).attr('selected', 'selected');
				}
			});
			$("#edBkPubSel").selectpicker('refresh');
		}
	});
}

/**
 * functions run when page loaded
 */

$(function(){
	$('#createBookModal').on('hidden.bs.modal', function () {
		$(this).find("input,textarea,select").val('');
		$('.selectpicker').selectpicker('refresh');
	});
	listBooks($('#searchText').val(), 1, pageSize);
});