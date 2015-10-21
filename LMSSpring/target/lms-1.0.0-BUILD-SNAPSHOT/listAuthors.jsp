<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ include file="fixedTopNavbar.jsp" %>
<%@ include file="dashboardTemplate.jsp" %>
<%@ include file="template/authorEntryTemplate.jsp" %>
<%@ include file="template/pageEntryTemplate.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Author List</title>
</head>
<body>
	<div class="modal fade" id="deleteAuthorModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Delete Author</h4>
				</div>
				<div class="modal-body">
					<div id="delAuMsg"> Are you sure you want to delete this author?</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="deAuId">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-danger" onclick="javascript:destoryAuthor();" data-dismiss="modal">Yes</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="modal fade" id="editAuthorModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Author</h4>
				</div>
				<div class="modal-body">
					<form name="editAuthorForm" action="editAuthor" method="post">
						<input type="hidden" id="edAuId">
						Author Name: <input type="text" name="name" class="form-control" id="edAuName"/> 
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="javascript:updateAuthor();" data-dismiss="modal">Update</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="modal fade" id="createAuthorModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create Author</h4>
				</div>
				<div class="modal-body">
					<form name="addAuthorForm" action="addAuthor" method="post">
						New Author Name: <input type="text" name="name" class="form-control" id="crtAuName"/> 
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="javascript:addAuthor();" data-dismiss="modal">Add</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<div class="table-responsive">
	<div class="col-md-2 col-xs-9 nopadding"><input type="text" class="form-control" placeholder="Enter text to search" id="searchText" onkeyup="javascript:listAuthors($('#searchText').val(), 1, pageSize);"></div>
	<div align="right">
	<button type="button" class="btn btn-info" data-toggle="modal" onclick="createAuthorModal();">
          <span class="glyphicon glyphicon-plus-sign"></span> Create New Author
    </button>
    </div>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Author Name</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody id="authorTbody">

		</tbody>
	</table>
	</div>
	<div align="middle">
        <ul class="pagination" id="pages">
        
        </ul>
    </div>
    </div>
</body>
<script src="./scripts/adminAuthorHelper.js"></script>
<script src="./scripts/bootstrap-notify.min.js"></script>
</html>