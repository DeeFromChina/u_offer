<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	
	var fulllink = contextpath + "artist/ArtistCollectAgency.do";

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
		if(!checkString(getElement('record.agencyName'),"收藏机构",100,true)) return;
		return true;
	}
	
	function closedialog(ret) {
		var res=ret['ret'];
		if(res==true) {
			if(editMode=="WORKS") {
				getElement('form.record.collectWorks').value = ret['str1'];
				getElement('form.record.collectWorksName').value = ret['str2'];
			}
			if(editMode=="country"){
		    	 getElement('record.agencyId').value=ret['str'];
		    	 getElement('record.agencyName').value=ret['str1'];
		    }
		}
		
		editMode="";
	}

	function goWorksName(){
		var url_link = contextpath + "works/artWorks.do?query.parameters.type=2&query.parameters.selectType=m";
	    openWindow("选择作品",url_link,0.8,0.8);	
	    editMode="WORKS";
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}
	
	function goArtAgency(){
		editMode="country"
			var url_link="<%=CONTEXT_PATH%>system/artAgency.do?query.parameters.type=1";
	    openWindow("选择机构",url_link,0.8,0.8);	
	}
	
	function goWorks(id){
		var url_link= contextpath + 'works/artWorks.do?action=EDIT&ids='+id;
	    openWindow("修改作品基本信息",url_link,0.8,0.8);
	}

	function clear(name){
		$$(name).value = "";
		var v = name + "Name";
		$$(v).value = "";
	}
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtistCollect" id="ArtistCollectForm" method="post" namespace="/artist">
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="record.id" id="id"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>收藏机构：</td>
					<td width="35%" class="textL">
						<s:textfield readonly="true" name="record.agencyName" style="width:80%;" />
  	                    <s:hidden name="record.agencyId" />
  	                    <gl:button name="btnArtAgency" styleClass="btnFormStyle" onClick="goArtAgency()">选择</gl:button>
					</td>
				</tr>
				<tr>
					<td class="textR" valign="top">时间：</td>
					<td><s:textfield name="form.record.collectTime" placeholder="xxxx年xx月xx日"></s:textfield></td>
			    </tr> 
				<tr>
					<td class="textR" valign="top">作品：</td>
					<td class="textL" valign="top">
					<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
						<s:if test="record.worksName == 1">
							<tr>
								<td class="textL">
								<s:iterator value="cookies" id="co" status="coco">
									<a href="#" onclick="goWorks('${co.id}')">${co.name}</a>
								</s:iterator>
								</td>
							</tr>
						</s:if>
					        <tr>
					            <td>
					            <s:hidden name="form.record.collectWorks" id="collectWorks"/>
								<s:textarea name="form.record.collectWorksName" id="collectWorksName" placeholder="多个以、隔开"></s:textarea>
								 </td>
								 <td width="45px">
								 <gl:button name="btnBirthPlace" styleClass="btnFormStyle" onClick="goWorksName()">选择</gl:button>
					             </td>
					             <td width="45px">
								 <gl:button name="btnBirthPlace" styleClass="btnFormStyle" onClick="clear('collectWorks')">清空</gl:button>
					             </td>
					        </tr>
					    </table>
					</td>
			    </tr> 
				<tr>
					<td class="textR" valign="top">描述：</td>
					<td><s:textarea height="80px" name="form.record.collectDesc"></s:textarea></td>
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