<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
</head>
<body>
<%
String errorCode =(String)request.getAttribute("errorCode");
if(errorCode==null){
%>
Fatal Error occured
<%}else if (errorCode.equals("1")){ %>
Please specify params
<%}else if (errorCode.equals("2")){ %>
Master object does not exist
<%} %>
</body>
</html>