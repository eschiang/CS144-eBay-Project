<%@ page import="edu.ucla.cs.cs144.*" %>
<!DOCTYPE html>
<html>
	<head>
	</head>

	<body>
		<ul>
		<% SearchResult[] results = (SearchResult[]) request.getAttribute("results");
			Integer numResults = (Integer) request.getAttribute("numResults");
			for(int i = 0; i < 10; i++) { %>
			<li><a href="/items?id=<%= results[i].getItemId()%>"><%= results[i].getName() %></a></li>

			<% } %>
		</ul>
	</body>
</html>
