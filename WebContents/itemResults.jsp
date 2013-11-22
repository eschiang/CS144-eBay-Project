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
    <title><%= item.i_name %></title>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script type="text/javascript">
        var geocoder;

        function initialize() {
            geocoder = new google.maps.Geocoder();
            var latlng = new google.maps.LatLng(0.0, 0.0);
             var myOptions = { 
      zoom: 4, // default is 8  
      center: latlng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP 
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
        myOptions);
            
            var address = "<%= seller.getLocation() %>, <%= seller.getCountry() %>";
            geocoder.geocode( { 'address': address},
            function(results, status)
            {
              if (status == google.maps.GeocoderStatus.OK)
              {
                map.setCenter(results[0].geometry.location);
                map.fitBounds(results[0].geometry.viewport);
                map.setZoom(8);
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location});
              }
              else {
                  geocoder.geocode( { 'address': "<%= "USA" %>"},
                  function(results, status)
                  {
                    if (status == google.maps.GeocoderStatus.OK)
                    {
                        map.setCenter(results[0].geometry.location);
                        map.fitBounds(results[0].geometry.viewport);
                        var marker = new google.maps.Marker({
                            map: map,
                            position: results[0].geometry.location});
                    }
                  })
                }
            });
            
        }
        </script>
    </head>

    <body onload="initialize()">
        <form action="./item" method="GET">
            Item ID <input type="text" name="id">
            <input type="submit" value="Get Item">
        </form><br/>
        <h1><%= item.i_name %></h1>
         <div id="map_canvas" style="width: 50%; height: 400px"></div>
        <br/>
        <hr>
        <a href="http://www.w3schools.com">Visit W3Schools</a>

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
