<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
Object o=request.getAttribute("result");
String status=null;
if(o != null){
	status=(String)((Map)o).get("status");
	
}else{
	status="";
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="/MyProject4/jquery/jquery-1.11.3.js"></script>
<script src="/MyProject4/bootstrap/bootstrap.min.js"></script>
<link rel="/MyProject4/bootstrap/bootstrap.min.css">
<script type="text/javascript">

var status ='<%=status%>';
if(status != ""){
	alert(status);
}
function add(){
	document.addApp.submit();
}
function modify(){
	$('[name=actionId]').val("7");
	document.addApp.submit();
}
function load(){
	$.ajax({
		url:"/MyProject4/CommonServlet"	,
		data:"actionId=6&appId="+$('[name="appId"]').val(),
		success:function (responseText){setValues(responseText)},
		failure:function (responseText){showAlert(responseText)}
	}
	);
}
function clearAll(){
	$('[name="appId"]').val("");
	$('[name="app"]').val("");
	$('[name="appDescription"]').val("");
	$('[name="passCode"]').val("");
	
}
function showAlert(txt){
	alert("something failed"+txt);
}
function setValues(txt){
	clearAll();
	var valjsonArr=$.parseJSON(txt);
	if(valjsonArr.length ==1){
		valjson = valjsonArr[0];
		for(valjsonElementIndex in valjson ){
			//alert(valjson[valjsonElementIndex]);
			$('[name="'+valjsonElementIndex+'"]').val(valjson[valjsonElementIndex]);
		}
	}else{
		alert("multiple alert ");
	}
	
	
}
</script>
</head>
<body>
<div class="container">
<form id="addApp" name="addApp" action="/MyProject4/CommonServlet">
	<table class="table">
		<tr class="row">
			<td>
				App Id
			</td>
			<td>
				<input type="text" name="appId" id="appId" >
			</td>
			<td>
				<input type="button" value="Load" onclick="load()">
			</td>
		</tr>
		<tr>
			<td>
				App
			</td>
			<td>
				<textarea id="app"  name="app" rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				App Description
			</td>
			<td>
				<textarea id="appDescription"  name="appDescription"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Pass Code
			</td>
			<td>
				<textarea id="passCode"  name="passCode"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		
		<tr>
			<td>
				<input type="button" value="Add" onclick="add()">
			</td>
			<td>
				<input type="button" value="Modify" onclick="modify()">
			</td>
			<td>
				<input type="button" value="Clear" onclick="clearAll()">
			</td>
		</tr>	
		<tr>
			<td>
				<input type="hidden" name="actionId" value="5" >
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>