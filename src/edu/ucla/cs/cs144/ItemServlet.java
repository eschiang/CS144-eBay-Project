<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
    </head>

    <body>
        <ul>
        <% 

            String name = (String) request.getAttribute("name");
            String description = (String) request.getAttribute("description");
            String userid = (String) request.getAttribute("userid");
            String first_bid = (String) request.getAttribute("first_bid");
            String start_time = (String) request.getAttribute("start_time");
            String end_time = (String) request.getAttribute("end_time");
            SortedSet set =  (SortedSet) request.getAttribute("categories");
            Iterator it = set.iterator();

            while (it.hasNext()) 
            {
                String element = it.next().toString();
                %>
                <li><%= "Category : " + element %></li>
                <% 
            } 
             SortedSet bidset =  (SortedSet) request.getAttribute("bids");
            Iterator it1 = bidset.iterator();

            while (it1.hasNext()) 
            {
                String bid = it1.next().toString();
                %>
                <li><%= "Bid : " + bid %></li>
                <% 
            } 

          %>
            <li><%= "Name : " + name %></li>
            <li><%= "Description : " +description %></li>
            <li><%= "UserId : " + userid %></li>
            <li><%= "First Bid : " + first_bid %></li>
            <li><%= "Start Time : " + start_time %></li>
            <li><%= "End Time : " + end_time %></li>

           

            <%  %>
        </ul>
    </body>
</html>
