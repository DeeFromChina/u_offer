<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "auction/ArtAuction.do";	
	
	var ret=new Object();

	function goCancel(){
		parent.closedialog('false');
		
	}

	function goSubmit() {
		if(checkFormInput()){
		    var url =fulllink+"?action=EXCEL";
		  	document.forms[0].action=url;
			document.forms[0].submit();
		}
	}
	
	function checkFormInput(){
		if(!checkFile()) return;
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			if(!checkString(this,"excel",400,true)) {
				isPass = false;
				return false;
			}
			var format = this.value.split(".");
			var i = format.length;
			if(format[i-1] != "xls" && format[i-1] != "xlsx"){
				alert("只能上传xls或者xlsx格式！");
				isPass = false;
				return false;
			}
		});
		return isPass;
	}

	function downloadTemp() {
		var editMode = "DOWNLOAD";
		document.forms[0].action = fulllink + "?action=" + editMode;
		document.forms[0].target = "_self";
		document.forms[0].submit();
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
	<div class="special-padding" id="mainDiv">
		<s:form action="artWorks" id="artWorksForm" method="post" ENCTYPE="multipart/form-data">
		<s:hidden name="record.id" id="id"/>
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0"
				class="formTable">
				<tbody>
					<tr>
						<td width="20%" class="textR">请选择：</td>
						<td width="80%" class="textL"><s:file name="importFile"></s:file>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;" colspan="2"><gl:button
								name="download" onClick="downloadTemp()">下载Excel模板</gl:button> <gl:button
								name="import" onClick="goSubmit()">导入</gl:button> <gl:button
								name="cancel" onClick="goCancel()">关闭</gl:button>
						</td>
					</tr>
				</tbody>
			</table>
		</s:form>
	</div>
</body>
</html>