<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Method"%>
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
boolean isError=false;
Object tableObject=null;
String className=request.getParameter("className");
if(className==null){
	isError=true;
	request.setAttribute("errorCode", "1");
}
Class<Object> tableClass=null;
if(!isError){
try{
	tableClass=(Class<Object>)Class.forName(className);
}catch(ClassNotFoundException e){
	isError=true;
	request.setAttribute("errorCode", "2");
}
}
if(!isError){
	tableObject=tableClass.newInstance();
	System.out.println("tableObject:"+tableObject);
	Map requestMap=request.getParameterMap();
	Iterator i=requestMap.entrySet().iterator();
	while(i.hasNext()){
		Map.Entry pair=(Map.Entry)i.next();
		Method  m=null;
		try{
			m=tableClass.getDeclaredMethod("set"+pair.getKey(), String.class);//new Method(tableClass,"get"+(String)pair.getKey(),pair.getValue());
		}catch(NoSuchMethodException ee ){
			continue;
		}
				m.invoke(tableObject,((String[])pair.getValue())[0]);
				
	}
	Class c= Class.forName("org.myProject.master.MasterAdapter");
	Object o=c.newInstance();
	Method m =c.getDeclaredMethod("save",Object.class);
	Object res=m.invoke(o,  tableObject);
	
	
}
if(isError){
	request.getRequestDispatcher("errorPage.jsp").forward(request, response);
}
%>
</body>
</html>