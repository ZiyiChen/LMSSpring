<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ include file="fixedTopNavbar.jsp" %>
<%@ include file="dashboardTemplate.jsp" %>
<%@ include file="template/publisherEntryTemplate.jsp" %>
<%@ include file="template/pageEntryTemplate.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Publisher List</title>
</head>
<body>
	<div class="modal fade" id="deletePublisherModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Delete Publisher</h4>
				</div>
				<div class="modal-body">
					<div id="delPbMsg"> Are you sure you want to delete this publisher?</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="dePbId">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-danger" onclick="javascript:destoryPublisher();" data-dismiss="modal">Yes</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="modal fade" id="editPublisherModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Publisher</h4>
				</div>
				<div class="modal-body">
					<form name="editPublisherForm" action="editPublisher" method="post">
						<input type="hidden" id="edPbId">
						Publisher Name: <input type="text" name="name" class="form-control" id="edPbName"/> 
						<br>Publisher Address: <input type="text" name="name" class="form-control" id="edPbAddr"/> 
						<br>Publisher Phone: <input type="text" name="name" class="form-control" id="edPbPhone"/> 
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="javascript:updatePublisher();" data-dismiss="modal">Update</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="modal fade" id="createPublisherModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create Publisher</h4>
				</div>
				<div class="modal-body">
					<form name="addPublisherForm" action="addPublisher" method="post">
						New Publisher Name: <input type="text" name="name" class="form-control" id="crtPbName"/> 
						<br>New Publisher Address: <input type="text" name="name" class="form-control" id="crtPbAddr"/> 
						<br>New Publisher Phone: <input type="text" name="name" class="form-control" id="crtPbPhone"/> 
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="javascript:addPublisher();" data-dismiss="modal">Add</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<div class="table-responsive">
	<div class="col-md-2 col-xs-9 nopadding"><input type="text" class="form-control" placeholder="Enter text to search" id="searchText" onkeyup="javascript:listPublishers($('#searchText').val(), 1, pageSize);"></div>
	<div align="right">
	<button type="button" class="btn btn-info" data-toggle="modal" onclick="createPublisherModal();">
          <span class="glyphicon glyphicon-plus-sign"></span> Create New Publisher
    </button>
    </div>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Publisher Name</th>
            <th>Publisher Address</th>
            <th>Publisher Phone</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody id="publisherTbody">

		</tbody>
	</table>
	</div>
	<div align="middle">
        <ul class="pagination" id="pages">
        
        </ul>
    </div>
    </div>
</body>
<script src="./scripts/adminPublisherHelper.js"></script>
<script src="./scripts/bootstrap-notify.min.js"></script>
</html>