<%@ page import="java.net.URL"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Find the class </TITLE>
</HEAD>

<BODY>
<%
try{
String className = (String)request.getParameter("name");
String propFile = null;
URL url = null;

	if(className != null){
		if(!className.startsWith("/"))
			className = "/" + className;
		
		className = className.replace('.', '/');
		className = className + ".class";
		url = this.getClass().getResource(className);
	}else{
		className = (String)request.getParameter("prop");
		if(className == null){
			className = (String)request.getParameter("dispprop");
			java.util.Properties prop = new java.util.Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream(className));
			out.println(prop+"<BR>");
		}
		url = this.getClass().getClassLoader().getResource(className);

	}
    
    
	String path = null;
	if(url != null){
		path = "\nClass '" + className + "' found in \n'" + url.getFile() + "'";
	}else{
		path = "\nClass '" + className + "' not found in \n'"
						   + System.getProperty("java.class.path") + "'";
	}

	out.println("<html><body><table width=100% ><tr><td>"+path+"</td></tr></table>");
}catch(Exception e){
   e.printStackTrace();
   out.println(e);
}
%>
</BODY>
</HTML>

