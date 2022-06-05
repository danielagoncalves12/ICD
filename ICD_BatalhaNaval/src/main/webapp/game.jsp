<%@ page import="socket.User" import="battleship.GameView" import="bean.Check" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Batalha Naval</title>
</head>
<body>
<h2>JOGO</h2>

<h2>Bem vindo <%=session.getAttribute("username")%> !!</h2>

<%
User player      = (User) session.getAttribute("player");
String playerNum = (String) session.getAttribute("player_num");
String info = "", result = "";

if (playerNum == null) {
	
	//player = new User();

	//System.out.println("Criou o Jogador!");
	//session.setAttribute("player", player);

	playerNum = player.sendRequestInfo("0");
	session.setAttribute("player_num", playerNum);
	System.out.println("cheguei");
}

info = player.sendRequestInfo(playerNum);
String position = request.getParameter("position");
if (position != null) result = player.sendRequestPlay(playerNum, position);
%>

<div style="width: 100%; display: table;">
    <div style="display: table-row">
        <div style="width: 600px; display: table-cell;"> 
        	<%=GameView.viewBoard(player.sendRequestBoard(playerNum, "true"))%>
        </div>
        
        <div style="display: table-cell;">
        	<%=GameView.viewBoard(player.sendRequestBoard(playerNum, "false"))%>
        </div>
    </div>
</div>

<p><%=info%></p>
<br>
<p><%=result%></p>

<form method="POST">
    <input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
    <input type="submit" value="Jogar">
</form>

</body>
</html>