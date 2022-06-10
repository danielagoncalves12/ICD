<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" import="session.Profile" import="java.awt.Color"%>
<%@page import="java.time.*" import="java.time.format.DateTimeFormatter" import="java.time.temporal.ChronoUnit"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Batalha Naval - P�gina Inicial</title>
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
String date 	= Check.date(request);
String picture  = Check.picture(request);

String rgbColor = Profile.hex2Rgb(color);

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(date, formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));
%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px">
			
				<div style="width:50%; text-align:center; border-radius:20px; padding:10px; padding-left:100px; padding-right:100px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>
					
					<img style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" src="pictures/<%=picture%>" width="140px" height="140px" /><br>
					
					<hr style="width: 80%; margin-left:10% !important; margin-right:10% !important;">
					
					<p><b>Nome p�blico:</b> <%=name%></p>
					<p><b>Idade:</b> <%=age%> anos</p>
					<p><b>Cor favorita:</b> <%=color%></p>
					<p><b>Total de vit�rias:</b> <%%></p>
					
					<div class="container-login100-form-btn">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Editar" aria-current="page" href="edit.jsp">Editar perfil</a>
					</div>
					
					<div class="container-login100-form-btn" style="padding-top:8px">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Sa�da" aria-current="page" href="LogoutServlet">Sair</a>
					</div>
				
				</div>

				<!-- Procurar Jogo -->
				<div style="padding: 10px; padding-left: 20px">
					<span class="login100-form-title" style="padding:12px">
						Batalha Naval!
					</span>

	
					<div class="container-login100-form-btn" style="margin: auto; width: 80%">
						<a href="game.jsp"><button type="button" class="btn btn-outline-primary">Play</button></a>
						<!--<input href="game.jsp" class="login100-form-btn" type = "submit" value = "Jogar!"/>  -->
					</div>
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