var pageSize = 13;
var currentPage = 1;
var entries = 0;
/**
 * functions for publisher
 * 
 */

function listPublishers(searchText, pageNo, pageSize) {
	$.ajax({
		method:"POST",
		url:"listPublishersPage",
		data:{
			searchText: searchText,
			pageNo: pageNo,
			pageSize : pageSize
		}
	}).done(function (data) {
		var dataString = "";
		var source = $("#publisherEntryTemplate").html();
		var template = Handlebars.compile(source);
		data = $.parseJSON(data);
		entries = 0;
		currentPage = pageNo;
		$.each(data, function(id, item){
			var html = template (item);
			dataString += html;
			entries++;
		});
		$("#publisherTbody").html(dataString);
	});
	$.ajax({
		method:"POST",
		url:"countPublisher",
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
			var pg = {pageNo : i, pageSize : pageSize, cass : cass, method : 'listPublishers'};
			var html = template(pg);
			dataString += html;
		}
		$("#pages").html(dataString);
	});
}

function addPublisher () {
	$.ajax({
		  method: "POST",
		  url: "addPublisher",
		  data: {publisherName: $("#crtPbName").val(),
			  publisherAddress: $("#crtPbAddr").val(),
			  publisherPhone: $("#crtPbPhone").val()}
	}).done(function(msg) {
		if (!msg) 
			showSuccess("Add Publisher Success");
		else
			showWarning(msg);
		listPublishers($('#searchText').val(), currentPage, pageSize);
	});
}

function updatePublisher () {
	$.ajax({
		  method: "POST",
		  url: "updatePublisher",
		  data: {publisherId: $("#edPbId").val(),
			  publisherName: $("#edPbName").val(),
			  publisherAddress: $("#edPbAddr").val(),
			  publisherPhone: $("#edPbPhone").val()}
	}).done(function( msg ) {
		if (!msg) 
			showSuccess("Update Publisher Success");
		else
			showWarning(msg);
		listPublishers($('#searchText').val(), currentPage, pageSize);
	});
}

function createPublisherModal() {
	$('#createPublisherModal').modal();
}


function deletePublisherModal (id, name) {
	$('#delPbMsg').html("Are you sure you want to delete: \"<em>"+name+"</em>\" ?");
	$('#dePbId').val(id);
	$('#deletePublisherModal').modal();
}

function destoryPublisher() {
	var c = $("#dePbId").val();
	$.ajax({
		method:"POST",
		url:"deletePublisher",
		data: { publisherId: $('#dePbId').val()}
	}).done(function (msg) {
		if (!msg) 
			showSuccess("Delete Publisher Success");
		else
			showWarning(msg);
		if(--entries < 1)
			currentPage = currentPage > 1 ? currentPage - 1 : 1;
		listPublishers($('#searchText').val(), currentPage, pageSize);
	});
}

function editPublisher(id, name, addr, phone) {
	$('#edPbName').val(name);
	$('#edPbAddr').val(addr);
	$('#edPbPhone').val(phone);
	$('#edPbId').val(id);
	$('#editPublisherModal').modal();
}

/**
 * functions run when load
 */

$(function(){
	$('#createPublisherModal').on('hidden.bs.modal', function () {
		$(this).find("input,textarea").val('');
	});
	listPublishers($('#searchText').val(), 1, pageSize);
});