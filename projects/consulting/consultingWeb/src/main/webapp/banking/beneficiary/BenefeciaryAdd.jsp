<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="/Consulting/CommonController">
	<table>
		<tr>
			<td>Beneficiary Name :<input type="text" name="benefName"></td>
		</tr>
		<tr>
			<td>Beneficiary Id :<input type="text" name="benefId"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="4" > 
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="Submit">
			</td>
		</tr>
	</table>
	</form>
</body>
</html>