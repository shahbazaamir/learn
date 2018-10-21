<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
function login(){
	document.forms[0].submit();
}

</script>
</head>
<body>
<form id="loginForm" action="/MyProject4/CommonServlet" method="post">
	<table>
		<tr>
			<td>
				Enter userid:
			</td>
			<td>
				<input type="text" name="userId" id="userId" >
			</td>
		</tr>
		<tr>
			<td>
				Enter password:
			</td>
			<td>
				<input type="password" name="pass" id="pass" >
			</td>
		</tr>
		<tr>
			<td>
				Admin Login:
			</td>
			<td>
				<input type="button" value="login" onclick="login()" >
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="1" >
			</td>
		</tr>
	</table>
	</form>
</body>
</html>