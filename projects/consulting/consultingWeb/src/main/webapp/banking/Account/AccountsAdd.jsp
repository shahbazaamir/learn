<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>

<title>Accounts Add</title>
</head>
<body>
<form id="accountAdd" action="/Consulting/CommonController" method="post">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Add Accounts 
		</th>
	</tr>
		<tr>
			<td  >Account No</td><td><input type="text" name="accountNo" id="accountNo"></td>
		</tr>
		<tr>
			<td  >Customer Information Folio</td><td><input type="text" name="cif" id="cif"></td>
		</tr>
		<tr>
			<td  >Account Type  </td><td><input type="text" name="accountType" id="accountType"></td>
		</tr>
		<tr>
			<td  >Bank Code</td><td><input type="text" name="bankCode" id="bankCode"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="AccountsAdd" >
				
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