<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" import="session.Profile" import="java.awt.Color"%>
<%@page import="java.time.*" import="java.time.format.DateTimeFormatter" import="java.time.temporal.ChronoUnit"%>
<%@page import="protocol.XMLUtils" import="java.util.HashMap" %>
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
// Obter dados
String username = Check.username(request, response);
HashMap<String, String> profile = Check.profile(request, response);

String rgbColor = Profile.hex2Rgb(profile.get("Color"));

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(profile.get("Date"), formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));

// Quadro de honra
String honor  = new User().sendRequestHonorBoard();
String[][] players = XMLUtils.stringToArray2D(honor);

%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px">

				<div style="width:50%; text-align:center; border-radius:20px; padding:10px; padding-left:80px; padding-right:80px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>
					
					<img style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" src="pictures/<%=profile.get("Picture")%>" width="140px" height="140px" /><br>
					
					<hr style="width: 80%; margin-left:10% !important; margin-right:10% !important;">
					
					<p><b>Nome público:</b> <%=profile.get("Name")%></p>
					<p><b>Idade:</b> <%=age%> anos</p>
					<p><b>Cor favorita:</b> <%=profile.get("Color")%></p>
					<p><b>Total de vitórias:</b> <%=profile.get("WinsNum")%></p>
					
					<div class="container-login100-form-btn">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Editar" aria-current="page" href="edit.jsp">Editar perfil</a>
					</div>
					
					<div class="container-login100-form-btn" style="padding-top:8px">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Saída" aria-current="page" href="LogoutServlet">Sair</a>
					</div>
				
				</div>

				<!-- Procurar Jogo -->
				<div style="padding: 10px; padding-left: 50px">
				
					<div style="height: 50%">
						<span class="login100-form-title" style="padding:12px">
							Batalha Naval!
						</span>
						
						<h6>Procurar um jogador: </h6>
						<input style="border: 1px solid black" type="text"/>
		
						<div class="container-login100-form-btn" style="margin: auto; width: 80%">
							<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" href="game.jsp">Play</a>
							<!--<input href="game.jsp" class="login100-form-btn" type = "submit" value = "Jogar!"/>  -->
						</div>
					</div>				
				</div>
				<br>
							
				<div style="width: 100%; text-align: center">
					<br><hr>
					<h4 style="text-decoration: underline">Quadro de honra</h4><br>
					
					<table style="width: 100%">
					  <tr>
					    <th style="text-align:left; background-color: #9e9e9e; color: white"> Pos.</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Player</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Victories</th>
					    <th>  </th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white"> Pos.</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Player</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Victories</th>
					  </tr>
					  <tr>
					    <td style="text-align:left"> 1º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[0][1]%>" width="44px" height="44px"/> <%=players[0][0]%></td>
					    <td><%=players[0][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 6º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[5][1]%>" width="44px" height="44px"/> <%=players[5][0]%></td>
					    <td><%=players[5][2]%></td>
					  </tr>
					  <tr>
					    <td style="text-align:left"> 2º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[1][1]%>" width="44px" height="44px"/> <%=players[1][0]%></td>
					    <td><%=players[1][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 7º</td>
						<td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[6][1]%>" width="44px" height="44px"/> <%=players[6][0]%></td>
					    <td><%=players[6][2]%></td>
					  </tr>
					  <tr>
					    <td style="text-align:left"> 3º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[2][1]%>" width="44px" height="44px"/> <%=players[2][0]%></td>
					    <td><%=players[2][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 8º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[7][1]%>" width="44px" height="44px"/> <%=players[7][0]%></td>
					    <td><%=players[7][2]%></td>
					  </tr>
					  <tr>
					    <td style="text-align:left"> 4º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[3][1]%>" width="44px" height="44px"/> <%=players[3][0]%></td>
					    <td><%=players[3][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 9º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[8][1]%>" width="44px" height="44px"/> <%=players[8][0]%></td>
					    <td><%=players[8][2]%></td>
					  </tr>
					  <tr>
					    <td style="text-align:left"> 5º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[4][1]%>" width="44px" height="44px"/> <%=players[4][0]%></td>
					    <td><%=players[4][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 10º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="pictures/<%=players[9][1]%>" width="44px" height="44px"/> <%=players[9][0]%></td>
					    <td><%=players[9][2]%></td>
					  </tr>
					</table> 
				
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