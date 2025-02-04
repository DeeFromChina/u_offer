<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "literature/ArtLiteratureMedium.do";	
	
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

	function ping(key){
		var val = "";
		$(".ten").each(function(){
			if(this.value == undefined || this.value == "" || this.value == " "){
				return;
			}
			if("form."+key == this.name){
				if(val != ""){
					val = val + ",";
				}
				val = val + this.value;
			}
		});
		if(val == ""){
			return "&" + key + "=" + "";
		}
		return "&" + key + "=" + val;
	}
	
	function checkFormInput(){
		if(!checkString(getElement('record.artistId'),"艺术家",100,true)) return;
		if(!checkString(getElement('form.record.literatureTitle'),"文献标题",200,true)) return;
		//if(!checkString(getElement('form.record.whenLong'),"时长",50,true)) return;
		if(!checkFile()) return;
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			/* if(!checkString(this,"附件",100,true)) {
				isPass = false;
				return false;
			} */
			if(this.value == ""){
				isPass = true;
				return true;
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
		var res=ret['ret'];
		if(res==true) {
	    	 getElement('record.artistId').value=ret['str'];
	    	 getElement('record.artistName').value=ret['str1'];
	     }
		if(ret=='true') {
			goSearch();
		}
		editMode="";
	}

	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}
	
	function addFileRow() {
		var t = document.getElementById('tblUpload');
		var rows = t.rows.length;
		if (rows >= 3) {
			alert("超过最大上传文件数！");
			return;
		} else {
			var x = t.insertRow(rows);
			var one = x.insertCell(0);
			one.className = "textL";
			var two = x.insertCell(1);
			one.innerHTML = '<input type="file" id="file" class="checkFile" name="files" />';
			two.innerHTML = '<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
		}
	}
	
	function delRow(obj) { 
		var childNode=obj.parentNode.parentNode;
		var parentNode=obj.parentNode.parentNode.parentNode;
		parentNode.removeChild(childNode);
	}

	function goArtArtist(){
		var url_link=contextpath+'literature/ArtLiteratureWords.do?action=ARTIST&ids='+$$("artistId").value;
	    openWindow("选择作者",url_link,200,300);	
	}

	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
   .ten {width:17% !important;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtLiteratureMedium" id="ArtLiteratureMediumForm" method="post" namespace="/literature" enctype="multipart/form-data">
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>艺术家：</td>
					<td width="35%" class="textL">
							<s:textfield readonly="true" name="record.artistName" style="width:80%;" />
  	                        <s:hidden name="record.artistId" id="artistId"/>
  	                        <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtArtist()">选择</gl:button>
  	                </td>
					<td class="textR" width="15%" ><font color="red">*</font>文献标题：</td>
					<td width="35%" ><s:textfield name="form.record.literatureTitle"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >时长：</td>
					<td width="35%" ><s:textfield name="form.record.whenLong"/></td>
					<td class="textR" width="15%" >制作方：</td>
					<td width="35%" ><s:textfield name="form.record.shotPeople"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >拍摄时间：</td>
					<td width="35%" >
						 <s:textfield name="form.record.shotTime" placeholder="xxxx年xx月xx日"/>
					</td>
					<td/><td/>
				</tr>
				<tr>
					<td class="textR" width="15%" >内容描述：</td>
					<td colspan="3"><s:textarea height="80%" name="form.record.contentDesc"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献提及作品：</td>
					<td colspan="3" class="textL">
					    <s:textarea height="50px"  name="form.record.literatureWorks" placeholder="关键词，多个用、号隔开"/>
					</td>
				</tr>
				<tr>
					<td class="textR">文献相关人物：</td>
					<td colspan="3" class="textL">
						<s:textarea height="50px"  name="form.record.personInvolved" placeholder="人物名，多个用、号隔开"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献相关展览：</td>
					<td colspan="3" class="textL">
						<s:textarea height="50px"  name="form.record.relatedExhib" placeholder="展览，多个用、号隔开"/> 
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献相关事件：</td>
					<td colspan="3" class="textL">
						<s:textarea height="50px"  name="form.record.relatedEvent" placeholder="事件，多个用、号隔开"/> 
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >链接：</td>
					<td colspan="3" class="textL"><s:textfield name="form.record.siteLink"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >附件：</td>
					<td colspan="3" class="textL">
					 <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					   <tr>
						   <td class="textL">
							   <input type="file" name="files" id="file" class="checkFile" />
						   </td>
					   </tr>
					   <tr>
						   <td class="textL">
							   <input type="file" name="files" id="file" class="checkFile" />
						   </td>
					   </tr>
					   <tr>
						   <td class="textL">
							   <input type="file" name="files" id="file" class="checkFile" />
						   </td>
					   </tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >附件来源：</td>
					<td colspan="3" class="textL">
						<s:textfield name="record.attachmentSource"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="textC">文献研究</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >主观重要度评级：</td>
					<td><s:select list="codeSets.COMMENT_LEVEL" listKey="value" listValue="codeName" name="record.subjectiveEval"/></td>
					<td class="textR" width="15%" >客观重要度评级：</td>
					<td><s:select list="codeSets.COMMENT_LEVEL" listKey="value" listValue="codeName" name="record.objectiveEval"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >核心论断：</td>
					<td colspan="3" class="textL">
						<s:textarea height="80px" name="record.coreThesis"/>
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