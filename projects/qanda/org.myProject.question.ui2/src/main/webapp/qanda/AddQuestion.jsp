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
	document.addQuestion.submit();
}
function modify(){
	$('[name=actionId]').val("4");
	document.addQuestion.submit();
}
function getSubjects(){
	$.ajax({
		url:"/MyProject4/CommonServlet"	,
		data:"actionId=3&questionId="+$('[name="questionId"]').val(),
		success:function (responseText){setValues(responseText)},
		failure:function (responseText){showAlert(responseText)}
	}
	);
}
function load(){
	$.ajax({
		url:"/MyProject4/CommonServlet"	,
		data:"actionId=3&questionId="+$('[name="questionId"]').val(),
		success:function (responseText){setValues(responseText)},
		failure:function (responseText){showAlert(responseText)}
	}
	);
}
function clearAll(){
	$('[name="questionId"]').val("");
	$('[name="question"]').val("");
	$('[name="optionA"]').val("");
	$('[name="optionB"]').val("");
	$('[name="optionC"]').val("");
	$('[name="optionD"]').val("");
	$('[name="answer"]').val("");
	$('[name="explanation"]').val("");
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
<form id="addQuestion" name="addQuestion" action="/MyProject4/CommonServlet">
<div style="padding: 6%;">
	<table class="table">
		<tr class="row">
			<td>
				Subject
			</td>
			<td>
				<select name="subject" id="subject" >
					<option value="jpa">JPA</option>
				</select>
			</td>
			
		</tr>
		<tr class="row">
			<td>
				Question Id
			</td>
			<td>
				<input type="text" name="questionId" id="questionId" >
			</td>
			<td>
				<input type="button" value="Load" onclick="load()">
			</td>
		</tr>
		<tr>
			<td>
				Question
			</td>
			<td>
				<textarea id="question"  name="question" rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Option A
			</td>
			<td>
				<textarea id="optionA"  name="optionA"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Option B
			</td>
			<td>
				<textarea id="optionB"  name="optionB"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Option C
			</td>
			<td>
				<textarea id="optionC"  name="optionC"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Option D
			</td>
			<td>
				<textarea id="optionD"  name="optionD"  rows="4" cols="100"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				Answer
			</td>
			<td>
				<input type="text" id="answer"  name="answer"  maxlength="4">
			</td>
		</tr>
		<tr>
			<td>
				Explanation
			</td>
			<td>
				<textarea id="explanation"  name="explanation"  rows="4" cols="100"></textarea>
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
				<input type="hidden" name="actionId" value="2" >
			</td>
		</tr>
	</table>
	</div>
</form>
</div>
</body>
</html>