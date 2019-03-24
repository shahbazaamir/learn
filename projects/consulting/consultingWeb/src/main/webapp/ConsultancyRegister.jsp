<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consultancy</title>
<script src="/Project/jquery-2.0.2.js"></script>
<script type="text/javascript">
	function initPage(){
		$("#someImage").hide();
		}
</script>
</head>
<body onload="initPage()">
	<span>Register Your Company</span>
	<div id="someImage">
		<img src="/Consulting/home.jpg">
	</div>
	<form id="commonForm" action = "/Consulting/CommonController" method="post">
		<input type="text" name="assosiateId">
		 <input type="text"	name="assosiateName"> 
		 <input type="submit">
	</form>
</body>
</html>