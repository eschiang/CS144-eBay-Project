<%@ page import="edu.ucla.cs.cs144.*" %>
<html>	
	<head>	
  		<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="./css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="./css/main.css">
        <script src="./js/jquery1-10-1.js"></script>
        <script src="./js/bootstrap.min.js"></script>		
        <title> Purchase Purchase Purchase!!!</title> 
	</head>	
	<body>
		<div class="container">
			<div class="row">
				<% if (request.getAttribute("valid").equals("false")) { %>
					<div class="jumbotron">
						<h1 class="text-center"> Invalid </h1>
						
					</div>


				<% } else { %>
					<% Item item = (Item) request.getAttribute("item"); %>
					<div class="row">
			            <div class="col-lg-10 col-lg-offset-1">
			                <div class="jumbotron">
			                    <h1 class="text-center">Time to Buy some Good Stuff!!</h1>
			                    <tr>
												<th> Item Name :</th>
												<td> <%= item.i_name %> </td>
											</tr>
											<br>
											<tr>
												<th> Description :</th>
												<td> <%= item.i_description %> </td>
											</tr>
											<br>
											<tr>
												<th> Payment Amount :</th>
												<td> <%= item.i_buyprice%> </td>
											</tr>
											<br>
											<th> Credit Card Number </th>

			                    <div class="col-lg-10 col-lg-offset-1">
			                    <!-- @Josh : This is yet to be done. the credit card is posted to confirmation page which already uses ssl//8443. Make sure you have installed ssl on catalina and stuff before you move forward
			                    -->
			                        <form class="form-inline" role="form" action="https://<%= request.getServerName() %>:8443<%= request.getContextPath() %>/confirmation" method="POST">


			                            <div class="form-group">
			                                <input class="form-control" id="ccnumber" name="card" type="text"/>  
			                            </div>     
			                           
			                            <button type="submit" class="btn btn-lg btn-primary">Place it</button>
			                            <div id="search-container">
			                            </div>
			                        </form>
											

			                    </div>
			                    <br>
			                    <br>
			                     <br>
			                    <br>
			                     <br>
			                    <br>
			                     <br>
			                    <br>
			                     <br>
			                    <br>

			                </div>
			            </div>
			        </div>
	













					
				<% } %>
			</div>
		</div>
	</body>	
</html>
