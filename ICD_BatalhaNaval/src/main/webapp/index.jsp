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
    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
</head>
<body>
	
<% 
// Obter dados
String username = Check.username(request, response);
HashMap<String, String> profile = Check.profile(username);

String rgbColor = Profile.hex2Rgb(profile.get("Color"));

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(profile.get("Date"), formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));

// Quadro de honra
String honor = new User().sendRequestHonorBoard();
String[][] players = XMLUtils.stringToArray2D(honor);

// Lista de jogadores inscritos
String playersListStr = new User().sendRequestPlayers();
String[] playersList  = XMLUtils.stringToArray(playersListStr);
%>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px; padding-left: 95px; padding-right: 95px">

				<div style="width:50%; text-align:center; border-radius:20px; padding:10px; padding-left:80px; padding-right:80px; line-height: 20px;
background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>

					<img src="data:image/png;base64,<%=profile.get("Picture")%>" style="object-fit:cover; border-radius: 100%; border: 2px solid #9e9e9e" width="140px" height="140px"/> <br>
					
					<hr style="width: 80%; margin-left:10% !important; margin-right:10% !important;">
					
					<p><b>Nome público:</b> <%=profile.get("Name")%></p>
					<p><b>Idade:</b> <%=age%> anos</p>
					<!--  <p><b>Cor favorita:</b> <%=profile.get("Color")%></p> --> 
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
				
					<div style="height: 50%; text-align:center">
						<span class="login100-form-title" style="padding:12px">
							Batalha Naval!
						</span>
						
						<div class="container-login100-form-btn" style="margin: auto; width: 80%">
							<a id="link" onclick="javascript:clickAndDisable(this);" style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" href="game.jsp">Jogar</a>
							<p id="searching" style="color: red; display: none">A procurar um oponente...</p>
						</div>				

						<br><br>
						<span class="login100-form-title" style="padding:12px">
							Procurar um jogador!
						</span>
						<br>
						<h6>Introduza o nome: </h6>
						<form action="SearchServlet" method="POST">
							<input type="hidden" name="validate" value="<%=username%>"/>
							<input type="text" style="margin-top:10px; border: 1px solid black" name="search_name" id="tags"/><br><br>	
							<input type="submit" id="profile" style="padding-right:0px; padding-left:0px; font-size: 13px; background-color:#8b989e" class="login100-form-btn" value="Ver Perfil"/>
						</form>
					</div>	
				</div>
				<br>
							
				<div style="width: 100%; text-align: center">
					<br><hr>
					<h4>Quadro de honra</h4><br>
					
					<table style="width: 100%">
					  <tr>
					    <th style="text-align:left; background-color: #9e9e9e; color: white"> Pos.</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Jogador</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Vitorias</th>
					    <th style="border: 1px solid #9e9e9e"> </th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white"> Pos.</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Jogador</th>
					    <th style="text-align:left; background-color: #9e9e9e; color: white">Vitorias</th>
					  </tr>
					  <tr style="border: 1px solid #9e9e9e">
					    <td style="text-align:left"> 1º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[0][1]%>" width="44px" height="44px"/> <%=players[0][0]%></td>
					    <td><%=players[0][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 6º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[5][1]%>" width="44px" height="44px"/> <%=players[5][0]%></td>
					    <td><%=players[5][2]%></td>
					  </tr>
					  <tr style="border: 1px solid #9e9e9e">
					    <td style="text-align:left"> 2º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[1][1]%>" width="44px" height="44px"/> <%=players[1][0]%></td>
					    <td><%=players[1][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 7º</td>
						<td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[6][1]%>" width="44px" height="44px"/> <%=players[6][0]%></td>
					    <td><%=players[6][2]%></td>
					  </tr>
					  <tr style="border: 1px solid #9e9e9e">
					    <td style="text-align:left"> 3º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[2][1]%>"width="44px" height="44px"/> <%=players[2][0]%></td>
					    <td><%=players[2][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 8º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[7][1]%>" width="44px" height="44px"/> <%=players[7][0]%></td>
					    <td><%=players[7][2]%></td>
					  </tr>
					  <tr style="border: 1px solid #9e9e9e">
					    <td style="text-align:left"> 4º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[3][1]%>" width="44px" height="44px"/> <%=players[3][0]%></td>
					    <td><%=players[3][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 9º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[8][1]%>" width="44px" height="44px"/> <%=players[8][0]%></td>
					    <td><%=players[8][2]%></td>
					  </tr>
					  <tr style="border: 1px solid #9e9e9e">
					    <td style="text-align:left"> 5º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[4][1]%>" width="44px" height="44px"/> <%=players[4][0]%></td>
					    <td><%=players[4][2]%></td>
					    <td></td>
					    <td style="text-align:left"> 10º</td>
					    <td style="text-align:left"><img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[9][1]%>" width="44px" height="44px"/> <%=players[9][0]%></td>
					    <td><%=players[9][2]%></td>
					  </tr>
					</table> 
				
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	
	    function clickAndDisable(link) {
	    	document.getElementById("searching").style.display = "block";
			link.onclick = function(event) {
		        event.preventDefault();       
			}
		}   
	</script>
	
	<!--===============================================================================================-->	
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.0.0.min.js"></script>
	
  <script>
  $(function() {
	   
	var availablePlayers = new Array();
	
	<% for (String element : playersList) {%> 
		availablePlayers.push("<%=element%>");
	<% } %>

    $( "#tags" ).autocomplete({
      source: availablePlayers
    });
  } );
  </script>


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