<%@ page import="edu.ucla.cs.cs144.*" %>
<!DOCTYPE html>
<html>
	<head>
	</head>

	<body>
		<form action="./search" method="GET">
		Search:  <input name="q" type="text"/>
		      	 <input name="numResultsToSkip" type="hidden" value="0" />
		      	 <input name="numResultsToReturn" type="hidden" value="10" />
		</form>
		<ul>

		<% SearchResult[] results = (SearchResult[]) request.getAttribute("results");
			Integer numResults = (Integer) request.getAttribute("numResults");
			for(int i = 0; i < numResults; i++) { %>
			<li><a href="/eBay/item?id=<%= results[i].getItemId()%>"><%= results[i].getName() %></a></li>

			<% } %>
		</ul>
	</body>
</html>
