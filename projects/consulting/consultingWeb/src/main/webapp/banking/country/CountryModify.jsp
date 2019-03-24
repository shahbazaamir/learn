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
function findCountry(obj){
	$.ajax({
		url : "/Consulting/CommonController",
		data : "isAjax=Y&actionId=5&countryCode="+document.forms[0].countryCode.value+"&requestId=CountryFind&module=BANKING",
		success :  function(responseTxt) { displayAppDetail(responseTxt); },
		failure :  function(responseTxt) {failAppDetail(responseTxt) ; },
		complete :  function(responseTxt) { overAppDetail(responseTxt); }
	});
} 
function displayAppDetail(responseTxt){
	console.log(responseTxt);
	var a = eval('('+responseTxt+')') ;
	console.log(a);
	var resJson = eval('('+a+')');
	document.forms[0].countryName.value = resJson.country;
	document.forms[0].currencyCode.value =  resJson.currencyCode;
}
function overAppDetail(responseTxt){
	console.log(responseTxt);
}
</script>
<title>Country Modify</title>
</head>
<body>
<form id="accountAdd" action="/Consulting/CommonController" method="post">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Modify Country 
		</th>
	</tr>
		<tr>
			<td  >Country Code</td><td><input type="text" name="countryCode" id="countryCode"></td><td><input type="button" value="Find" onclick="findCountry(this)"></td>
		</tr>
		<tr>
			<td  >Country Name</td><td><input type="text" name="countryName" id="countryName"></td>
		</tr>
		<tr>
			<td  >Currency Code </td><td><input type="text" name="currencyCode" id="currencyCode"></td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
				<input type="hidden" name="module" value="BANKING" > 
				<input type="hidden" name="requestId" value="CountryModify" >
				
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