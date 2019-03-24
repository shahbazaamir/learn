<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Banking</title>
<script src="/Consulting/jquery-2.0.2.js"></script>
<script type="text/javascript">
function loadMyAccounts(){
	$("#frame").attr("src", "../banking/Account/Accounts.jsp");
}
</script>
</head>
<body>
<div id="header" style=" height:100px ;  background: #99FF00 bottom repeat-x; border-bottom: 1px solid #444;">
		<h1><a href="/"><img src="../user/images/UserLogo.png" alt="Project Management System" style="float:left"></a></h1>
	</div>
	<div style="top100px ; left:100px ; width:150px">
		<table>
			<tr>
				<td style="background-color:66FF00 " onclick="loadMyAccounts()">
						My Accounts
				</td>
				<td style="background-color:66FF00 ">
						Add Beneficiary
				</td>
			</tr>
		</table>
	</div>
	<div style="top100px ; left:250px ; width:150px">
		<iframe id="frame" src="../banking/Account/Accounts.jsp" width="100%" height="300">
   		</iframe>
	</div>
</body>
</html>