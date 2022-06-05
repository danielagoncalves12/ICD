<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="error.jsp"%>
<%@page import="bean.Check"%>
<% 
String username = Check.username(request, response);
String name 	= Check.name(request);
String picture  = Check.picture(request);
%>
   
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Página inicial</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"></script>
		
		<style>
			body {
				margin: 0;
				font-family: Arial, Helvetica, sans-serif;
			}

		</style>
	</head>
	<body>
		<header>
		
			<nav class="navbar navbar-expand-lg navbar-dark bg-primary">

				<ul class="navbar-nav">
					<li class="nav-item">
					  	<a class="nav-link" title="Entrada" href="index.jsp">Início</a>
					</li>
				</ul>
				
				<div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
			  		<ul class="navbar-nav">
						<li class="nav-item">
				  			<a class="nav-link" title="Saída" aria-current="page" href="LogoutServlet">Sair</a>
						</li>		
			  		</ul>		  
				</div>  				
			</nav>

		</header>
		
		<br><br>
		<div style="position: fixed; left: 50%; transform: translate(-50%, 0%);">
			<div style="margin:20px; display: table-row">

				<div style="padding-left:100px; padding-right:100px; padding-bottom:50px; width: 100%; height: 340px; display: table-cell; border-radius: 10px; border: 1px solid black; text-align: center;"> 
			    
					<p>Bem-vindo <%=username%> !!</p>
					<p>Nome público: <%=name%></p>
					<br>
					<img src="pictures/<%=picture%>" width=180px height=180px/>
					<br><br><br>
					<a href="exampleJSP.jsp"><button type="button" class="btn btn-outline-primary">Edit Profile</button></a>
					
			    </div>
			    
	         	<div style="padding:50px; text-align: center; width: 100%; display: table-cell;">
	         		
	         		 <form action="FindServlet" method="POST">
	         		 	<input type="hidden" id="username" name="username" value="<%=session.getAttribute("username") %>">
	         		 	<input type="submit" value="Play"/>
	         		 </form>
	         		<!-- <a href="game.jsp"><button type="button" class="btn btn-outline-primary">Play</button></a> -->
	         	</div>
		     
		     </div>
		</div>

		<%
		String pg = request.getParameter("page");
		if (pg != null)
		%><jsp:include page="<%=pg%>"></jsp:include>

	</body>
</html>