<%@page import="org.project.log.LogWriter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%
String respVO=(String)request.getAttribute("COMMON_RESPONSE");
LogWriter.writeLog(respVO);

if(respVO != null){
	//respVO = respVO.replaceAll("\"!!", "");
	//respVO = respVO.replaceAll("!!\"", "");
	//respVO = respVO.replaceAll("!!\\\"", "");
}

%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/Consulting/commonStyles.css" type="text/css">
<script src="/Consulting/jquery-2.0.2.js"></script>
<script src="/Consulting/commonScripts.js"></script>
<title>View Accounts</title>
<script type="text/javascript">

var respCommon = '<%=respVO%>';

console.log(respCommon);
function initPage(){
var respCommonJson = eval('('+respCommon+')');
console.log(respCommonJson);
jaarr = respCommonJson.commonRespHm.ACCOUNT_SEARCH_RESULT;
if(jaarr.length)
console.log(jaarr);
displayAppDetails(jaarr);
}
//respCommonJson = {"paramHm":{"accountNo":"019100000007","actionId":"4","benefId":""},"sessionId":"r+n8rcj22OWNKrgYPoTqPtsg","sessionStatus":0,"userStatus":false,"responsePage":"/banking/Account/AccountsView.jsp","commonRespHm":{"ACCOUNT":{"accountNo":"019100000007", "accountType":"casa", "customer":"model.Customer@3439d89b"}}}
//respCommonJson = {"paramHm":{"accountNo":"019100000007","actionId":"4","benefId":""},"sessionId":"m1FUsfdFIMvCiE+L8TTPlbu0","sessionStatus":0,"userStatus":false,"responsePage":"/banking/Account/AccountsView.jsp","commonRespHm":{"ACCOUNT":{"accountNo":"019100000007","accountType":"casa","customer":{"cif":"001003156","customerName":"ramkumar"}}}}; 
//document.forms[0].accountNo.value=respCommonJson.commonRespHm.
//document.forms[0].cif.value=respCommonJson.commonRespHm.ACCOUNT.customer.cif;
//document.forms[0].accountType.value=respCommonJson.commonRespHm.ACCOUNT.accountType;
//document.forms[0].accountNo.value=respCommonJson.commonRespHm.ACCOUNT.accountNo;
function loadDet(){
document.getElementById("accountNo").value=respCommonJson.commonRespHm.ACCOUNT.accountNo;
document.forms[0].cif.value=respCommonJson.commonRespHm.ACCOUNT.customer.cif;
document.forms[0].accountType.value=respCommonJson.commonRespHm.ACCOUNT.accountType;

}

//var json1 = {"paramHm":{"module":"BANKING","requestId":"AccountsSearch","accountNo":"0191","actionId":"5"},"sessionId":"1eFswIVlxBaXq-n9VRIxGVTI","sessionStatus":0,"userStatus":false,"responsePage":"/banking/Account/AccountsView.jsp","commonRespHm":{"ACCOUNT_SEARCH_RESULT":[{"accountNo":"019100000007","accountType":"casa","customer":{"cif":"001003156","customerName":"ramkumar"}},{"accountNo":"01910000008","accountType":"CASA","customer":{"cif":"001003157","customerName":"null"}}]}} ;
//jaarr = json1.commonRespHm.ACCOUNT_SEARCH_RESULT;
function displayAppDetails(responseTxt){
	//var resArr = $.parseJSON(responseTxt) ;
	var tabHtml = createRow(responseTxt);
	if(responseTxt.length >0){	
		document.getElementById("allRecords").innerHTML=tabHtml;
	}else{
		document.getElementById("allRecords").innerHTML=" No Records Found ";
	}
	//console.log(tabHtml);
	//console.log(resArr.length);
	//var resJson = eval('('+responseTxt+')');
	//console.log(responseTxt);
	/*
	document.forms[0].appName.value = resJson.APP_NAME;
	document.forms[0].appUserId.value =  resJson.APP_USER_ID;
	document.forms[0].appEmail.value =  resJson.APP_EMAIL;
	document.forms[0].appCode.value =  resJson.APP_CODE;
	document.forms[0].appPassword.value =  resJson.APP_PASSWORD;
	*/
}
</script>
</head>
<body onload="initPage()">
<form id="accountView" action="">
	<div class="addBox" >
	<div id="allRecords">
	
	</div>
	</div>
</form>
</body>
</html>