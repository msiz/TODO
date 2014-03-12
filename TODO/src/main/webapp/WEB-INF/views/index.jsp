<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<title>TODO</title>
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/css/bootstrap-editable.css"/>" rel="stylesheet">
	<link rel="shortcut icon" href="<c:url value="/resources/favicon.ico"/>">
	<script src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap-editable.min.js"/>"></script>
	
	<script>
		
		function showError(text)
		{
			$("#error_field").text(text);
			$("#error_div").show();
		}
		
		function auth(data)
		{
			if (data.success !== undefined && data.success !== null)
			{
				$("#token").val(data.success);
				$("#auth_form").toggle();
				$("#list_table").toggle();
				$("#error_div").hide();
				list();
			}
			else if (data.error !== undefined)
			{
				showError(data.error);
			}
		}
		
		function list()
		{
			$.post("api/list",
					{
					    token: $("#token").val()
					})
					.success(function(data)
							{
								if (data.error !== undefined && data.error !== null)
								{
									showError(data.error);
								}
								else
								{
									for (var i in data)
									{
										appendTask(data[i]);
									}
								}
							})
					.error(error);
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
			authRequest("auth/register");
		}
		
		function logout()
		{
			$("#token").val("");
			$("#table_body tr").remove(); 
			$("#auth_form").toggle();
			$("#list_table").toggle();
			$("#error_div").hide();
		}
		
		function del(id)
		{
			$.post("api/delete",
					{
					    token: $("#token").val(),
					    task: id
					})
					.success(function(data)
							{
								if (data.error !== undefined && data.error !== null)
								{
									showError(data.error);
								}
								else
								{
									$("#row" + id).remove();
								}
							})
					.error(error);
		}
		
		function add()
		{
			$.post("api/create",
					{
					    token: $("#token").val(),
					    task: $("#task").val()
					})
					.success(function(data)
							{
								if (data.error !== undefined && data.error !== null)
								{
									showError(data.error);
								}
								else
								{
									appendTask(data);
								}
							})
					.error(error);
		}
		
		function appendTask(task)
		{
			$("#table_body").append('<tr id="row' + task.id + '"><td class="span8">' + 
					'<a href="#" id="task' + task.id+ '" data-type="text" data-pk="' + task.id + '" data-name="task" data-url="api/edit" data-original-title="Enter task name">' + task.task + '</a>' 
					+ '</td><td class="span2">' + 
					'<a href="#" id="priority' + task.id+ '" data-type="text" data-pk="' + task.id + '" data-name="task" data-url="api/edit" data-original-title="Enter priority">' + task.priority + '</a>' 
					+ '</td><td class="span1">' +
					'<button class="btn btn-small btn-danger" type="button" onclick="del(' + task.id + ')">Delete</button>'
					+ '</td></tr>');
			$("#task" + task.id).editable({
					params: function(params) {
					    var data = {};
					    data['id'] = params.pk;
					    data['task'] = params.value;
					    data['token'] = $("#token").val();
					    return data;
					  }
					});
			$("#priority" + task.id).editable({
					params: function(params) {
					    var data = {};
					    data['id'] = params.pk;
					    data['priority'] = params.value;
					    data['token'] = $("#token").val();
					    return data;
					  },
					validate: function(value) {
						var n = ~~Number(value);
					    if(String(n) !== value || n < 0)
					    	return 'This should be a natural number.';
						}
					});
			$("#error_div").hide();
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
			<label class="control-label">Your token:</label>
	        <input type="text" placeholder="Token" id="token" disabled="disabled" />
	        <button class="btn btn-small btn-warning" type="button" onclick="logout()">Logout</button>
	        </div>
        </form>
        
        <form class="form-inline">
			<div class="form-group">
			<label class="control-label" for="task">Task:</label>
	        <input type="text" placeholder="Task name" id="task" />
	        <button class="btn btn-success" type="button" onclick="add()">Create</button>
	        </div>
        </form>
        
        <table id="table_id" class="table table-striped table-bordered">
        	<thead>
	        	<tr>
	        		<td>Task</td>
	        		<td>Priority</td>
	        		<td></td>
	        	</tr>
        	</thead>
        	<tbody id="table_body">
        	</tbody>
        </table>

    </div>

</body>
</html>
