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
HashMap<String, String> profile  = Check.profile(request, response);
String name = profile.get("Name");
%>

<%
// Atributos
String result = (String) session.getAttribute("result");
String state  = (String) session.getAttribute("state");
User user     = (User) session.getAttribute("user");

if (user == null) user = new User();
if (result == null) result = "";

// Primeira vez
if (state == null) {
	user.sendRequestGame(username);
	session.setAttribute("clean", false);
}

// Ao longo do jogo
String myBoard = user.sendRequestBoard(username, "true");
String anotherBoard = user.sendRequestBoard(username, "false");
%>

<audio src="resources/background.mp3" autoplay loop>
</audio>

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
				<h3>Bem-vindo <%=name%> !!</h3>
				<span>Os teus navios foram distribuidos aleatoriamente pelo tabuleiro.</span>
				<span>Para vencer acerte em todos os navios inimigos, antes do inimigo descobrir todos os teus navios.</span>
			</div>			

		</div>
		<div class="center" style="text-align:left; height:130px; width: 42%;  padding: 1%; float: right;"> 
			
			<div class="center" style="width:40%; float:left">
				
				<!-- FORM JOGADA -->
				<form method="POST" action="PlayServlet"><br>
					<input type="hidden" name="username" id="username" value="<%=username%>">
					<span id="title">Jogada </span><input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
					<input type="submit" name="play" id="play" value="Jogar" onclick="buttonClick()">
				</form>
				
				<div id="time" class="circle wrapper">
				
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
			
			<div class="center" style="width:59%; float:right">
								
				<br><span style="float:left;"><%=result%></span><br><br><br>
				<a style="float:left;" href="index.jsp" class="button-exit" id="exit">Regressar ao Menu</a>
				
				<br>
				
			</div>
		</div>

	</div>
	<br>
	<div style="text-align:center; width: 100%;">
	    <div style="width: 50%; height: 450px; float: left; font-size: 0px"> 
	        <%=GameView.viewBoardView(myBoard)%>
	    </div>
	    <div style="margin-left: 50%; height: 450px; font-size: 0px"> 
	        <%=GameView.viewBoard(anotherBoard)%>  
	    </div>
	</div>
</div>


<script type="text/javascript">

	function buttonClick() {
		
		document.getElementById('play').style.pointerEvents = 'none';
		document.getElementById('play').style.cursor = 'not-allowed';
		document.getElementById('play').style.opacity = '0.65';
		
		document.getElementById('position').style.pointerEvents = 'none';
		document.getElementById('position').style.cursor = 'not-allowed';
		document.getElementById('position').style.opacity = '0.65';
		
		var shotSound = new Audio('resources/shot.mp3');
		shotSound.loop = false;
		shotSound.play();
	}
	
	var state = '<%=session.getAttribute("state")%>';
	if (state === "ended") {
		
		document.getElementById('title').disabled = true;
        document.getElementById('title').style.display = 'none';
		
		document.getElementById('position').disabled = true;
        document.getElementById('position').style.display = 'none';
		
		document.getElementById('play').disabled = true;
        document.getElementById('play').style.display = 'none';
        
        document.getElementById('time').disabled = true;
        document.getElementById('time').style.display = 'none';
        
        if (<%=(boolean) session.getAttribute("clean")%> == true) {
        	<%session.removeAttribute("result");%>
            <%session.removeAttribute("state");%>
            <%session.setAttribute("clean", false);%>
        }      
        <%session.setAttribute("clean", true);%> 
	}
	else {
		document.getElementById('exit').disabled = true;
        document.getElementById('exit').style.display = 'none';
	}

</script>

<script src="js/clock.js"></script>

</body>
</html>