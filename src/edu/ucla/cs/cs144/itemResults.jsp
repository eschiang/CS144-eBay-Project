<%@ page import="edu.ucla.cs.cs144.*" %>
<!DOCTYPE html>
<html>
    <head>
    </head>

    <body>
        <ul>
        <% 
            String nameit = (String) request.getAttribute("name");
            for(int i = 0; i < 10; i++) { %>
            <li><a href="/items?id=<%= nameit%>"><%= nameit %></a></li>

            <% } %>
        </ul>
    </body>
</html>
