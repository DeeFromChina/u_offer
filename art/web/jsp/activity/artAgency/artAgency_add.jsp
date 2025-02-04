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
		    var url =fulllink+"?action=SAVE";
		  	document.forms[0].action=url;
			document.forms[0].submit();
		}
	}
	
	function checkFormInput(){
		//if(!checkString(getElement('form.record.agencyCName'),"机构中文名",30,true)) return;
		return true;
	}
	
	function closedialog(ret) {
		var country=ret['country'];
		if(ret=='true') {
			goSearch();
		}
		if(editMode=="Country"){
	    	 getElement('record.countryId').value=ret['str'];
	    	 getElement('record.countryName').value=ret['str1'];
	     }
		editMode="";
	}

	function goArtCountry(ret){
		if(ret == 'choose'){
			editMode="Country";
			var url_link=contextpath+'system/artCountry.do?record.type=1&query.parameters.type=1';
		    openWindow("选择国家",url_link,0.8,0.8);
		}
		if(ret == 'cancel'){
			$$("countryName").value = "";
			$$("countryId").value = "";
		}
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
			<s:hidden name="record.artistId" id="artistId"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="20%" >机构中文名：</td>
					<td width="80%" class="textL">
						<s:textfield name="form.record.agencyCName"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >机构英文名：</td>
					<td width="35%" class="textL">
						<s:textfield name="form.record.agencyEName"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >国家：</td>
					<td width="35%" class="textL">
						<s:textfield readonly="true" name="record.countryName" id="countryName" style="width:75%;" />
  	                    <s:hidden name="record.countryId" id="countryId"/>
  	                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtCountry('choose')">选择</gl:button>
  	                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtCountry('cancel')">清除</gl:button>
					</td>
				</tr>
				<tr>
					<td class="textR" width="20%" >城市：</td>
					<td width="35%" class="textL">
						<s:textfield name="form.record.city"/>
					</td>
				</tr>
				<tr>
					<td class="textR" valign="top">机构类型：</td>
					<td><s:select list="codeSets.AGENCY_TYPE" listKey="value" listValue="codeName" name="record.agencyType"/></td>
			    </tr> 
		 	</table>
	</div>
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>&nbsp;
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>

</body>
</html>