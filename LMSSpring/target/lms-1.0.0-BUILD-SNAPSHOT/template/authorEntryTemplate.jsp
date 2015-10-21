<script id="authorEntryTemplate" type="text/x-handlebars-template">
  <tr>
    <td>{{authorName}}</td>
	<td><button type="button" class="btn btn-primary" onclick="javascript:editAuthor('{{authorId}}','{{authorName}}');">Edit</button></td>
	<td><button type="button" class="btn btn-danger" onclick="javascript:deleteAuthorModal('{{authorId}}','{{authorName}}');">Delete</button></td>
  </tr>
</script>