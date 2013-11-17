<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ page import="java.util.*" %>
<% 

            Item item = (Item) request.getAttribute("item");
            User seller = (User) request.getAttribute("seller");
            SortedSet catSet =  (SortedSet) request.getAttribute("categories");
            Iterator categories = catSet.iterator();
            SortedSet bidSet =  (SortedSet) request.getAttribute("bids");
            Iterator bids = bidSet.iterator();
%>
<!DOCTYPE html>
<html>
    <head>
    </head>

    <body>
        <form action="./item" method="GET">
            Item ID <input type="text" name="id">
            <input type="submit" value="Get Item">
        </form><br/>
        <h1><%= item.i_name %></h1>
        <hr>
        <h4>Tags</h4>
        <ul>
            <% while (categories.hasNext()) { %>
                <li><%= categories.next().toString() %></li>
            <% } %>
        </ul>

        <h4>Item Info</h4>
        <ul>
            <li>Description: <%= item.i_description %></li>
            <li>Seller: <%= seller.u_name %></li>
            <li>Seller Rating: <%= seller.u_rating %></li>
            <li>Currently: $<%= item.i_currently %></li>
            <li>Buy Now: $<%= item.i_buyprice %></li>
            <li>Number of Bids: <%= item.i_numberofbids %></li>
            <li>First: <%= item.i_firstbid %></li>
            <li>Started: <%= item.i_started %></li>
            <li>Ends: <%= item.i_ends %></li>
        </ul>

        <h4>Bidding History</h4>
        <ul>
        <% while (bids.hasNext()) { %>
            <li><%= bids.next().toString() %></li>
        <% } %> 
        </ul>
        
    </body>
</html>
