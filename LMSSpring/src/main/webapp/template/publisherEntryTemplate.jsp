<script id="publisherEntryTemplate" type="text/x-handlebars-template">
  <tr>
    <td>{{publisherName}}</td>
	<td>{{address}}</td>
	<td>{{phone}}</td>
	<td><button type="button" class="btn btn-primary" onclick="javascript:editPublisher('{{publisherId}}','{{publisherName}}','{{address}}','{{phone}}');">Edit</button></td>
	<td><button type="button" class="btn btn-danger" onclick="javascript:deletePublisherModal('{{publisherId}}','{{publisherName}}');">Delete</button></td>
  </tr>
</script>