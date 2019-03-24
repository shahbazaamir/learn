/**
 * Commonly used scripts
 */

function clearForm(obj) {
//	HTMLCollection forms11 = new HTMLCollection();
	//forms11[0] = obj ;
	
	//document.forms[0]
}

var testJson = {"key1":"value1","key2":"value2"};
function createTable(jsonArr){
	var str = "<table>";
	for(var i=0 ;i<jsonArr.length ; i++){
		var jsonObj = jsonArr[i];
		for (var keySet in jsonObj ){
			str = str + "<tr><td>" + keySet + "</td>" ;
			str = str + "<td>" + jsonObj[keySet] + "</td></tr>";
		}
	}
	str=str+"</table>";
	return str ;
	
}


function createRow(jsonArr){
	var str = "<table><tr>";
	for (var keySet in jsonArr[1] ){
		str = str + "<th>" + keySet + "</th>" ;
	}
	str = str + "</tr>";
	
	for(var i=0 ;i<jsonArr.length ; i++){
		var rowClass ="row"+(i%2);
		var jsonObj = jsonArr[i];
		str = str + "<tr class='"+rowClass+"'>";
		for (var keySet in jsonObj ){
			str = str + "<td>" + jsonObj[keySet] + "</td>";
		}
		str = str + "</tr>";
	}
	str=str+"</table>";
	return str ;
	
}