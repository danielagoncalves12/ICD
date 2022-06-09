<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" import="session.Profile" import="java.awt.Color"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Batalha Naval - Página Inicial</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
<!--===============================================================================================-->
</head>
<body>
	
<% 
String username = Check.username(request, response);
String name 	= Check.name(request);
String color	= Check.color(request);
String picture  = Check.picture(request);

String rgbColor = Profile.hex2Rgb(color);
%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px">
			
				<div style="width:50%; text-align:center; border-radius:20px; padding:10px; padding-left:100px; padding-right:100px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>
					
					<img style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" src="pictures/<%=picture%>" width="140px" height="140px" /><br>
					
					<hr style="width: 80%; margin-left:10% !important; margin-right:10% !important;">
					
					<p><b>Nome público:</b> <%=name%></p>
					<p><b>Idade:</b> <%%></p>
					<p><b>Cor favorita:</b> <%=color%></p>
					<p><b>Total de vitórias:</b> <%%></p>
					
					<div class="container-login100-form-btn">
						<input style="width:100%" class="login100-form-btn" type = "submit" value = "Editar perfil"/>
					</div>
					
					<div class="container-login100-form-btn" style="padding-top:8px">
						<a  class="login100-form-btn" title="Saída" aria-current="page" href="LogoutServlet">Sair</a>
					</div>
				
				</div>

				<!-- Procurar Jogo -->
				<div style="padding: 10px">
					<form class="login100-form validate-form" method="POST" action ="LoginServlet">
						<span class="login100-form-title" style="padding:12px">
							Batalha Naval!
						</span>
						
						<div class="login100-pic js-tilt" style="margin: auto; width: 92%">
						<br><br>
							<img src="images/img-01.png" alt="IMG">
						</div>
		
						<div class="container-login100-form-btn" style="margin: auto; width: 80%">
							<input class="login100-form-btn" type = "submit" value = "Jogar!"/>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>

<!--===============================================================================================-->	
	<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/bootstrap/js/popper.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/tilt/tilt.jquery.min.js"></script>
	<script >
		$('.js-tilt').tilt({
			scale: 1.1
		})
	</script>
<!--===============================================================================================-->
	<script src="js/main.js"></script>

</body>
</html>