<%@ page import="socket.User" import="java.util.HashMap" import="battleship.GameView" import="bean.Check" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/game_styles.css">
<link rel="stylesheet" href="css/clock.css">

<title>Batalha Naval</title>

</head>
<body>
<% 
String username = Check.username(request, response);
HashMap<String, String> profile  = Check.profile(username);
%>

<%
// Atributos
String gameID = (String) session.getAttribute("game_id");

String result = (String) session.getAttribute("result");
String state  = (String) session.getAttribute("state");
User user     = (User) session.getAttribute("user");

if (user == null) user = new User();
if (result == null) result = "";

// Primeira vez
if (state == null) {	
	gameID = user.sendRequestGame(username);
	
	do {
		gameID = user.sendRequestGame(username);
	} while(gameID.equals("ERROR"));
	
	session.setAttribute("game_id", gameID);
	session.setAttribute("clean", false);
	state = "playing";
}

// Ao longo do jogo

String myBoard = user.sendRequestBoard(gameID, username, "true");
String anotherBoard = user.sendRequestBoard(gameID, username, "false");

%>

<audio src="resources/background.mp3" autoplay loop></audio>

<br>
<div style="border-radius:5%; box-shadow: inset 0px 0px 78px 3px rgba(0,0,0,0.73); background-color: rgba(101, 149, 207, 0.8); text-align:center; width:70%" class="center">

	<br>
	<img src="images/titulo.png" width="300px"/>

	<div style="background-color: rgba(91, 148, 217, 0.8); box-shadow: inset 0px 0px 78px 3px rgba(0,0,0,0.73); width: 100%; overflow: hidden; text-align: center" class="center">
	
		<div class="center" style="text-align:left; padding: 1%; width: 51%; float: left;"> 
		
			<div class="center" style="padding-top: 10px; text-align: center; width:29%; float:left">
				<img style="box-shadow: 0px 20px 0px -10px rgba(91, 148, 217, 0.8), 0px -20px 0px -10px rgba(91, 148, 217, 0.8),
				20px 0px 0px -10px rgba(91, 148, 217, 0.8), -20px 0px 0px -10px rgba(91, 148, 217, 0.8), 0px 0px 0px 10px #374980,
				2px 2px 11px 7px #5b6da3;" src="data:image/png;base64,<%=profile.get("Picture")%>" width="100px" height="100px"/>
			</div>
			
			<div class="center" style="width:70%; float:right">
				<h3>Bem-vindo <%=profile.get("Name")%> !!</h3>
				<span>Os teus navios foram distribuidos aleatoriamente pelo tabuleiro.</span>
				<span>Para vencer acerte em todos os navios inimigos, antes do inimigo descobrir todos os teus navios.</span>
			</div>			

		</div>
		<div class="center" style="text-align:left; height:130px; width: 42%;  padding: 1%; float: right;"> 
			
			<div class="center" style="width:30%; float:left">
				
				<div id="time" style="top:50%; transform: translateY(30%);" class="circle wrapper">
				
					<svg width="70" viewBox="0 0 220 220" xmlns="http://www.w3.org/2000/svg">
					     <g transform="translate(110,110)">
					        <circle r="100" class="e-c-base"/>
					        <g transform="rotate(-90)">
					           <circle r="100" class="e-c-progress"/>
					           <g id="e-pointer">
					              <circle cx="100" cy="0" r="8" class="e-c-pointer"/>
					           </g>
					        </g>
					     </g>
					</svg>

					<div class="text">
						<div class="controlls">
						  <div class="display-remain-time">00:30</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="center" style="width:69%; float:right">
								
				<br><span id="result" style="float:left;"><%=result%></span><br><br><br>
				<a style="float:left;" href="index.jsp" style="display: none" class="button-exit" id="exit">Regressar ao Menu</a>
			
				<br>
				
			</div>
		</div>

	</div>
	<br>
	<div style="text-align:center; width: 100%;">
	    <div style="width: 50%; height: 450px; float: left; font-size: 0px"> 
	    
	    	<span style="font-size:18px; font-weight: bold">O seu Tabuleiro</span><br>
	        <%=GameView.viewBoardView(myBoard)%>
	    </div>
	    <div style="margin-left: 50%; height: 450px; font-size: 0px"> 
	    	<span style="font-size:18px; font-weight: bold;">Tabuleiro Adversário</span><br>
	    	
	    	<!-- FORM JOGADA -->
	    	<form method="POST" style="float:right; padding-right:80px; display: flex; flex-flow: wrap; width:396px" action="PlayServlet">
	    	
	    		<input type="hidden" name="username" id="username" value="<%=username%>"/>
				<input type="hidden" name="game_id" id="game_id" value="<%=gameID%>"/>
	        	<%=GameView.viewBoard(anotherBoard)%>  
	        </form>
	    </div>
	</div>
</div>

<script src="js/clock.js"></script>
<script type="text/javascript">

document.getElementById('exit').disabled = true;
document.getElementById('exit').style.display = 'none';

	if ("<%=state%>" == "ended") {
	
	    if (<%=(boolean) session.getAttribute("clean")%> == true) {
	    	<%session.removeAttribute("result");%>
	        <%session.removeAttribute("state");%>
	        <%session.setAttribute("clean", false);%>
	    }      
	    document.getElementById('exit').disabled = false;
        document.getElementById('exit').style.display = 'block';

	    document.getElementsByName("position").forEach((e) => {
	        e.disabled = true;
	    });
	    
	    <%session.setAttribute("clean", true);%> 
	}
	else {
		document.getElementById("exit").disabled = true;
		document.getElementById('exit').style.display = 'none';
	
	   	var beepSound = new Audio('resources/beeps.mp3');
	   	beepSound.loop = false;
	   	beepSound.play();
	}

	function buttonClick() {

		pauseTimer();
		
		document.getElementsByName("position").forEach((e) => {
		    e.style.cursor = 'not-allowed';
		    e.style.opacity = '0.65';
		    e.style.pointerEvents = 'none';
		});
		
		document.getElementById('result').innerHTML = "Aguardando a jogada do adversário";
		
		var shotSound = new Audio('resources/shot.mp3');
		shotSound.loop = false;
		shotSound.play();
	}
	
	function timer(seconds) {
		let remainTime = Date.now() + (seconds * 1000);
		displayTimeLeft(seconds);

		if ("<%=state%>" == "playing") {
			intervalTimer = setInterval(function() {
				
				timeLeft = Math.round((remainTime - Date.now()) / 1000);
				if (timeLeft < 0) {
					clearInterval(intervalTimer);
					isStarted = false;
					setterBtns.forEach(function(btn) {
						btn.disabled = false;
						btn.style.opacity = 1;
					});
					displayTimeLeft(wholeTime);
					displayOutput.textContent = "00:00";
			        
			        if (<%=(boolean) session.getAttribute("clean")%> == true) {
			        	<%session.removeAttribute("result");%>
			            <%session.removeAttribute("state");%>
			            <%session.setAttribute("clean", false);%>
			        }      
			        <%session.setAttribute("clean", true);%> 
	        
				        document.getElementById('result').innerHTML = "<b>Terminado: </b> Perdeste o jogo.<br> Não jogaste durante o tempo pedido.";
				        document.getElementById('exit').disabled = false;
				        document.getElementById('exit').style.display = 'block';
						return;			        
				}
				displayTimeLeft(timeLeft);
			}, 1000);
		}
	}
	
</script>

</body>
</html>