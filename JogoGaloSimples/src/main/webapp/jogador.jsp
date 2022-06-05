<%@ page import="client.Jogador" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Jogo do Galo</title>
</head>
<body>
<%
//falta libertar os recursos quando o jogo termina
//como usa sempre o mesmo nome "Jogador" pode dar confusão
Jogador JW = (Jogador)session.getAttribute("Jogador");

System.out.println(JW);

if(JW==null) {
	JW = new Jogador();
	System.out.println("Criou o Jogador!");
	session.setAttribute("Jogador", JW);
}
String jogada = request.getParameter("quadricula");
if(jogada!=null)
	JW.jogar(Short.parseShort(jogada));
else
	System.out.println("Primeira vez!");
System.out.println("Espera pelo tabuleiro!");
%>
<%=JW.tabuleiro("<br/>") %>
<form>  <!-- envia por get para se ver a jogada  -->
    <input type="number" name="quadricula" id="quadricula" min="1" max="9" style="width: 40px; ">
    <input type="submit" value="Jogar">
</form>
</body>
</html>