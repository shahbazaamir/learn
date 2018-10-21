<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.annotation.Annotation"%>
<%@page import="javax.persistence.OneToMany"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%!
//PrintWriter out=null;
String fieldName=null;
Class<Object> tableObject=null;
public String getFieldName(String methodName){
	String fieldName=methodName.substring(3);
	char [] filedNameChar=fieldName.toCharArray();
	filedNameChar[0]=Character.toUpperCase(filedNameChar[0]);
	return new String(filedNameChar);
}

public String getProperFieldName(String fieldName){
	char [] filedNameChar=fieldName.toCharArray();
	filedNameChar[0]=Character.toUpperCase(filedNameChar[0]);
	return new String(filedNameChar);
}

boolean isError;
%>
<%
//out=response.getWriter();
//out.print("hi");
Object o=request.getAttribute("result");
String status=null;
if(o != null){
	status=(String)((Map)o).get("status");
	
}else{
	status="";
}

String className =request.getParameter("className");
if(className==null){
	request.setAttribute("errorCode", "1");
	request.getRequestDispatcher("errorPage.jsp").forward(request, response);
}else{
	try{
	 tableObject= (Class<Object>) Class.forName(className);
	}catch(ClassNotFoundException e){
		e.printStackTrace();
		isError=true;
		request.setAttribute("errorCode", "2");
		request.getRequestDispatcher("errorPage.jsp").forward(request, response);
	}

	if(!isError){
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=className %></title>
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
<form id="addQuestion" name="addQuestion" action="MasterAction.jsp">
<div style="padding: 6%;">
	<table class="table">
		<tr>
		<td>
			<input type="hidden" name="className" value="<%=className%>">
		</td>
		</tr>
		<% 
		
		//Annotation a=tableObject.getAnnotation(OneToMany.class);
		//a
		
		
		if(false)for(Field field : tableObject.getDeclaredFields()){
			OneToMany a=field.getAnnotation(OneToMany.class);
			if(a==null)continue;
			String foreignKey=a.mappedBy();
			foreignKey=getProperFieldName(foreignKey);
			%>
			<tr class="row">
			<td>
				<%
					 
					out.write(foreignKey);
				%>
			</td>
			<td>
				<input type="text" name="<%=foreignKey %>" id="<%=foreignKey %>" >
			</td>
		</tr>
			
			<%
		}
		
		for(Method method : tableObject.getDeclaredMethods()){
				String methodName=method.getName();
				if(!(methodName.indexOf("get")==0)) 	continue;
				if(method.getReturnType()==List.class)continue;
		%>
		
		
		
		<tr class="row">
			<td>
				<%
					 fieldName=  getFieldName(methodName);
					out.write(fieldName);
				%>
			</td>
			<td>
				<input type="text" name="<%=fieldName %>" id="<%=fieldName %>" >
			</td>
			
		</tr>
		<%} %>
		<tr>
			<td>
				<input type="button" value="Add" onclick="add()">
			</td>
			<td>
				<input type="button" value="Modify" onclick="modify()">
			</td>
			<td>
				<input type="button" value="Delete" onclick="modify()">
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
<%
	}
	} %>
</body>
</html>