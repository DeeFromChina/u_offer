<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "auction/ArtAuctionWords.do";	
	
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
		if(!checkString(getElement('form.record.wordsTheme'),"文章主题",30,true)) return;
		if(!checkString(getElement('form.record.wordsSource'),"来源",30,true)) return;
		if(!checkString(getElement('form.record.wordsTime'),"时间",30,true)) return;
		/* if(!checkFile()) return; */
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			if(!checkString(this,"相片",400,true)) {
				isPass = false;
				return false;
			}
			var format = this.value.split(".");
			var i = format.length;
			if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
				alert("只能上传图片格式！");
				isPass = false;
				return false;
			}
		});
		return isPass;
	}
	
	function closedialog(ret) {
		if(ret=='true') {
			goSearch();
		}
		editMode="";
	}

	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		calendar1 = initCalendar("wordsTime","imgWordsTime");
	}
	
	function download(allName) {
		document.forms[0].action = fulllink + "?action=DOWNLOADFILE&record.allName=" + allName;//&record.auctionId=" + auctionId + "
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
	
	function openThis(name) {
		var format = name.split(".");
		var i = format.length;
		if (format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png") {
			document.forms[0].action = fulllink + "?action=DOWNLOADFILE&record.name=" + name;//&record.auctionId=" + auctionId + "
			document.forms[0].target = "_self";
			document.forms[0].submit();
		} else {
			var auctionId = document.getElementById("auctionId").value;
			openWindow("浏览作品图片",fulllink+'?action=SHOW&record.name=' + name + "&record.auctionId="+auctionId,0.8,0.8);
		}
	}

	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtAuctionWords" id="ArtAuctionWordsForm" method="post" namespace="/auction">
		<s:hidden name="record.id"/>
		<s:hidden name="record.auctionId" id="auctionId"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>文章主题：</td>
					<td width="35%" ><s:textfield name="form.record.wordsTheme" disabled="true"/></td>
					<td class="textR" width="15%" ><font color="red">*</font>时间：</td>
					<td width="35%" class="textL">
						<s:textfield name="form.record.wordsTime" readonly="true" id="wordsTime" disabled="true"/> 
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>来源：</td>
					<td colspan ="3"><s:textfield name="form.record.wordsSource" disabled="true"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>附件名称：</td>
					<td colspan="3" class="textL">
					<table  id="tblUpload" cellpadding="0" cellspacing="0" width="100%"  class="formTable" align="left">
					<tr>
						<s:iterator value="filesList" id="p" status="pp">
							<tr>
								<td class="textL">
									<a href="#" onclick="javascript:openThis('${p.name}')" class="fileName" id="${p.name}">${p.name}</a>
								</td>
							</tr>
						</s:iterator>
					</tr>
					</table>
					</td>
				</tr>
		 	</table>
	</div>
	<div id="page_1" style="padding:1px;"> </div>
	<div id="page_2" style="padding:1px;"> </div>
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>

</body>
</html>