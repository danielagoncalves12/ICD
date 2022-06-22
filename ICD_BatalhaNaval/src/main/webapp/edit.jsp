<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" import="session.Profile" import="java.awt.Color"%>
<%@page import="java.time.*" import="java.time.format.DateTimeFormatter" import="java.time.temporal.ChronoUnit"%>
<%@page import="java.util.HashMap"%>
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
HashMap<String, String> profile = Check.profile(username);

String rgbColor = Profile.hex2Rgb(profile.get("Color"));

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(profile.get("Date"), formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));
%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px">
			
				<div style="width:100%; text-align:center; border-radius:20px; padding:10px; padding-left:100px; padding-right:100px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Edição do perfil de <b><%=username%></b></h5><br>				
					<img src="data:image/png;base64,<%=profile.get("Picture")%>" style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" width="140px" height="140px"/> <br>
					
					<hr style="width: 80%; margin-left:10% !important; margin-right:10% !important;">
							
					<form method="POST" action="EditServlet" enctype="multipart/form-data">
					
						<div style="display: inline-block; text-align: left;">	
							<input type="hidden" name="username" id="username" value="<%=username%>" />					
							<span>Nome público:  </span> <input style="width:200px; border: 2px solid #9e9e9e" type="text" name="new_name" id="new_name" value="<%=profile.get("Name")%>" required/><br>
							<span>Cor favorita:       </span> <input style="width:200px; border: 2px solid #9e9e9e" type="color" name="new_color" id="new_color" value="<%=profile.get("Color")%>" required/> <br>
							<span>Fotografia:         </span> <input style="width:200px; border: 2px solid #9e9e9e" type="file" accept="" name="new_picture" id="new_picture" value="<%=profile.get("Picture")%>"/> <br> 
							<span>Data de nascimento:      </span> <input style="border: 2px solid #9e9e9e" type="date" name="new_date" id="new_date" value="<%=profile.get("Date")%>" required/> <br>							
					    </div>								
		
						<div class="container-login100-form-btn">
							<input style="width:50%; font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Editar" type="submit" value="Atualizar"/>
						</div>
					</form>
					
					<div style="padding-top: 10px;" class="container-login100-form-btn">
						<a style="width:50%; font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Cancelar" aria-current="page" href="index.jsp">Cancelar</a>
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