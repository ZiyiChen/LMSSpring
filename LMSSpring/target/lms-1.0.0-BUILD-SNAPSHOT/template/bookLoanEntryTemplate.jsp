<script id="bookLoanEntryTemplate" type="text/x-handlebars-template">
  <tr>
    <td>{{book.title}}</td>
	<td>{{branch.name}}</td>
	<td>{{borrower.name}}</td>
	<td>{{dateOut}}</td>
	<td>
	{{#unless dateIn}}
		<input type="text" class="form-control" id="{{book.bookId}}-{{branch.branchId}}-{{borrower.cardNo}}"> 
	{{else}}
		{{dueDate}}
	{{/unless}}
	</td>
	<td>{{dateIn}}</td>
  </tr>
</script>