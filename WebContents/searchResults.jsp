<DOCTYPE !html>
<html>
	<head>
	</head>

	<body>
		<ul>
		<% searchResult[] results = request.getAttribute("results");
			int numResults = request.getAttribute("numResults");
			for(int i = 0; i < numResults; i++) { %>
			<li><a href="/items?id=<%= results[i].getItemId()%>"><%= results[i].getName() %></a></li>

			<% } %>
		</ul>
	</body>
</html>
