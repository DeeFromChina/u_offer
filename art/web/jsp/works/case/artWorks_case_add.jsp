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
	var fulllink = contextpath + "works/artCase.do";	
	var partSize;
	var i = 0;
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
		if(!checkString(getElement('record.caseName'),"个案名称",30,true)) return;
		if(!checkString(getElement('record.caseAuther'),"作者",30,true)) return;
		if(!checkString(getElement('record.caseTime'),"日期",30,true)) return;
		if(!checkString(getElement('record.researchTopic'),"研究主题",30,true)) return;
		if(!checkString(getElement('record.caseContent'),"内容",1000,true)) return;
		if(!checkFile()) return;
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
		calendar1 = initCalendar("caseTime","imgCaseTime");
	}
	
	function addFileRow(){
		 var t  = document.getElementById('tblUpload');
		 var rows = t.rows.length;
		 if(rows>=5){
			 alert("超过最大上传文件数！");
			 return;
		 } else {
			 var x=t.insertRow(rows);
			 var one=x.insertCell(0);
			 one.className="textL";
			 var two=x.insertCell(1);
			 one.innerHTML='<input type="file" id="file" class="checkFile" name="files" />';
			 two.innerHTML='<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
		 }
	  }
	
	function delRow(obj) { 
		var childNode=obj.parentNode.parentNode;
		var parentNode=obj.parentNode.parentNode.parentNode;
		parentNode.removeChild(childNode);
	}
	
	function checkFile(){
		var isPass = true;
		$(".checkFile").each(function(){
			if(!checkString(this,"附件",400,true)) {
				isPass = false;
				return false;
			}
			/* var format = this.value.split(".");
			var i = format.length;
			if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
				alert("只能上传图片格式！");
				isPass = false;
				return false;
			} */
		});
		return isPass;
	}
	
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="artCase" id="artCaseForm" method="post" namespace="/works"  enctype="multipart/form-data">
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="record.worksId" id="worksId"/>
			<div id="formDiv" class="formDiv" style="padding:0px;">
			<table cellpadding="0" cellspacing="0" class="formTable">
				<tr>
					<td width="25%" class="textR"><font color="red">*</font>个案名称：</td>
					<td width="75%"><s:textfield name="record.caseName"/></td>
				</tr>
				<tr>
					<td width="25%" class="textR"><font color="red">*</font>作者：</td>
					<td width="75%"><s:textfield name="record.caseAuther"/></td>
				</tr>
				<tr>
					<td width="25%" class="textR"><font color="red">*</font>日期：</td>
					<td width="75%" class="textL">
						<s:textfield name="record.caseTime" cssStyle="width:80%;" readonly="true" id="caseTime" />
						<img id="imgCaseTime" style="cursor: pointer;" src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif" />
					</td>
				</tr>
				<tr>
					<td width="25%" class="textR"><font color="red">*</font>研究主题：</td>
					<td width="75%"><s:textfield name="record.researchTopic"/></td>
				</tr>
				<tr>
					<td class="textR" valign="top"><font color="red">*</font>内容：</td>
					<td colspan="3" valign="top">
						<s:textarea style="height:80px;" name="record.caseContent" ></s:textarea>
					</td>
				</tr>
				<tr >
					<td><font color="red">*</font>附件：</td>
					<td colspan="3" class="textL">
					<table  id="tblUpload" cellpadding="0" cellspacing="0" width="100%"  class="formTable" align="left">
					   <tr>
						   <td class="textL">
							   <input type="file" name="files" id="file" class="checkFile" />
						   </td>
					      <td width="10%"><input type="button" style="width:50px;" value="增加" onclick="addFileRow()"/></td>
					   </tr>
					</table>
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