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

	var fulllink = contextpath + "artist/ArtistInformation.do";	
	var experienceLink = contextpath + "artist/ArtistExperience.do";
	var albumLink = contextpath + "artist/ArtistAlbum.do";
	var eduLink = contextpath + "artist/ArtistEdu.do";
	var honorsLink = contextpath = "artist/ArtistHonors.do";
	var collectAgencyLink = contextpath = "artist/ArtistCollectAgency.do";
	var collectLink = contextpath = "artist/ArtistCollector.do";
	var coopLink = contextpath = "artist/ArtistCoop.do";
	var donationLink = contextpath = "artist/ArtistDonation.do";
	var seriesLink = contextpath = "artist/ArtistWorksSeries.do";

	function goCancel(){
		parent.closedialog('false');
	}


	function init(){
		document.getElementById("cancel").style.display="none";
		showMessage('<s:property value="errorMessage" escape="false"/>');
		hidebtn();
	}
	
	
	function hidebtn(){
		$(".dhxtabbar_tab_text").each(function(){
			this.addEventListener('click',function(){
				if(this.innerHTML != "基本信息"){
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
	<s:form action="ArtistInformation" id="ArtistInformationForm" method="post" namespace="/artist" enctype="multipart/form-data">
			<s:hidden name="record.id" id="artistId"/>
			<div id="tblForm"></div>
			<div id="page_0"  style="padding:2px; height:100%; width:100%;">
			<div id="formDiv" class="formDiv" style="padding:2px;">
			<table cellpadding="0" cellspacing="0"  style="width: 99%;" class="formTable">
				<tr>
					<td class="textR" width="15%" >中文名：</td>
					<td width="35%"  class="textL"><s:property value="form.record.cName"/></td>
					<td class="textR" width="15%" >英文名：</td>
					<td width="35%" class="textL"><s:property value="form.record.eName" /></td>
				</tr>
				
				<tr>
					<td class="textR" width="15%" >性别：</td>
					<td  class="textL"><s:property value="form.record.sex"/></td>
					<td/><td/>
				</tr>
				<tr>	
					<td class="textR" width="15%">生日期：</td>
					<td class="textL">
						<s:property value="form.record.birthTime" />
					</td>
					<td class="textR" width="15%">卒日期：</td>
					<td class="textL">
						<s:property value="form.record.deathTime" />
					</td>
				</tr>
				<tr>
					<td class="textR">出生国家：</td>
					<td class="textL">
						<s:property value="record.birthCountryName"  />
					</td>
				 	<td class="textR">出生地点：</td>
					<td class="textL"><s:property value="form.record.birthplace"/></td>
				</tr>
				<tr>
					<td class="textR"  >祖籍：</td>
					<td class="textL">
						<s:property value="form.record.ancestralHome"/>
					</td>
					<td class="textR" >国籍：</td>
					<td class="textL">
						<s:property value="record.nationalityName"  />
					</td>
				</tr>
				<tr>
				    <td class="textR" >星座：</td>
					<td class="textL"><s:property value="form.record.zodiac"/></td>
				    <td class="textR">现居住工作：</td>
					<td class="textL">
						<s:property value="form.record.nhom" />
					</td>
				</tr>
				<tr>
				    <td class="textR" >艺术家时期类型：</td>
					<td class="textL"><s:property value="form.record.artistType"/></td>
					<td class="textR" >主要创作媒价：</td>
					<td class="textL"><s:checkboxlist list="codeSets.MEDIUM_TYPE" listKey="value" listValue="codeName" name="strs" cssStyle="width:20px;"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >相片显示：</td>
					<td class="textL" id="picphoto">
						<s:iterator value="pic" id="p" status="pp">
							<a href="<%=CONTEXT_PATH%>upload/photo/<s:property value='record.photoPath'/>/${p.realName}" target="_blank" >${p.name}</a>
						</s:iterator>
					</td>
					<td class="textL" colspan="2" >
					</td>
				</tr>
				<tr>
					<td class="textR" valign="top">个人主页：</td>
					<td  class="textL"colspan="3" valign="top" style="height:40px"><s:property value="form.record.personalPage" /></td>
				</tr>
				<tr>
					<td class="textR" valign="top">简介：</td>
					<td  class="textL"colspan="3" valign="top" style="height:40px"><s:property value="form.record.cResume" /></td>
			    </tr>
		 	</table>
		 </div>
		 </div>
		 <iframe  id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_2" name="page_2" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_3" name="page_3" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_4" name="page_4" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_5" name="page_5" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
		 <iframe  id="page_6" name="page_6" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
		 <iframe  id="page_7" name="page_7" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
		 <iframe  id="page_8" name="page_8" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
</s:form>

<div id="btnDiv" class="btnDiv">
	 <gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

	<%@include file="/common/dialog.jsp"%>
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
		tabbar.addTab("a0", "基本信息", "100px");
		tabbar.addTab("a1", "教育与工作", "130px");
		tabbar.addTab("a2", "获奖与荣誉", "110px");
		tabbar.addTab("a3", "收藏机构", "100px");
		tabbar.addTab("a4", "收藏家", "100px");
		tabbar.addTab("a5", "捐赠", "80px");
		tabbar.addTab("a6", "合作机构", "100px");
		tabbar.addTab("a7", "人生年表", "100px");
		tabbar.addTab("a8", "作品系列", "100px");
		tabbar.tabs("a0").attachObject("formDiv");
		tabbar.tabs("a0").setActive();
		tabbar.tabs("a1").attachObject("page_1");
		tabbar.tabs("a2").attachObject("page_2");
		tabbar.tabs("a3").attachObject("page_3");
		tabbar.tabs("a4").attachObject("page_4");
		tabbar.tabs("a5").attachObject("page_5");
		tabbar.tabs("a6").attachObject("page_6");
		tabbar.tabs("a7").attachObject("page_7");
		tabbar.tabs("a8").attachObject("page_8");
		tabbar.enableAutoReSize(true);
		tabbar.enableTabCloseButton(true);
		tabbar.attachEvent("onTabClick", function(idClicked, idSelected){
			if(idClicked != "a0"){
				document.getElementById("btnDiv").style.display="none";
			}else{
				document.getElementById("btnDiv").style.display="";
			}
		});
		window.frames["page_1"].location.href = eduLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_2"].location.href = honorsLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_3"].location.href = collectAgencyLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_4"].location.href = collectLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_5"].location.href = donationLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_6"].location.href = coopLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_7"].location.href = experienceLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
		window.frames["page_8"].location.href = seriesLink + '?action=LIST&record.artistId=' + $$('artistId').value+"&record.hide=1";
	</script>
</body>
</html>