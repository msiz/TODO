<%@page import="com.sms.todo.model.User"%>
<%@page import="com.sms.todo.model.Task"%>
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
		
		function showSuccess(text)
		{
			$("#error_div").hide();
			$("#success_div").show();
			$("#success_field").text(text);
		}
	
		function showError(text)
		{
			$("#success_div").hide();
			$("#error_field").text(text);
			$("#error_div").show();
		}
		
		
		function error(XMLHttpRequest, textStatus, errorThrown)
		{
			showError(errorThrown);
		}
		
		function ajax(type, url, params, success)
		{
			$.ajax(
			{
			    type: type,
			    url: url,
			    data: params,
			    headers: 
		    	{
			        "Token" : $("#token").val()
			    }
			})
			.success(function(data)
			{
				if (data.error !== undefined && data.error !== null)
				{
					showError(data.error);
				}
				else
				{
					if (data.success !== undefined && data.success !== null)
					{
						showSuccess(data.success);
					}
					$("#error_div").hide();
					success(data);
				}
			})
			.error(error);
		}
		
		function auth(data)
		{
			if (data.data !== undefined && data.data !== null)
			{
				$("#token").val(data.data);
				$("#auth_form").toggle();
				$("#list_table").toggle();
				$("#login_field").val("");
				$("#password_field").val("");
				list();
			}
		}
		
		function authRequest(type, checkMax)
		{
			if (validateUserName(checkMax) && validatePassword(checkMax))
			{
				ajax(type, "auth/", 
				{ 
					login: $("#login_field").val(), 
					password: $("#password_field").val() 
				}, 
				auth);
			}
		}
	
		function login()
		{
			authRequest("GET", false);
		}
		
		function register()
		{
			authRequest("PUT", true);
		}
		
		function logout()
		{
			ajax("POST", "auth/logout", null, function(data)
			{
				$("#token").val("");
				$("#table_body tr").remove(); 
				$("#auth_form").toggle();
				$("#list_table").toggle();
			});
		}
		
		function list()
		{
			ajax("GET", "api/list", null, function(data)
			{
				for (var i in data)
				{
					appendTask(data[i]);
				}
			});
		}
		
		function add()
		{
			var result = validateTaskLength($("#task").val());
			if (result !== undefined)
			{
				showError(result);
			}
			else
			{
				ajax("PUT", "api/list", {task: $("#task").val()}, function(data)
				{
					appendTask(data.data);
					$("#task").val("");
				});
			}
		}
		
		function del(id)
		{
			ajax("DELETE", "api/list/" + id, null, function(data)
			{
				$("#row" + id).remove();
			});
		}
		
		function appendTask(task)
		{
			$("#table_body").append('<tr id="row' + task.id + '"><td class="span8">' + 
					'<a href="#" id="task' + task.id+ '" data-type="text" data-pk="' + task.id + '" data-name="task" data-url="api/list/' + task.id + '" data-original-title="Enter task name">' + task.task + '</a>' 
					+ '</td><td class="span2">' + 
					'<a href="#" id="priority' + task.id+ '" data-type="text" data-pk="' + task.id + '" data-name="task" data-url="api/list/' + task.id + '" data-original-title="Enter priority">' + task.priority + '</a>' 
					+ '</td><td class="span1">' +
					'<button class="btn btn-small btn-danger" type="button" onclick="del(' + task.id + ')">Delete</button>'
					+ '</td></tr>');
			$("#task" + task.id).editable(
			{
				params: function(params) 
				{
				    var data = {};
				    data['task'] = params.value;
				    data['token'] = $("#token").val();
				    return data;
			  	},
				validate: validateTaskLength
			});
			$("#priority" + task.id).editable(
			{
				params: function(params) 
				{
				    var data = {};
				    data['priority'] = params.value;
				    data['token'] = $("#token").val();
				    return data;
			  	},
				validate: validateTaskPriority
			});
		}
		
		function validateTaskLength(value)
		{
			if(value == "")
		    	return 'Task name cannot be empty.';
		    if(value.length > <%= Task.MAX_LENGTH %>)
		    	return 'Task name is too long. Maximal length: <%= Task.MAX_LENGTH %>.';
		}
		
		function validateTaskPriority(value)
		{
			var n = ~~Number(value);
		    if(String(n) !== value || n < <%= Task.MIN_PRIORITY %> || n > <%= Task.MAX_PRIORITY %>)
		    	return 'Priority should be a number between <%= Task.MIN_PRIORITY %> and <%= Task.MAX_PRIORITY %>.';
		}
		
		function validateLength(value, max, emptyMessage, longMessage, checkMax)
		{
			var error = null;
			
			if (value.length == 0)
			{
				error = emptyMessage;
			}
			else if (checkMax && value.length > max)
			{
				error = longMessage;
			}
			
			if (error !== null)
			{
				showError(error);
				return false;
			}
			else
			{
				return true;
			}
		}
		
		function validateUserName(checkMax)
		{
			return validateLength($("#login_field").val(), <%= User.MAX_USERNAME_LENGTH%>, "Login cannot be empty.", "Login is too long.", checkMax);
		}
		
		function validatePassword(checkMax)
		{
			return validateLength($("#password_field").val(), <%= User.MAX_PASSWORD_LENGTH%>, "Password cannot be empty.", "Password is too long.", checkMax);
		}
		
		$.fn.editable.defaults.success = function(data) 
		{ 
			if (data.error !== undefined && data.error !== null)
			{
				showError(data.error);
			}
			else if (data.success !== undefined && data.success !== null)
			{
				showSuccess(data.success);
			}
		};
		
		$.fn.editable.defaults.error = error;
		
	</script>
</head>
<body>

	<div class="container">
		<div class="alert alert-error" id="error_div" style="display:none">
		  	<button type="button" class="close" onclick="$('#error_div').hide()">X</button>
		  	<h4>Error!</h4>
		  	<p id="error_field"/>
		</div>
		<div class="alert alert-success" id="success_div" style="display:none">
		  	<button type="button" class="close" onclick="$('#success_div').hide()">X</button>
		  	<h4>Success!</h4>
		  	<p id="success_field"/>
		</div>
	</div>

	<div class="container" id="auth_form">

        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="input-block-level" placeholder="Login" id="login_field" />
        <input type="password" class="input-block-level" placeholder="Password" id="password_field" />
        <button class="btn btn-large btn-primary" type="button" onclick="login()">Sign in</button>
        <button class="btn btn-large btn-success" type="button" onclick="register()">Sign up</button>

    </div>
    
   	<div class="container" id="list_table" style="display:none; padding-top: 10px">

		<form class="form-inline">
			<div class="form-group">
			<label class="control-label">Your token:</label>
	        <input type="text" placeholder="Token" id="token" disabled="disabled" style="width: 83%" />
	        <button class="btn btn-small btn-warning" type="button" onclick="logout()">Logout</button>
	        </div>
        </form>
        
        <form class="form-inline">
			<div class="form-group">
			<label class="control-label" for="task">Task:</label>
	        <input type="text" placeholder="Task name" id="task" style="width: 87%" />
	        <button class="btn btn-small btn-success" type="button" onclick="add()">Create</button>
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
