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
		    var url =fulllink+"?action=UPDATE&record.fileName=" + fileName();
		  	document.forms[0].action=url;
			document.forms[0].submit();
		}
	}
	
	function checkFormInput(){
		if(!checkString(getElement('form.record.wordsTheme'),"文章主题",100,true)) return;
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
	
	function fileName(){
		var fileName = "";
		$(".fileName").each(function(){
			fileName = fileName+this.id+";"
		});
		if (fileName.length > 0) {
			fileName = fileName.substring(0, fileName.length - 1);
		}
		return fileName;
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
	<s:form action="ArtAuctionWords" id="ArtAuctionWordsForm" method="post" namespace="/auction" enctype="multipart/form-data">
		<s:hidden name="record.id"/>
		<s:hidden name="record.auctionId" id="auctionId"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>文章主题：</td>
					<td width="35%" ><s:textfield name="form.record.wordsTheme"/></td>
					<td class="textR" width="15%" >时间：</td>
					<td width="35%" class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					    <tr>
					       <td>
							<s:textfield name="form.record.wordsTime" readonly="true" id="wordsTime"/> 
							</td>
						    <td width="15px">
							  <img id="imgWordsTime" style="cursor:pointer;" src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
							</td>
  	                     </tr>
  	                  </table>    
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >来源：</td>
					<td colspan="3"><s:textfield name="form.record.wordsSource"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >附件：</td>
					<%-- <td class="textL">
						<s:iterator value="filesList" id="p" status="pp">
							<a href="#" onclick="javascript:download('${p.name},${p.newName}')" class="fileName" id="${p.newName}">${p.name}</a>
						</s:iterator>
					</td> --%>
					<td colspan="3" class="textL">
					<table  id="tblUpload" cellpadding="0" cellspacing="0" width="100%"  class="tbnBtnStyle" align="left">
					   <tr>
						   <td class="textL">
							   <input type="button" style="width:50px;" value="增加" onclick="addFileRow()"/>
						   </td>
						   <td width="10%"></td>
						   <s:iterator value="filesList" id="p" status="pp">
								<tr>
								<td class="textL">
									<a href="#" onclick="javascript:openThis('${p.name}')" class="fileName" id="${p.name}">${p.name}</a>
								</td>
								<td>
									<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>
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
	<gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>&nbsp;
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>

</body>
</html>