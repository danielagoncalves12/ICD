<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="error.jsp" %>  
<%@page import="bean.Check"%>
<%String name=Check.login(request, response);%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Página de Entrada</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.topnav {
  overflow: hidden;
  background-color: #333;
}

.topnav a {
  float: left;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.topnav a.active {
  background-color: #04AA6D;
  color: white;
}
</style>
</head>
<body>
<header>
<!-- <ul>
    <li><a href="index.jsp?page=ExampleServlet">Servlet: Chama Metodos do Request</a></li>
    <li><a href="index.jsp?page=exampleJSP.jsp">JSP: Data/Hora e Utilizador Remoto</a></li>
    <li><a href="LogoutServlet">Logout</a></li>
</ul> -->
<div class="topnav">
  <a class="active" href="index.jsp" title="Entrada">Home</a>
  <a href="index.jsp?page=ExampleServlet" title="Servlet: Chama Metodos do Request">Servlet</a>
  <a href="index.jsp?page=exampleJSP.jsp" title="JSP: Data/Hora e Utilizador Remoto">JSP</a>
  <a href="LogoutServlet" title="Saída">Logout</a>
</div>
<hr>
<h1>Hello, "<%=name%>" welcome!</h1>
<hr>
</header>
<%
String pg=request.getParameter("page");
if(pg!=null)
	%><jsp:include page="<%=pg%>"></jsp:include>
<hr>
</body>
</html>