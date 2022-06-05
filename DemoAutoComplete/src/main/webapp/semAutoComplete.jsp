<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<title>Exemplos sem AutoComplete</title>
</head>
<body>
<label for="country">DataList: Indique o país:</label>
<input list="CountriesList" name="country" id="country">
<datalist id="CountriesList">
<%
db.DummyDB db = new db.DummyDB("paises");
List<String> items = db.getData("",0 , 300);
Iterator<String> iterator = items.iterator();
while (iterator.hasNext())
		out.write("<option>"+(String) iterator.next()+ "</option>");
%>
</datalist>
<br>
<br>
<label for="month" >JavaScript com DataList: Indique o mês:</label>
<input id="month" name="month" list="months" >
<datalist id="months" >
</datalist>
<br>
<br>
<label for="mes" >DataList: Indique o mês:</label>
<input name="mes" list="meses" >
<datalist id="meses" >
<%
db.DummyDB dbmeses = new db.DummyDB("meses");
List<String> meses = dbmeses.getData("",0 , 12);
Iterator<String> mes = meses.iterator();
while (mes.hasNext())
		out.write("<option>"+(String) mes.next()+ "</option>");
%>

</datalist>
<br>
<br>
<label for="country">Select: Indique a palavra:</label>
<select>
<%
db.DummyDB dbpalavras = new db.DummyDB("palavras");
List<String> palavras = dbpalavras.getData("",0 , 100);
Iterator<String> palavra = palavras.iterator();
while (palavra.hasNext())
		out.write("<option>"+(String) palavra.next()+ "</option>");
%>
</select>


<script>
var str=''; // variable to store the options
var month = new Array("January","February","March","April","May","June","July","August",
"September","October","November","December");
for (var i=0; i < month.length;++i){
	str += '<option value="'+month[i]+'" />'; // Storing options in variable
}
var my_list=document.getElementById("months");
my_list.innerHTML = str;
</script>

</body>
</html>