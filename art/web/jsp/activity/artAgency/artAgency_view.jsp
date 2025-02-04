<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "system/artAgency.do";
	
	var ret=new Object();

	function goCancel(){
		parent.closedialog('false');
		
	}

	function goSubmit() {
		if(checkFormInput()){
		    var url =fulllink+"?action=UPDATE";
		  	document.forms[0].action=url;
			document.forms[0].submit();
		}
	}
	
	function checkFormInput(){
		if(!checkString(getElement('form.record.agencyCName'),"机构中文名",30,true)) return;
		return true;
	}
	
	function closedialog(ret) {
		var country=ret['country'];
		if(ret=='true') {
			goSearch();
		}
		if(country==true){
	    	 getElement('record.countryId').value=ret['str'];
	    	 getElement('record.countryName').value=ret['str1'];
	     }
		editMode="";
	}

	function goArtCountry(){
		var url_link=fulllink+'?action=COUNTRY&ids='+$$("artistId").value;
	    openWindow("选择国家",url_link,200,300);	
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}
	

	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtistCollect" id="ArtistCollectForm" method="post" namespace="/artist">
			<s:hidden name="record.id" id="id"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="20%" ><font color="red">*</font>机构中文名：</td>
					<td width="80%" class="textL">
						<s:property value="form.record.agencyCName"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >机构英文名：</td>
					<td width="35%" class="textL">
						<s:property value="form.record.agencyEName"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >国家：</td>
					<td width="35%" class="textL">
						<s:property value="record.countryName"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >城市：</td>
					<td width="35%" class="textL">
						<s:property value="form.record.city"/>
					</td>
				</tr>
				<tr>
					<td class="textR" valign="top">机构类型：</td>
					<td class="textL"><s:property value="record.agencyType"/></td>
			    </tr> 
		 	</table>
	</div>
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>

</body>
</html>