<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ajax Response</title>
<script src="/Consulting/jquery-2.0.2.js"></script>
<script type="text/javascript">
	function test(variable){
		eval(variable + "();");
		
	}
	function test2(){
		console.log("hello");
	}
	test("test2");
</script>
</head>
<body>
	
</body>
</html>