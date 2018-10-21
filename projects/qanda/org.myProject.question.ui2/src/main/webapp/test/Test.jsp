<%@page import="java.io.File"%>
<%@page import="org.commonPluggin.web.LoginAction"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%


final File f = new File(LoginAction.class.getProtectionDomain().getCodeSource().getLocation().getPath());
out.print(f.getAbsolutePath());
%>
</body>
</html>