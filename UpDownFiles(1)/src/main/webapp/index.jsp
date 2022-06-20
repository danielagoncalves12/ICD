<%@page import="bean.Dir"%>
<%@page import="java.io.File,java.net.URLEncoder,java.nio.charset.StandardCharsets"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<title>Demonstração de manipulação de ficheiros</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
</head>

<%
String msg = (String) request.getAttribute("message");
	System.out.println(">>" + msg);
	if (msg != null)
		out.println("<script>alert(decodeURIComponent('" + URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()) + "').replace( /\\+/g, ' ' ));</script>");
%>

<%!//devolve array de strings com lista de imagens na pasta indicada em argumento
	public static String[] getFiles(String pasta) {
		int j = 0;
		File folder = new File(pasta);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null)
			return null;
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				j = j + 1;
		String[] files = new String[j];
		j = 0;
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				files[j++] = listOfFiles[i].getName();
		return files;
	}%>
<script>
	function doMethod(url, method, param, value) {
		var form = document.createElement('form');
		form.method = method;
		form.action = url;
		var input = document.createElement('input');
		input.type = "text";
		input.name = param;
		input.value = value;
		form.appendChild(input);
		document.getElementsByTagName('body')[0].appendChild(form);
		form.submit();
	};

	function eliminar(file) {
		/*var redirectUrl = "FileRemove?file=" + file;
		window.location = redirectUrl; 		*/  // funciona por GET
		if (confirm("Quer apagar a imagem?")) 
			doMethod("FileRemove", "POST", "file", file);
	}
	
	  function resizeIFrameToFitContent(iFrame){
	    iFrame.width  = iFrame.contentWindow.document.body.scrollWidth;
	    iFrame.height = iFrame.contentWindow.document.body.scrollHeight;

	  }
	  // https://blog.logrocket.com/the-ultimate-guide-to-iframes/
	  // document.write('<iframe onload="resizeIFrameToFitContent(this);" src="/dragDrop/index.html" title="Acesso Drag and Dropn" style="border:none;">Browser not compatible. </iframe>');
</script>
<body>
	<br>
	<form action="FileUpload" method="post" enctype="multipart/form-data">
	<fieldset>
    <legend> Seleção de imagens </legend>
		<input type="file"
			title="Selecione uma ou mais imagens" name="fileName" value="Search"
			multiple accept="image/*" required> 
			<input title="Carrega os imagens indicados" type="submit" value="Submeter">
	</fieldset>
	</form>
	<hr>
	<%
	String[] ficheiros = getFiles(bean.Dir.getDir(request));
	if(ficheiros!=null) {
		%><h3>Existem <%=ficheiros.length%> imagens...</h3><%
			for (int i = 0; i < ficheiros.length; i++) {
	%>
		<img ondblclick="eliminar('<%=ficheiros[i]%>');"
			title="Clique duplo para apagar (<%=ficheiros[i]%>)!" id="<%=ficheiros[i]%>"
			src="FileDownload?file=<%=ficheiros[i]%>">
		<%
		}
			}
			else{
		%>
	<h3>Não existem imagens em:&nbsp;'<%=bean.Dir.getDir(request)%>'</h3>	
	<%} %>
	<hr>
</body>
</html>