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
if (state == null) user.sendRequestGame(username);

// Ao longo do jogo
String myBoard = user.sendRequestBoard(username, "true");
String anotherBoard = user.sendRequestBoard(username, "false");
%>

<div class="center">

	<div style="text-align: center" class="center">
	
		<h2>JOGO</h2>
		<h2> Bem vindo <%=name%> !! </h2>

		<form method="POST" action="PlayServlet">
			<input type="hidden" name="username" id="username" value="<%=username%>">
			<input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
			<input type="submit" name="play" id="play" value="Jogar">
		</form>
		
		<form method="POST" action="LoginServlet">
			<input type="submit" name="exit" id="exit" value="Sair">
		</form> 

		<br>
		<p><%=result%></p>

	</div>
</div>

<div style="text-align:center; width: 100%;">
    <div style="width: 50%; height: 450px; float: left; font-size: 0px"> 
        <%=GameView.viewBoardView(myBoard)%>
    </div>
    <div style="margin-left: 50%; height: 450px; font-size: 0px"> 
        <%=GameView.viewBoard(anotherBoard)%>  
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
	}
	else {
		document.getElementById('exit').disabled = true;
		document.getElementById('exit').style.display = 'block';
        document.getElementById('exit').style.display = 'none';
	}

</script>

</body>
</html>