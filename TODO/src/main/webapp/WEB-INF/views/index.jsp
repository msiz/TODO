<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<title>TODO</title>
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
	<link rel="shortcut icon" href="<c:url value="/resources/favicon.ico"/>">
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
	
	<script>
		
		function showError(text)
		{
			$("#error_field").text(text);
			$("#error_div").show();
		}
		
		function auth(data)
		{
			if (data.token !== undefined)
			{
				$("#token").val(data.token);
				$("#auth_form").toggle();
				$("#list_table").toggle();
			}
			else if (data.error !== undefined)
			{
				showError(data.error);
			}
		}
		
		function error(XMLHttpRequest, textStatus, errorThrown)
		{
			showError(errorThrown);
		}
		
		function authRequest(url)
		{
			$.post(url,
					{
					    login: $("#login_field").val(),
					    password: $("#password_field").val()
					})
					.success(auth)
					.error(error);
		}
	
		function login()
		{
			authRequest("auth/login");
		}
		
		function register()
		{
			//authRequest("auth/register");
			authRequest("api/list");
		}
		
		function logout()
		{
			
		}

	</script>
</head>
<body>

	<div class="container">
		<div class="alert alert-error" id="error_div" style="display:none">
		  	<button type="button" class="close" data-dismiss="alert" onclick="$('#error_div').hide()">X</button>
		  	<h4>Error!</h4>
		  	<p id="error_field"/>
		</div>
	</div>

	<div class="container" id="auth_form">

        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="input-block-level" placeholder="Login" id="login_field" />
        <input type="password" class="input-block-level" placeholder="Password" id="password_field" />
        <button class="btn btn-large btn-primary" type="button" onclick="login()">Sign in</button>
        <button class="btn btn-large btn-success" type="button" onclick="register()">Sign up</button>

    </div>
    
   	<div class="container" id="list_table" style="display:none">

		<form class="form-inline">
			<div class="form-group">
			<label class="control-label" for="token">Your token:</label>
	        <input type="text" placeholder="Token" id="token" disabled="disabled" />
	        <button class="btn btn-small btn-primary" type="button" onclick="logout()">Logout</button>
	        </div>
        </form>

    </div>

</body>
</html>
