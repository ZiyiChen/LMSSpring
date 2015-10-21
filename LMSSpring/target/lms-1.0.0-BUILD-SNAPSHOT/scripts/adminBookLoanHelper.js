var pageSize = 13;
var currentPage = 1;
/**
 * functions for author
 * 
 */

function listBookLoans(pageNo, pageSize) {
	$.ajax({
		method:"POST",
		url:"listBookLoansPage",
		data:{
			pageNo: pageNo,
			pageSize : pageSize
		}
	}).done(function (data) {
		var dataString = "";
		var source = $("#bookLoanEntryTemplate").html();
		var template = Handlebars.compile(source);
		data = $.parseJSON(data);
		currentPage = pageNo;
		$("#bookLoanTbody").html("");
		$.each(data, function(id, item){
			var html = template (item);
			$("#bookLoanTbody").append(html);
			if(!item.dateIn) {
				var id = item.book.bookId + '-' + item.branch.branchId + '-' + item.borrower.cardNo;
				var dOut = item.dateOut;
				var dDate = item.dueDate;
				initDatepicker(id, dOut, dDate);
			}
		});
	});
	$.ajax({
		method:"POST",
		url:"countBookLoan"
	}).done(function (data) {
		var noPage = Math.ceil(data/pageSize);
		var dataString = "";
		var source = $("#pageEntryTemplate").html();
		var template = Handlebars.compile(source);
		for(i = 1; i <= noPage; i++){
			var cass = "";
			if (i == pageNo)
				cass = "active";
			var pg = {pageNo : i, pageSize : pageSize, cass : cass, method : 'listBookLoans'};
			var html = template(pg);
			dataString += html;
		}
		$("#pages").html(dataString);
	});
}

function overrideDueDate (edBlBkId, edBlBhId, edBlCdNo, edBlDdate) {
	$.ajax({
		  method: "POST",
		  url: "updateBookLoan",
		  data: {bookId: edBlBkId,
			  cardNo: edBlCdNo,
			  branchId: edBlBhId,
			  dueDate: edBlDdate}
	}).done(function( msg ) {
		if (!msg) 
			showSuccess("Over-ride BookLoan Due Date Success");
		else
			showWarning(msg);
		//listBookLoans(currentPage, pageSize);
	});
}

function initDatepicker (id, dateOut, dueDate) {
	$('#'+id)
	.datepicker({
		language: 'en',
 	    format: 'yyyy-mm-dd',
	    startDate: dateOut,
		autoclose: true})
	.on(
		'hide', function(event) {
   		 	var arr = $(this).attr('id').split('-');
    		overrideDueDate(arr[0],arr[1],arr[2],$(this).val());
	});
	if (dueDate) {
		var d = dueDate.split("-").join("/");;
		$('#'+id).datepicker('setDate', new Date(d));
	}
}

/**
 * functions run when load
 */

$(function(){
	listBookLoans(1, pageSize);
});