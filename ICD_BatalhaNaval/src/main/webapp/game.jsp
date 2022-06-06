<%@ page import="socket.User" import="battleship.GameView" import="bean.Check" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Batalha Naval</title>

<style>
.center {
  margin: auto;
  width: 80%;
  border: 1px solid black;
  padding: 10px;
}

.container {
  width: 100%;
  height: 450px;
  background: aqua;
  margin: auto;
}

.one {
  width: 50%;
  height: 450px;
  float: left;
}

.two {
  margin-left: 15%;
  height: 480px;
  float: right;
}
</style>

</head>
<body>
<% String username = Check.username(request, response); %>

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
	
	user.sendRequestGame(username);
	myBoard = user.sendRequestBoard(username, "true");
	anotherBoard = user.sendRequestBoard(username, "false");
}
else {	
	myBoard = user.sendRequestBoard(username, "true");
	anotherBoard = user.sendRequestBoard(username, "false");
	//String position = request.getParameter("position");
	//if(position != null)
	//	user.sendRequestPlay(username, position);
}
%>
<div class="center">

	<div style="text-align: center" class="center">
	
		<h2>JOGO</h2>
		<h2> Bem vindo <%=username%> !! </h2>

		<form method="POST" action="PlayServlet">
			<input type="hidden" name="username" id="username" value="<%=username%>">
			<input type="text" name="position" id="position" min="1" max="9" style="width: 40px;">
			<input type="submit" value="Jogar">
		</form>

		<br>
		<p><%=result%></p>

	</div>

	<section class="container">
	  	<div class="one" style="font-size: 0">
	  		<%=GameView.viewBoard(myBoard)%>
	  	</div>
	  	<div class="two" style="font-size: 0">
	  		<%=GameView.viewBoard(anotherBoard)%>  
 		</div>
	</section>

</div>

</body>
</html>