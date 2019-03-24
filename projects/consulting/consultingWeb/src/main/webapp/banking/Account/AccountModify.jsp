<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>
<script type="text/javascript">
function findAccount(obj){
	$.ajax({
		url : "/Consulting/CommonController",
		data : "isAjax=Y&actionId=5&countryCode="+document.forms[0].countryCode.value+"&requestId=AccountFind&module=BANKING",
		success :  function(responseTxt) { displayAppDetail(responseTxt); },
		failure :  function(responseTxt) {failAppDetail(responseTxt) ; },
		complete :  function(responseTxt) { overAppDetail(responseTxt); }
	});
} 
function displayAppDetail(responseTxt){
	console.log(responseTxt);
	var resJson = eval('('+responseTxt+')');
	document.forms[0].countryName.value = resJson.country;
	document.forms[0].currencyCode.value =  resJson.currencyCode;
}
function overAppDetail(responseTxt){
	console.log(responseTxt);
}
</script>
<title>Account Modify</title>
</head>
<body>
<form id="accountAdd" action="/Consulting/CommonController" method="post">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Modify Account 
		</th>
	</tr>
		
		<tr>
			<td  >Account No</td><td><input type="text" name="accountNo" id="accountNo"></td><td><input type="button" value="Find" onclick="findAccount(this)"></td>
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
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="AccountModify" >
				
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