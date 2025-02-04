<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var fulllink = contextpath + "activity/ArtActivityExhibit.do";	
	var artistlink = contextpath + "activity/ArtActivityExhibitArtist.do";
	var workslink = contextpath + "activity/ArtWorksExhibit.do";

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
		if(!checkString(getElement('record.exhibitName'),"展览名",100,true)) return;
		if(!checkString(getElement('record.exhibitors'),"展览机构名",200,true)) return;
		if(getElement('form.record.activityYear').value != ""){
			if(!checkNumeric(getElement('form.record.activityYear'),"年",4,false,false)) return;
		}
		return true;
	}
	
	function closedialog(ret) {
		var res=ret['ret'];
		var artAgency=ret['artAgency'];
		var country=ret['country'];
		if(editMode=="Artist") {
	    	 getElement('record.artistId').value=ret['str'];
	    	 getElement('record.artArtistName').value=ret['str1'];
	     }
		if(editMode=="Country") {
	    	 getElement('record.countryId').value=ret['str'];
	    	 getElement('record.countryName').value=ret['str1'];
	     }
		if(editMode=="Agency") {
	    	 getElement('record.exhibitors').value=ret['str'];
	    	 getElement('record.artAgencyName').value=ret['str1'];
	     }
		if(ret=='true') {
			goSearch();
		}
		editMode="";
	}

	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		//hidebtn();
	}

	function goArtArtist(ret){
		if(ret == 'choose'){
			editMode="Artist";
			var url_link="<%=CONTEXT_PATH%>artist/ArtistInformation.do?query.parameters.type=1";
		    openWindow("选择艺术家",url_link,0.8,0.8);	
		}
		if(ret == 'cancel'){
			$$('artArtistId').value="";
			$$('artArtistName').value="";
		}
	}

	function goArtAgency(ret){
		if(ret == 'choose'){
			editMode="Agency";
			var url_link="<%=CONTEXT_PATH%>system/artAgency.do?query.parameters.type=1";
		    openWindow("选择机构",url_link,0.8,0.8);	
		}
		if(ret == 'cancel'){
			$$('artAgencyId').value="";
			$$('artAgencyName').value="";
		}
	}

	function goCountry(ret){
		if(ret == 'choose'){
			editMode="Country";
			var url_link=contextpath+'system/artCountry.do?&record.type=1&query.parameters.type=1';
		    openWindow("选择国家",url_link,0.8,0.8);	
		}
		if(ret == 'cancel'){
			$$('countryId').value="";
			$$('countryName').value="";
		}
	}
	
	function hidebtn(){
		$(".dhxtabbar_tab_text").each(function(){
			this.addEventListener('click',function(){
				if(this.innerHTML != "展览信息"){
					document.getElementById("btnDiv").style.display="none";
				}else{
					document.getElementById("btnDiv").style.display="";
				}
			});
		});
	}

	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtActivityExhibit" id="ArtActivityExhibitForm" method="post" enctype="multipart/form-data">
			<s:hidden name="record.type" id="type"/>
			<s:hidden name="record.next" id="next"/>
			<s:hidden name="record.id" id="id"/>
			<div id="tblForm"></div>
			<div id="page_0"  style="padding:2px; height:100%; width:100%;">
			<div id="formDiv" class="formDiv" style="padding:2px;">
			<table cellpadding="0" cellspacing="0" style="width: 99%;"  class="formTable">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>展览名：</td>
					<td width="35%" class="textL"><s:textfield name="record.exhibitName"/></td>
					<td class="textR" width="15%" ><font color="red">*</font>展览机构名：</td>
					<td width="35%" class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					            	<s:textfield readonly="true" name="record.artAgencyName" id="artAgencyName" />
  	                        		<s:hidden name="record.exhibitors" id="artAgencyId"/>
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtAgency('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtAgency('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
				</tr>
				<s:if test="record.type=='person'">
				<tr>
					<td class="textR">艺术家：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					            	<s:textfield readonly="true" name="record.artArtistName" id="artArtistName" />
  	                        		<s:hidden name="record.artistId" id="artArtistId"/>
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtArtist('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtArtist('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
					<td></td><td></td>
				</tr>
				</s:if>
				<tr>
					<td class="textR">策展人：</td>
					<td class="textL" colspan="3"><s:textfield name="form.record.curator" placeholder="（用、号区分）" /></td>
				</tr>
				<tr>
					<td class="textR">年：</td>
					<td class="textL"><s:textfield name="form.record.activityYear"/></td>
					<td class="textR">月：</td>
					<td class="textL"><s:textfield name="form.record.activityMonth"/></td>
				</tr>
				<tr>
					<td class="textR">国家：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					            	<s:textfield readonly="true" name="record.countryName" id="countryName" />
  	                        		<s:hidden name="record.countryId" id="countryId"/>
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goCountry('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goCountry('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
					<td class="textR">城市：</td>
					<td class="textL"><s:textfield name="form.record.city"/></td>
				</tr>
		 	</table>
	</div>
	</div>
	<iframe  id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
	<iframe  id="page_2" name="page_2" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<script type="text/javascript">
	var mainbody=window.document.body;
	if(window.addEventListener) {
		     window.addEventListener("resize",goResize,false); 
	}
	else {
		     window.attachEvent('onresize',goResize);
	}
	
	var fDiv=window.document.getElementById('formDiv');
	var bDiv=window.document.getElementById('btnDiv');
	var btbl=window.document.getElementById('tblForm');
	
	function goResize(){
	   fDiv.style.width=mainbody.offsetWidth-fDiv.offsetLeft*2+"px";
	   bDiv.style.width=mainbody.offsetWidth-bDiv.offsetLeft*2+"px";
	   fDiv.style.height=mainbody.offsetHeight-bDiv.offsetHeight-fDiv.offsetTop*2+"px";
	   bDiv.style.top=fDiv.offsetHeight+fDiv.offsetTop*2-5+"px";
	   if(btbl){
    	   btbl.style.width=fDiv.offsetWidth-fDiv.offsetLeft-9+"px";
    	   btbl.style.height=fDiv.offsetHeight-fDiv.offsetTop-12+"px";
       }
	}
	goResize();   
	
	var tabbar = new dhtmlXTabBar("tblForm", "top");
    tabbar.setSkin('dhx_terrace');
    tabbar.setArrowsMode("auto");
    tabbar.addTab("a0", "展览信息", "120px");
    if($$('type').value!='person'){
    tabbar.addTab("a1", "艺术家", "120px");
    tabbar.tabs("a1").attachObject("page_1");
    window.frames["page_1"].location.href=artistlink+'?action=LIST&record.hide=1&query.parameters.exhibitId='+$$('id').value;
    }
    tabbar.addTab("a2", "相关作品", "120px");
    tabbar.tabs("a0").attachObject("formDiv");
    tabbar.tabs("a2").attachObject("page_2");
    var next = $$('next').value;
    if(next == '1'){
    	/* if($$('type').value=='person'){
    		tabbar.tabs("a2").setActive();
    	}else{
	    	tabbar.tabs("a1").setActive();
    	} */
    	//document.getElementById("save").style.display="none";
    }else{
	    tabbar.tabs("a0").setActive();
    }
    tabbar.tabs("a0").setActive();
    tabbar.enableAutoReSize(true);
    tabbar.enableTabCloseButton(true);
    tabbar.attachEvent("onTabClick", function(idClicked, idSelected){
    	if(idClicked != "a0"){
    		document.getElementById("btnDiv").style.display="none";
    	}else{
    		document.getElementById("btnDiv").style.display="";
    	}
    });
    window.frames["page_2"].location.href=workslink+'?action=WORKS&query.parameters.exhibitId='+$$('id').value+"&record.zhan=1&record.hide=1";
</script>
<%@include file="/common/dialog.jsp"%>

</body>
</html>