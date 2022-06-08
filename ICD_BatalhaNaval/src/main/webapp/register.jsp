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
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100">
				<div class="login100-pic js-tilt" data-tilt>
				<br><br>
					<img src="images/img-01.png" alt="IMG">
				</div>

				<!-- FORM de inicio de sessao -->
				<form class="login100-form validate-form" method = "POST" action ="RegisterServlet">
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
					
					<div class="wrap-input100">
						<input class="inputimage" type="file" id="new_picture" name="new_picture" accept="image/png, image/jpeg" value="" placeholder="Foto de Perfil"><br>
						<span class="focus-input100"></span>
					</div>
					
					<div class="container-login100-form-btn">
						<input class="login100-form-btn" type = "submit" value = "Registar!"/>
					</div>

					<div class="text-center p-t-80">
						<a class="txt2" href="login.jsp">
							Já tem uma conta? Inicie sessão!
							<i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	

	
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