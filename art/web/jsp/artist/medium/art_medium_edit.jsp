<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	var fulllink = contextpath + "artist/medium.do";	
	var partSize;
	var i = 0;
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
		if (!checkString(getElement('record.chineseName'), "媒介名称", 50, true)) return;
		return true;
	}
	
	function closedialog(ret) {
		if(ret=='true') {
			goSearch();
		}
		editMode="";
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
	<s:form action="artMedium" id="artMediumForm" method="post" namespace="/artist">
			<s:hidden name="record.id"/>
			<s:hidden name="record.treeCode"/>
			<div id="formDiv" class="formDiv" style="padding:0px;">
			<table cellpadding="0" cellspacing="0" class="formTable">
				<tr>
					<td width="20%" class="textR"><font color="red">*</font>媒介名称：</td>
					<td width="80%"><s:textfield name="record.chineseName"/></td>
				</tr>
		 	</table>
		 </div>
</s:form>

<div id="btnDiv" class="btnDiv">
<gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>
     &nbsp;
	 <gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<script type="text/javascript"> 
 
</script>
<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>
</body>
</html>