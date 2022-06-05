<%@page contentType="text/html" import="java.util.*" %>
<%@page errorPage="error.jsp" %>  
<%String name=bean.Check.login(request, response);%> 
<html>
<head>
<style>
table {
    background-color: #EEFFCA;
    color: white;
    border: 0px;
    cellpadding: 0px;
    cellspacing: 0px;
    width: 460px
}
</style>
</head>
<body>
<p>&nbsp;</p>
<div align="center">
<table>
<tr>
<td width="100%"><font size="6" color="#008000">&nbsp;Date Example</font></td>
</tr>
<tr>
<td width="100%"><b>&nbsp;Data e Hora atual:&nbsp; <font color="#FF0000">
<%= new Date() %>
</font></b></td>
</tr>
</table>
</div>
<h2>Utilizador Remoto: <%= name %></h2>
</body>
</html> 