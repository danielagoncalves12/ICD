<%@ page import="socket.User" import="battleship.GameView" import="bean.Check" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/GameStyle.css">
<title>Batalha Naval</title>

<script type="text/javascript">



</script>

</head>
<body>
<% 
String username = Check.username(request, response);
String name = Check.name(request);
%>

<%
// Atributos
String result = (String) session.getAttribute("result");
String state  = (String) session.getAttribute("state");
User user     = (User) session.getAttribute("user");

if (user == null) user = new User();
if (result == null) result = "";
String myBoard = "", anotherBoard = "";

System.out.println("Username -> " + session.getAttribute("username"));
System.out.println("User aqui no jogo -> " + user);

// Primeira vez
if (state == null) {
	System.out.println("Primeira vez");
	user.sendRequestGame(username);
	myBoard = user.sendRequestBoard(username, "true");
	anotherBoard = user.sendRequestBoard(username, "false");
}
else {	
	myBoard = user.sendRequestBoard(username, "true");
	anotherBoard = user.sendRequestBoard(username, "false");
}
%>

<div class="center">

	<div style="text-align: center" class="center">
	
		<h2>JOGO</h2>
		<h2> Bem vindo <%=name%> !! </h2>

		<form method="POST" action="PlayServlet">
			<input type="hidden" name="username" id="username" value="<%=username%>">
			<input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
			<input type="submit" value="Jogar">
		</form>

		<br>
		<p><%=result%></p>

	</div>
</div>

<div style="text-align:center; width: 100%;">
       <div style="width: 50%; height: 450px; float: left; font-size: 0px"> 
           <%=GameView.viewBoard(myBoard)%>
       </div>
       <div style="margin-left: 50%; height: 450px; font-size: 0px"> 
           <%=GameView.viewBoard(anotherBoard)%>  
       </div>
   </div>



</body>
</html>