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
		<%
		 int numSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
        int numReturn = Integer.parseInt(request.getAttribute("numResultsToReturn").toString());
        String query = (String) request.getAttribute("query");
  		if (numSkip > 0)
        {
                %> <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip-10 %>&numResultsToReturn=10">Previous Page</a>
                <%
        }
        
        SearchResult[] results = (SearchResult[]) request.getAttribute("results");
        
        if (results.length - 10 >= 0)
        {
                %> <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip+10 %>&numResultsToReturn=10">Next Page</a>
                <%
        }
        %>
		


		<% 
				
			for(int i = 0; i < results.length; i++) { %>
			<li><a href="/eBay/item?id=<%= results[i].getItemId()%>"><%= results[i].getName() %></a></li>

			<% } %>
		</ul>


	</body>
</html>
