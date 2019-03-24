<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>
<title>Customer Add</title>
</head>
<body>
<form id="customerAdd" action="/Consulting/CommonController">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Add Customer 
		</th>
	</tr>
		
		<tr>
			<td  >Customer Information Folio</td><td><input type="text" name="cif" id="cif"></td>
		</tr>
		<tr>
			<td  >Customer Name</td><td><input type="text" name="customerName" id="customerName"></td>
		</tr>
		<tr>
			<td  >SWIFT Code</td><td><input type="text" name="swiftCode" id="swiftCode"></td>
		</tr>
		<tr>
			<td  >Branch Code</td><td><input type="text" name="branchCode" id="branchCode"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="customerAdd" >
				
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