<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" import="session.Profile" import="java.awt.Color"%>
<%@page import="java.time.*" import="java.time.format.DateTimeFormatter" import="java.time.temporal.ChronoUnit"%>
<%@page import="protocol.XMLUtils" import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Batalha Naval - Perfil</title>
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
    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
</head>
<body>
	
<% 
// Obter dados
String validate = Check.username(request, response);
String username = (String) session.getAttribute("search_username");
HashMap<String, String> profile = Check.profile(username);

String rgbColor = Profile.hex2Rgb(profile.get("Color"));

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(profile.get("Date"), formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));
%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px; padding-left:95px; padding-right:95px">

				<div style="width:100%; text-align:center; border-radius:20px; padding:10px; padding-left:80px; padding-right:80px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>

					<img src="data:image/png;base64,<%=profile.get("Picture")%>" style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" width="140px" height="140px"/> <br>
					
					<hr style="width: 100%; margin-left:10% !important; margin-right:10% !important;">
					
					<p><b>Nome público:</b> <%=profile.get("Name")%></p>
					<p><b>Idade:</b> <%=age%> anos</p>
					<!--  <p><b>Cor favorita:</b> <%=profile.get("Color")%></p> --> 
					<p><b>Total de vitórias:</b> <%=profile.get("WinsNum")%></p>

					<div class="container-login100-form-btn" style="padding-top:8px">
						<a id="profile" style="padding-right:20px; padding-left:20px; font-size: 13px; background-color:#8b989e" class="login100-form-btn" href="index.jsp">Regressar ao seu Perfil</a>					
					</div>
				
				</div>

				<br>		
			</div>
		</div>
	</div>

	
	<!--===============================================================================================-->	
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.0.0.min.js"></script>

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