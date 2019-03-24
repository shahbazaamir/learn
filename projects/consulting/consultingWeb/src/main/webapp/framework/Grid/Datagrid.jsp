<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<title>Sample grid</title>
<script src="/Consulting/jquery-2.0.2.js"></script>
<script type="text/javascript">
/*
$(document).ready(function() {
    var groups = ['A','B','C','D','E','F','G'];
    for (var groupLetter in groups){
        var myClone = $('#template').clone();
        myClone.attr("id", "template-"+groups[groupLetter]);

        var index = 1;
        myClone.find("input[type^=radio]").each(function() {
            var myName = $(this).attr("name");
            $(this).attr("name", myName+groups[groupLetter]+index++);
        });

        myClone.prependTo('#placeholder' + groups[groupLetter]);
    }
});
*/
var gridSerialNo =  0;
function addNew(){
	var myClone = $('#template').clone();	
	myClone.find("input").each(function() {
        var myName = $(this).attr("name");
        $(this).attr("name", myName+  (++gridSerialNo));
    });

	$('#dataGrid tr:last').after(myClone);
}

</script>
</head>
<body>
<form action="/CommonEntryServlet">
<input type="hidden" name="actionId" value="Test">
<table id="dataGrid">
	<tr id="template" >
		<!-- elements  -->
		<td>
			id :
		</td>
		<td>
			<input type="text" name="id">
		</td>
		<td>
			name :
		</td>
		<td>
			<input type="text" name="name">
		</td>
	</tr>
</table>
<table>
	<tr>
		<td>
			<input type="button" value="Add New" onclick="addNew()">
			<input type="submit"  value="Save"> 
		</td>
	</tr>
</table>
</form>
</body>
</html>