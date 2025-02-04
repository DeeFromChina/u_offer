<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "artist/ArtistAlbum.do";	
	
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
		if(!checkFile()) return;
		if(!checkString(getElement('form.record.photoDesc'), "描述", 200, true)) return;
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			/* if(!checkString(this,"相片",30,true)) {
				isPass = false;
				return false;
			} */
			var format = this.value.split(".");
			var length = this.value.length;
			var i = format.length;
			if(length != 0) {
				if(!checkString(this,"相片",30,true)) {
					isPass = false;
					return false;
				}
				if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
					alert("只能上传图片格式！");
					isPass = false;
					return false;
				}
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
	}
	
	function openImg(obj){
		var artistId = document.getElementById("artistId").value;
		openWindow("浏览作品图片",fulllink+'?action=SHOW&record.name='+obj+"&record.artistId="+artistId,0.8,0.8);
	}
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtistAlbum" id="ArtistAlbumForm" method="post" namespace="/artist" enctype="multipart/form-data">
		<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.artistId" id="artistId"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>相片：</td>
					<td class="textL" width="45%" >
						<input type="file" name="files" id="file" class="checkFile" />
					</td>
					<td colspan="2" />
				</tr>
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>相片显示：</td>
					<td class="textL">
						<s:iterator value="pic" id="p" status="pp">
							<a href="#" onclick="javascript:openImg('${p.realName}')" class="fileName" id="${p.realName}">${p.name}</a>
						</s:iterator>
					</td>
					<td colspan="2" />
				<tr>
					<td class="textR" valign="top"><font color="red">*</font>描述：</td>
					<td colspan="3" ><s:textarea name="form.record.photoDesc"></s:textarea></td>
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