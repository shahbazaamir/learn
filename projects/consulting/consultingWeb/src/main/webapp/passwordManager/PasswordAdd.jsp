<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
String respVO=(String)request.getAttribute("COMMON_RESPONSE");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Password Manager</title>
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>
<script type="text/javascript">

var respCommon = '<%=respVO%>';
var respCommonJson = eval('('+respCommon+')');
console.log(respCommonJson);

function loadAppDetail(obj) {
		console.log("detail 1  :");
		$.ajax({
			url : "/Consulting/CommonController",
			data : "isAjax=Y&actionId=1&appCode="+document.forms[0].appCode.value,
			success :  function(responseTxt) { displayAppDetail(responseTxt); },
			failure :  function(responseTxt) {failAppDetail(responseTxt) ; },
			complete :  function(responseTxt) { overAppDetail(responseTxt); }
		});
	}
function loadAppDetails(obj) {
	console.log("details :");
	$.ajax({
		url : "/Consulting/CommonController",
		data : "isAjax=Y&actionId=2&appCode="+document.forms[0].appCode.value,
		success :  function(responseTxt) { displayAppDetails(responseTxt); },
		failure :  function(responseTxt) {failAppDetails(responseTxt) ; },
		complete :  function(responseTxt) { overAppDetails(responseTxt); }
	});
}
	function displayAppDetail(responseTxt){
		var resJson = eval('('+responseTxt+')');
		document.forms[0].appName.value = resJson.APP_NAME;
		document.forms[0].appUserId.value =  resJson.APP_USER_ID;
		document.forms[0].appEmail.value =  resJson.APP_EMAIL;
		document.forms[0].appCode.value =  resJson.APP_CODE;
		document.forms[0].appPassword.value =  resJson.APP_PASSWORD;
		
	}
	function overAppDetails(responseTxt){
		console.log(responseTxt);
	}
	function displayAppDetails(responseTxt){
		console.log(responseTxt);
		var resArr = $.parseJSON(responseTxt) ;
		console.log(resArr);
		var tabHtml = createRow(resArr);
		document.getElementById("allRecords").innerHTML=tabHtml;
	}
	function overAppDetail(responseTxt){
		console.log(responseTxt);
	}
	
	function clearForm(){
		document.forms[0].appName.value ="";
		document.forms[0].appUserId.value ="";
		document.forms[0].appEmail.value ="";
		document.forms[0].appCode.value ="";
		document.forms[0].appPassword.value ="";
	}
	function addRecord(){
		document.forms[0].actionId.value ="1";
		document.forms[0].submit;
	}
	function updateRecord(){
		document.forms[0].actionId.value ="2";
		document.forms[0].submit;
	}
</script>
</head>
<body>
<form  id="passwordManagerForm" action = "/Consulting/CommonController" method="post">
	<div class="addBox" >
	<div>
	<table>
	<tr>
		<th>
			Add Application Details 
		</th>
	</tr>
		<tr>
			<td  >Application Code</td><td><input type="text" name="appCode" ></td>
		</tr>
		<tr>
			<td  >Application Name</td><td><input type="text" name="appName" ></td>
		</tr>
		<tr>
			<td  >Application Email </td><td><input type="text" name="appEmail" ></td>
		</tr>
		<tr>
			<td  >Application User id </td><td><input type="text" name="appUserId" ></td>
		</tr>
		<tr>
			<td>Application Password </td><td><input type="text" name="appPassword" ></td>
		</tr>
		<tr>	
			<td><input type="hidden" name="actionId" value="2" ></td> 
		</tr>
		<tr>
			<td>
			<input type="button" value="add" onclick="addRecord()">
			<input type="button" value="update" onclick="updaeRecord()">
			<input type="button" name="loadApp" onclick="loadAppDetail(this)" value="Load App Details ">
			</td>
			</tr><tr>
			<td><input type="button" value="Clear" onclick="clearForm(document.forms[0])"></td>
		</tr>
		<tr>
			<td>
				<input type="button" value="Get All Details"  onclick="loadAppDetails(this)">
			</td>
		</tr>
	</table>
	</div>
	</div>
</form>
<div>
	<span id="appCode"></span>
	<span id="appName"></span>
	<span id="appUserId"></span>
	<span id="appPassword"></span>
</div>
<div class="viewBox" >
<div id="allRecords"></div>
</div>
</body>
</html>