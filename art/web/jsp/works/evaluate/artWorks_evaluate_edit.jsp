<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	var fulllink = contextpath + "works/artEvaluate.do";	
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
		if(!checkString(getElement('record.evaluateType'),"评价类型",30,true)) return;
		if(!checkString(getElement('record.evaluates'),"评价",2000,true)) return;
		return true;
	}
	
	function closedialog(ret) {
		var res=ret['ret'];
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
	<s:form action="artEvaluate" id="artEvaluateForm" method="post" namespace="/works">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="record.worksId" id="worksId"/>
			<div id="formDiv" class="formDiv" style="padding:0px;">
			<table cellpadding="0" cellspacing="0" class="formTable">
				<tr>
					<td width="25%" class="textR"><font color="red">*</font>评价类型：</td>
					<td width="75%"><s:select list="codeSets.EVAL_TYPE" listKey="value" listValue="codeName" name="record.evaluateType" /></td>
				</tr>
				<tr>
					<td class="textR" valign="top"><font color="red">*</font>评价：</td>
					<td colspan="2" valign="top">
						<s:textarea style="height:80px;" name="record.evaluates" ></s:textarea>
					</td>
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

<%@include file="/common/dialog.jsp"%>
<%@include file="/common/resize.jsp" %>
</body>
</html>