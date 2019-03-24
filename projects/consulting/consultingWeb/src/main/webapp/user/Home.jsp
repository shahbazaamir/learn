<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%
	String json = (String ) request.getAttribute("COMMON_RESPONSE");
%>
<script src="/Consulting/jquery-2.0.2.js"></script>
<script type="text/javascript">
	var json = eval('(' + '<%=json%>' + ')');

	console.log(json);
	
</script>
</head>

	

<body>
	<div id="header" style=" height:100px ;  background: #000 bottom repeat-x; border-bottom: 1px solid #444;">
		<h1><a href="/"><img src="../user/images/UserLogo.png" alt="Project Management System" style="float:left"></a></h1>
	</div>

	
	<div id="body" >
		 
		<div id="details">
			<a href="../user/PasswordManager.jsp">Password Manager</a>
			<a href="../user/Banking.jsp">Banking</a>
		</div>
		<div id="image" style="left:800px">
		 
			<img alt="" src="../user/images/HomeImage.jpg" style=" size: auto; height: 400px ; width: 400px">
			
		</div>
		
		
	</div>
	


</body>
</html>