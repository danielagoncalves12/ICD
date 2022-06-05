<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" %>
<%@page import="java.net.URLEncoder" import="javax.servlet.http.Cookie" import="session.Session" import="com.google.gson.Gson"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate"/>
  <meta http-equiv="Pragma" content="no-cache"/>
  <meta http-equiv="Expires" content="-1"/>

<% 
User user = (User) session.getAttribute("user");

System.out.println(user);
if (user == null) {

	session.setAttribute("user", new User());
	System.out.println("User criado");
}

 %>
<title>Iniciar sessão</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<link rel="icon" href="/favicon.ico" type="image/x-icon">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Tangerine">
	<style>
		body {background-color:powderblue;}
		label, input {font-size:16px; margin: 5px;}
	</style>
  </head>
  
   <body>
   
   <br><br>
   <div style="position: fixed; left: 50%; transform: translate(-50%, 0%);">
		<div style="margin:20px; display: table-row">

			<div style="padding-left:50px; padding-right:50px; padding-bottom:50px; width: 200px; height: 300px; display: table-cell; border-radius: 10px; border: 1px solid black; text-align: center;"> 
			    <!-- FORM de inicio de sessao -->
			    <br>
			    <h2>Iniciar Sessão</h2>
			    <br>
		        <form method = "POST" action ="LoginServlet">
		      
			        <label for="input_username">Nome de utilizador: </label><br>
			        <input type = "text" name="input_username" id="input_username" required><br>
			        
			        <label for="input_password">Palavra-passe: </label><br>
			        <input type = "password" name="input_password" id="input_password" required><br>
			        
			        <input type = "submit" value = "Entrar!">
		      </form>
		    </div>
		    
         	<div style="padding-left:50px; padding-right:50px; padding-bottom:50px; width: 200px; height: 300px; display: table-cell; border-radius: 10px; border: 1px solid black; text-align: center;">
				<!-- FORM de inscricao -->
				<br>
				<h2>Criar uma conta</h2>
				<br>
				<form method="POST" action="RegisterServlet">
 
					<label for="new_username">Nome de utilizador: </label><br>
					<input type="text" name="new_username" id="new_username" required><br>

					<label for="new_name">Nome público: </label><br>
					<input type="text" name="new_name" id="new_name" required><br>
					
					<label for="new_password">Palavra-passe: </label><br>
					<input type="password" name="new_password" id="new_password" required><br>
					
					<label for="new_picture">Foto de perfil: </label><br> 
					<input type="file" id="new_picture" name="new_picture" accept="image/png, image/jpeg" value=""><br>
					
					<input type="submit" value="Entrar!">
				</form>
				
			</div>	     
		</div>
	</div>

   </body>
</html>