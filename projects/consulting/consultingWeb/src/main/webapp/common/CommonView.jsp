
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="org.project.log.LogWriter"%>
 <%
String respVO=(String)request.getAttribute("COMMON_RESPONSE");
LogWriter.writeLog(respVO);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>
<title>View Country</title>
<script type="text/javascript">
//var respCommonJson = {"paramHm":{"module":"BANKING","requestId":"AccountsSearch","accountNo":"0191","actionId":"5"},"sessionId":"1eFswIVlxBaXq-n9VRIxGVTI","sessionStatus":0,"userStatus":false,"responsePage":"/banking/Account/AccountsView.jsp","commonRespHm":{"ACCOUNT_SEARCH_RESULT":[{"accountNo":"019100000007","accountType":"casa","customer":{"cif":"001003156","customerName":"ramkumar"}},{"accountNo":"01910000008","accountType":"CASA","customer":{"cif":"001003157","customerName":"null"}}]}} ;
var respCommon = '<%=respVO%>';
//var respCommon = '{"paramHm":{"module":"BANKING","requestId":"CountrySearch","countryCode":"IN","actionId":"5"},"sessionId":"ob3KCtYyfVkFSCTK0XXd+aWV","sessionStatus":0,"userStatus":false,"responsePage":"/banking/country/CountryView.jsp","commonRespHm":{"COUNTRY_SEARCH_RESULT":[{"countryCode":"IN","country\"":"INDIA","currencyCode\"":"USD","banks\"":"[]"}]}}';
function initPage(){
		var respCommonJson = eval('('+respCommon+')');
		console.log(respCommonJson);
		var jaarr = respCommonJson.commonRespHm.SEARCH_RESULT;
		if(jaarr.length >0){
			displayAppDetails(jaarr);
		}else{ 
			document.getElementById("allRecords").innerHTML=" No Records Found ";
		}

}
function displayAppDetails(responseTxt){
	var tabHtml = createRow(responseTxt);
		document.getElementById("allRecords").innerHTML=tabHtml;
	}
	</script>
	</head>
	<body onload="initPage()">
	<form id="countryView" action="">
		<div class="viewBox" >
		<div id="allRecords">
		
		</div>
		</div>
	</form>
	</body>
	</html>