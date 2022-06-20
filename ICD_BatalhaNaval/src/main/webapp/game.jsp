<%@ page import="socket.User" import="java.util.HashMap" import="battleship.GameView" import="bean.Check" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/GameStyle.css">
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


<div style="border-radius:5%; background-color: rgba(108, 122, 186, 0.6); text-align:center; width:70%" class="center">

	<br>
	<img src="images/titulo.png" width="300px"/>
	
	<hr style="width: 90%"/>
	<div style="width: 100%; overflow: hidden; text-align: center" class="center">
	
		<div class="center" style="text-align:left; padding: 1%; width: 51%; float: left;"> 
		
			<div class="center" style="text-align: center; width:29%; float:left">
				<img style="object-fit:cover; border: 2px solid #00008B;" src="data:image/png;base64,<%=profile.get("Picture")%>" width="100px" height="100px"/>
			</div>
			
			<div class="center" style="width:70%; float:right">
				<h3>Bem-vindo <%=name%> !!</h3>
				<span>Os teus navios foram distribuidos aleatoriamente pelo tabuleiro.</span>
				<span>Acerte em todos os navios inimigos, antes do inimigo descobrir todos os teus navios, para vencer.</span>
			</div>			

		</div>
		<div class="center" style="text-align:left; height:130px; width: 42%;  padding: 2%; float: right;"> 
			
			<div class="center" style="width:100%; float:left">
				
				<!-- FORM JOGADA -->
				<form method="POST" action="PlayServlet"><br>
					<input type="hidden" name="username" id="username" value="<%=username%>">
					<span>Jogada </span><input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
					<input type="submit" name="play" id="play" value="Jogar">
				</form>
					
				<a href="index.jsp" id="exit">Sair</a>
				
				<br><br>
				<span>Resultado: <%=result%></span><br>
			</div>
		</div>
		<br>

	</div>
	<br>
	<hr style="width: 90%">
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

	var state = '<%=session.getAttribute("state")%>';
	if (state === "ended") {
		
		document.getElementById('position').disabled = true;
		document.getElementById('position').style.display = 'block';
        document.getElementById('position').style.display = 'none';
		
		document.getElementById('play').disabled = true;
		document.getElementById('play').style.display = 'block';
        document.getElementById('play').style.display = 'none';
        
        if (<%=(boolean) session.getAttribute("clean")%> == true) {
        	<%session.removeAttribute("result");%>
            <%session.removeAttribute("state");%>
            <%session.setAttribute("clean", false);%>
        }      
        <%session.setAttribute("clean", true);%> 
	}
	else {
		document.getElementById('exit').disabled = true;
		document.getElementById('exit').style.display = 'block';
        document.getElementById('exit').style.display = 'none';
	}

</script>

</body>
</html>