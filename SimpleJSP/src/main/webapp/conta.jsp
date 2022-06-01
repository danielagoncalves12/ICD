<html>
<head>
<title>Contador de Visitas</title>
</head>
<body>
<%! private int contagem=0;%>
<%! private synchronized int getContagem(){
    return ++contagem;
}%>

<p>Já acedeu <%= getContagem() %> vezes.</p>

<%
java.lang.String pattern = "EEEEE, dd MMMMM yyyy (HH:mm:ss.SSSZ)";
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, new java.util.Locale("PT"/*"da", "DK"*/));
sdf.setTimeZone( java.util.TimeZone.getTimeZone("Europe/Lisbon"));
java.lang.String text = sdf.format(new java.util.Date()); %>
 
<p>Gerado agora, <% out.write(text); /*out.println(new java.util.Date());*/ %></p>
</body>
</html>
