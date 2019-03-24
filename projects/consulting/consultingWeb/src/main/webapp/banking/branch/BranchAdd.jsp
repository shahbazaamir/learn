<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>

<title>Branch Add</title>
</head>
<body>
<form id="accountAdd" action="/Consulting/CommonController" method="post">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Add Branch 
		</th>
	</tr>
		<tr>
			<td  >Branch Code</td><td><input type="text" name="branchCode" id="branchCode"></td>
		</tr>
		<tr>
			<td  >Branch Name</td><td><input type="text" name="branchName" id="branchName"></td>
		</tr>
		<tr>
			<td  >Branch Address</td><td><input type="text" name="branchAddress" id="branchAddress"></td>
		</tr>
		<tr>
			<td  >SWIFT Code</td><td><input type="text" name="swiftCode" id="swiftCode"></td>
		</tr>
		<tr>
			<td  >Bank Code </td><td><input type="text" name="bankCode" id="bankCode"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="BranchAdd" >
				
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