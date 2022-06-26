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
	<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	
<% 
// Obter dados
String username = Check.username(request, response);

// Informacoes do perfil
HashMap<String, String> profile = Check.profile(username);	
String rgbColor = Profile.hex2Rgb(profile.get("Color"));

// Calculo da idade
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate birth = LocalDate.parse(profile.get("Date"), formatter);
String age = String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));

// Quadro de honra
String honor = new User().sendRequestHonorBoard();
String[][] players = XMLUtils.stringToArray2D(honor);
%>

<script type="text/javascript">

	function getXHR() {
		var invocation = null;
		try {
			invocation = new XMLHttpRequest();
		} catch (e) {
			try { invocation = new ActiveXObject("Msxml2.XMLHTTP");} catch (e) {
				try {invocation = new ActiveXObject("Microsoft.XMLHTTP");} catch (e) {alert("Your browser broke!");return null;}}
		}
		return invocation;
	}

	var xmlHttp = getXHR();
	
	function showState(str) {
		if (event.key === 'Enter') {
			document.getElementById("player").value = document.getElementById("players").options[0].value;
			return;
		}
		var url = "AutoServlet";
		url += "?query=" + str + "&num_letters=0&num_items=10";
		url = encodeURI(url);
		
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
				document.getElementById("players").innerHTML = xmlHttp.responseText;
				document.getElementById("player").placeholder = "Procurar um jogador...";
			} else
				document.getElementById("player").placeholder = "Erro.";
		}
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}

</script>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top:50px; padding-left: 95px; padding-right: 95px">

				<div class="white-board" style="background: linear-gradient(180deg, <%=rgbColor%> 0%, rgba(255,255,255,0) 75%, rgba(255,255,255,1) 100%);">
					
					<h5 style="padding: 10px">Perfil de <%=username%></h5><br>
					<img class="circular-image" src="data:image/png;base64,<%=profile.get("Picture")%>" width="140px" height="140px"/> <br>
					
					<hr class="hr-style">
					
					<p style="line-height:10px"><b>Nome público:</b> <%=profile.get("Name")%></p>
					<p style="line-height:10px"><b>Idade:</b> <%=age%> anos</p>
					<!--  <p><b>Cor favorita:</b> <%=profile.get("Color")%></p> --> 
					<p style="line-height:10px"><b>Total de vitórias:</b> <%=profile.get("WinsNum")%></p>
					
					<div class="container-login100-form-btn">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Editar" aria-current="page" href="edit.jsp">Editar perfil</a>
					</div>
					
					<div class="container-login100-form-btn" style="padding-top:8px">
						<a style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" title="Saída" aria-current="page" href="LogoutServlet">Sair</a>
					</div>
				
				</div>

				
				<div style="padding: 10px; padding-left: 50px">
				
					<div style="height: 50%; text-align:center">
						<span class="login100-form-title" style="padding:12px">
							Batalha Naval!
						</span>
						
						<!-- Procurar jogo -->
						<div class="container-login100-form-btn" style="margin: auto; width: 80%">
							<a id="link" onclick="javascript:findGame(this);" style="font-size: 13px; background-color:#8b989e" class="login100-form-btn" href="game.jsp">Jogar</a>
							<p id="searching" style="color: red; display: none">A procurar um oponente...</p>
						</div>				

						<!-- Encontrar jogador -->
						<br><br>
						<span class="login100-form-title" style="padding:12px">
							Procurar um jogador!
						</span>
						<br>
						<h6>Introduza o nome: </h6>			
						
						<form action="SearchServlet" method="GET">
							<input style="border: 1px solid black" autocomplete="off" list="players" id="player" type="search" name="player" onkeyup="showState(this.value);" style="width: 450px; ">
							<br>	
							<datalist id="players"> 
							<option>A carregar... </option>
							</datalist>
							
							<br>
							<input type="submit" id="profile" style="padding-right:0px; padding-left:0px; font-size: 13px; background-color:#8b989e" class="login100-form-btn" value="Ver Perfil"/>
						</form>
					</div>	
				</div>			
			
			<!--  QUADRO DE HONRA  -->
			<section class="main-content" style="width:100%">
			<hr>
			<div style="text-align:center" class="container align-items-center">
			
			<h2>Quadro de Honra</h2>
			<br>
			<br>

			<div class="row">
				<div class="col-sm-4">
					<div class="leaderboard-card">
						<div class="leaderboard-card__top">
							<h3 class="text-center">2º Lugar</h3>
						</div>
						<div class="leaderboard-card__body">
							<div class="text-center">
								<img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[1][1]%>" width="50px" height="50px"/>
								<h5 class="mb-0"><%=players[1][0]%></h5>			
								<hr>
								<p class="text-muted mb-0">Vitórias: <%=players[1][2]%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="leaderboard-card leaderboard-card--first">
						<div class="leaderboard-card__top">
							<h3 class="text-center">1º Lugar</h3>
						</div>
						<div class="leaderboard-card__body">
							<div class="text-center">
								<img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[0][1]%>" width="70px" height="70px"/>
								<h5 class="mb-0"><%=players[0][0]%></h5>							
								<hr>
								<p class="text-muted mb-0">Vitórias: <%=players[0][2]%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="leaderboard-card">
						<div class="leaderboard-card__top">
							<h3 class="text-center">3º Lugar</h3>
						</div>
						<div class="leaderboard-card__body">
							<div class="text-center">
								<img style="object-fit:cover; border-radius: 100%; border: 1px solid #9e9e9e" src="data:image/png;base64,<%=players[2][1]%>" width="50px" height="50px"/>
								<h5 class="mb-0"><%=players[2][0]%></h5>
								<hr><p class="text-muted mb-0">Vitórias: <%=players[2][2]%></p>							
							</div>
						</div>
					</div>
				</div>
				
			</div>

			<br><hr>

			<table class="table">
				<thead>
					<tr style="text-align: left;">
						<th>Jogador</th>
						<th>Posição</th>
						<th>Vitórias</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[3][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[3][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">4º Lugar</span>
							</div>
						</td>
						<td><%=players[3][2]%></td>
					</tr>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[4][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[4][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">5º Lugar</span>
							</div>
						</td>
						<td><%=players[4][2]%></td>
					</tr>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[5][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
								<h5 class="mb-0"><%=players[5][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">6º Lugar</span>
							</div>
						</td>
						<td><%=players[5][2]%></td>
					</tr>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[6][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[6][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">7º Lugar</span>
							</div>
						</td>
						<td><%=players[6][2]%></td>
					</tr>
					
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[7][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[7][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">8º Lugar</span>
							</div>
						</td>
						<td><%=players[7][2]%></td>
					</tr>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[8][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[8][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">9º Lugar</span>
							</div>
						</td>
						<td><%=players[8][2]%></td>
					</tr>
					<tr>
						<td>
							<div class="d-flex align-items-center">
								<img class="circle-img circle-img--small mr-2" src="data:image/png;base64,<%=players[9][1]%>" width="50px" height="50px"/>
								<div class="user-info__basic">
									<h5 class="mb-0"><%=players[9][0]%></h5>
								</div>
							</div>
						</td>
						<td>
							<div class="d-flex align-items-baseline">
								<span class="mr-1">10º Lugar</span>
							</div>
						</td>
						<td><%=players[9][2]%></td>
					</tr>
				</tbody>
			</table>
		</div>
	</section>

			</div>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>		

	<script type="text/javascript">
	
	    function findGame(link) {
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