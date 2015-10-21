var pageSize = 13;
var currentPage = 1;
var entries = 0;
/**
 * functions for author
 * 
 */

function listAuthors(searchText, pageNo, pageSize) {
	$.ajax({
		method:"POST",
		url:"listAuthorsPage",
		data:{
			searchText: searchText,
			pageNo: pageNo,
			pageSize : pageSize
		}
	}).done(function (data) {
		var dataString = "";
		var source = $("#authorEntryTemplate").html();
		var template = Handlebars.compile(source);
		data = $.parseJSON(data);
		entries = 0;
		currentPage = pageNo;
		$.each(data, function(id, item){
			var html = template (item);
			dataString += html;
			entries++;
		});
		$("#authorTbody").html(dataString);
	});
	$.ajax({
		method:"POST",
		url:"countAuthor",
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
			var pg = {pageNo : i, pageSize : pageSize, cass : cass, method : 'listAuthors'};
			var html = template(pg);
			dataString += html;
		}
		$("#pages").html(dataString);
	});
}

function addAuthor () {
	$.ajax({
		  method: "POST",
		  url: "addAuthor",
		  data: {authorName: $("#crtAuName").val()}
	}).done(function(msg) {
		if (!msg) 
			showSuccess("Add Author Success");
		else
			showWarning(msg);
		listAuthors($('#searchText').val(), currentPage, pageSize);
	});
}

function updateAuthor () {
	$.ajax({
		  method: "POST",
		  url: "updateAuthor",
		  data: {authorId: $("#edAuId").val(),
			  authorName: $("#edAuName").val()}
	}).done(function( msg ) {
		if (!msg) 
			showSuccess("Update Author Success");
		else
			showWarning(msg);
		listAuthors($('#searchText').val(), currentPage, pageSize);
	});
}

function createAuthorModal() {
	$('#createAuthorModal').modal();
}


function deleteAuthorModal (id, name) {
	$('#delAuMsg').html("Are you sure you want to delete: \"<em>"+name+"</em>\" ?");
	$('#deAuId').val(id);
	$('#deleteAuthorModal').modal();
}

function destoryAuthor() {
	var c = $("#deAuId").val();
	$.ajax({
		method:"POST",
		url:"deleteAuthor",
		data: { authorId: $('#deAuId').val()}
	}).done(function (msg) {
		if (!msg) 
			showSuccess("Delete Author Success");
		else
			showWarning(msg);
		if(--entries < 1)
			currentPage = currentPage > 1 ? currentPage - 1 : 1;
		listAuthors($('#searchText').val(), currentPage, pageSize);
	});
}

function editAuthor(id, name) {
	$('#edAuName').val(name);
	$('#edAuId').val(id);
	$('#editAuthorModal').modal();
}

/**
 * functions run when load
 */

$(function(){
	$('#createAuthorModal').on('hidden.bs.modal', function () {
		$(this).find("input,textarea").val('');
	});
	listAuthors($('#searchText').val(), 1, pageSize);
});