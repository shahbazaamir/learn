<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>

<title>Accounts Search</title>
</head>
<body>
<form id="accountSearch" action="/Consulting/CommonController">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Search Accounts 
		</th>
	</tr>
		<tr>
			<td  >Account No</td><td><input type="text" name="accountNo" id="accountNo"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="AccountsSearch" >
				
			</td>
		</tr>
		
		<tr>
			<td><input type="submit" value="submit" ></td>	
		</tr>
		<tr>
			
		</tr>
	</table>
	</div>
	</div>
</form>

</body>
</html>