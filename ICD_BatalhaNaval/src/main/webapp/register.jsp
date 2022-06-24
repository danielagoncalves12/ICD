<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="bean.Check" import="socket.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Batalha Naval - Criar uma conta nova</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
<!--===============================================================================================-->

</head>
<body>
	
	<%
	String error = (String) session.getAttribute("error");
	if (error == null) error = "";
	%>
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100" style="padding-top: 100px">
				<div class="login100-pic js-tilt" data-tilt>
				<br><br>
					<img style="padding-top: 30%" src="images/img-01.png" alt="IMG">
				</div>

				<!-- FORM de inicio de sessao -->
				<form class="login100-form validate-form" method = "POST" action ="RegisterServlet" enctype="multipart/form-data">
					<span class="login100-form-title">
						Criar uma conta nova
					</span>

					<div class="wrap-input100 validate-input" data-validate = "Nome de utilizador é obrigatório.">
						<input class="input100" type = "text" name="new_username" id="new_username" placeholder="Nome de utilizador">
						<span class="focus-input100"></span>
						<span class="symbol-input100">
							<i class="fa fa-envelope" aria-hidden="true"></i>
						</span>
					</div>
					
					<div class="wrap-input100 validate-input" data-validate = "Nome público é obrigatório.">
						<input class="input100" type = "text" name="new_name" id="new_name" placeholder="Nome público">
						<span class="focus-input100"></span>
						<span class="symbol-input100">
							<i class="fa fa-envelope" aria-hidden="true"></i>
						</span>
					</div>

					<div class="wrap-input100 validate-input" data-validate = "Palavra-passe é obrigatório.">
						<input class="input100" type = "password" name="new_password" id="new_password" placeholder="Palavra-passe">
						<span class="focus-input100"></span>
						<span class="symbol-input100">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
					</div>
					
					<div>
						<hr>
						<p>Selecione a sua cor favorita: <span style="color: red">*</span></p>
						<input style="width: 100%; height:38px; border: 8px solid #E6E6E6; border-radius: 10px" type="color" id="new_color" name="new_color" placeholder="Cor Favorita" required><br>
						<p style="line-height: 10px; font-size: 10px"> </p>
						
						<p>Selecione a sua data de nascimento: <span style="color: red">*</span></p>
						<input style="width: 100%; height:38px; border: 8px solid #E6E6E6; border-radius: 10px" type="date" id="new_date" name="new_date" placeholder="Data de nascimento" required>
						<p style="line-height: 10px; font-size: 10px"> </p>
						
						<p>Selecione a sua foto de perfil (Opcional):</p>
						<input style="width: 100%; height:40px; border: 8px solid #E6E6E6; border-radius: 10px" type="file" onchange="show(this)" id="new_picture" name="new_picture" accept="image/png, image/jpeg" value="" placeholder="Foto de Perfil"><br>
						<img src="" width="100" height="100" id="showimg">
					</div>
					
					<p style="color:red"><%=error%></p>
					
					<div class="container-login100-form-btn">
						<input class="login100-form-btn" type = "submit" value = "Registar!"/>
					</div>

					<div class="text-center p-t-30">
						<a class="txt2" href="login.jsp">
							Já tem uma conta? Inicie sessão!
							<i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	
	function show(file) {
        var reader = new FileReader();  //Create file read object
        var files = file.files[0];      //Get the file in the file component
        reader.readAsDataURL(files);    //File read and install to base64 type
        reader.onloadend = function(e) {
            document.getElementById("showimg").src = this.result;
        }
    }
	
	</script>
	
<!--===============================================================================================-->	
	<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/bootstrap/js/popper.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/tilt/tilt.jquery.min.js"></script>
	<script >
		$('.js-tilt').tilt({
			scale: 1.1
		})
	</script>
<!--===============================================================================================-->
	<script src="js/main.js"></script>

</body>
</html>